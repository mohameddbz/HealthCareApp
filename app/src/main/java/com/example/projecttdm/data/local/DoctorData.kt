package com.example.projecttdm.data.local

import com.example.projecttdm.R
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty

object DoctorData {
    // Define specialties
    val specialties = listOf(
        Specialty(id = "1", name = "Cardiologists"),
        Specialty(id = "2", name = "Immunologists"),
        Specialty(id = "3", name = "Neurologists"),
        Specialty(id = "4", name = "Allergists"),
        Specialty(id = "5", name = "General Practitioner")
    )

    val listDcctors = listOf(
        Doctor(
            id = "1",
            name = "Randy Wigham",
            specialty = specialties[0], // Cardiologists
            hospital = "The Valley Hospital",
            rating = 4.8f,
            reviewCount = 4279,
            imageResId = R.drawable.doctor_image
        ),
        Doctor(
            id = "2",
            name = "Jenny Watson",
            specialty = specialties[1], // Immunologists
            hospital = "Christ Hospital",
            rating = 4.4f,
            reviewCount = 6942,
            imageResId = R.drawable.logo
        ),
        Doctor(
            id = "3",
            name = "Raul Zirkind",
            specialty = specialties[2], // Neurologists
            hospital = "Franklin Hospital",
            rating = 4.8f,
            reviewCount = 5362,
            imageUrl = "https://example.com/doctor3.jpg"
        ),
        Doctor(
            id = "4",
            name = "Elijah Baranick",
            specialty = specialties[3], // Allergists
            hospital = "JFK Medical Center",
            rating = 4.6f,
            reviewCount = 3837,
            imageUrl = "https://example.com/doctor4.jpg"
        ),
        Doctor(
            id = "5",
            name = "Stephen Shute",
            specialty = specialties[4], // General Practitioner
            hospital = "Memorial Hospital",
            rating = 4.5f,
            reviewCount = 4050,
            imageUrl = "https://example.com/doctor5.jpg"
        )
    )
}