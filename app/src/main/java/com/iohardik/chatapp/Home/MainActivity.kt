package com.iohardik.chatapp.Home
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iohardik.chatapp.R
import com.iohardik.chatapp.Ui.LinkDevicesActivity

import com.iohardik.chatapp.Ui.NewBroadcastActivity
import com.iohardik.chatapp.Ui.NewGroupActivity
import com.iohardik.chatapp.Ui.SettingsActivity
import com.iohardik.chatapp.adapter.UserAdapter
import com.iohardik.chatapp.databinding.ActivityMainBinding
import com.iohardik.chatapp.model.User

class MainActivity : AppCompatActivity() {
    var binding:ActivityMainBinding?=null
    var database:FirebaseDatabase?=null
    var users:ArrayList<User>?=null
    var userAdapter:UserAdapter?=null
    var dialog:ProgressDialog?=null
    var user:User?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dialog=ProgressDialog(this@MainActivity)
        dialog!!.setMessage("Uploading Image.....")
        dialog!!.setCancelable(false)
        database=FirebaseDatabase.getInstance()
        users= ArrayList<User>()
        userAdapter= UserAdapter(this@MainActivity,users!!)
        val  layoutManager =GridLayoutManager(this@MainActivity,2)
        binding!!.mRec.layoutManager =layoutManager
        database!!.reference.child("users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
        user = snapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        binding!!.mRec.adapter = userAdapter
        database!!.reference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              users!!.clear()
                for (snapsort1 in snapshot.children){
                    val user:User?=snapsort1.getValue(User::class.java)
                    if (!user!!.uid.equals(FirebaseAuth.getInstance().uid)) users!!.add(user)

                }
                userAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // Find buttons by their IDs
        // Find buttons by their IDs
        val btnNewGroup = findViewById<Button>(com.iohardik.chatapp.R.id.btnNewGroup)
        val btnNewBroadcast = findViewById<Button>(com.iohardik.chatapp.R.id.btnNewBroadcast)
        val btnLinkDevices = findViewById<Button>(com.iohardik.chatapp.R.id.btnLinkDevices)
        val btnSettings = findViewById<Button>(com.iohardik.chatapp.R.id.btnSettings)

        // Set click listeners for the buttons
        // Set click listeners for the buttons
//        btnNewGroup.setOnClickListener { openNewGroupActivity() }
//
//        btnNewBroadcast.setOnClickListener { openNewBroadcastActivity() }
//
//        btnLinkDevices.setOnClickListener { openLinkDevicesActivity() }
//
//        btnSettings.setOnClickListener { openSettingsActivity() }
//        val menubarImageView = findViewById<ImageView>(R.id.menubar)
//
//        menubarImageView.setOnClickListener {
//            // Handle the click event here
////            onMoreClick()
//            val intent = Intent(this, MenubarActivity::class.java)
//            startActivity(intent)
//        }



    }

    private fun onMoreClick() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        val currenId=FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currenId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currenId=FirebaseAuth.getInstance().uid
        database!!.reference.child("presence")
            .child(currenId!!).setValue("Offline")
    }


    // Methods to open new activities for each button
//    private fun openNewGroupActivity() {
//        // Add code to open the new group activity XML here
//        val intent = Intent(this, NewGroupActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun openNewBroadcastActivity() {
//        // Add code to open the new broadcast activity XML here
//        val intent = Intent(this, NewBroadcastActivity::class.java)
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

}