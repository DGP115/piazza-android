package com.example.piazza

// Purpose:
//  To identify the Rails server for this Android app.
//  Android's BuildConfig class is used to do this
class Api {
    companion object {
        // When running this app on your Android phone, replace 192.168.*.*
        // with the ip address of the PC running the rails server
        private val baseUrl = if(BuildConfig.DEBUG)
            "http://192.168.68.103:3000"
        else
            "https://piazza-web-61uo.onrender.com"
        val rootUrl = "$baseUrl/"
        val profileUrl = "$baseUrl/profile"
        val loginUrl = "$baseUrl/login"
    }
}
