package com.example.techorbit.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.io.IOException
import java.lang.reflect.Method

const val TAG="TAG"
fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.hideKeyboard() {
    val inputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    val inputMethodManager =
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
}

fun Fragment.getJsonDataFromAsset(context: Context, filename: String): String? {
    val jsonstring: String
    try {
        jsonstring = context.assets.open(filename).bufferedReader().use { it ->
            it.readText()
        }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonstring
}

fun isZeroPrefix(benNo: String): Boolean {
    return benNo.isNotEmpty() && benNo[0] == '0'
}
fun isZeroNotInPrefix(benNo: String): Boolean {
    return benNo.isNotEmpty() && benNo[0] != '0'
}

fun removeZero(benNo: String): String {
    val stringBuilder= StringBuilder(benNo)
    stringBuilder.deleteCharAt(0)
    return stringBuilder.toString()
}

fun getUniqueIMEIId(context: Context): String? {
    try {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ""
        }
        var imei: String? = ""
        imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            telephonyManager.deviceId
        }
//        OmangApplication.setIMEI(imei)
        Log.d(TAG, "=$imei")
        return if (imei != null && !imei.isEmpty()) {
            imei
        } else {
            Build.SERIAL
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "no imei"



//        return "12345678";
}

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}
