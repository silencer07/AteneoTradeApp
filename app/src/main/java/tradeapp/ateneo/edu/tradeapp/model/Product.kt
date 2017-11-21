package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal
import java.util.*

/**
 * Created by aldrin on 11/20/17.
 */

open class Product : RealmObject() {

    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()

    var user: User? = null

    var category: Category? = null

    var title: String = StringUtils.EMPTY

    var price = 0f

    var dateCreated = Date()

    var sold: Boolean = false

    var reservedTo: User? = null

    var photos: RealmList<ByteArray> = RealmList<ByteArray>();

    var description: String = StringUtils.EMPTY
}