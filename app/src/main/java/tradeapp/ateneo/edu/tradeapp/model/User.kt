package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.apache.commons.lang3.StringUtils

/**
 * Created by aldrin on 11/20/17.
 */

open class User : RealmObject() {

    @PrimaryKey
    var username: String = StringUtils.EMPTY
        set(value){ field = value.toLowerCase() }

    @Required
    var password: String = StringUtils.EMPTY

    var studentId: Long = 0

    var name: String = StringUtils.EMPTY
        set(value){ field = value.toLowerCase() }

    var degree: String? = null


    var photo: ByteArray? = null

    fun getDisplayName(): String{
        return if(StringUtils.isNotBlank(name)) name else username
    }
}