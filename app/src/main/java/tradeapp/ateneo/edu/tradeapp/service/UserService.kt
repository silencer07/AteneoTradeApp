package tradeapp.ateneo.edu.tradeapp.service

import io.realm.Case
import io.realm.Realm
import org.androidannotations.annotations.EBean
import tradeapp.ateneo.edu.tradeapp.model.ApplicationData
import tradeapp.ateneo.edu.tradeapp.model.User

@EBean
open class UserService {
    fun getLoggedInUser(): User?{
        val appData = Realm.getDefaultInstance().where(ApplicationData::class.java).equalTo("id", 1L).findFirst()
        return if(appData != null) appData.loggedInUser else null
    }

    fun loginOrCreate(username: String, password: String){
        val realm = Realm.getDefaultInstance();
        val user = realm.where(User::class.java).equalTo("username", username.toLowerCase()).findFirst();
        if(user != null){
            if(user.password.equals(password)){
                val appData = realm.where(ApplicationData::class.java).equalTo("id", 1L).findFirst() ?: ApplicationData();
                realm.executeTransaction { realm ->
                    appData.loggedInUser = user;
                    realm.copyToRealmOrUpdate(appData)
                }
            } else {
                throw AuthenticationException("Username or password incorrect");
            }
        } else {
            realm.executeTransaction { realm ->
                val newUser =  User();
                newUser.username = username
                newUser.password = password
                realm.copyToRealm(newUser)
            }
        }
    }

    fun logoutUser(){
        val realm = Realm.getDefaultInstance()
        val appData = realm.where(ApplicationData::class.java).equalTo("id", 1L).findFirst()
        if(appData != null) {
            realm.executeTransaction { realm ->
                appData.loggedInUser = null
                realm.copyToRealmOrUpdate(appData)
            }
        }
    }
}
