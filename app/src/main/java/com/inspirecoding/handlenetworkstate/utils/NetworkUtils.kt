package com.inspirecoding.handlenetworkstate.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Network Utility to detect availability or unavailability of Internet connection
 */
// 1
object NetworkUtils : ConnectivityManager.NetworkCallback()
{
    // 2
    private val networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Returns instance of [LiveData] which can be observed for network changes.
     */
    // 3
    fun getNetworkLiveData(context: Context): LiveData<Boolean>
    {
        // 4
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // 5
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            connectivityManager.registerDefaultNetworkCallback(this)
        }
        else
        {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), this)
        }

        var isConnected = false

        // 6
        // Retrieve current status of connectivity
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                {
                    isConnected = true
                    return@forEach
                }
            }
        }

        networkLiveData.postValue(isConnected)

        return networkLiveData
    }

    // 7
    override fun onAvailable(network: Network)
    {
        networkLiveData.postValue(true)
    }

    // 8
    override fun onLost(network: Network)
    {
        networkLiveData.postValue(false)
    }
}
