
package com.iohardik.chatapp.Activty

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.iohardik.chatapp.R
import com.iohardik.chatapp.adapter.MessageAdapter
import com.iohardik.chatapp.databinding.ActivityChatActiviyyBinding
import com.iohardik.chatapp.model.Message
import java.net.URL
import java.util.Calendar
import java.util.Date

@SuppressLint("StaticFieldLeak")
var binding :ActivityChatActiviyyBinding?=null
@SuppressLint("StaticFieldLeak")
var adapter:MessageAdapter?=null
var messages:ArrayList<Message>?=null
var senderRoom:String?=""
var receiverRoom:String?=""
var database:FirebaseDatabase?=null
var storage:FirebaseDatabase?=null
var dialog:ProgressDialog?=null
var receiverUid:String?=null
var senderUid:String?=null

class ChatActiviyy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityChatActiviyyBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        database= FirebaseDatabase.getInstance()
        storage= FirebaseDatabase.getInstance()
        dialog= ProgressDialog(this@ChatActiviyy)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        messages= ArrayList()
        val name = intent.getStringExtra("name")
        val profile=intent.getStringExtra("image")
        binding!!.name.text= name
        Glide.with(this@ChatActiviyy).load(profile)

            .placeholder(R.drawable.profile_image)
            .into(binding!!.profile01)
          binding!!.imageView2.setOnClickListener{finish()


        }
        receiverUid=intent.getStringExtra("uid")
        senderUid= FirebaseAuth.getInstance().uid
        Log.d("ABC", "$receiverUid,$senderUid")
        database!!.reference.child("Presence").child(receiverUid!!)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() ){
                        val status=snapshot.getValue(String::class.java)
                        if (status=="offline"){
                            binding!!.Status.visibility=View.GONE
                        }
                        else{
                            binding!!.Status.text = status
                            binding!!.Status.visibility=View.VISIBLE

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        senderRoom = senderUid+ receiverUid;
        receiverRoom= receiverUid+ senderUid;

        adapter= MessageAdapter(this@ChatActiviyy,  messages ,senderRoom!!, receiverRoom!!)
        binding!!.recycleView.layoutManager = LinearLayoutManager(this@ChatActiviyy)
        binding!!.recycleView.adapter= adapter

        database!!.reference.child("chats")
            .child(senderRoom!!)
            .child("messages")
            .addValueEventListener(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot)  {
                    messages!!.clear()
                   for (snapsort1 in snapshot.children){
                      val message:Message?=snapsort1.getValue(Message::class.java)
                       message!!.messageId=snapsort1.key
                       messages!!.add(message)
                   }
                   adapter!!.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        binding!!.sendBtn.setOnClickListener{

              val messageText:String= binding!!.messageBox.text.toString()
              val date= Date()
              val message=Message(messageText, senderUid,date.time)


             binding!!.messageBox.setText("")
            val randomKey= database!!.reference.push().key
            val lastMsgObj =HashMap<String,Any>()
            lastMsgObj["lastMsg"]=message.message!!
            lastMsgObj["lastMsgTime"]=date.time

            Log.d("ABC","$senderRoom,$receiverRoom")

//            UPDATE SENDER ROOM
            database!!.reference.child("chats").child(senderRoom!!)
                .updateChildren(lastMsgObj)

//            UPDATE RECIEVER ROOM

            database!!.reference.child("chats").child(receiverRoom!!)
                .updateChildren(lastMsgObj)

//            UPDATE SENDER ROOM MESSAGES
            database!!.reference.child("chats").child(senderRoom!!)
                .child("messages")
                .child(randomKey!!)
                .setValue(message).addOnSuccessListener {

//                    UPDATE RECIEVER ROOM MESSAGES

                    database!!.reference.child("chats")
                    .child(receiverRoom!!)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Sent Successfully!!", Toast.LENGTH_SHORT).show()
                            Toast.makeText(
                                this,
                                "Scoll now",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            binding!!.recycleView.scrollToPosition(
                                adapter!!.itemCount-1)
                        }

                }
            }
        binding!!.attachment.setOnClickListener{
            val intent= Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            Log.d("ABC","${intent.data}")
            binding!!.attachment.setImageURI(intent.data)
            startActivityForResult(intent,25)
        }
        val handler=Handler()
        binding!!.messageBox.addTextChangedListener(object :TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                database!!.reference.child("Presence")
                    .child(senderUid!!)
                    .setValue("typing...")
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userStoppedTyping,1000)

            }
            var userStoppedTyping= Runnable {
                database!!.reference.child("Presence")
                    .child(senderUid!!)
                    .setValue("Online")
            }

        })
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    override fun onResume() {
        super.onResume()
        val currentId=FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("Online")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==25){
            if (data!=null){
                if (data.data!=null){
                    val selectedImage=data.data
                        val celender = Calendar.getInstance()
                     val reference = FirebaseStorage.getInstance().getReference("chats")
                        .child(celender.timeInMillis.toString()+"")
                   dialog!!.show()
                    reference.putFile(selectedImage!!)
                        .addOnCompleteListener{task->
                            dialog!!.dismiss()
                            if (task.isSuccessful){
                                reference.downloadUrl.addOnSuccessListener { uri->
                                    val filePath=uri.toString()
                                    val messageTxt:String= binding?.messageBox?.text.toString()
                                    val date=Date()
                                    val message=Message(messageTxt, senderUid,date.time)
                                    message.message="sent an image"
                                    message.imageUrl=filePath
                                    binding!!.messageBox.setText("")
                                    val randomKey= database!!.reference.push().key
                                    val lastMsgObj=HashMap<String,Any>()
                                    lastMsgObj["lastmsg"]=message.message!!
                                    lastMsgObj["lastMsgTime"]=date.time
                                    database!!.reference.child("chats")
                                        .updateChildren(lastMsgObj)
                                    database!!.reference.child("chats")
                                        .child(receiverRoom!!)
                                    randomKey?.let {
                                        database!!.reference.child("chats")
                                            .child(senderUid!!)
                                            .child("messages")
                                            .child(it)
                                            .setValue(message).addOnSuccessListener {
                                                database!!.reference.child("chats")
                                                    .child(receiverRoom!!)
                                                    .child("messages")
                                                    .child(randomKey!!)
                                                    .setValue(message)
                                                    .addOnSuccessListener {
                                                        Toast.makeText(
                                                            this,
                                                            "Scoll now",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                        binding!!.recycleView.scrollToPosition(
                                                            adapter!!.itemCount-1)
                                                    }
        
                                            }
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val currentId=FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("Offline")

    }


}