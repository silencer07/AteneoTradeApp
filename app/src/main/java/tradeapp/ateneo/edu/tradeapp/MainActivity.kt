package tradeapp.ateneo.edu.tradeapp

import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.adapters.CategoryListAdapter
import tradeapp.ateneo.edu.tradeapp.model.Category


@EActivity(R.layout.activity_main)
open class MainActivity : AbstractMainActivity() {

    override fun showData(){
        async(UI) {
            val realm = Realm.getDefaultInstance()
            val categories: RealmResults<Category> = realm.where(Category::class.java).findAll()
            listView.adapter = CategoryListAdapter(baseContext, categories)
        }
    }
}
