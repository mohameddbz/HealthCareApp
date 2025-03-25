 // Contient des données statiques avant l'intégration d'une base de données

package com.example.projecttdm.data.local

import com.example.projecttdm.data.model.User

object StaticData {
    val users = listOf(
        User(1, "Dr. John Doe", "john@example.com", "doctor"),
        User(2, "Jane Doe", "jane@example.com", "patient")
    )
}