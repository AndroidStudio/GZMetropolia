package  pl.gzmetropolia.knox

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import pl.gzmetropolia.constants.Constants

class AdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        context.sendBroadcast(Intent(Constants.DEVICE_ADMIN_ENABLED))
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return "Device Admin is going to be disabled."
    }

    override fun onDisabled(context: Context, intent: Intent) {
        context.sendBroadcast(Intent(Constants.DEVICE_ADMIN_DISABLED))
    }
}
