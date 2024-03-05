@file:Suppress("DEPRECATION")

package com.iohardik.chatapp.Activty

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import com.iohardik.chatapp.databinding.ActivityOtpReceivedBinding

import java.util.concurrent.TimeUnit

class OTP_RECEIVED : AppCompatActivity() {
    var binding: ActivityOtpReceivedBinding?=null
    var verificationId:String?=null
    private var auth: FirebaseAuth?=null
    var dialog : ProgressDialog? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpReceivedBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dialog= ProgressDialog(this@OTP_RECEIVED)
        dialog!!.setMessage("Sending OTP...")
        dialog!!.setCancelable(false)
        dialog!!.show()
        auth=FirebaseAuth.getInstance()
        supportActionBar?.hide()
        val phoneNumber=intent.getStringExtra("phoneNumber")
        binding!!.phoneLBE.text="verify $phoneNumber"

        val options= PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@OTP_RECEIVED)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    TODO("Not yet implemented")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    TODO("Not yet implemented")
                }
                override fun onCodeSent(
                    verfyId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verfyId,forceResendingToken)
                    dialog!!.dismiss()
                    verificationId=verfyId
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
                    binding!!.otpView.requestFocus()
                }

            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        binding!!.otpView.setOtpCompletionListener { otp ->
            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            auth!!.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@OTP_RECEIVED, SetupProfileActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        Toast.makeText(this@OTP_RECEIVED, "Failed", Toast.LENGTH_SHORT).show()
                    }


                }
        }

    }
}