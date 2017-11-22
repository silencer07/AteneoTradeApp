package tradeapp.ateneo.edu.tradeapp

import io.realm.Realm
import io.realm.Sort
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.UiThread
import tradeapp.ateneo.edu.tradeapp.adapters.ProductListAdapter
import tradeapp.ateneo.edu.tradeapp.model.Product


@EActivity(R.layout.activity_main)
open class ProductListActivity : AbstractMainActivity() {

    override fun showData() {
        updateView()
    }

    @UiThread
    protected open fun updateView(){
        val realm = Realm.getDefaultInstance()
        val products = realm.where(Product::class.java).findAllSorted("dateCreated", Sort.DESCENDING)
        listView.adapter = ProductListAdapter(this.applicationContext, products)
    }

}
