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

    override fun onCreate(savedInstanceState: Bundle?) {
        sessionName = "tab_$tag"
        tabsViewModel.tabForId(id)?.url?.let { startLocation = it }
        super.onCreate(savedInstanceState)
    }

    //The user agent is set on the session’s web view so the Rails server can detect
    //requests made from the app.
    override fun onSessionCreated() {
        super.onSessionCreated()
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
