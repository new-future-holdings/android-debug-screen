package com.pandulapeter.beagleExample.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleExample.R
import com.pandulapeter.beagleExample.networking.NetworkingManager
import com.pandulapeter.beagleExample.utils.executeRequest
import com.pandulapeter.beagleExample.utils.logMessages

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { Beagle.fetch() }
        findViewById<View>(R.id.perform_network_request_button).setOnClickListener { performNetworkRequest() }
        findViewById<View>(R.id.add_log_message_button).setOnClickListener { Beagle.log(message = logMessages.random(), payload = listOf("Random payload", null).random()) }
        findViewById<View>(R.id.open_login_screen_button).setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }

    private fun performNetworkRequest() = NetworkingManager.networkingService.performNetworkRequest().executeRequest(
        onSuccess = { musicGenre -> Toast.makeText(this, musicGenre, Toast.LENGTH_SHORT).show() },
        onError = { Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show() }
    )
}