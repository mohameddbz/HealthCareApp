package com.example.projecttdm.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import com.example.projecttdm.R
import com.example.projecttdm.data.db.AppDatabase
import com.example.projecttdm.data.endpoint.PrescriptionEndPoint
import com.example.projecttdm.data.entity.PrescriptionEntity
import com.example.projecttdm.data.entity.SyncPrescriptionsRequest
import com.example.projecttdm.data.entity.SyncPrescriptionsResponse
import com.example.projecttdm.data.model.*
import com.example.projecttdm.utils.NetworkUtils
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PrescriptionRepository(private val endpoint: PrescriptionEndPoint, private val localDB: AppDatabase) {


        suspend fun getAllPrescriptions(): List<Prescriptions> {
            return endpoint.getAllPrescriptions()
        }

        suspend fun getPrescriptionById(id: String): PrescriptionResponse {
            return endpoint.getPrescriptionById(id)
        }

        suspend fun getPrescriptionsByDoctor(doctorId: String): List<Prescriptions> {
            return endpoint.getPrescriptionsByDoctor(doctorId)
        }

        suspend fun getPrescriptionsByPatient(patientId: String): List<Prescriptions> {
            return endpoint.getPrescriptionsByPatient(patientId)
        }

  /*  suspend fun createPrescription(
        patientId: String,
        medications: List<Medications>,
        instructions: String,
        expiryDate: String,
        appointmentId: String  // Ajout du paramètre appointmentId optionnel
    ): PrescriptionResponse {
        val request = PrescriptionRequest(
            patientId = patientId,
            medications = medications,
            instructions = instructions,
            expiryDate = expiryDate,
            appointmentId = appointmentId  // Ajout du champ appointmentId
        )

        println("-=-=-=-=-=-=-= ${patientId}")
        return endpoint.createPrescription(request)
    }

   */

    suspend fun createPrescription(
        context: Context,
        patientId: String,
        medications: List<Medications>,
        instructions: String,
        expiryDate: String,
        appointmentId: String
    ): PrescriptionResponse {
        val request = PrescriptionRequest(
            patientId = patientId,
            medications = medications,
            instructions = instructions,
            expiryDate = expiryDate,
            appointmentId = appointmentId
        )

        return if (NetworkUtils.isNetworkAvailable(context)) {
            // Envoie au serveur distant
            endpoint.createPrescription(request)
        } else {
            // Sérialisation des médicaments
            val medicationsJson = Gson().toJson(medications)

            localDB.presctriptionDao().insertPrescription(
                PrescriptionEntity(
                    patientId = patientId,
                    appointmentId = appointmentId,
                    instructions = instructions,
                    expiryDate = expiryDate,
                    medicationsJson = medicationsJson
                )
            )

            PrescriptionResponse(
                success = false,
                message = "Prescription enregistrée localement. Elle sera synchronisée plus tard.",
                prescription = null
            )
        }
    }

    suspend fun updatePrescription(
        id: String,
        patientId: String,
        doctorId: String,
        medications: List<Medications>,
        instructions: String,
        expiryDate: String,
        appointmentId: String   // Ajout du paramètre appointmentId optionnel
    ): PrescriptionResponse {
        val request = PrescriptionRequest(
            patientId = patientId,
//            doctorId = doctorId,
            medications = medications,
            instructions = instructions,
            expiryDate = expiryDate,
            appointmentId = appointmentId  // Ajout du champ appointmentId
        )
        return endpoint.updatePrescription(id, request)
    }

        suspend fun deletePrescription(id: String): PrescriptionResponse {
            return endpoint.deletePrescription(id)
        }


    fun getSamplePrescription(): FullPrescription {
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

        val patient = PatientX(
            patient_id = 3,
            user_id = 12,
            fullName = "Dabouz Mohamed",
            sexe = "Homme",
            date_birthday = "2025-04-08T10:52:12.000Z",
        )

        val medications = listOf(
            Medication("Lisinopril", "10mg", "1 fois par jour", "30 jours"),
            Medication("Aspirine", "81mg", "1 fois par jour", "30 jours")
        )

        return FullPrescription(
            Doctor = doctor,
            Patient = patient,
            MEDICATIONs = medications,
            prescription_id =  2,
            patient_id =  3,
           doctor_id =  4,
           instructions = "madir welo sobhan lah ew ",
          appointment_id =12 ,  // Ajout du champ appointment_id optionnel
          created_at = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
            expiry_date= ""
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

    suspend fun getPrescriptions(patientId: String): Flow<Result<PrescriptionResult>> = flow {
        try {
            val response = endpoint.getPrescriptions(patientId)
            if (response.success) {
                // Extract the prescriptions list from the nested data structure
                emit(Result.success(response.data))
            } else {
                emit(Result.failure(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun syncPrescriptions(syncPrescriptionsRequest: SyncPrescriptionsRequest):Response<SyncPrescriptionsResponse> {
      return endpoint.syncPrescriptions(syncPrescriptionsRequest)
    }
}
