package tradeapp.ateneo.edu.tradeapp.init

import android.content.Context
import io.realm.Realm
import org.apache.commons.io.IOUtils
import tradeapp.ateneo.edu.tradeapp.model.Category
import tradeapp.ateneo.edu.tradeapp.model.Product
import tradeapp.ateneo.edu.tradeapp.model.ProductComment
import tradeapp.ateneo.edu.tradeapp.model.User

/**
 * Created by aldrin on 11/20/17.
 */

open class SampleDataInit(val context: Context) {

    private lateinit var aldrin: User
    private lateinit var nadine: User
    private lateinit var unverifiedUser: User

    fun init(){
        initUsers()
        initSampleProducts()
    }

    private fun initUsers(){
        Realm.getDefaultInstance().executeTransaction { realm ->
            aldrin = realm.where(User::class.java).equalTo("name", "aldrin").findFirst() ?: User()
            nadine = realm.where(User::class.java).equalTo("name", "nadine").findFirst() ?: User()
            unverifiedUser = realm.where(User::class.java).equalTo("name", "unverifiedUser").findFirst() ?: User()

            if(realm.where(User::class.java).count() == 0L){
                aldrin.username = "aldrin"
                aldrin.password = "password"
                aldrin.studentId = 147567
                aldrin.name = "Aldrin Tingson"
                aldrin.degree = "MSIT"
                aldrin.photo = toBytes("james-reid.jpg")
                realm.copyToRealmOrUpdate(aldrin)

                nadine.username = "nadine"
                nadine.password = "password"
                nadine.studentId = 147568
                nadine.name = "Nadine Lustre"
                nadine.degree = "BSCS"
                nadine.photo = toBytes("nadine-lustre.jpg")
                realm.copyToRealmOrUpdate(nadine)

                unverifiedUser.username = "unverifiedUser"
                unverifiedUser.password = "password"
                realm.copyToRealmOrUpdate(unverifiedUser)
            }
        }
    }

    private fun toBytes(fileName: String): ByteArray {
        val assets = context.assets
        return IOUtils.toByteArray(assets.open(fileName))
    }

    private fun initSampleProducts(){
        val assets = context.assets
        val realm = Realm.getDefaultInstance();

        val animals = realm.where(Category::class.java).equalTo("name", "animals").findFirst()
        val electronics = realm.where(Category::class.java).equalTo("name", "electronics").findFirst()

        realm.executeTransaction { realm ->
            if(realm.where(Product::class.java).count() == 0L){
                val dog = Product()
                dog.title = "Dogs"
                dog.user = nadine
                dog.category = animals
                dog.description = "Adorable dogs for sale!! Completely vaccinated and neutered."
                dog.photos.add(toBytes("dog.jpg"))
                dog.photos.add(toBytes("dog2.jpg"))
                dog.price = 2_000f
                realm.copyToRealmOrUpdate(dog)

                val cat = Product()
                cat.title = "Kitteh"
                cat.user = nadine
                cat.category = animals
                cat.description = "Purry friends that will lighten up your day!"
                cat.photos.add(toBytes("kitten.jpg"))
                cat.price = 1_000f
                realm.copyToRealmOrUpdate(cat)

                val productComment1 = ProductComment()
                productComment1.user = unverifiedUser
                productComment1.product = cat
                productComment1.text = "They are adorable. How can I avail?"
                realm.copyToRealmOrUpdate(productComment1)

                val productComment2 = ProductComment()
                productComment2.user = nadine
                productComment2.product = cat
                productComment2.text = "Please contact me in 0908-12345678 for more details"
                realm.copyToRealmOrUpdate(productComment2)

                val productComment3 = ProductComment()
                productComment3.user = aldrin
                productComment3.product = cat
                productComment3.text = "I want to avail this too!"
                realm.copyToRealmOrUpdate(productComment3)

                val iphone = Product()
                iphone.title = "iPhone X"
                iphone.user = nadine
                iphone.category = electronics
                iphone.description = "Get yours while supplies last!"
                iphone.photos.add(toBytes("iphone-x.jpg"))
                iphone.price = 50_000f
                iphone.reservedTo = aldrin
                realm.copyToRealmOrUpdate(iphone)

                val productComment4 = ProductComment()
                productComment4.user = aldrin
                productComment4.product = iphone
                productComment4.text = "Reserve to me, I will be getting this one too."
                realm.copyToRealmOrUpdate(productComment4)

                val macbook = Product()
                macbook.title = "Macbook Pro 2017 touchbar"
                macbook.user = nadine
                macbook.category = electronics
                macbook.description = "Brand new and sealed with 1 year official Apple Warranty!"
                macbook.photos.add(toBytes("macbook.jpg"))
                macbook.price = 150_000f
                macbook.reservedTo = aldrin
                macbook.sold = true
                realm.copyToRealmOrUpdate(macbook)

                val productComment5 = ProductComment()
                productComment5.user = aldrin
                productComment5.product = macbook
                productComment5.text = "Reserve to me, I will be getting this one."
                realm.copyToRealmOrUpdate(productComment5)
            }
        }
    }
}
