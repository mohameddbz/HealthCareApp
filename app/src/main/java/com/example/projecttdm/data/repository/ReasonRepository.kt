package com.example.projecttdm.data.repository

import android.content.Context
import com.example.projecttdm.data.local.CancelReasonData
import com.example.projecttdm.data.local.RescheduleReasonData
import com.example.projecttdm.data.model.RescheduleReason

class ReasonRepository(private val context: Context) {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("reschedule_prefs", Context.MODE_PRIVATE)
    }

    // Get all reasons including standard and custom ones
    fun getRescheduleReasons(): List<RescheduleReason> {
        val standardReasons = RescheduleReasonData.rescheduleReasons
        val customReasons = getCustomReasons()

        // Return standard reasons with custom ones
        return standardReasons
    }

    // Get all cancel reasons including standard and custom ones
    fun getCancelReasons(): List<RescheduleReason> {
        val standardReasons = CancelReasonData.cancelReasons
        val customCancelReasons = getCustomCancelReasons()

        // Create a combined list with a custom "others" reason
        val allReasons = standardReasons.toMutableList()
        allReasons.add(RescheduleReason("others", "Other reason"))

        return allReasons
    }

    // Get custom cancel reasons saved previously
    private fun getCustomCancelReasons(): List<RescheduleReason> {
        val savedReasons = sharedPreferences.getStringSet("custom_cancel_reasons", setOf()) ?: setOf()
        return savedReasons.map { reasonString ->
            val parts = reasonString.split("::")
            RescheduleReason(
                id = parts.getOrElse(0) { "custom" },
                text = parts.getOrElse(1) { reasonString }
            )
        }
    }

    // Get custom reasons saved previously
    private fun getCustomReasons(): List<RescheduleReason> {
        val savedReasons = sharedPreferences.getStringSet("custom_reasons", setOf()) ?: setOf()
        return savedReasons.map { reasonString ->
            val parts = reasonString.split("::")
            RescheduleReason(
                id = parts.getOrElse(0) { "custom" },
                text = parts.getOrElse(1) { reasonString }
            )
        }
    }

    // Save a custom reason
    fun saveOtherReason(reason: String) {
        if (reason.isBlank()) return

        // Create ID for the custom reason
        val reasonId = "custom_${System.currentTimeMillis()}"
        val reasonText = reason.trim()

        // Save to shared preferences
        val savedReasons = sharedPreferences.getStringSet("custom_reasons", setOf()) ?: setOf()
        val updatedReasons = savedReasons.toMutableSet()
        updatedReasons.add("$reasonId::$reasonText")

        sharedPreferences.edit()
            .putStringSet("custom_reasons", updatedReasons)
            .apply()

        // Also save as last used reason
        saveLastCustomReason(reasonText)
    }

    // Get the last used custom reason
    fun getLastCustomReason(): String {
        return sharedPreferences.getString("last_custom_reason", "") ?: ""
    }

    // Save the last used custom reason
    fun saveLastCustomReason(reason: String) {
        sharedPreferences.edit()
            .putString("last_custom_reason", reason)
            .apply()
    }

    // Save a custom cancel reason
    fun saveOtherCancelReason(reason: String) {
        if (reason.isBlank()) return

        // Create ID for the custom reason
        val reasonId = "custom_cancel_${System.currentTimeMillis()}"
        val reasonText = reason.trim()

        // Save to shared preferences
        val savedReasons = sharedPreferences.getStringSet("custom_cancel_reasons", setOf()) ?: setOf()
        val updatedReasons = savedReasons.toMutableSet()
        updatedReasons.add("$reasonId::$reasonText")

        sharedPreferences.edit()
            .putStringSet("custom_cancel_reasons", updatedReasons)
            .apply()

        // Also save as last used reason
        saveLastCustomCancelReason(reasonText)
    }

    // Get the last used custom cancel reason
    fun getLastCustomCancelReason(): String {
        return sharedPreferences.getString("last_custom_cancel_reason", "") ?: ""
    }

    // Save the last used custom cancel reason
    fun saveLastCustomCancelReason(reason: String) {
        sharedPreferences.edit()
            .putString("last_custom_cancel_reason", reason)
            .apply()
    }
}