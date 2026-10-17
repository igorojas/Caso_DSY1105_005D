package com.example.dsy1105_005d_2026.data.model

data class Credential(val username:String, val password:String){
    companion object{
        val Admin= Credential(username="admin", password ="123")
    }
}