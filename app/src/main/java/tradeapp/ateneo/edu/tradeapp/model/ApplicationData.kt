package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ApplicationData(): RealmObject() {
    @PrimaryKey
    var id: Long = 1

    var loggedInUser: User? = null
}