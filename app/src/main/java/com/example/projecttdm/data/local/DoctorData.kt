package com.example.projecttdm.data.local

import com.example.projecttdm.R
import com.example.projecttdm.data.model.Doctor

object DoctorData {
    val listDcctors = listOf(
        Doctor(
            id = "1",
            name = "Randy Wigham",
            specialty = "Cardiologists",
            hospital = "The Valley Hospital",
            rating = 4.8f,
            reviewCount = 4279,
            imageResId = R.drawable.doctor_image
        ),
        Doctor(
            id = "2",
            name = "Jenny Watson",
            specialty = "Immunologists",
            hospital = "Christ Hospital",
            rating = 4.4f,
            reviewCount = 6942,
            imageResId = R.drawable.logo
        ),
        Doctor(
            id = "3",
            name = "Raul Zirkind",
            specialty = "Neurologists",
            hospital = "Franklin Hospital",
            rating = 4.8f,
            reviewCount = 5362,
            imageUrl = "https://example.com/doctor3.jpg"
        ),
        Doctor(
            id = "4",
            name = "Elijah Baranick",
            specialty = "Allergists",
            hospital = "JFK Medical Center",
            rating = 4.6f,
            reviewCount = 3837,
            imageUrl = "https://example.com/doctor4.jpg"
        ),
        Doctor(
            id = "5",
            name = "Stephen Shute",
            specialty = "General Practitioner",
            hospital = "Memorial Hospital",
            rating = 4.5f,
            reviewCount = 4050,
            imageUrl = "https://example.com/doctor5.jpg"
        )
    )
}
