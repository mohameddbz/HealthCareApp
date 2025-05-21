package com.example.projecttdm.data.db

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.projecttdm.data.entity.MedicationDto
import com.example.projecttdm.data.entity.PrescriptionEntity
import com.example.projecttdm.data.entity.PrescriptionSyncDto
import com.example.projecttdm.data.entity.SyncPrescriptionsRequest
import com.example.projecttdm.data.repository.RepositoryHolder
import org.json.JSONArray
import java.time.Duration
import java.util.concurrent.TimeUnit

class SyncPrescriptionsWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    private val dao = DatabaseProvider.getDatabase(ctx).presctriptionDao()

    @RequiresApi(Build.VERSION_CODES.O)
    private val api = RepositoryHolder.prescriptionRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        try {
            // Récupère les prescriptions locales non synchronisées
            val localPrescriptions = dao.getUnsyncedPrescriptionsWithMedications()
            Log.d("SyncWorker", "Found ${localPrescriptions.size} unsynced prescriptions")

            if (localPrescriptions.isEmpty()) {
                return Result.success()
            }

            // Convertit les prescriptions en DTO pour l'API
            val syncList = localPrescriptions.map { prescription ->
                Log.d("SyncWorker", "Preparing prescription: ${prescription.id}")
                val medications = try {
                    parseMedications(prescription.medicationsJson)
                } catch (e: Exception) {
                    Log.e("SyncWorker", "Failed to parse medications for prescription ${prescription.id}", e)
                    return@map null // on ignore cette prescription si parsing échoue
                }

                medications?.let {
                    PrescriptionSyncDto(
                        patientId = prescription.patientId,
                        doctorId = "", // TODO: Remplacer par une valeur valide si nécessaire
                        instructions = prescription.instructions,
                        expiryDate = prescription.expiryDate,
                        medications = it,
                        appointmentId = prescription.appointmentId
                    )
                }
            }.filterNotNull()

            if (syncList.isEmpty()) {
                Log.e("SyncWorker", "No valid prescriptions to sync after filtering.")
                return Result.failure() // On arrête, toutes les prescriptions sont invalides
            }

            val request = SyncPrescriptionsRequest(syncList)
            val response = api.syncPrescriptions(request)

            Log.d("SyncWorker", "API call successful: ${response.body()?.success}")
            Log.d("SyncWorker", "API response body: ${response.body()}")

            if (response.isSuccessful && response.body()?.success == true) {

                for (prescription in localPrescriptions) {
                    Log.d("SyncWorker", "Deleting synced prescription: ${prescription.id}")
                    dao.deletePrescriptionWithMedications(prescription.id)
                }
                return Result.success()
            } else {
                Log.e("SyncWorker", "API error: code=${response.code()}, message=${response.message()}")
                return Result.retry()
            }

        } catch (e: Exception) {
            Log.e("SyncWorker", "Unexpected exception during sync", e)
            return Result.retry() // Réessaie plus tard si exception inattendue
        }
    }
}




fun scheduleSyncWork(context: Context) {
    // Définir les contraintes - nécessite une connexion Internet
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    // Créer une requête de travail périodique avec un intervalle d'une minute
    val periodicSyncRequest = PeriodicWorkRequestBuilder<SyncPrescriptionsWorker>(
        1, TimeUnit.MINUTES,  // Intervalle de répétition
    )
        .setConstraints(constraints)
        .build()

    Toast.makeText(context,"Worker ak cheyf ",Toast.LENGTH_LONG).show()
    Log.d("syncWorker","Worker rew ykhdem ")

    // Enregistrer le travail périodique avec WorkManager
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "auto_sync_prescriptions",
        ExistingPeriodicWorkPolicy.KEEP,  // Conserver le travail existant s'il y en a un
        periodicSyncRequest
    )
}





fun parseMedications(json: String): List<MedicationDto> {
    val medications = mutableListOf<MedicationDto>()
    val jsonArray = JSONArray(json)

    for (i in 0 until jsonArray.length()) {
        val item = jsonArray.getJSONObject(i)
        val medication = MedicationDto(
            name = item.getString("name").trim(),
            dosage = item.getString("dosage").trim(),
            frequency = item.getString("frequency").trim(),
            duration = item.getString("duration").trim()
        )
        medications.add(medication)
    }

    return medications
}









@Composable
fun SyncButton() {
    val context = LocalContext.current

    Button(
        onClick = {
            val syncRequest = OneTimeWorkRequestBuilder<SyncPrescriptionsWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueue(syncRequest)
        }
    ) {
        Text("Synchroniser maintenant")
    }
}