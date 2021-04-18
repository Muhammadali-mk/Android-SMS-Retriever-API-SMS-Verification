package com.example.myapplication

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val appSignatureHelper = AppSignatureHelper(this)
        appSignatureHelper.appSignatures
    }
}