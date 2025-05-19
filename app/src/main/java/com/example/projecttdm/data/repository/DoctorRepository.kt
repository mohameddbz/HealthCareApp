package com.example.projecttdm.data.repository

import com.example.projecttdm.R
import com.example.projecttdm.data.endpoint.DoctorEndPoint
import com.example.projecttdm.data.local.DoctorData
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Specialty
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoctorRepository (private val endpoint: DoctorEndPoint) {

    private val patientFavorites = mapOf(
        "patient1" to listOf("1", "3", "5"),
        "patient2" to listOf("2", "4"),
        "patient3" to listOf("1", "2", "3", "4", "5")
    )


    private val defaultPatientId = "patient1"


    fun getDoctors(): List<Doctor> = DoctorData.listDcctors
<<<<<<< Updated upstream
=======
    @RequiresApi(Build.VERSION_CODES.O)

    fun getAppointmentsByDate(
        date: String
    ): Flow<UiState<List<AppointmentWeekResponse>>> = flow {
        emit(UiState.Loading)
        try {
            val response = endpoint.getAppointmentsByDate(date)
            emit(UiState.Success(response.data)) // on ne garde que la liste d'appels
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Erreur lors du chargement des rendez-vous"))
        }
    }.flowOn(Dispatchers.IO)

>>>>>>> Stashed changes


    //fun getTopDoctors(): List<Doctor> = DoctorData.listDcctors
    fun getTopDoctors(): Flow<UiState<List<Doctor>>> = flow {
        emit(UiState.Loading) // Emit loading first
        try {
            val doctorsList = endpoint.getDoctors() // Fetch data
            emit(UiState.Success(doctorsList))      // Emit success
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown error")) // Emit error
        }
    }

    /**
     * Search doctors by name, specialty, or hospital
     */
    fun searchDoctors(query: String): Flow<UiState<List<Doctor>>> = flow {
        emit(UiState.Loading) // Emit loading state first
        try {
            // Simulate network call with local data
            val searchQuery = query.lowercase().trim()
            val filteredDoctors = DoctorData.listDcctors.filter { doctor ->
                doctor.name.lowercase().contains(searchQuery) ||
                        doctor.specialty.name.lowercase().contains(searchQuery) ||
                        doctor.hospital.lowercase().contains(searchQuery)
            }

            // Add small delay to simulate network request
            kotlinx.coroutines.delay(300)
            emit(UiState.Success(filteredDoctors))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Error searching for doctors"))
        }
    }

    fun getDoctorById(id: String): Doctor? {
        return DoctorData.listDcctors.find { it.id == id }
    }


//    fun getDetailDoctorById(doctorId: String): Doctor {
//        val existingDoctor = getDoctorById(doctorId)
//        if (existingDoctor != null) {
//            return existingDoctor
//        }
//
//
//        return Doctor(
//            id = "dr-jenny-watson",
//            name = "Dr. Jenny Watson",
//            specialty = Specialty(id = "12", name = "Ophtalmologue"),
//            hospital = "Christ Hospital",
//            hospitalLocation = "London, UK",
//            patients = 5000,
//            yearsExperience = 10,
//            rating = 4.8f,
//            reviewCount = 4942,
//            about = "Dr. Jenny Watson is the top most Immunologists specialist in Christ Hospital at London. She achieved several awards for her wonderful contribution in medical field. She is available for private consultation.",
//            workingHours = "Monday - Friday, 08.00 AM - 20.00 PM",
//            imageResId = R.drawable.doctor_image2,
//        )
//    }

    fun getDetailDoctorById(doctorId: String): Flow<UiState<Doctor>> = flow {
        emit(UiState.Loading)
        try {
            // Make network call directly on IO dispatcher
            val doctor = endpoint.getDoctorById(doctorId)
            emit(UiState.Success(doctor))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "An unexpected error occurred"))
        }
    }


    fun getFavoriteDoctors(patientId: String = defaultPatientId): List<Doctor> {
        val favoriteIds = patientFavorites[patientId] ?: emptyList()
        return DoctorData.listDcctors.filter { it.id in favoriteIds }
    }


    fun isFavorite(doctorId: String, patientId: String = defaultPatientId): Boolean {
        val favoriteIds = patientFavorites[patientId] ?: emptyList()
        return doctorId in favoriteIds
    }


    private val currentSessionFavorites = mutableSetOf<String>()


    init {
        patientFavorites[defaultPatientId]?.let {
            currentSessionFavorites.addAll(it)
        }
    }


    fun toggleSessionFavorite(doctorId: String): Boolean {
        return if (doctorId in currentSessionFavorites) {
            currentSessionFavorites.remove(doctorId)
            false
        } else {
            currentSessionFavorites.add(doctorId)
            true
        }
    }

    fun getSessionFavoriteDoctors(): List<Doctor> {
        return DoctorData.listDcctors.filter { it.id in currentSessionFavorites }
    }

    fun isSessionFavorite(doctorId: String): Boolean {
        return doctorId in currentSessionFavorites
    }
}