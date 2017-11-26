package tradeapp.ateneo.edu.tradeapp

import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import io.realm.Realm
import io.realm.RealmResults
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.model.Category
import kotlinx.android.synthetic.main.activity_add_product.*
import org.apache.commons.lang3.StringUtils

@EActivity(R.layout.activity_add_product)
open class AddProductActivity : AppCompatActivity() {

    @AfterViews
    fun setupCategories(){
        val categories: RealmResults<Category> = Realm.getDefaultInstance().where(Category::class.java).findAll()
        val names = categories.map { category -> StringUtils.capitalize(category.name)  }
        addProductCategorySpinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, names)
    }
}
