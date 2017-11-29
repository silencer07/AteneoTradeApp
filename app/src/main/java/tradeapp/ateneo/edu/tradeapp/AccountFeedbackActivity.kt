package tradeapp.ateneo.edu.tradeapp

import android.graphics.drawable.Drawable
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import io.realm.Realm
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.model.ActivityWithIconicsContext
import tradeapp.ateneo.edu.tradeapp.service.UserService
import java.io.ByteArrayInputStream
import kotlinx.android.synthetic.main.activity_account_feedback.*
import org.androidannotations.annotations.Extra
import tradeapp.ateneo.edu.tradeapp.model.User


@EActivity(R.layout.activity_account_feedback)
open class AccountFeedbackActivity : ActivityWithIconicsContext() {

    @Extra
    lateinit var username: String

    @Bean
    protected lateinit var userService: UserService

    @AfterViews
    open fun setupView(){
        val user = Realm.getDefaultInstance().where(User::class.java).equalTo("username", username).findFirst()
        usernameText.text = user!!.username

        val image:Drawable
        if(user.photo != null) {
            image = Drawable.createFromStream(ByteArrayInputStream(user.photo), user.getDisplayName())
        } else {
            val drawable = IconicsDrawable(this);
            image = drawable.icon(FontAwesome.Icon.faw_user_circle).actionBar()
        }

        userAvatar!!.setImageDrawable(image)
    }

}
