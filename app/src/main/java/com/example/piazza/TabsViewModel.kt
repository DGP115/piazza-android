package com.example.piazza
import androidx.lifecycle.ViewModel

// This kotlin class does the following:
//  - inform Turbo Native of the 5 different SessionNavHostFragments of the toolbar
//  - wire up the tab switching logic between them and the toolbar.
// Android has a ViewModel class which has been subclassed here.
// It provides handy lifecycle management under the hood.
class TabsViewModel : ViewModel() {
    data class Tab(val id: Int, val url: String)
    val tabs: List<Tab> = createTabs()
    fun tabForUrl(url: String): Tab? {
        return tabs.find { it.url == url }
    }
    fun tabForId(id: Int): Tab? {
        return tabs.find { it.id == id }
    }
    fun indexedTabForId(id: Int): IndexedValue<Tab>? {
        return tabs.withIndex().find { it.value.id == id }
    }
    private fun createTabs(): List<Tab> {

        return listOf(
            Tab(R.id.tab_home, Api.rootUrl),
            Tab(R.id.tab_saved_ads, Api.rootUrl),
            Tab(R.id.tab_messages, Api.rootUrl),
            Tab(R.id.tab_my_ads, Api.rootUrl),
            Tab(R.id.tab_profile, Api.rootUrl)
        )
    }
}