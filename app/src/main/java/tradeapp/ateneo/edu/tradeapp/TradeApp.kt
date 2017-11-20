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
        Realm.getDefaultInstance().executeTransaction { realm: Realm ->
            val apparels = Category()
            apparels.name = "apparels"
            apparels.photo = apparels.name
            realm.copyToRealmOrUpdate(apparels)

            val books = Category()
            books.name = "books"
            books.photo = books.name
            realm.copyToRealmOrUpdate(books)

            val electronics = Category()
            electronics.name = "electronics"
            electronics.photo = electronics.name
            realm.copyToRealmOrUpdate(electronics)

            val pets = Category()
            pets.name = "pets"
            pets.photo = pets.name
            realm.copyToRealmOrUpdate(pets)

            val tickets = Category()
            tickets.name = "tickets"
            tickets.photo = tickets.name
            realm.copyToRealmOrUpdate(tickets)

            val others = Category()
            others.name = "others"
            others.photo = others.name
            realm.copyToRealmOrUpdate(others)
        }

    }
}

