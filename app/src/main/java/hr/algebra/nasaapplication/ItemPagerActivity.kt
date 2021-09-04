package hr.algebra.nasaapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import hr.algebra.nasaapplication.framework.fetchItems
import hr.algebra.nasaapplication.model.Item

const val ITEM_POSITION = "hr.algebra.nasaapplication"

class ItemPagerActivity : AppCompatActivity() {

    private lateinit var items: MutableList<Item>
    private lateinit var pager: ViewPager2
    private var itemPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_pager)

        inti()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun inti() {
        items = fetchItems()
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        pager = findViewById(R.id.viewPager)
        pager.apply {
            adapter = ItemPagerAdapter(items, this@ItemPagerActivity)
            currentItem = itemPosition
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // back button
        return super.onSupportNavigateUp()
    }
}