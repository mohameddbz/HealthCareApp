package com.example.projecttdm.ui.common.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun showError(message: String) {
    val context = LocalContext.current
    println("---------------------")
    println("---------------------")

    println("---------------------")
    println("---------------------")
    println(message)
    println("---------------------")
    println("---------------------")
    println("---------------------")
    println("---------------------")
    println("---------------------")

    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}