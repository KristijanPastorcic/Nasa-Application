package hr.algebra.nasaapplication.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import hr.algebra.nasaapplication.model.Item

private const val DB_NAME = "items.db"
private const val DB_VERSION = 1 // -> SQLiteOpenHelper reads it's meta data and if version is changed he calls onUpgrade
private const val TABLE_NAME = "items"
private val CREATE_TABLE = "create table $TABLE_NAME( " +
        "${Item::_id.name} integer primary key autoincrement, " +
        "${Item::title.name} text not null, " +
        "${Item::explanation.name} text not null, " +
        "${Item::picturePath.name} text not null, " +
        "${Item::date.name} text not null, " +
        "${Item::read.name} integer not null)"
private const val DROP_TABLE = "drop table $TABLE_NAME"


class NasaSqlHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    NasaRepository {

    // SQLiteOpenHelper class will create db and call onCreate if db under DB_NAME does not exist on used device, first time it is used
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // use for migrations, steps: 1. Create backup, 2. upgrade db, 3. migrate data from old to new db
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(TABLE_NAME, selection, selectionArgs)


    override fun insert(values: ContentValues?) =
        writableDatabase.insert(
            TABLE_NAME,
            null, // if values == null -> "hackValue"
            values
        )


    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return readableDatabase.query(
            TABLE_NAME,
            projection,  // what columns to retrieve
            selection,
            selectionArgs,
            null, // group by
            null, // having
            sortOrder
        )
    }

    override fun update(values: ContentValues?, selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)


}