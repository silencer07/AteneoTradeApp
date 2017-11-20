package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by aldrin on 11/20/17.
 */

open class Feedback(): RealmObject() {

    companion object {
        val MAX_RATING = 5
    }

    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()

    var from: User? = null

    var to: User? = null

    @Required
    var text: String = StringUtils.EMPTY

    var rating = Feedback.MAX_RATING

    var dateCreated = Date()
    
}