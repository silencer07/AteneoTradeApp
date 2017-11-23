package tradeapp.ateneo.edu.tradeapp.model

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.mikepenz.iconics.context.IconicsContextWrapper

/**
 * Created by aldrin on 11/23/17.
 */
abstract class ActivityWithIconicsContext : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

}