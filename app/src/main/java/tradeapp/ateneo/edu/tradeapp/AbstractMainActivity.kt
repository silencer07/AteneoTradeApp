package tradeapp.ateneo.edu.tradeapp

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import android.widget.ListView
import android.widget.TextView
import com.mikepenz.iconics.context.IconicsLayoutInflater
import android.widget.Toast
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.context.IconicsContextWrapper
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.androidannotations.annotations.*
import tradeapp.ateneo.edu.tradeapp.adapters.CategoryListAdapter
import tradeapp.ateneo.edu.tradeapp.model.ActivityWithIconicsContext
import tradeapp.ateneo.edu.tradeapp.model.Category
import tradeapp.ateneo.edu.tradeapp.service.UserService

@EActivity(R.layout.activity_main)
abstract class AbstractMainActivity : ActivityWithIconicsContext() {

    @ViewById(R.id.navigation)
    protected lateinit var navigation: BottomNavigationView;

    @ViewById(R.id.toolbar)
    protected lateinit var toolbar: Toolbar;

    @ViewById(R.id.listView)
    protected lateinit var listView: GridView

    @Bean
    protected lateinit var userService: UserService

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->


        when (item.itemId) {
            R.id.navigation_account -> {
                if(userService.getLoggedInUser() != null) {
                    baseContext.startActivity(Intent(this, AccountDetailsActivity_::class.java))
                } else {
                    baseContext.startActivity(Intent(this, LoginActivity_::class.java))
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sell -> {
                if(userService.getLoggedInUser() != null) {
                    println("TODO");
                } else {
                    baseContext.startActivity(Intent(this, LoginActivity_::class.java))
                }

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @AfterViews
    fun setText() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    @AfterViews
    fun setupBottomNavigation(){
        navigation.menu.getItem(1).setChecked(true)

        navigation.menu.getItem(0).setIcon(IconicsDrawable(this).icon(FontAwesome.Icon.faw_user).actionBar())
        navigation.menu.getItem(2).setIcon(IconicsDrawable(this).icon(FontAwesome.Icon.faw_usd).actionBar())
    }

    @AfterViews
    @Background
    abstract fun showData()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val actionFavorite = menu.findItem(R.id.action_favorite)
        val drawable = IconicsDrawable(this);
        actionFavorite.setIcon(
                drawable.icon(FontAwesome.Icon.faw_heart).actionBar()
                        .color(ContextCompat.getColor(this, R.color.colorAccent))
        )
        return true
    }

    @AfterViews
    fun setToolbar(){
        setSupportActionBar(toolbar);
        super.getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
    }

    @OptionsItem(R.id.action_favorite)
    fun actionFavoriteClicked(){
        Toast.makeText(this@AbstractMainActivity, "Action clicked", Toast.LENGTH_LONG).show()
    }
}
