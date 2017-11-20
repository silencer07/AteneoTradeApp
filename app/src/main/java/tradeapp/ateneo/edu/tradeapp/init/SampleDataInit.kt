package tradeapp.ateneo.edu.tradeapp.init

import android.content.Context
import io.realm.Realm
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import org.apache.commons.io.IOUtils
import tradeapp.ateneo.edu.tradeapp.model.User

/**
 * Created by aldrin on 11/20/17.
 */

open class SampleDataInit(val context: Context) {

    fun init(){
        initUsers()
    }

    private fun initUsers(){
        Realm.getDefaultInstance().executeTransaction { realm ->
            val assets = context.assets
            if(realm.where(User::class.java).count() == 0L){
                var aldrin = User()
                aldrin.username = "aldrin"
                aldrin.password = "password"
                aldrin.studentId = 147567
                aldrin.name = "Aldrin Tingson"
                aldrin.degree = "MSIT"
                aldrin.photo = IOUtils.toByteArray(assets.open("james-reid.jpg"))
                realm.copyToRealm(aldrin)

                var nadine = User()
                nadine.username = "nadine"
                nadine.password = "password"
                nadine.studentId = 147568
                nadine.name = "Nadine Lustre"
                nadine.degree = "BSCS"
                nadine.photo = IOUtils.toByteArray(assets.open("nadine-lustre.jpg"))
                realm.copyToRealm(nadine)

                val unverifiedUser = User();
                unverifiedUser.username = "unverifiedUser"
                unverifiedUser.password = "password"
                realm.copyToRealm(unverifiedUser)
            }
        }
    }
}
