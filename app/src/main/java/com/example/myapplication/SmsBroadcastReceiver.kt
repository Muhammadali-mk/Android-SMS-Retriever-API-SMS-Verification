package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever.*
import com.google.android.gms.common.api.CommonStatusCodes.SUCCESS
import com.google.android.gms.common.api.CommonStatusCodes.TIMEOUT
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver() {

    data class Sms(val status: Status?, val body: String?)

    private var listener: SmsBroadcastReceiverListener? = null

    fun setOtpListeners(receiverListener: SmsBroadcastReceiverListener) {
        this.listener = receiverListener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.isSmsRetrievedAction() == false) return
        val sms = intent.getRetrieverSms()

        when (sms.status?.statusCode) {
            SUCCESS ->
                listener?.onOtpReceived(sms.body?.removeAllLetters())
            TIMEOUT ->
                listener?.onOtpTimeout()
        }
    }

    interface SmsBroadcastReceiverListener {
        fun onOtpReceived(otp: String?)
        fun onOtpTimeout()
    }

    private fun Intent?.isSmsRetrievedAction(): Boolean {
        return this?.action == SMS_RETRIEVED_ACTION
    }

    private fun Intent?.getRetrieverSms(): Sms {
        val extras = this?.extras
        return Sms(
            extras?.get(EXTRA_STATUS) as? Status,
            extras?.getString(EXTRA_SMS_MESSAGE)
        )
    }

    private fun String.removeAllLetters(): String =
        replace(( "[^0-9]+").toRegex(), "")
}