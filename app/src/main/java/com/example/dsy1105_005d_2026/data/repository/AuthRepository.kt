package com.example.dsy1105_005d_2026.data.repository

import com.example.dsy1105_005d_2026.data.model.Credential

class AuthRepository (
    private val validCredential: Credential = Credential.Admin
){
    fun login(username:String, password:String): Boolean{

        return username == validCredential.username && password==validCredential.password
    }


}