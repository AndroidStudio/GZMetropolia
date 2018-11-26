package pl.gzmetropolia

import android.app.Application
import timber.log.Timber

class GZMetropoliaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
