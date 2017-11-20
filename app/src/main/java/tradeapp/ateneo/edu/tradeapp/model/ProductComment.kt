package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by aldrin on 11/20/17.
 */

open class ProductComment(): RealmObject() {

    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()

    var user: User? = null

    var product: Product? = null

    var text: String = StringUtils.EMPTY

    var dateCreated = Date()
}