package tradeapp.ateneo.edu.tradeapp

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.LayoutInflaterCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.mikepenz.iconics.context.IconicsLayoutInflater
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById
import android.widget.Toast
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.context.IconicsContextWrapper
import org.androidannotations.annotations.OptionsItem


@EActivity(R.layout.activity_main)
open class MainActivity : AppCompatActivity() {

    @ViewById(R.id.message)
    protected lateinit var mTextMessage: TextView;

    @ViewById(R.id.navigation)
    protected lateinit var navigation: BottomNavigationView;

    @ViewById(R.id.toolbar)
    protected lateinit var toolbar: Toolbar;

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                mTextMessage!!.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                mTextMessage!!.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                mTextMessage!!.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override protected fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    @AfterViews
    fun setText() {
        navigation!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    @AfterViews
    fun setToolbar(){
        setSupportActionBar(toolbar);
    }

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

    @OptionsItem(R.id.action_favorite)
    fun actionFavoriteClicked(){
        Toast.makeText(this@MainActivity, "Action clicked", Toast.LENGTH_LONG).show()
    }
}
