package com.iohardik.chatapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.iohardik.chatapp.R
import com.iohardik.chatapp.databinding.DeleteLayoutBinding
import com.iohardik.chatapp.databinding.ReciveMessageBinding
import com.iohardik.chatapp.databinding.SendMessageBinding

import com.iohardik.chatapp.model.Message
import java.sql.Timestamp

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MessageAdapter(
    var context: Context,
    messages: ArrayList<Message>?,
    senderRoom:String,
    receiverRoom:String

): RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    lateinit var  messages: ArrayList<Message>
    var ITEM_SENT=1
    var ITEM_RECIVE=2
    var senderRoom:String=""
    var  receiverRoom:String=""





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =

          if (viewType == ITEM_SENT) {
              val view: View = LayoutInflater.from(context).inflate(R.layout.send_message, parent, false)
              SentMsgHolder(view)}
          else
          {   val view: View = LayoutInflater.from(context).inflate(R.layout.recive_message, parent,false)
              ReceiverMsgHolder(view)
          }

    override fun getItemViewType(position: Int): Int {
       val message=messages[position]
        return if (FirebaseAuth.getInstance().uid==message.senderId){
           ITEM_SENT
        }else{
            ITEM_RECIVE

        }
    }

    override fun getItemCount(): Int=messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val  message=messages[position]
        if (holder.javaClass==SentMsgHolder::class.java){
            val viewHolder= holder as SentMsgHolder
            if (message.message.equals("photo")){
                viewHolder.binding.image.visibility=View.VISIBLE
                viewHolder.binding.message.visibility=View.GONE
                viewHolder.binding.mLiner.visibility=View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.profile_image)
                    .into(viewHolder.binding.image)

            }

            viewHolder.binding.message.text=message.message



//            val dF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            var currentTime = dF.format(message.timeStamp)
//
//
//
//            viewHolder.binding.timeStamp.text= time.toString()
            viewHolder.itemView.setOnLongClickListener{
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout,null)
                val binding:DeleteLayoutBinding= DeleteLayoutBinding.bind(view)

                val dialog:AlertDialog=AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyone.setOnClickListener {
                    message.message="This message is removed"
                    message.messageId?.let { it1->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1).setValue(message)

                    }
                    message.messageId.let { it1->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(it1!!).setValue(message)


                    }
                    dialog.dismiss()

                }

                binding.delete.setOnClickListener{
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .child(it1!!).setValue(null)
                    }

                    dialog.dismiss()
                }
                binding.cancle.setOnClickListener{
                    dialog.dismiss()
                }
                dialog.show()
                    false
            }

        }
        else{
            val  viewHolder=holder as ReceiverMsgHolder
            if (message.message.equals("sent an image")) {
                viewHolder.binding.image.visibility=View.VISIBLE
                viewHolder.binding.message.visibility=View.GONE
                viewHolder.binding.mLiner.visibility=View.GONE
                Glide.with(context)
                    .load(message.imageUrl)
                    .placeholder(R.drawable.profile_image)
                    .into(viewHolder.binding.image)

            }
            viewHolder.binding.message.text=message.message

            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.delete_layout,null)
                val binding:DeleteLayoutBinding= DeleteLayoutBinding.bind(view)

                val dialog:AlertDialog=AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyone.setOnClickListener{
                    message.message="This message is removed"
                    message.messageId?.let { it1->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(message)

                    }
                    message.messageId?.let { it1->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(receiverRoom)
                            .child("message")
                            .child(it1).setValue(message)


                    }
                    dialog.dismiss()

                }
                binding.delete.setOnClickListener{
                    message.messageId?.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("chats")
                            .child(senderRoom)
                            .child("message")
                            .child(it1).setValue(null)
                    }
                    dialog.dismiss()
                }
                binding.cancle.setOnClickListener{
                    dialog.dismiss()
                }
                dialog.show()
                false
            }
        }

    }


    inner class SentMsgHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var binding:SendMessageBinding= SendMessageBinding.bind(itemView)


}
    inner class ReceiverMsgHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var binding: ReciveMessageBinding = ReciveMessageBinding.bind(itemView)
    }

    init {
        if (messages !=null){
            this.messages=messages
        }

        this.senderRoom=senderRoom
        this.receiverRoom=receiverRoom
    }


}