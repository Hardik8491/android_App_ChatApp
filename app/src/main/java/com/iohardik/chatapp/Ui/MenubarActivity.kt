//package com.iohardik.chatapp.Ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import com.iohardik.chatapp.databinding.MenubarBinding
//
//
//class MenubarActivity : AppCompatActivity() {
//    private lateinit var binding:MenubarBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = MenubarBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Find buttons by their IDs
//        // Find buttons by their IDs
//        val btnNewGroup = findViewById<Button>(com.iohardik.chatapp.R.id.btnNewGroup)
//        val btnNewBroadcast = findViewById<Button>(com.iohardik.chatapp.R.id.btnNewBroadcast)
//        val btnLinkDevices = findViewById<Button>(com.iohardik.chatapp.R.id.btnLinkDevices)
//        val btnSettings = findViewById<Button>(com.iohardik.chatapp.R.id.btnSettings)
//
//        // Set click listeners for the buttons
//        // Set click listeners for the buttons
//        btnNewGroup.setOnClickListener { openNewGroupActivity() }
//
//        btnNewBroadcast.setOnClickListener { openNewBroadcastActivity() }
//
//        btnLinkDevices.setOnClickListener { openLinkDevicesActivity() }
//
//        btnSettings.setOnClickListener { openSettingsActivity() }
//
//    }
//
//    // Methods to open new activities for each button
//    private fun openNewGroupActivity() {
//        // Add code to open the new group activity XML here
//        val intent = Intent(this, NewGroupActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun openNewBroadcastActivity() {
//        // Add code to open the new broadcast activity XML here
//        val intent = Intent(this,NewBroadcastActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun openLinkDevicesActivity() {
//        // Add code to open the link devices activity XML here
//        val intent = Intent(this, LinkDevicesActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun openSettingsActivity() {
//        // Add code to open the settings activity XML here
//        val intent = Intent(this, SettingsActivity::class.java)
//        startActivity(intent)
//    }
//
//}