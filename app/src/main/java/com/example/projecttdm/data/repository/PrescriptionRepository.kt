package com.example.projecttdm.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import com.example.projecttdm.R
import com.example.projecttdm.data.endpoint.PrescriptionEndPoint
import com.example.projecttdm.data.model.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PrescriptionRepository(private val endpoint: PrescriptionEndPoint) {


        suspend fun getAllPrescriptions(): List<Prescriptions> {
            return endpoint.getAllPrescriptions()
        }

        suspend fun getPrescriptionById(id: String): Prescriptions {
            return endpoint.getPrescriptionById(id)
        }

        suspend fun getPrescriptionsByDoctor(doctorId: String): List<Prescriptions> {
            return endpoint.getPrescriptionsByDoctor(doctorId)
        }

        suspend fun getPrescriptionsByPatient(patientId: String): List<Prescriptions> {
            return endpoint.getPrescriptionsByPatient(patientId)
        }

        suspend fun createPrescription(
            patientId: String,
            doctorId: String,
            medications: List<Medications>,
            instructions: String,
            expiryDate: String
        ): PrescriptionResponse {
            val request = PrescriptionRequest(
                patientId = patientId,
                doctorId = doctorId,
                medications = medications,
                instructions = instructions,
                expiryDate = expiryDate
            )
            return endpoint.createPrescription(request)
        }

        suspend fun updatePrescription(
            id: String,
            patientId: String,
            doctorId: String,
            medications: List<Medications>,
            instructions: String,
            expiryDate: String
        ): PrescriptionResponse {
            val request = PrescriptionRequest(
                patientId = patientId,
                doctorId = doctorId,
                medications = medications,
                instructions = instructions,
                expiryDate = expiryDate
            )
            return endpoint.updatePrescription(id, request)
        }

        suspend fun deletePrescription(id: String): PrescriptionResponse {
            return endpoint.deletePrescription(id)
        }


    fun getSamplePrescription(): Prescription {
        val doctor = Doctor(
            id = "doc123",
            name = "Dr. Aitmekidech",
            specialty = Specialty(id = "1", name = "Cardiologue"),
            hospital = "Centre Hospitalier Universitaire",
            rating = 4.8f,
            reviewCount = 124,
            imageResId = R.drawable.doctor_image,
            about = "Spécialiste des maladies cardiaques avec une expérience de plus de 15 ans",
            yearsExperience = 15,
            hospitalLocation = "Alger, Algérie",
            patients = 1500,
            workingHours = "08:00 - 17:00"
        )

        val patient = Patient(
            id = "pat456",
            fullName = "Dabouz Mohamed",
            gender = "Homme",
            age = 66,
            problemDescription = "Hypertension artérielle"
        )

        val medications = listOf(
            Medication("Lisinopril", "10mg", "1 fois par jour", "30 jours"),
            Medication("Aspirine", "81mg", "1 fois par jour", "30 jours")
        )

        return Prescription(
            doctor = doctor,
            patient = patient,
            medications = medications,
            date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        )
    }

    suspend fun createPdfFromView(context: Context, view: View): Uri? {
        return try {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            val fileName = "prescription_${System.currentTimeMillis()}.pdf"
            val file = File(context.filesDir, fileName)

            FileOutputStream(file).use { output ->
                pdfDocument.writeTo(output)
            }
            pdfDocument.close()

            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
