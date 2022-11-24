package com.app.transitbookingsystem.models

data class Application(
    val id: String?,
    val email: String?,
    val visitorName: String?,
    val purpose: String?,
    val dateOfArrival: String?,
    val dateOfDeparture: String?,
    val TotalNumberOfDays: String?,
    val studentName: String?,
    val regNo: String?,
    val hostel: String?,
    val roomNo: String?,
    val mobNo: String?,
    val paymentMode: String?,
    val approvedByHostel: Boolean?,
    val approvedByGuestHouse: Boolean?
        )