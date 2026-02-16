package com.example.piazza

class Api {
    companion object {
        // Replace 192.168.1.1 with your computer's actual IPv4 address
        private val baseUrl = if(BuildConfig.DEBUG)
            "http://192.168.68.113:3000"
        else
            "https://piazza-web-61uo.onrender.com"
        val rootUrl = "$baseUrl/"
    }
}
