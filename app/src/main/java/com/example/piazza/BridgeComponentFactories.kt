package com.example.piazza

import dev.hotwire.strada.BridgeComponentFactory

val bridgeComponentFactories = listOf(
    BridgeComponentFactory("nav-menu", ::NavMenuComponent)
)