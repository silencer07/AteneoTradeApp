package tradeapp.ateneo.edu.tradeapp

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.widget.*
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.synnapps.carouselview.CarouselView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_product_details.*
import org.androidannotations.annotations.*
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.adapters.CommentCardAdapter
import tradeapp.ateneo.edu.tradeapp.model.ActivityWithIconicsContext
import tradeapp.ateneo.edu.tradeapp.model.Bookmark
import tradeapp.ateneo.edu.tradeapp.model.Product
import tradeapp.ateneo.edu.tradeapp.model.ProductComment
import tradeapp.ateneo.edu.tradeapp.service.UserService
import java.io.ByteArrayInputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


@EActivity(R.layout.activity_product_details)
open class ProductDetailsActivity : ActivityWithIconicsContext() {

    @Extra
    lateinit var productUuid: String

    @ViewById(R.id.carouselView)
    lateinit var carousel: CarouselView

    @ViewById(R.id.productTitleText)
    lateinit var productTitle: TextView

    @ViewById(R.id.postedByText)
    lateinit var postedBy: TextView

    @ViewById(R.id.createdDateText)
    lateinit var datePosted: TextView

    @ViewById(R.id.priceText)
    lateinit var price: TextView

    @ViewById(R.id.reservedToText)
    lateinit var reservedTo: TextView

    @ViewById(R.id.descriptionText)
    lateinit var description: TextView

    @ViewById(R.id.commentList)
    lateinit var commentList: RecyclerView

    @ViewById(R.id.addCommentLabel)
    lateinit var addCommentLabel: TextView

    @ViewById(R.id.addCommentText)
    lateinit var addCommentText: EditText

    @ViewById(R.id.productDetailsNavigation)
    lateinit var navigation: BottomNavigationView

    @ViewById(R.id.scrollView)
    lateinit var scrollView: NestedScrollView

    @ViewById(R.id.bookmarkButton)
    lateinit var bookmarkButton: Button

    @Bean
    lateinit var userService: UserService

    companion object {
        val UPDATE_PRODUCT = 2
    }

    @AfterViews
    fun initActivity(){
        val product = getProduct()
        carousel.pageCount = product!!.photos.size

        carousel.setImageListener({ position: Int, imageView: ImageView ->
            val image = Drawable.createFromStream(ByteArrayInputStream(product.photos[position]), product.title)
            imageView.setImageDrawable(image);
            imageView.scaleType = ImageView.ScaleType.FIT_XY;
        })

        productTitle.text = product.title
        postedBy.text = product.user!!.getDisplayName()
        datePosted.text = SimpleDateFormat("MM/dd/yyyy").format(product.dateCreated)
        price.text = DecimalFormat("#,###.##").format(product.price)
        reservedTo.text = if(product.reservedTo != null) product.reservedTo!!.getDisplayName() else "none"
        description.text = product.description
    }

    @AfterViews
    fun hideIfNotLoggedIn(){
        if(userService.getLoggedInUser() == null){
            addCommentLabel.visibility = View.GONE
            addCommentText.visibility = View.GONE
            addCommentBtn.visibility = View.GONE
        }
    }

    @AfterViews
    @UiThread
    open fun initComments(){
        commentList.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(this);
        commentList.layoutManager = linearLayoutManager;

        val realm = Realm.getDefaultInstance()
        val comments: RealmResults<ProductComment> = realm.where(ProductComment::class.java)
                .equalTo("product.uuid", productUuid)
                .findAllSorted("dateCreated")
        commentList.adapter = CommentCardAdapter(baseContext, comments)
        commentList.isScrollContainer = false
    }

    private fun getProduct(): Product?{
        return Realm.getDefaultInstance().where(Product::class.java).equalTo("uuid", productUuid).findFirst()
    }

    @Click(R.id.addCommentBtn)
    open fun saveComment(){
        val commentStr = addCommentText.text.toString()
        if(StringUtils.isBlank(commentStr)){
            Toast.makeText(this,"Cannot add empty comment", Toast.LENGTH_SHORT).show()
        } else {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction { realm ->
                val product = getProduct()
                val comment: ProductComment = realm.createObject(ProductComment::class.java, UUID.randomUUID().toString())
                comment.text = commentStr
                comment.product = product
                comment.user = userService.getLoggedInUser()

                realm.copyToRealm(product)
            }

            val comments: RealmResults<ProductComment> = realm.where(ProductComment::class.java)
                    .equalTo("product.uuid", productUuid)
                    .findAllSorted("dateCreated")
            commentList.adapter = CommentCardAdapter(baseContext, comments)

            Toast.makeText(this,"comment added successfully", Toast.LENGTH_SHORT).show()
            addCommentText.text.clear()
        }
    }

    @AfterViews
    fun setupBottomNavigation(){
        if(getProduct()!!.user?.equals(userService.getLoggedInUser())!!){
            navigation.menu.getItem(0).setIcon(IconicsDrawable(this).icon(FontAwesome.Icon.faw_pencil).actionBar())

            if(getProduct()!!.sold) {
                navigation.menu.removeItem(R.id.navigation_product_details_delete)
            } else {
                navigation.menu.getItem(1).setIcon(IconicsDrawable(this).icon(FontAwesome.Icon.faw_trash).actionBar())
            }

            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        } else {
            navigation.visibility = BottomNavigationView.INVISIBLE

            val p = scrollView.layoutParams as CoordinatorLayout.LayoutParams
            p.bottomMargin = 0
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_product_details_edit -> {
                val i = Intent(this, AddProductActivity_::class.java)
                i.putExtra("productUuid", getProduct()!!.uuid)
                this.startActivityForResult(i, UPDATE_PRODUCT)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_product_details_delete -> {
                val dialogClickListener = DialogInterface.OnClickListener() { dialog: DialogInterface, which: Int ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            Realm.getDefaultInstance().executeTransaction { realm ->
                                getProduct()!!.deleteFromRealm()
                                finish()
                            }
                        }
                    }
                };

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == UPDATE_PRODUCT && resultCode == Activity.RESULT_OK) {
            initActivity()
        }
    }

    @AfterViews
    open fun setupBookmarkButton(){
        if(userService.getLoggedInUser() != null) {
            var drawable = IconicsDrawable(this)
            drawable = drawable.icon(FontAwesome.Icon.faw_heart).actionBar()

            val isBookmarked = Realm.getDefaultInstance().where(Bookmark::class.java)
                    .equalTo("product.uuid", getProduct()!!.uuid)
                    .equalTo("user.username", userService.getLoggedInUser()!!.username)
                    .count() > 0

            val colorId = if(isBookmarked) R.color.colorAccent else R.color.colorLight
            drawable = drawable.color(ContextCompat.getColor(this, colorId))

            bookmarkButton.background = drawable
        } else {
            bookmarkButton.visibility = View.GONE
        }

    }

    @Click(R.id.bookmarkButton)
    open fun bookmark(){
        val product = getProduct()
        val loggedInUser = userService.getLoggedInUser()
        val drawable = bookmarkButton.background as IconicsDrawable
        Realm.getDefaultInstance().executeTransaction { realm ->
            var bookmark = Realm.getDefaultInstance().where(Bookmark::class.java)
                    .equalTo("product.uuid", product!!.uuid)
                    .equalTo("user.username", loggedInUser!!.username)
                    .findFirst()
            if(bookmark != null){
                bookmark.deleteFromRealm()
                drawable.color(ContextCompat.getColor(this, R.color.colorLight))
            } else {
                bookmark = Bookmark()
                bookmark.user = loggedInUser
                bookmark.product = product
                realm.copyToRealmOrUpdate(bookmark)
                drawable.color(ContextCompat.getColor(this, R.color.colorAccent))
            }
        }
    }

    @Click(R.id.postedByText)
    fun showMessages(){
        if(userService.getLoggedInUser() != null) {
            val i = Intent(this, MessageActivity_::class.java)
            i.putExtra("username", getProduct()!!.user!!.username)
            startActivity(i)
        }
    }
}
