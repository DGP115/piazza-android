package com.example.piazza

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ViewFlipper
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate

class MainActivity : AppCompatActivity(), TurboActivity {
    override lateinit var delegate: TurboActivityDelegate
    lateinit var tabBar: BottomNavigationView

    private lateinit var tabSwitcher: ViewFlipper

    /*
    The below line lazily instantiates the tabsViewModel using the Android
    platform. Android’s ViewModel class does some lifecycle management behind the scenes
    so we can just ask the system for one instead of instantiating it ourselves.
     */
    private val tabsViewModel: TabsViewModel by viewModels()

    /*
    Below, the call to setContentView which is passed a reference to the
    XML layout of the activities [activity_man]. This means the layout XML will be
    deserialized and laid out within the MainActivity when it is displayed.
    That puts SessionNavHostFragment on screen and Turbo Native takes
    care of the rest!
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Register each tab's SessionNavHostFragment with Turbo Native
        configureTurboDelegates()

        // Handle logic for switching between tabs
        configureTabs()

    }

    /*
    Next, MainActivity needs to implement the TurboActivity interface.
    This entails creating an instance of TurboActivityDelegate and passing
    in a reference to the SessionNavHostFragments, of which we have 5.
     */

    private fun configureTurboDelegates() {
        delegate =
            TurboActivityDelegate(this, tabsViewModel.tabs.first().id)
        tabsViewModel.tabs.forEach {
            delegate.registerNavHostFragment(it.id)
        }
    }


    // Tab switching logic:
    //   Get references to the relevant views using their XML IDs.
    //   The listener on the tabBar is fired whenever a new item is selected.
    //   Within it, the displayed view is updated to the one corresponding to the selected tab.
    //
    // TurboActivityDelegate is also updated with the newly selected
    // NavHostFragment and its web view is refreshed.
    private fun configureTabs() {
        tabSwitcher = findViewById(R.id.tabSwitcher)
        tabBar = findViewById(R.id.tabBar)

        tabBar.setOnItemSelectedListener {
            tabSwitcher.displayedChild =
                tabsViewModel.indexedTabForId(it.itemId)!!.index

            delegate.currentNavHostFragmentId = it.itemId
            delegate.refresh(false)

            return@setOnItemSelectedListener true
        }
    }
}
