package tradeapp.ateneo.edu.tradeapp

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.adapters.ProductListAdapter
import tradeapp.ateneo.edu.tradeapp.model.Product

@EActivity(R.layout.activity_main)
open class ProductListActivity : AbstractMainActivity() {

    override fun showData() {
        val realm = Realm.getDefaultInstance()
        //TODO should get the data depending on the passed string from @Extra
        val products: RealmResults<Product> = realm.where(Product::class.java).findAllSorted("dateCreated", Sort.DESCENDING)
        listView.adapter = ProductListAdapter(baseContext, products)
    }

}
