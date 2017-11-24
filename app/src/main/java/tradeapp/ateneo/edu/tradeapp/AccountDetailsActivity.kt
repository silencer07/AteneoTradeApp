package tradeapp.ateneo.edu.tradeapp

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_account_details.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.service.UserService
import java.io.ByteArrayInputStream
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.ByteArrayOutputStream


@EActivity(R.layout.activity_account_details)
open class AccountDetailsActivity : AppCompatActivity() {

    companion object {
        val PICK_IMAGE_REQUEST = 1
    }

    @Bean
    protected lateinit var userService: UserService

    @AfterViews
    open fun setupView(){
        val user = userService.getLoggedInUser()
        usernameText.append(user!!.username)
        passwordText.append(user.password)
        studentIdText.append(if(user.studentId != 0L) user.studentId.toString() else "")
        nameText.append(user.name)
        degreeText.append(user.degree)

        val image:Drawable
        if(user.photo != null) {
            image = Drawable.createFromStream(ByteArrayInputStream(user.photo), user.getDisplayName())
        } else {
            val drawable = IconicsDrawable(this);
            image = drawable.icon(FontAwesome.Icon.faw_user_circle).actionBar()
        }

        userAvatar!!.setImageDrawable(image)
    }

    @Click(R.id.saveButton)
    open fun save(){
        if(passwordText.text.length <= 4){
            passwordText.error = "Too short"
            return
        }

        val user = userService.getLoggedInUser()
        Realm.getDefaultInstance().executeTransaction { realm ->
            user!!.password  = passwordText.text.toString()
            user.studentId = studentIdText.text.toString().toLong()
            user.name = nameText.text.toString()
            user.degree = degreeText.text.toString()
            realm.copyToRealmOrUpdate(user)


            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            Toast.makeText(this, "Save successful", Toast.LENGTH_SHORT).show()
        }

    }

    @Click(R.id.logoutButton)
    open fun logout(){
        userService.logoutUser()
        finish()
    }

    @Click(R.id.userAvatar)
    open fun changeAccountAvatar(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && intent != null && intent.data != null) {
            val uri = intent.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)

            val user = userService.getLoggedInUser()
            Realm.getDefaultInstance().executeTransaction { realm ->
                user!!.photo = bos.toByteArray()
                realm.copyToRealmOrUpdate(user)
            }

            val image = Drawable.createFromStream(ByteArrayInputStream(user!!.photo), user.getDisplayName())
            userAvatar.setImageDrawable(image)
        }
    }
}
