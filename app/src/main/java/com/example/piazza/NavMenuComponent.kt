package com.example.piazza

import dev.hotwire.strada.BridgeComponent
import dev.hotwire.strada.BridgeDelegate
import dev.hotwire.strada.Message

import android.util.Log
import android.view.MenuItem

import androidx.appcompat.widget.AppCompatTextView
import android.view.Gravity
import android.view.ViewGroup
import android.graphics.Color
import android.util.TypedValue

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)

class NavMenuComponent(
    name: String,
    private val bridgeDelegate: BridgeDelegate<NavDestination>
): BridgeComponent<NavDestination>(name, bridgeDelegate) {

    private val fragment: WebFragment
    get() = bridgeDelegate.destination.fragment as WebFragment

    override fun onReceive(message: Message) {
        when (message.event) {
            "connect" -> renderMenu(message)
            "disconnect" -> clearMenu()
            else -> Log.w("Piazza", "Unknown event for message: $message")
        }
        Log.d("Piazza", "NavMenu received event=${message.event}")
    }

    private fun renderMenu(message: Message) {
        Log.d("Piazza", "NavMenu renderMenu called")

        val data = message.data<MessageData>()

        Log.d( "Piazza", "NavMenu data=$data")

        if (data == null) {
            Log.e("Piazza", "NavMenu message data could not be deserialized")
            return
        }

        Log.d("Piazza", "NavMenu items=${data.items}")

        // Clear the toolbar before adding new items
        // (otherwise every time 'Create Ad is clicked a new 'Create Ad' button is added)
        val toolbar = fragment.toolbarForNavigation() ?: return
        toolbar.menu.clear()

        // Now add the menu item(s)
        data.items.forEach {
            item -> addMenuItem(item)
        }
    }

    private fun addMenuItem(item: NavMenuItem) {
        val toolbar = fragment.toolbarForNavigation() ?: return

        val menuItemId = 1000 + item.index

        val menuItem = toolbar.menu.add(
            0,
            menuItemId,
            item.index,
            item.title
        )

        val textButton = AppCompatTextView(fragment.requireContext()).apply {
            text = item.title
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            gravity = Gravity.CENTER
            isClickable = true
            isFocusable = true
            setPadding(32, 0, 32, 0)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setOnClickListener {
                Log.d("Piazza", "NavMenu text button clicked ${item.title}")

                replyTo(
                    event = "connect",
                    data = ResponseData(selectedIndex = item.index)
                )
            }
        }

        menuItem.actionView = textButton
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        Log.d("Piazza", "added menu item=${item.title}, menu size=${toolbar.menu.size()}")
    }

    private fun clearMenu() {
        Log.d("Piazza", "NavMenu clearMenu called")
        fragment.toolbarForNavigation()?.menu?.clear()
      //  this.fragment.sessionNavHostFragment
      //      .currentNavDestination
      //      .toolbarForNavigation()
       //     ?.menu
        //    ?.clear() //
    }

    @Serializable
    data class MessageData(
        @SerialName("items") val items: List<NavMenuItem>
    )

    @Serializable
    data class NavMenuItem(
        @SerialName("title") val title: String,
        @SerialName("index") val index: Int
    )

    @Serializable
    data class ResponseData(
        @SerialName("selectedIndex") val selectedIndex: Int
    )

}