package tradeapp.ateneo.edu.tradeapp.adapters

import android.content.Context
import android.content.Intent
import io.realm.OrderedRealmCollection
import tradeapp.ateneo.edu.tradeapp.MessageActivity_
import tradeapp.ateneo.edu.tradeapp.model.UserMessage

/**
 * Created by aldrin on 12/1/17.
 */
class MessagesOverviewAdapter(context: Context, userMessages: OrderedRealmCollection<UserMessage>): MessageListAdapter(context, userMessages) {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        super.onBindViewHolder(viewHolder, position)
        viewHolder.avatar!!.setOnClickListener {
            val i = Intent(context, MessageActivity_::class.java)
            val message = data!![position]
            i.putExtra("username", message.from!!.username)
            context.startActivity(i)
        }
    }

}