package com.iohardik.chatapp.Activty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.iohardik.chatapp.Home.MainActivity
import com.iohardik.chatapp.databinding.ActivityReciveotpPageBinding

class VerifySendActivity : AppCompatActivity() {

 var binding:ActivityReciveotpPageBinding?=null

   var auth: FirebaseAuth?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test","otp send")
       binding = ActivityReciveotpPageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth=FirebaseAuth.getInstance()
        if (auth!!.currentUser !=null){
            val intent = Intent(this@VerifySendActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        supportActionBar?.hide()
        binding!!.editNumber.requestFocus()
        binding!!.continuBtn.setOnClickListener{
            val  intent = Intent(this@VerifySendActivity, OTP_RECEIVED::class.java  )
            intent.putExtra("phoneNumber",binding!!.editNumber.text.toString())
            Toast.makeText(this, "otp send", Toast.LENGTH_SHORT).show()
            startActivity(intent)
       }
    }
}