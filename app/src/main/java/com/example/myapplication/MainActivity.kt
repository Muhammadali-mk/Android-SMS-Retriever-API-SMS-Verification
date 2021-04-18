package com.example.myapplication

import android.content.IntentFilter
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetriever.SMS_RETRIEVED_ACTION


class MainActivity : AppCompatActivity(), SmsBroadcastReceiverListener {
    private lateinit var textView: EditText
    private var receiver: SmsBroadcastReceiver = SmsBroadcastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.text_view_sms_code)
        receiver.setOtpListeners(this)
        applicationContext.registerReceiver(receiver, getIntentFilter())
        SmsRetriever.getClient(this).startSmsRetriever()
    }

    private fun getIntentFilter(): IntentFilter =
        IntentFilter().apply { addAction(SMS_RETRIEVED_ACTION) }

    override fun onOtpReceived(otp: String?) {
        Toast.makeText(this, "Otp Received $otp", Toast.LENGTH_LONG).show()
        textView.setText(otp)
    }

    override fun onOtpTimeout() {
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.unregisterReceiver(receiver)
    }
}