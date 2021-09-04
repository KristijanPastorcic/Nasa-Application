package hr.algebra.nasaapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import hr.algebra.nasaapplication.databinding.ActivityHostBinding

class  HostActivity : AppCompatActivity() {

    private lateinit var b: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContentView(b.root)
        initNavigation()
        initHamburgerMenu()
    }

    private fun initHamburgerMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    private fun initNavigation() {
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(b.navigationView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.host_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                toggle()
                return true
            }
            R.id.menuExit -> {
                exitApp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitApp() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.exit))
            setMessage(R.string.really)
            setIcon(R.drawable.exit)
            setCancelable(true)
            setNegativeButton(getString(R.string.cancel), null)
            setPositiveButton(getString(R.string.yes)) { _, _, -> finish() }
            show()
        }
    }

    private fun toggle() {
        if (b.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            b.drawerLayout.closeDrawers()
        }
        else b.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun init(){
        b = ActivityHostBinding.inflate(layoutInflater)
    }

}
