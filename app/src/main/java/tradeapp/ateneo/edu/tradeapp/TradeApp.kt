package tradeapp.ateneo.edu.tradeapp

import android.app.Application

import org.androidannotations.annotations.EApplication

import io.realm.Realm
import tradeapp.ateneo.edu.tradeapp.model.Category

/**
 * Created by aldrin on 11/19/17.
 */

@EApplication
open class TradeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        initCategories()
    }

    fun initCategories() {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            val apparels = realm.createObject(Category::class.java, "apparels")
            apparels.photo = apparels.name + ".jpg"

            val books = realm.createObject(Category::class.java, "books")
            books.photo = books.name + ".jpg"

            val electronics = realm.createObject(Category::class.java, "electronics")
            electronics.photo = electronics.name + ".jpg"

            val pets = realm.createObject(Category::class.java, "pets")
            pets.photo = pets.name + ".jpg"

            val tickets = realm.createObject(Category::class.java, "tickets")
            tickets.photo = tickets.name + ".jpg"

            val others = realm.createObject(Category::class.java, "others")
            others.photo = others.name + ".png"
        }

    }
}

