package tradeapp.ateneo.edu.tradeapp

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.GridView
import android.widget.Toast
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import org.androidannotations.annotations.*
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.filter.ProductFilter
import tradeapp.ateneo.edu.tradeapp.model.ActivityWithIconicsContext
import tradeapp.ateneo.edu.tradeapp.service.UserService
import android.content.res.TypedArray



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
                //TODO if subclass return to ActivityMain
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sell -> {
                if(userService.getLoggedInUser() != null) {
                    baseContext.startActivity(Intent(this, AddProductActivity_::class.java))
                } else {
                    baseContext.startActivity(Intent(this, LoginActivity_::class.java))
                }

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @AfterViews
    fun setNavigatonItemSelectedListener() {
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

        val actionMessages= menu.findItem(R.id.action_messages)
        val drawable2 = IconicsDrawable(this);
        val array = theme.obtainStyledAttributes(intArrayOf(android.R.attr.colorBackground))
        actionMessages.setIcon(
                drawable2.icon(FontAwesome.Icon.faw_comment).actionBar()
                        .color(array.getColor(0, 0xFFFFFF))
        )
        return true
    }

    @OptionsItem(R.id.action_messages)
    open fun showMessageOverview(){
        startActivity( Intent(this, MessageListActivity_::class.java))
    }

    @AfterViews
    fun setToolbar(){
        setSupportActionBar(toolbar);
    }

    @OptionsItem(R.id.action_bookmarks)
    fun actionBookmarksClicked(){
        showProductListActivity("bookmark")
    }

    @OptionsItem(R.id.action_bought)
    fun actionBoughtClicked(){
        showProductListActivity("bought")
    }

    @OptionsItem(R.id.action_sold)
    fun actionSoldClicked(){
        showProductListActivity("sold")
    }

    private fun showProductListActivity(type: String){
        if(userService.getLoggedInUser() != null) {
            val filter = ProductFilter(type = type, keyword = StringUtils.EMPTY)
            val i = Intent(baseContext, ProductListActivity_::class.java)
            i.putExtra("filter", filter)
            baseContext.startActivity(i)
        } else {
            baseContext.startActivity(Intent(this, LoginActivity_::class.java))
        }
    }
}
