package com.example.piazza

class Api {
    companion object {
        // When running this app on your Android phone, replace 192.168.*.*
        // with your computer's actual IPv4 address
        private val baseUrl = if(BuildConfig.DEBUG)
            "http://192.168.68.103:3000"
        else
            "https://piazza-web-61uo.onrender.com"
        val rootUrl = "$baseUrl/"
        val profileUrl = "$baseUrl/profile"
        val loginUrl = "$baseUrl/login"
    }
}
