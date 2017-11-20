package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by aldrin on 11/20/17.
 */

open class Bookmark(): RealmObject() {

    @PrimaryKey
    var uuid: UUID = UUID.randomUUID()

    @Required
    var user: User? = null

    @Required
    var product: Product? = null

}