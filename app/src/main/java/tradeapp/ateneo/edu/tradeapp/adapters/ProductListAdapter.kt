package tradeapp.ateneo.edu.tradeapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.ProductDetailsActivity_
import tradeapp.ateneo.edu.tradeapp.ProductListActivity_
import tradeapp.ateneo.edu.tradeapp.R
import tradeapp.ateneo.edu.tradeapp.filter.ProductFilter
import tradeapp.ateneo.edu.tradeapp.model.Bookmark
import tradeapp.ateneo.edu.tradeapp.model.Category
import tradeapp.ateneo.edu.tradeapp.model.Product
import tradeapp.ateneo.edu.tradeapp.model.User
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

/**
 * Created by aldrin on 11/20/17.
 */

class ProductListAdapter(val loggedInUser: User?, val context: Context, products: OrderedRealmCollection<Product>): RealmBaseAdapter<Product>(products)
        , ListAdapter {

    private class ViewHolder {
        internal var productView: ImageView? = null
        internal var productTitle: TextView? = null
        internal var productDetails: TextView? = null
        internal var bookmarkButton: TextView? = null
    }

    override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder
        var convertView = _convertView;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.product_card, parent, false)
            viewHolder = ViewHolder()
            viewHolder.productView = convertView.findViewById(R.id.productView) as ImageView
            viewHolder.productTitle = convertView.findViewById(R.id.productTitle) as TextView
            viewHolder.productDetails = convertView.findViewById(R.id.productDetails) as TextView
            viewHolder.bookmarkButton = convertView.findViewById(R.id.bookmarkButton) as Button
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        if (adapterData != null) {
            val product = adapterData!![position]
            val image = Drawable.createFromStream(ByteArrayInputStream(product.photos[0]), product.title)
            viewHolder.productView!!.setImageDrawable(image)
            viewHolder.productView!!.contentDescription = product.title
            viewHolder.productTitle!!.text = StringUtils.capitalize(product.title)
            viewHolder.productDetails!!.text = "${product.user!!.getDisplayName()} - ${SimpleDateFormat("MM/dd").format(product.dateCreated)}"
            convertView!!.setOnClickListener { v: View ->
                val i = Intent(context, ProductDetailsActivity_::class.java)
                i.putExtra("productUuid", product.uuid)
                context.startActivity(i)
            }



            if(loggedInUser != null){
                var drawable = IconicsDrawable(context)
                drawable = drawable.icon(FontAwesome.Icon.faw_heart).actionBar()

                val isBookmarked = Realm.getDefaultInstance().where(Bookmark::class.java)
                    .equalTo("product.uuid", product.uuid)
                    .equalTo("user.username", loggedInUser.username)
                    .count() > 0

                val colorId = if(isBookmarked) R.color.colorAccent else R.color.colorLight
                drawable = drawable.color(ContextCompat.getColor(context, colorId))

                viewHolder.bookmarkButton!!.background = drawable
                viewHolder.bookmarkButton!!.setOnClickListener { v ->
                    Realm.getDefaultInstance().executeTransaction { realm ->
                        var bookmark = Realm.getDefaultInstance().where(Bookmark::class.java)
                            .equalTo("product.uuid", product.uuid)
                            .equalTo("user.username", loggedInUser.username)
                            .findFirst()
                        if(bookmark != null){
                            bookmark.deleteFromRealm()
                            drawable.color(ContextCompat.getColor(context, R.color.colorLight))
                        } else {
                            bookmark = Bookmark()
                            bookmark.user = loggedInUser
                            bookmark.product = product
                            realm.copyToRealmOrUpdate(bookmark)
                            drawable.color(ContextCompat.getColor(context, R.color.colorAccent))
                        }
                    }
                }
            } else {
                viewHolder.bookmarkButton!!.visibility = View.GONE
            }
        }

        return convertView!!
    }

}
