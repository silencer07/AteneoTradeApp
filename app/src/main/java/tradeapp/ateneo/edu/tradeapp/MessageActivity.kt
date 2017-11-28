package tradeapp.ateneo.edu.tradeapp

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import kotlinx.android.synthetic.main.activity_message.*
import org.androidannotations.annotations.Bean
import tradeapp.ateneo.edu.tradeapp.adapters.MessageListAdapter
import tradeapp.ateneo.edu.tradeapp.model.ProductComment
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
//                .beginGroup()
//                    .equalTo("from.username", username)
//                    .equalTo("to.username", loggedInUser!!.username)
//                .endGroup()
//                .or()
//                .beginGroup()
//                    .equalTo("from.username", loggedInUser.username)
//                    .equalTo("to.username", username)
//                .endGroup()
                .findAllSorted("dateCreated")
        messageList.adapter = MessageListAdapter(this, userMessages)
        messageList.layoutManager = LinearLayoutManager(this);
    }


}
