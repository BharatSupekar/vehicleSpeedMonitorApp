package com.example.vehiclespeedmonitorapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vehiclespeedmonitorapp.R
import com.example.vehiclespeedmonitorapp.utils.SpeedMonitor
import com.example.vehiclespeedmonitorapp.interfaces.VehicleSpeedListener

class MainActivity : AppCompatActivity(), VehicleSpeedListener {
    private lateinit var speedMonitor: SpeedMonitor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_activity)

        startRentalForCustomer("customer id")
    }

    fun startRentalForCustomer(customerId: String) {
        val speedLimit = getCustomerSpeedLimitFromDatabase(customerId) // Fetch speed limit of customer from backend
        speedMonitor = SpeedMonitor(this,this)
        speedMonitor.setSpeedLimit(speedLimit)
        speedMonitor.startMonitoringSpeed()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the listener when the activity is destroyed
        speedMonitor.unRegisterVehicleSpeedListener()
    }

    override fun showAlertMessage() {
        Toast.makeText(this, "Warning: Speed limit exceeded!", Toast.LENGTH_LONG).show()
    }

    override fun notifyRentalCompany(currentSpeed: Float) {
        val notificationData = mapOf(
            "message" to "Speed limit exceeded",
            "currentSpeed" to currentSpeed.toString(),
            "speedLimit" to speedLimit.toString()
        )

        FirebaseMessaging.getInstance().send(FirebaseMessage(notificationData))
    }
}
