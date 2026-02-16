package com.example.piazza

import dev.hotwire.turbo.fragments.TurboWebFragment
import dev.hotwire.turbo.nav.TurboNavGraphDestination

@TurboNavGraphDestination(uri = "piazza://fragment/web")
open class WebFragment : TurboWebFragment() {}