package pl.gzmetropolia

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

@NonNull
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "UNCHECKED_CAST", "UNUSED")
fun AppCompatActivity.getApp(): GZMetropoliaApplication {
    return application as GZMetropoliaApplication
}

@Suppress("UNUSED")
fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

@Suppress("UNUSED")
fun AppCompatActivity.hideKeyboard() {
    currentFocus?.let {
        val manager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        manager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

private fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

@Suppress("UNUSED")
inline fun <reified T : Any> Context.launchActivity(init: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent)
}

fun Context.makeToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.isInternetConnection(): Boolean {
    val manager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
    val networkInfo = manager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}



