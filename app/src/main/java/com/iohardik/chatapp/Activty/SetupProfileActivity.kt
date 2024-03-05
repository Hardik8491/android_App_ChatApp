@file:Suppress("DEPRECATION")

package com.iohardik.chatapp.Activty

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.iohardik.chatapp.Home.MainActivity
import com.iohardik.chatapp.databinding.ActivitySetupProfileBinding

import com.iohardik.chatapp.model.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SetupProfileActivity : AppCompatActivity() {

    private var binding: ActivitySetupProfileBinding? = null
    private var auth: FirebaseAuth? = null
    private var database: FirebaseDatabase? = null
    private var storage: FirebaseStorage? = null
    private var selectedImage: Uri? = null
    private var dialog: ProgressDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        database=FirebaseDatabase.getInstance()
        storage= FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
        binding?.ProfileImg?.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,45)
        }
        binding?.SetupBtn?.setOnClickListener {
            if(binding?.ProfileName?.text!!.isEmpty())
            {
                Toast.makeText(this, "please enter your name", Toast.LENGTH_SHORT).show()
            }
            else if (selectedImage==null){
                Toast.makeText( this, "please select your Image", Toast.LENGTH_SHORT).show()

            }else uploadData()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadData() {
        val
                dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

        val timestamp = dateFormat.format(Date())
     val ref= storage!!.reference.child("profile").child(Date().time.toString())
        ref.putFile(selectedImage!!).addOnCompleteListener{

            if (it.isSuccessful){
                ref.downloadUrl.addOnSuccessListener { task->
                    uploadInfo(task.toString())
                }
            }
            else
            {
                val user=User(auth!!.uid.toString(),binding!!.ProfileName?.text.toString(),auth?.currentUser?.phoneNumber,"No Image")

                database?.reference?.child("users")
                    ?.child(auth?.uid.toString())
                    ?.setValue(user)
                    ?.addOnCanceledListener {
                        Toast.makeText(this, "Information Uploaded...", Toast.LENGTH_SHORT).show() }

                   var intent=Intent(this@SetupProfileActivity, MainActivity::class.java)

                startActivity(intent)
                finish()
            }

        }

    }

    private fun uploadInfo(imgUrl: String) {
        val user=User(auth!!.uid.toString(),binding!!.ProfileName?.text.toString(),auth?.currentUser?.phoneNumber,imgUrl)

        database?.reference?.child("users")
            ?.child(auth?.uid.toString())
            ?.setValue(user)
            ?.addOnSuccessListener {

                Toast.makeText(this, "Information Uploaded...", Toast.LENGTH_SHORT).show() }

                var intent=Intent(this@SetupProfileActivity, MainActivity::class.java)

                startActivity(intent)
                finish()
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode,resultCode,data)

        if(data!=null)
        {
            if(data.data!=null)
            {
                val uri=data.data
                val storage=FirebaseStorage.getInstance()
                val time=Date().time
                val ref=storage.reference.child("profile").child(time.toString()+"")

                ref.putFile(uri!!).addOnCompleteListener { task->
                    if(task.isSuccessful)
                    {
                        ref.downloadUrl.addOnCompleteListener { uri->
                            val filePath=uri.toString()
                            val obj=HashMap<String,Any>()
                            obj["image"]=filePath
                            database?.
                            reference?.child("users")
                            ?.child(FirebaseAuth.getInstance()?.uid!!)?.updateChildren(obj)
                        }
                    }
                }
                binding?.ProfileImg!!.setImageURI(data.data)
                selectedImage=data.data



            }
        }
    }
}
