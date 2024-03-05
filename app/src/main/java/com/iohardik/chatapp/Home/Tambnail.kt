package com.iohardik.chatapp.Home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.iohardik.chatapp.R
import com.iohardik.chatapp.databinding.ActivityTambnailBinding

class Tambnail : AppCompatActivity() {

    private var binding:ActivityTambnailBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTambnailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        // Use a Handler to delay the redirection to MainActivity
        Handler().postDelayed({
            // Create an Intent to start MainActivity
            val intent = Intent(this, MainActivity::class.java)

            // Add any extra data or flags if needed
            // For example, you can use intent.putExtra(key, value) to pass data to MainActivity

            // Start MainActivity
            startActivity(intent)

            // Finish the current activity to prevent going back to Tambnail
            finish()
        }, 60000) // 60000 milliseconds = 1 minute
    }
}