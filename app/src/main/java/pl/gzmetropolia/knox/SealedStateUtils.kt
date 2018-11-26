package  pl.gzmetropolia.knox

import android.app.enterprise.knoxcustom.CustomDeviceManager

internal object SealedStateUtils {

    const val KNOX_EXIT_PASSWORD = "switch123"

    fun getResultString(pResult: Int): String {
        val mResult: String
        when (pResult) {
            CustomDeviceManager.SUCCESS -> mResult = "SUCCESS"
            CustomDeviceManager.FAIL -> mResult = "FAIL"
            CustomDeviceManager.PRO_KIOSK_NOT_ACTIVE -> mResult = "PRO_KIOSK_NOT_ACTIVE"
            CustomDeviceManager.PRO_KIOSK_ACTIVE -> mResult = "PRO_KIOSK_ACTIVE"
            CustomDeviceManager.PERMISSION_DENIED -> mResult = "PERMISSION_DENIED"
            CustomDeviceManager.BUSY -> mResult = "BUSY"
            CustomDeviceManager.NOT_SUPPORTED -> mResult = "NOT_SUPPORTED"
            CustomDeviceManager.POLICY_RESTRICTED -> mResult = "POLICY_RESTRICTED"
            CustomDeviceManager.INVALID_PASSCODE -> mResult = "INVALID_PASSCODE"
            CustomDeviceManager.INVALID_PACKAGE -> mResult = "INVALID_PACKAGE"
            CustomDeviceManager.INVALID_RING_TONE_TYPE -> mResult = "INVALID_RING_TONE_TYPE"
            CustomDeviceManager.RING_TONE_NOT_FOUND -> mResult = "RING_TONE_NOT_FOUND"
            CustomDeviceManager.INVALID_ADDRESS -> mResult = "INVALID_ADDRESS"
            CustomDeviceManager.INVALID_PERMISSION -> mResult = "INVALID_PERMISSION"
            CustomDeviceManager.INVALID_SOUND_TYPE -> mResult = "INVALID_SOUND_TYPE"
            CustomDeviceManager.INVALID_ROTATION_TYPE -> mResult = "INVALID_ROTATION_TYPE"
            CustomDeviceManager.INVALID_STRING -> mResult = "INVALID_STRING"
            CustomDeviceManager.INVALID_STRING_TYPE -> mResult = "INVALID_STRING_TYPE"
            CustomDeviceManager.INVALID_PERCENT_VALUE -> mResult = "INVALID_PERCENT_VALUE"
            CustomDeviceManager.INVALID_LOCALE -> mResult = "INVALID_LOCALE"
            CustomDeviceManager.INVALID_TIMEOUT -> mResult = "INVALID_TIMEOUT"
            CustomDeviceManager.INVALID_UID -> mResult = "INVALID_UID"
            CustomDeviceManager.INVALID_DEVICE -> mResult = "INVALID_DEVICE"
            CustomDeviceManager.ERROR_UNKNOWN -> mResult = "ERROR_UNKNOWN"
            else -> mResult = "Unknown " + pResult
        }
        return mResult
    }
}
