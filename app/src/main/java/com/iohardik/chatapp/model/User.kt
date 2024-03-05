package com.iohardik.chatapp.model

import com.google.firebase.auth.FirebaseUser

class User {
    var uid:String?=""
    var name:String?=""
    var phoneNumber:String?=""
    var profileImage:String?=""
    constructor(){}

    constructor(
        uid:String?,
        name:String?,
        phoneNumber:String?,
        profileImage:String?
    ){  this.uid=uid
        this.name=name
        this.profileImage=profileImage
        this.phoneNumber=phoneNumber
    }

}
