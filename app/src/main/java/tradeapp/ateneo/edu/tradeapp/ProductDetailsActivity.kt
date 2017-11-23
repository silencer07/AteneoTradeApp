package tradeapp.ateneo.edu.tradeapp

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.synnapps.carouselview.CarouselView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_product_details.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import org.androidannotations.annotations.ViewById
import tradeapp.ateneo.edu.tradeapp.model.Product
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat


@EActivity(R.layout.activity_product_details)
open class ProductDetailsActivity : AppCompatActivity() {

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

    @AfterViews
    fun initActivity(){
        val product = Realm.getDefaultInstance().where(Product::class.java).equalTo("uuid", productUuid).findFirst()
        carousel.pageCount = product!!.photos.size

        carousel.setImageListener({ position: Int, imageView: ImageView ->
            val image = Drawable.createFromStream(ByteArrayInputStream(product.photos[position]), product.title)
            imageView.setImageDrawable(image);
            imageView.scaleType = ImageView.ScaleType.FIT_XY;
        })

        productTitle.text = product.title
        postedBy.text = product.user!!.getDisplayName()
        datePosted.text = SimpleDateFormat("MM/dd/yyyy").format(product.dateCreated)
        price.text = String.format("%.2f", product.price)
        reservedTo.text = if(product.reservedTo != null) product.reservedTo!!.getDisplayName() else "none"
        description.text = product.description
    }
}
