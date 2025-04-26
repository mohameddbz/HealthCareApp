 // Contient des données statiques avant l'intégration d'une base de données

package com.example.projecttdm.data.local

import com.example.projecttdm.data.model.Notification
import com.example.projecttdm.data.model.User

object StaticData {
    val users = listOf<User>(
      //  User(1, "Dr. John Doe", "john@example.com", "doctor"),
       // User(2, "Jane Doe", "jane@example.com", "patient")
    )
    val notifications = listOf(
        Notification(
            notification_id = 1,
            user_id = 12345,
            title = "Appointment Cancelled",
            message = "Your appointment with Dr. Alan Watson on December 24, 2024, at 10:00 PM has been cancelled. 80% of the funds will be refunded to your account.",
            date = "2024-12-24",
            heure = "15:36 PM",
            is_read = false
        ),
        Notification(
            notification_id = 2,
            user_id = 12345,
            title = "Schedule Modified",
            message = "Your appointment with Dr. Alan Watson has been rescheduled to December 24, 2024, at 13:00 PM. Please confirm your new time slot.",
            date = "2024-12-23",
            heure = "09:23 AM",
            is_read = false
        ),
        Notification(
            notification_id = 3,
            user_id = 12345,
            title = "Appointment Confirmed",
            message = "Your appointment with Dr. Alan Watson on December 24, 2024, at 10:00 AM is now confirmed. Don't forget to activate your reminder.",
            date = "2024-12-19",
            heure = "06:35 PM",
            is_read = true
        ),
        Notification(
            notification_id = 4,
            user_id = 12345,
            title = "New Services Available",
            message = "Exciting update! You can now book multiple medical appointments simultaneously and easily cancel appointments online.",
            date = "2024-12-14",
            heure = "10:52 AM",
            is_read = true
        ),
        Notification(
            notification_id = 5,
            user_id = 12345,
            title = "Payment Method Updated",
            message = "Your credit card has been successfully linked with Medica. You can now make seamless payments for your medical services.",
            date = "2024-12-12",
            heure = "03:38 PM",
            is_read = true
        ),
        Notification(
            notification_id = 6,
            user_id = 12345,
            title = "Medical Records Update",
            message = "Your recent medical test results are now available in your patient portal. Please log in to view them.",
            date = "2024-12-10",
            heure = "11:15 AM",
            is_read = true
        ),
        Notification(
            notification_id = 7,
            user_id = 12345,
            title = "Prescription Renewal",
            message = "Your prescription for medication X is due for renewal. Please contact your doctor to schedule a review.",
            date = "2024-12-08",
            heure = "02:45 PM",
            is_read = false
        ),
        Notification(
            notification_id = 8,
            user_id = 12345,
            title = "Upcoming Vaccination",
            message = "Your annual flu vaccination is recommended. Book your slot now to stay protected this winter.",
            date = "2024-12-05",
            heure = "09:00 AM",
            is_read = false
        )
    )
}