package com.example.projecttdm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.example.projecttdm.data.db.scheduleSyncWork

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            Log.d("NetworkChangeReceiver", "Connexion Internet détectée")

            // Lancer le Worker
            context?.let {
                scheduleSyncWork(it)
            }
        }
    }
}
