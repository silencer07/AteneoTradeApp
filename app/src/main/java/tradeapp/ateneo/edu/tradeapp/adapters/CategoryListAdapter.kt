package tradeapp.ateneo.edu.tradeapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.ProductListActivity_
import tradeapp.ateneo.edu.tradeapp.R
import tradeapp.ateneo.edu.tradeapp.filter.ProductFilter
import tradeapp.ateneo.edu.tradeapp.model.Category
import java.util.concurrent.Executors

/**
 * Created by aldrin on 11/20/17.
 */

class CategoryListAdapter(val context: Context, categories: OrderedRealmCollection<Category>): RealmBaseAdapter<Category>(categories)
        , ListAdapter {

    private class ViewHolder {
        internal var categoryView: ImageView? = null
        internal var categoryName: TextView? = null
    }

    override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder
        var convertView = _convertView;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.category_card, parent, false)
            viewHolder = ViewHolder()
            viewHolder.categoryView = convertView.findViewById(R.id.categoryView) as ImageView
            viewHolder.categoryName = convertView.findViewById(R.id.categoryName) as TextView
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        if (adapterData != null) {
            val category = adapterData!![position]
            val resources = parent!!.context.resources
            val packageName = parent.context.packageName
            val image = resources.getDrawable(resources.getIdentifier(category.photo, "drawable", packageName), null)
            viewHolder.categoryView!!.setImageDrawable(image)
            viewHolder.categoryView!!.contentDescription = category.name
            viewHolder.categoryName!!.text = StringUtils.capitalize(category.name)

            convertView!!.setOnClickListener { v: View ->
                Executors.newSingleThreadExecutor().execute {
                    Looper.prepare()

                    Realm.getDefaultInstance().use { realm ->
                        val imageView = v.findViewById(viewHolder.categoryView!!.id) as ImageView
                        val category = realm.where(Category::class.java).equalTo("name", imageView.contentDescription.toString()).findFirst()
                        val filter = ProductFilter(type = "category", keyword = category!!.name)

                        val i = Intent(context, ProductListActivity_::class.java)
                        i.putExtra("filter", filter)
                        context.startActivity(i)

                        Looper.loop()
                    }
                }
            }
        }
        return convertView!!
    }

}
