package tradeapp.ateneo.edu.tradeapp

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_message.*
import org.androidannotations.annotations.*
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.adapters.MessageListAdapter
import tradeapp.ateneo.edu.tradeapp.model.ProductComment
import tradeapp.ateneo.edu.tradeapp.model.User
import tradeapp.ateneo.edu.tradeapp.model.UserMessage
import tradeapp.ateneo.edu.tradeapp.service.UserService

@EActivity(R.layout.activity_message)
open class MessageActivity : AppCompatActivity() {

    @Extra
    lateinit var username: String

    @Bean
    lateinit var userService: UserService

    @AfterViews
    open fun setupMessageView(){
        val loggedInUser =  userService.getLoggedInUser()
        val realm = Realm.getDefaultInstance()
        val userMessages: RealmResults<UserMessage> = realm.where(UserMessage::class.java)
                .beginGroup()
                    .equalTo("from.username", username)
                    .equalTo("to.username", loggedInUser!!.username)
                .endGroup()
                .or()
                .beginGroup()
                    .equalTo("from.username", loggedInUser.username)
                    .equalTo("to.username", username)
                .endGroup()
                .findAllSorted("dateCreated")
        messageList.adapter = MessageListAdapter(this, userMessages)
        messageList.layoutManager = LinearLayoutManager(this);
    }


    @UiThread
    @Click(R.id.sendMessageButton)
    open fun sendMessage(){
        if(StringUtils.isNotBlank(messageText.text.toString())){
            Realm.getDefaultInstance().executeTransaction { realm ->
                val message = UserMessage()
                message.from = userService.getLoggedInUser()
                message.to = realm.where(User::class.java).equalTo("username", username).findFirst()
                message.text = messageText.text.toString()
                realm.copyToRealm(message)
            }
            setupMessageView()
            messageText.text.clear()
        } else {
            Toast.makeText(this, "Cannot send empty message", Toast.LENGTH_SHORT).show()
        }
    }

}
