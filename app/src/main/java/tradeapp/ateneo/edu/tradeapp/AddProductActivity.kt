package tradeapp.ateneo.edu.tradeapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ImageView
import io.realm.Realm
import io.realm.RealmResults
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.model.Category
import kotlinx.android.synthetic.main.activity_add_product.*
import org.androidannotations.annotations.Click
import org.apache.commons.lang3.StringUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@EActivity(R.layout.activity_add_product)
open class AddProductActivity : AppCompatActivity() {

    val imageByteArraysToBeShown = ArrayList<ByteArray>()

    @AfterViews
    fun setupCategories(){
        val categories: RealmResults<Category> = Realm.getDefaultInstance().where(Category::class.java).findAll()
        val names = categories.map { category -> StringUtils.capitalize(category.name)  }
        addProductCategorySpinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, names)
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

            if(imageByteArraysToBeShown.isNotEmpty()){
                addProductCarouselView.setImageListener({ position: Int, imageView: ImageView ->
                    val image = Drawable.createFromStream(ByteArrayInputStream(imageByteArraysToBeShown.get(position)), "Image " + position)
                    imageView.setImageDrawable(image);
                    imageView.scaleType = ImageView.ScaleType.FIT_XY;
                })

                addProductCarouselView.background = null
                addProductCarouselView.pageCount = imageByteArraysToBeShown.size
            }
        }
    }

    private fun addToImageByteArraysToBeShown(uri: Uri?) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        imageByteArraysToBeShown.add(bos.toByteArray())
    }

}
