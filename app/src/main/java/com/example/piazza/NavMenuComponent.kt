package com.example.piazza

import dev.hotwire.strada.BridgeComponent
import dev.hotwire.strada.BridgeDelegate
import dev.hotwire.strada.Message

import android.util.Log

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
    }

    private fun renderMenu(message: Message) {
        val data = message.data<MessageData>() ?: return
        data.items.forEach { addMenuItem(it) }
    }

    private fun addMenuItem(menuItem: NavMenuItem) {
        val item = this.fragment
            .sessionNavHostFragment
            .currentNavDestination
            .toolbarForNavigation()
            ?.menu
            ?.add(
                0,
                menuItem.index,
                menuItem.index,
                menuItem.title
            )
            ?: return

        item.setOnMenuItemClickListener {
            // Reply to message
            replyTo(
                event = "connect",
                data = ResponseData(selectedIndex = menuItem.index)
            )
            return@setOnMenuItemClickListener true
        }
    }

    private fun clearMenu() {
        this.fragment.sessionNavHostFragment
            .currentNavDestination
            .toolbarForNavigation()
            ?.menu
            ?.clear()
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