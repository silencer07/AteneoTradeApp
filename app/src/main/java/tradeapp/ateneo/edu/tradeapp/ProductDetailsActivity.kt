package tradeapp.ateneo.edu.tradeapp

import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.synnapps.carouselview.CarouselView
import io.realm.Realm
import io.realm.RealmResults
import org.androidannotations.annotations.*
import tradeapp.ateneo.edu.tradeapp.adapters.CommentCardAdapter
import tradeapp.ateneo.edu.tradeapp.model.ActivityWithIconicsContext
import tradeapp.ateneo.edu.tradeapp.model.Product
import tradeapp.ateneo.edu.tradeapp.model.ProductComment
import java.io.ByteArrayInputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat


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
}
