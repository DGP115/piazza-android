package com.example.piazza

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.hotwire.turbo.config.TurboPathConfiguration
import dev.hotwire.turbo.session.TurboSessionNavHostFragment
import kotlin.reflect.KClass
import android.os.Bundle
import androidx.fragment.app.activityViewModels

import android.webkit.WebView
import dev.hotwire.strada.Bridge
import dev.hotwire.strada.Strada

// DGP:  Added error logging:
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient


//This class defines everything the session needs to know to communicate with
//the Rails server and perform navigation.
class SessionNavHostFragment : TurboSessionNavHostFragment() {
    override var sessionName = "default"
    override var startLocation = Api.rootUrl

    val tabsViewModel: TabsViewModel by activityViewModels()

    //The list of Activities and Fragments that Turbo Native can navigate to need
    //to be registered. The app has a single Activity so that list can be empty.
    //WebFragment is registered so it can be navigated to.

    override val registeredActivities:
            List<KClass<out AppCompatActivity>>
        get() = listOf()

    override val registeredFragments:
            List<KClass<out Fragment>>
        get() = listOf(
            WebFragment::class,
            ModalWebFragment::class,
            TabbedWebFragment::class
        )

    //  The sessionName of each TurboSessionNavHostFragment in an app
    //  has to be unique, so "default" can’t be used. It’s now assigned dynamically
    //  when the fragment is created using a tag value from the XML. The start
    //  location is set based on the value in the View Model.
    override fun onCreate(savedInstanceState: Bundle?) {
        sessionName = "tab_$tag"

        //DGP:  Logging code:
       // android.util.Log.d("PIAZZA_URL", "Api.rootUrl = ${Api.rootUrl}")
       // android.util.Log.d("PIAZZA_URL", "tab url = ${tabsViewModel.tabForId(id)?.url}")

        tabsViewModel.tabForId(id)?.url?.let { startLocation = it }

        ////DGP:  Logging code:
       // android.util.Log.d("PIAZZA_URL", "final startLocation = $startLocation")

        super.onCreate(savedInstanceState)
    }

    //The user agent is set on the session’s webview so the Rails server can detect
    //requests made from the app.
    override fun onSessionCreated() {
        super.onSessionCreated()

        //DGP error logging:
/*        session.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.e(
                    "WEBVIEW_ERROR",
                    "url=${request?.url} code=${error?.errorCode} desc=${error?.description}"
                )
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: android.webkit.WebResourceResponse?
            ) {
                Log.e(
                    "WEBVIEW_HTTP",
                    "url=${request?.url} status=${errorResponse?.statusCode} reason=${errorResponse?.reasonPhrase}"
                )
                super.onReceivedHttpError(view, request, errorResponse)
            }
        }*/
        //  DGP: Error logging end

        session.webView.settings.userAgentString = session.webView.customUserAgent

        Bridge.initialize(session.webView)
    }

    //Pass in the path configuration.
    override val pathConfigurationLocation:
            TurboPathConfiguration.Location
        get() = TurboPathConfiguration.Location(
            assetFilePath = "json/configuration.json"
       )

    private val WebView.customUserAgent: String
        get() {
            val stradaSubstring = Strada.userAgentSubstring(bridgeComponentFactories)
            return "Piazza Turbo Native Android; $stradaSubstring;${settings.userAgentString}"
        }

}


