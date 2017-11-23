package tradeapp.ateneo.edu.tradeapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Looper
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
import tradeapp.ateneo.edu.tradeapp.ProductDetailsActivity_
import tradeapp.ateneo.edu.tradeapp.ProductListActivity_
import tradeapp.ateneo.edu.tradeapp.R
import tradeapp.ateneo.edu.tradeapp.filter.ProductFilter
import tradeapp.ateneo.edu.tradeapp.model.Category
import tradeapp.ateneo.edu.tradeapp.model.Product
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

/**
 * Created by aldrin on 11/20/17.
 */

class ProductListAdapter(val context: Context, products: OrderedRealmCollection<Product>): RealmBaseAdapter<Product>(products)
        , ListAdapter {

    private class ViewHolder {
        internal var productView: ImageView? = null
        internal var productTitle: TextView? = null
        internal var productDetails: TextView? = null
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
        }

        return convertView!!
    }

}
