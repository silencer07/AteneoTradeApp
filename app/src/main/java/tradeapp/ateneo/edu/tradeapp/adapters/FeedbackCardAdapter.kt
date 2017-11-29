package tradeapp.ateneo.edu.tradeapp.adapters

import agency.tango.android.avatarview.views.AvatarView
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Looper
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.ProductListActivity_
import tradeapp.ateneo.edu.tradeapp.R
import tradeapp.ateneo.edu.tradeapp.filter.ProductFilter
import tradeapp.ateneo.edu.tradeapp.model.Category
import tradeapp.ateneo.edu.tradeapp.model.Feedback
import java.io.ByteArrayInputStream
import java.util.concurrent.Executors

/**
 * Created by aldrin on 11/20/17.
 */

class FeedbackCardAdapter(val context: Context, feedback: OrderedRealmCollection<Feedback>): RealmBaseAdapter<Feedback>(feedback)
        , ListAdapter {

    private class ViewHolder {
        internal var avatar: AvatarView? = null
        internal var name: TextView? = null
        internal var text: TextView? = null
        internal var ratingBar: RatingBar? = null
    }

    override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder
        var convertView = _convertView;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.feedback_card, parent, false)
            viewHolder = ViewHolder()
            viewHolder.avatar = convertView.findViewById(R.id.feedbackAvatar) as AvatarView
            viewHolder.name = convertView.findViewById(R.id.feedbackName) as TextView
            viewHolder.text = convertView.findViewById(R.id.feedbackText) as TextView
            viewHolder.ratingBar = convertView.findViewById(R.id.feedbackRating) as RatingBar
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        if (adapterData != null) {
            val feedback = adapterData!![position]
            val image: Drawable
            if(feedback.from!!.photo != null) {
                image = Drawable.createFromStream(ByteArrayInputStream(feedback.from!!.photo), feedback.from!!.getDisplayName())
            } else {
                val drawable = IconicsDrawable(context);
                image = drawable.icon(FontAwesome.Icon.faw_user_circle).actionBar()
            }
            viewHolder.avatar!!.setImageDrawable(image)
            viewHolder.name!!.text = feedback.from!!.getDisplayName()
            viewHolder.text!!.text = StringUtils.capitalize(feedback.text)
            viewHolder.ratingBar!!.rating = feedback.rating
        }
        return convertView!!
    }

}
