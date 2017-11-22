package tradeapp.ateneo.edu.tradeapp.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListAdapter
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.R
import tradeapp.ateneo.edu.tradeapp.model.Product
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat

/**
 * Created by aldrin on 11/20/17.
 */

class ProductListAdapter(context: Context, categories: OrderedRealmCollection<Product>): RealmBaseAdapter<Product>(categories)
        , ListAdapter {

    private class ViewHolder {
        internal var productButton: ImageButton? = null
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
            viewHolder.productButton = convertView.findViewById(R.id.productButton) as ImageButton
            viewHolder.productTitle = convertView.findViewById(R.id.productTitle) as TextView
            viewHolder.productDetails = convertView.findViewById(R.id.productDetails) as TextView
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        if (adapterData != null) {
            val product = adapterData!![position]
            val image = Drawable.createFromStream(ByteArrayInputStream(product.photos[0]), product.title)
            viewHolder.productButton!!.setImageDrawable(image)
            viewHolder.productButton!!.contentDescription = product.title
            viewHolder.productTitle!!.text = StringUtils.capitalize(product.title)
            viewHolder.productDetails!!.text = "${product.user!!.getDisplayName()} - ${SimpleDateFormat("MM/dd").format(product.dateCreated)}"
        }

        return convertView!!
    }

}
