package com.abdurashidov.dictionary.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.abdurashidov.dictionary.utils.Data
import com.abdurashidov.dictionary.utils.NetworkHelper

class InternetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val networkHelper=NetworkHelper(context)
        if (networkHelper.isNetworkConnected()){
            Data.network.postValue(true)
        }else{
            Data.network.postValue(false)
        }
    }
}