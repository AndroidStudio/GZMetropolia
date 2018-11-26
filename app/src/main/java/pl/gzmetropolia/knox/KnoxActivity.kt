package  pl.gzmetropolia.knox

import android.annotation.SuppressLint
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.app.enterprise.EnterpriseDeviceManager
import android.app.enterprise.kioskmode.KioskMode
import android.app.enterprise.knoxcustom.KnoxCustomManager
import android.app.enterprise.license.EnterpriseLicenseManager
import android.content.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sec.enterprise.knox.license.KnoxEnterpriseLicenseManager
import kotlinx.android.synthetic.main.knox_activity.*
import pl.gzmetropolia.R
import pl.gzmetropolia.constants.Constants
import pl.gzmetropolia.isInternetConnection
import pl.gzmetropolia.makeToast
import timber.log.Timber
import java.util.*

class KnoxActivity : AppCompatActivity() {

    private val mKnoxLicenseIntentFilter = object : IntentFilter() {
        init {
            addAction(Constants.DEVICE_ADMIN_ENABLED)
            addAction(Constants.DEVICE_ADMIN_DISABLED)
            addAction(EnterpriseLicenseManager.ACTION_LICENSE_STATUS)
            addAction(KnoxEnterpriseLicenseManager.ACTION_LICENSE_STATUS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.knox_activity)
        registerReceiver(this.knoxBroadcastReceiver, mKnoxLicenseIntentFilter)

        if (!isSamsungDevice) {
            makeToast("Knox is not supported")
            finish()
            return
        }

        enableSealedModeButton.setOnClickListener { enableKnox(this, true) }
        enableLicenceButton.setOnClickListener {
            showProgressBar(true)
            activeKnoxLicense()
        }
        enableDeviceAdminButton.setOnClickListener { enableDeviceAdmin() }
    }

    override fun onResume() {
        super.onResume()
        updateViewState()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(this.knoxBroadcastReceiver)
    }

    private val knoxBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(pContext: Context, intent: Intent) {
            updateLicense(intent)
            updateViewState()
        }
    }

    private fun updateLicense(intent: Intent) {
        val action = intent.action ?: return
        when (action) {
            KnoxEnterpriseLicenseManager.ACTION_LICENSE_STATUS -> {
                val status = intent.getStringExtra(EnterpriseLicenseManager.EXTRA_LICENSE_STATUS)
                if (status == null || status == "fail") {
                    showProgressBar(false)
                }
                Timber.d("ELM: %s", status)
            }
            EnterpriseLicenseManager.ACTION_LICENSE_STATUS -> {
                val status = intent.getStringExtra(KnoxEnterpriseLicenseManager.EXTRA_LICENSE_STATUS)
                if (status == null || status == "fail") {
                    showProgressBar(false)
                }
                Timber.d("KELM %s", status)
            }
            else -> {

            }
        }
    }

    private fun updateViewState() {
        if (!isDeviceAdminEnabled(this)) {
            enableDeviceAdminButton.isEnabled = true
            enableLicenceButton.isEnabled = false
            enableSealedModeButton.isEnabled = false
            return
        } else {
            enableDeviceAdminButton.isEnabled = false
        }

        if (!isKnoxLicenceActivated) {
            enableLicenceButton.isEnabled = true
            enableSealedModeButton.isEnabled = false
            return
        } else {
            enableLicenceButton.isEnabled = false
            showProgressBar(false)
        }
        enableSealedModeButton.isEnabled = true
    }

    private fun showProgressBar(show: Boolean) {
        try {
            if (show) {
                findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
            } else {
                findViewById<View>(R.id.progressBar).visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun enableKnox(context: Context, finishActivity: Boolean) {
        if (!isSamsungDevice) {
            makeToast("Knox is not supported")
            return
        }
        if (!isDeviceAdminEnabled(context)) {
            makeToast("Knox disabled, please enable device admin")
            return
        }
        if (!isKnoxLicenceActivated) {
            makeToast("Knox disabled, please active knox license")
            return
        }
        enableSealedMode(context, finishActivity)
    }

    private fun enableDeviceAdmin() {
        val deviceAdmin = ComponentName(this, AdminReceiver::class.java)
        try {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun activeKnoxLicense() {
        if (!isInternetConnection()) {
            makeToast("Brak dostępu do internetu")
            showProgressBar(false)
            return
        }

        val knoxEnterpriseLicenseManager = KnoxEnterpriseLicenseManager.getInstance(applicationContext)
        val knoxLicenseKey = "KLM03-NTZB0-5J566-77DHN-0WC47-ABET1"
        knoxEnterpriseLicenseManager.activateLicense(knoxLicenseKey)

        val enterpriseLicenseManager = EnterpriseLicenseManager.getInstance(applicationContext)
        val licenseKey =
            "1211D072A3AF5EF71BD6C6DE6154C732EA4AE0A3FF7B5F758693C55BA06956AB2613D713D2DF52C92A403EAA72F600AA34633A487BA774ADADEAB4ED188CDE6C"
        enterpriseLicenseManager.activateLicense(licenseKey)
    }

    private fun isDeviceAdminEnabled(pContext: Context): Boolean {
        val mDeviceAdmin = ComponentName(pContext, AdminReceiver::class.java)
        val mDevicePolicyManager = pContext.getSystemService(Context.DEVICE_POLICY_SERVICE)
                as DevicePolicyManager
        val mDeviceAdminActive = mDevicePolicyManager.isAdminActive(mDeviceAdmin)

        Timber.d("isDeviceAdminEnabled: %b", mDeviceAdminActive)
        return mDeviceAdminActive
    }

    @Suppress("DEPRECATION")
    private val isKnoxLicenceActivated: Boolean
        get() {
            val knoxManager = KnoxCustomManager.getInstance()
            val sealedMode = "com.sec.enterprise.knox.permission.CUSTOM_SEALEDMODE"
            val hwControl = "android.permission.sec.MDM_HW_CONTROL"

            val enterprisePermission = knoxManager.checkEnterprisePermission(sealedMode)
            val knoxEnterprisePermission = knoxManager.checkEnterprisePermission(hwControl)
            return if (enterprisePermission && knoxEnterprisePermission) {
                Timber.d("isKnoxLicenceActivated: %b", true)
                true
            } else {
                Timber.d("isKnoxLicenceActivated:  %b", false)
                false
            }
        }


    @Suppress("unused", "DEPRECATION")
    companion object {
        val isSealedModeState: Boolean
            get() {
                if (!isSamsungDevice) {
                    return false
                }

                val knoxCustomManager = KnoxCustomManager.getInstance()
                val sealedState = knoxCustomManager.sealedState
                Timber.d("sealedState %b", sealedState)
                return sealedState
            }

        private val isSamsungDevice: Boolean
            get() = android.os.Build.MANUFACTURER == "samsung"
    }

    @Suppress("DEPRECATION")
    @SuppressLint("WrongConstant")
    private fun enableSealedMode(context: Context, finishActivity: Boolean) {
        try {
            val appContext = context.applicationContext
            ArrayList<Int>().apply {
                add(26)
                KioskMode.getInstance(appContext).allowHardwareKeys(this, true)
            }

            val knoxManager = KnoxCustomManager.getInstance()

            knoxManager.setSealedModeString(KnoxCustomManager.SEALED_ON_STRING, "Tablet zablokowany")
            knoxManager.setAutoRotationState(true, KnoxCustomManager.SENSOR_ORIENTATION)
            knoxManager.setSealedState(true, SealedStateUtils.KNOX_EXIT_PASSWORD)
            knoxManager.sealedPowerDialogOptionMode = KnoxCustomManager.SHOW
            knoxManager.sealedHomeActivity = appContext.packageName
            knoxManager.cpuPowerSavingState = false
            knoxManager.setMultiWindowState(false)
            knoxManager.removeLockScreen()

            val deviceManager = appContext
                .getSystemService(EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE)
                    as EnterpriseDeviceManager

            deviceManager.dateTimePolicy.timeZone = "Europe/Warsaw"
            deviceManager.applicationPolicy.disableAndroidMarket()

            val restrictionPolicy = deviceManager.restrictionPolicy
            restrictionPolicy.allowFactoryReset(false)
            restrictionPolicy.allowPowerOff(false)
            restrictionPolicy.allowSafeMode(false)
            restrictionPolicy.setHomeKeyState(false)
            restrictionPolicy.setWiFiState(true)

            Timber.d("knox v: %s", knoxManager.knoxCustomSdkVer.knoxCustomInternalSdkVer)
            Timber.d("SealedMode %s", "enabled")

            if (finishActivity && context is Activity) {
                context.makeToast("Knox aktywny")
                context.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
