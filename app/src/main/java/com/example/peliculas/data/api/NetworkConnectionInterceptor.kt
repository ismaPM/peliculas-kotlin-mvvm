package com.example.peliculas.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.peliculas.application.DroidApp
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkException() : IOException() {
    override val message: String?
        get() = "No Internet Connection"
}

class TimeOutException() : IOException() {
    override val message: String?
        get() = "No Internet Connection"
}

class NetworkConnectionInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NetworkException()
        return chain.proceed(chain.request())
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager = DroidApp.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

}