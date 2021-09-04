package hr.algebra.nasaapplication

import android.content.*
import android.database.Cursor
import android.net.Uri
import hr.algebra.nasaapplication.dao.NasaRepository
import hr.algebra.nasaapplication.factory.getNasaRepository
import hr.algebra.nasaapplication.model.Item

private const val AUTHORITY = "hr.algebra.nasaapplication.api.provider"
private const val PATH = "items"
val NASA_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

// Rest api NASA_PROVIDER_CONTENT_URI
// content://hr.algebra.nasaapplication.api.provider/items -> On this public URI i can execute CRUD operations with contentResolver

private const val ITEMS = 10
private const val ITEM_ID = 20
private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    // register qualified URI-es
    addURI(AUTHORITY, PATH, ITEMS) // under key ITEMS, this path returns all items
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)  // under key ITEM_ID returns 1 item like ../items/id
    this
}

private const val CONTENT_DIR_TYPE =
    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH
private const val CONTENT_ITEM_TYPE =
    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH

class NasaProvider : ContentProvider() {

    private lateinit var repository: NasaRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> return repository.delete(selection, selectionArgs)
            ITEM_ID -> {
                val id = uri.lastPathSegment
                if (id != null) {
                    return repository.delete("${Item::_id.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalAccessException("Wrong URI")
    }

    override fun getType(uri: Uri): String? {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> return CONTENT_DIR_TYPE
            ITEM_ID -> return CONTENT_ITEM_TYPE
        }
        throw IllegalAccessException("Wrong URI")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (URI_MATCHER.match(uri) == ITEMS) {
            val id = repository.insert(values)
            return ContentUris.withAppendedId(NASA_PROVIDER_CONTENT_URI, id)
        }
        throw IllegalAccessException("Wrong URI")
    }

    override fun onCreate(): Boolean {
        repository = getNasaRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        if (URI_MATCHER.match(uri) == ITEMS) {
            return repository.query(projection, selection, selectionArgs, sortOrder)
        }
        throw IllegalAccessException("Wrong URI")
    }


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when (URI_MATCHER.match(uri)) {
            ITEMS -> repository.update(values, selection, selectionArgs)
            ITEM_ID -> {
                val id = uri.lastPathSegment
                if (id != null) {
                    return repository.update(values, "${Item::_id.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalAccessException("Wrong URI")
    }
}