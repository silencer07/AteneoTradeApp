package tradeapp.ateneo.edu.tradeapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.apache.commons.lang3.StringUtils

/**
 * Created by aldrin on 11/20/17.
 */

open class Category : RealmObject() {

    @PrimaryKey
    var name: String = StringUtils.EMPTY
        set(value){ field = value.toLowerCase() }

    var photo: String = StringUtils.EMPTY
        get(){ return "@drawable/" + field }

}