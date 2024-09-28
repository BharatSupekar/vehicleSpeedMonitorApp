package com.example.vehiclespeedmonitorapp.utils

import android.content.Context
import android.util.Log
import com.example.vehiclespeedmonitorapp.interfaces.VehicleSpeedListener

class SpeedMonitor(context: Context,vehicleSpeedListener: VehicleSpeedListener) {

    private var car: Car? = null
    private var carPropertyManager: CarPropertyManager? = null
    private var speedLimit = 80.0f // Example limit in km/h
    private var mVehicleSpeedListener: VehicleSpeedListener


    init {
        car = Car.createCar(context)
        carPropertyManager = car?.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
        mVehicleSpeedListener = vehicleSpeedListener
    }

    // Set the speed limit for the current customer (called when rental starts)
    fun setSpeedLimit(limit: Float) {
        speedLimit = limit
    }

    fun startMonitoringSpeed() {
        carPropertyManager?.registerCallback(object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(event: CarPropertyValue<*>) {
                if (event.propertyId == VehiclePropertyIds.PERF_VEHICLE_SPEED) {
                    val currentSpeed = (event.value as? Float) ?: 0f
                    Log.d("SpeedMonitor", "Current speed: $currentSpeed km/h")
                    checkSpeed(currentSpeed)
                }
            }

            override fun onErrorEvent(propId: Int, zone: Int) {
                Log.e("SpeedMonitor", "Error event: $propId, zone: $zone")
            }
        }, VehiclePropertyIds.PERF_VEHICLE_SPEED, CarPropertyManager.SENSOR_RATE_ONCHANGE)
    }

    fun unRegisterVehicleSpeedListener() {
        // Unregister the listener when the activity is destroyed
        carPropertyManager.unregisterCallback { propId, zone -> }
        car.disconnect()
    }

    private fun checkSpeed(currentSpeed: Float) {
        val speedInKmH = currentSpeed * 3.6f
        Log.d("CarSpeedMonitor", "Current speed: $speedInKmH km/h")
        if (speedInKmH > speedLimit) {
            // Notify user with an alert
            showWarningAlert()

            // Notify rental company via Firebase (or AWS)
            notifyRentalCompany(speedInKmH)
        }
    }


    private fun showWarningAlert() {
        // You can show a custom dialog, system alert, or notification
        mVehicleSpeedListener.showAlertMessage()
    }

    fun notifyRentalCompany(currentSpeed: Float) {
        // Prepare data to send to Firebase
        mVehicleSpeedListener.notifyRentalCompany(currentSpeed)
    }
}