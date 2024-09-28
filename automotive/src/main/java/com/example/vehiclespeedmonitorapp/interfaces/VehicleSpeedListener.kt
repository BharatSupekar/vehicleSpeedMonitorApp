package com.example.vehiclespeedmonitorapp.interfaces

interface VehicleSpeedListener {

    fun showAlertMessage()

    fun notifyRentalCompany(currentSpeed: Float)
}