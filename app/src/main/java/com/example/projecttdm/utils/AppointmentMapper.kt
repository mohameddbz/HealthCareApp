package com.example.projecttdm.utils


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.AppointmentWeekResponse
import com.example.projecttdm.data.model.DoctorInfo
import com.example.projecttdm.data.model.PatientInfo
import com.example.projecttdm.data.model.SlotInfo
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Mapper class to convert backend JSON responses to AppointmentWeekResponse objects
 */
object AppointmentMapper {


    @RequiresApi(Build.VERSION_CODES.O)
    fun mapAppointmentsResponse(jsonResponse: String): List<AppointmentWeekResponse> {
        val responseJson = JSONObject(jsonResponse)
        val appointmentsArray = responseJson.getJSONArray("data")
        val appointments = mutableListOf<AppointmentWeekResponse>()

        for (i in 0 until appointmentsArray.length()) {
            val appointmentJson = appointmentsArray.getJSONObject(i)
            appointments.add(mapAppointment(appointmentJson))
        }

        return appointments
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun mapAppointment(appointmentJson: JSONObject): AppointmentWeekResponse {
        // Parse the main appointment fields
        val appointmentId = appointmentJson.getString("appointment_id")
        val slotId = appointmentJson.getString("slot_id")
        val patientId = appointmentJson.getString("patient_id")
        val statusString = appointmentJson.getString("status")
        val status = AppointmentStatus.fromString(statusString)

        // Parse optional fields
        val reason = if (appointmentJson.has("reason") && !appointmentJson.isNull("reason"))
            appointmentJson.getString("reason") else null
        val qrData = if (appointmentJson.has("qr_data") && !appointmentJson.isNull("qr_data"))
            appointmentJson.getString("qr_data") else null

        // Parse slot info
        val slotInfo = if (appointmentJson.has("slot_info") && !appointmentJson.isNull("slot_info")) {
            val slotInfoJson = appointmentJson.getJSONObject("slot_info")
            mapSlotInfo(slotInfoJson)
        } else null

        // Parse patient info
        val patientInfo = if (appointmentJson.has("patient_info") && !appointmentJson.isNull("patient_info")) {
            val patientInfoJson = appointmentJson.getJSONObject("patient_info")
            mapPatientInfo(patientInfoJson)
        } else null

        // Parse doctor info
        val doctorInfo = if (appointmentJson.has("doctor_info") && !appointmentJson.isNull("doctor_info")) {
            val doctorInfoJson = appointmentJson.getJSONObject("doctor_info")
            mapDoctorInfo(doctorInfoJson)
        } else null

        return AppointmentWeekResponse(
            appointment_id = appointmentId,
            slot_id = slotId,
            patient_id = patientId,
            status = status.toString(),
            reason = reason,
            qr_data = qrData,
            slot_info = slotInfo,
            patient_info = patientInfo,
            doctor_info = doctorInfo
        )
    }

    /**
     * Maps a JSON slot info object to a SlotInfo object
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun mapSlotInfo(slotInfoJson: JSONObject): SlotInfo {
        val startTimeString = slotInfoJson.getString("start_time")
        val endTimeString = slotInfoJson.getString("end_time")
        val workingDateString = slotInfoJson.getString("working_date")
        val isBooked = slotInfoJson.getBoolean("is_booked")

        // Parse time strings using the same format as your adapters
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        return SlotInfo(
            start_time = startTimeString,
            end_time = endTimeString,
            working_date = workingDateString,
            is_booked = isBooked
        )
    }

    /**
     * Maps a JSON patient info object to a PatientInfo object
     */
    private fun mapPatientInfo(patientInfoJson: JSONObject): PatientInfo {
        val name = patientInfoJson.getString("name")

        val email = if (patientInfoJson.has("email") && !patientInfoJson.isNull("email"))
            patientInfoJson.getString("email") else null

        val phone = if (patientInfoJson.has("phone") && !patientInfoJson.isNull("phone"))
            patientInfoJson.getString("phone") else null

        return PatientInfo(
            name = name,
            email = email,
            phone = phone
        )
    }


    private fun mapDoctorInfo(doctorInfoJson: JSONObject): DoctorInfo {
        val doctorId = doctorInfoJson.getString("doctor_id")
        val name = doctorInfoJson.getString("name")

        return DoctorInfo(
            doctor_id = doctorId,
            name = name
        )
    }
}