package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.math.BigDecimal
import java.util.*

/**
 * Created by aldrin on 11/20/17.
 */

open class Product : RealmObject() {

    @PrimaryKey
    var uuid: UUID = UUID.randomUUID()

    @Required
    var user: User? = null

    @Required
    var price: BigDecimal = BigDecimal.ZERO

    val dateCreated = Date()

    var sold: Boolean = false

    var reservedTo: User? = null

}