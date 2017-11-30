package tradeapp.ateneo.edu.tradeapp

import android.graphics.drawable.Drawable
import android.support.percent.PercentRelativeLayout
import android.view.View
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_account_feedback.*
import org.androidannotations.annotations.*
import tradeapp.ateneo.edu.tradeapp.adapters.FeedbackCardAdapter
import tradeapp.ateneo.edu.tradeapp.model.ActivityWithIconicsContext
import tradeapp.ateneo.edu.tradeapp.model.Feedback
import tradeapp.ateneo.edu.tradeapp.model.Product
import tradeapp.ateneo.edu.tradeapp.model.User
import tradeapp.ateneo.edu.tradeapp.service.UserService
import java.io.ByteArrayInputStream


@EActivity(R.layout.activity_account_feedback)
open class AccountFeedbackActivity : ActivityWithIconicsContext() {

    @Extra
    lateinit var username: String

    @Bean
    protected lateinit var userService: UserService

    @UiThread
    @AfterViews
    open fun setupView(){
        val user = Realm.getDefaultInstance().where(User::class.java).equalTo("username", username).findFirst()
        usernameText.text = user!!.getDisplayName()

        val image:Drawable
        if(user.photo != null) {
            image = Drawable.createFromStream(ByteArrayInputStream(user.photo), user.getDisplayName())
        } else {
            val drawable = IconicsDrawable(this);
            image = drawable.icon(FontAwesome.Icon.faw_user_circle).actionBar()
        }

        userAvatar!!.setImageDrawable(image)

        val feedbacks = Realm.getDefaultInstance()
                .where(Feedback::class.java)
                .equalTo("to.username", username)
                .findAllSorted("dateCreated")
        val rating = feedbacks.map { f -> f.rating }.average()

        ratingBar.rating = if(!rating.isNaN()) rating.toFloat() else 3f

        feedbackList.adapter = FeedbackCardAdapter(baseContext, feedbacks)
    }

    @AfterViews
    open fun hideFeedbackContainerIfNecessary(){
        if(userService.getLoggedInUser() == null){
            hideFeedbackContainer()
            return
        }

        val loggedInUsername = userService.getLoggedInUser()!!.username
        if(loggedInUsername == username){
            hideFeedbackContainer()
            return
        }

        val realm = Realm.getDefaultInstance()

        val userFeedbackCount = realm
                .where(Feedback::class.java)
                .equalTo("from.username", loggedInUsername)
                .equalTo("to.username", username)
                .count()

        val boughtOrSold = realm.where(Product::class.java)
                //bought items
                .beginGroup()
                .equalTo("user.username", username)
                .equalTo("reservedTo.username", loggedInUsername)
                .equalTo("sold", true)
                .endGroup()

                .or()

                //sold items
                .beginGroup()
                .equalTo("reservedTo.username", username)
                .equalTo("user.username", loggedInUsername)
                .equalTo("sold", true)
                .endGroup()

                .findAllSorted("soldDate")

        val productWithNoFeedbackCount = boughtOrSold.size - userFeedbackCount
        if(productWithNoFeedbackCount > 0){
            val index = (boughtOrSold.size - productWithNoFeedbackCount - 1).toInt()
            val productWithNoFeedbackYet = boughtOrSold.get(index)
            addFeedbackText.hint = "Add Feedback for the item " + productWithNoFeedbackYet!!.title
        } else {
            hideFeedbackContainer()
        }
    }

    private fun hideFeedbackContainer(){
        addFeedbackContainer.visibility = View.GONE
        val params = mainLayout.layoutParams as PercentRelativeLayout.LayoutParams
        val info = params.percentLayoutInfo
        info.heightPercent = 0.85f
    }

}
