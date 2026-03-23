package com.example.piazza

import android.os.Bundle
import android.view.View

import dev.hotwire.turbo.fragments.TurboWebFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination

import dev.hotwire.strada.BridgeDelegate
import dev.hotwire.turbo.views.TurboWebView

@TurboNavGraphDestination(uri = "piazza://fragment/web")

// WebFragment is a subclass of TurboWebFragment and implements the NavDestination interface.
open class WebFragment :
    TurboWebFragment() , NavDestination {

    private val bridgeDelegate by lazy {
        BridgeDelegate(
            location = location,
            destination = this,
            componentFactories = bridgeComponentFactories
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(bridgeDelegate)
    }

    override fun onColdBootPageStarted(location: String) {
        bridgeDelegate.onColdBootPageStarted()
    }
    override fun onColdBootPageCompleted(location: String) {
        bridgeDelegate.onColdBootPageCompleted()
    }

    override fun onWebViewAttached(webView: TurboWebView) {
        bridgeDelegate.onWebViewAttached(webView)
    }


    override fun onWebViewDetached(webView: TurboWebView) {
        bridgeDelegate.onWebViewDetached()
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(bridgeDelegate)
    }

    override fun onVisitErrorReceived(
        location: String, errorCode: Int
    ) {
        when (errorCode) {
            401 -> if(isActive) navigate(Api.loginUrl)
            else -> super.onVisitErrorReceived(location, errorCode)
        }
    }

    override fun onVisitStarted(location: String) {
        super.onVisitStarted(location)
        toolbarForNavigation()?.menu?.clear()
    }
}