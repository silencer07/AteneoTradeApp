package tradeapp.ateneo.edu.tradeapp

import android.app.Application

import org.androidannotations.annotations.EApplication

import io.realm.Realm

/**
 * Created by aldrin on 11/19/17.
 */

@EApplication
open class TradeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}

