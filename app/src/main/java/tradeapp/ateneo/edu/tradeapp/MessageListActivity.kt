package tradeapp.ateneo.edu.tradeapp

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_message_list.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.adapters.MessagesOverviewAdapter
import tradeapp.ateneo.edu.tradeapp.model.UserMessage
import tradeapp.ateneo.edu.tradeapp.service.UserService

@EActivity(R.layout.activity_message_list)
open class MessageListActivity : AppCompatActivity() {

    @Bean
    lateinit var userService: UserService

    @AfterViews
    open fun setupMessageListView(){
        val loggedInUser =  userService.getLoggedInUser()
        val realm = Realm.getDefaultInstance()
        val messageGroupedBySender = realm.where(UserMessage::class.java)
                .equalTo("to.username", loggedInUser!!.username)
                .findAllSorted("dateCreated")
                .groupBy { m -> m.from!!.username }
                .map { map -> map.value }

        val latestMessageUuids = arrayListOf<String>()
        for(messages in messageGroupedBySender){
            latestMessageUuids.add(
                    messages.maxBy { m -> m.dateCreated }!!.uuid
            )
        }

        val userMessages = realm.where(UserMessage::class.java).`in`("uuid", latestMessageUuids.toTypedArray()).findAllSorted("dateCreated")
        messageList.adapter = MessagesOverviewAdapter(this, userMessages)
        messageList.layoutManager = LinearLayoutManager(this);
    }

}
