package com.example.projecttdm.data.local

import com.example.projecttdm.data.model.Patient

object PatientData {

    val samplePatients = listOf(
        Patient(
            id = "p1",
            fullName = "Andrew Ainsley",
            gender = "Male",
            age = 27,
            problemDescription = "Hello Dr. Jenny, I have a problem with my immune system. Lorem ipsum dolor sit amet, consectetur adipiscing elit..."
        ),
        Patient(
            id = "p2",
            fullName = "Sarah Johnson",
            gender = "Female",
            age = 34,
            problemDescription = "I've been experiencing severe migraines for the past month, especially after long screen time at work."
        ),
        Patient(
            id = "p3",
            fullName = "Michael Chen",
            gender = "Male",
            age = 42,
            problemDescription = "I need a follow-up appointment regarding my diabetes management. My blood sugar levels have been fluctuating lately."
        ),
        Patient(
            id = "p4",
            fullName = "Emily Rodriguez",
            gender = "Female",
            age = 29,
            problemDescription = "I've been having persistent back pain that worsens when I sit for long periods. I work in an office environment."
        ),
        Patient(
            id = "p5",
            fullName = "James Wilson",
            gender = "Male",
            age = 58,
            problemDescription = "I would like to discuss the results of my recent cardiovascular tests and next steps for treatment."
        )
    )

    // Common gender options for dropdown
    val genderOptions = listOf("Male", "Female", "Non-binary", "Prefer not to say")
}