package tradeapp.ateneo.edu.tradeapp.adapters

import agency.tango.android.avatarview.views.AvatarView
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.R
import tradeapp.ateneo.edu.tradeapp.model.ProductComment
import tradeapp.ateneo.edu.tradeapp.model.UserMessage
import java.io.ByteArrayInputStream

/**
 * Created by aldrin on 11/20/17.
 */

class MessageListAdapter(val context: Context, userMessages: OrderedRealmCollection<UserMessage>):
        RealmRecyclerViewAdapter<UserMessage, MessageListAdapter.ViewHolder>(userMessages, true) {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        internal var avatar: AvatarView? = null
        internal var commentName: TextView? = null
        internal var text: TextView? = null

        init {
            avatar = view.findViewById(R.id.commentAvatar) as AvatarView
            commentName = view.findViewById(R.id.commentName) as TextView
            text = view.findViewById(R.id.commentText) as TextView
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val message = data!![position]
        val image: Drawable
        if(message.from!!.photo != null) {
            image = Drawable.createFromStream(ByteArrayInputStream(message.from!!.photo), message.from!!.getDisplayName())
        } else {
            val drawable = IconicsDrawable(context);
            image = drawable.icon(FontAwesome.Icon.faw_user_circle).actionBar()
        }
        viewHolder.avatar!!.setImageDrawable(image)
        viewHolder.commentName!!.text = message.from!!.getDisplayName()
        viewHolder.text!!.text = StringUtils.capitalize(message.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val commentCard = LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_card, parent, false)
        return ViewHolder(commentCard)
    }
}
