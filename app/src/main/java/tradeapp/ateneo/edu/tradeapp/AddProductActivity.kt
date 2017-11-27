package tradeapp.ateneo.edu.tradeapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import io.realm.Case
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import tradeapp.ateneo.edu.tradeapp.model.Category
import kotlinx.android.synthetic.main.activity_add_product.*
import org.androidannotations.annotations.*
import org.apache.commons.lang3.StringUtils
import tradeapp.ateneo.edu.tradeapp.model.Product
import tradeapp.ateneo.edu.tradeapp.service.UserService
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.RoundingMode
import java.text.DecimalFormat

@EActivity(R.layout.activity_add_product)
open class AddProductActivity : AppCompatActivity() {

    @Bean
    protected lateinit var userService: UserService

    @JvmField
    @Extra
    protected var productUuid: String = StringUtils.EMPTY

    private val imageByteArraysToBeShown = ArrayList<ByteArray>()

    @AfterViews
    fun setupCategories(){
        val categories: RealmResults<Category> = Realm.getDefaultInstance().where(Category::class.java).findAll()
        val names = categories.map { category -> StringUtils.capitalize(category.name)  }
        addProductCategorySpinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, names)
    }

    @AfterViews
    fun setupProduct(){
        val p = getProduct()
        if(p != null){
            addProductTitle.append(p.title)
            addProductPrice.append(p.price.toString())
            addProductDescription.append(p.description)

            for (i in 0 until addProductCategorySpinner.getCount()) {
                if (addProductCategorySpinner.getItemAtPosition(i).toString().equals(p.category!!.name, true)) {
                    addProductCategorySpinner.setSelection(i)
                    break
                }
            }

            imageByteArraysToBeShown.addAll(p.photos)
            showImagesToCarousel()

            addProductButton.text = "Update Item"
        } else {
            addProductReserveButton.visibility = Button.INVISIBLE
        }
    }

    @Click(R.id.addProductCarouselView)
    open fun addImageToCarousel(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AccountDetailsActivity.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == AccountDetailsActivity.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && intent != null) {
            if(intent.data != null){
                val uri = intent.data
                addToImageByteArraysToBeShown(uri)
            } else if (intent.getClipData() != null){
                val mClipData = intent.getClipData();
                for (i in 0 until mClipData.getItemCount()) {
                    val uri = mClipData.getItemAt(i).getUri();
                    addToImageByteArraysToBeShown(uri)
                }
            }

            showImagesToCarousel()
        }
    }

    private fun showImagesToCarousel() {
        if (imageByteArraysToBeShown.isNotEmpty()) {
            addProductCarouselView.setImageListener({ position: Int, imageView: ImageView ->
                val image = Drawable.createFromStream(ByteArrayInputStream(imageByteArraysToBeShown.get(position)), "Image " + position)
                imageView.setImageDrawable(image);
                imageView.scaleType = ImageView.ScaleType.FIT_XY;
            })

            addProductCarouselView.background = null
            addProductCarouselView.pageCount = imageByteArraysToBeShown.size
        }
    }

    private fun addToImageByteArraysToBeShown(uri: Uri?) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        imageByteArraysToBeShown.add(bos.toByteArray())
    }

    @Click(R.id.addProductButton)
    open fun postItem(){
        if(imageByteArraysToBeShown.isEmpty()){
            Toast.makeText(this, "Please select pictures for the posting", Toast.LENGTH_SHORT).show()
            return
        }
        var focusView: View? = null
        if(StringUtils.isEmpty(addProductTitle.text.toString())){
            addProductTitle.error = "Title is required"
            focusView = addProductTitle
        } else if(StringUtils.isEmpty(addProductPrice.text.toString())){
            addProductPrice.error = "Price is required"
            focusView = addProductPrice
        }

        if(focusView != null){
            focusView.requestFocus()
            return
        }

        Realm.getDefaultInstance().executeTransaction { realm ->
            val product = getProduct() ?: Product()
            product.title = addProductTitle.text.toString()
            product.description = addProductDescription.text.toString()

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            product.price = df.format(addProductPrice.text.toString().toFloat()).toFloat()

            product.photos.clear()
            product.photos.addAll(imageByteArraysToBeShown)


            product.user = userService.getLoggedInUser()

            val category = realm.where(Category::class.java).equalTo("name", addProductCategorySpinner.selectedItem.toString(), Case.INSENSITIVE).findFirst()
            product.category = category

            realm.copyToRealm(product)
        }
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getProduct(): Product?{
        if(StringUtils.isNotBlank(productUuid)){
            return Realm.getDefaultInstance().where(Product::class.java).equalTo("uuid", productUuid).findFirst()
        }
        return null
    }
}
