package com.example.projecttdm.data.repository



import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecttdm.R
import com.example.projecttdm.data.model.Appointment
import com.example.projecttdm.data.model.AppointmentReviewData
import com.example.projecttdm.data.model.AppointmentStatus
import com.example.projecttdm.data.model.Doctor
import com.example.projecttdm.data.model.Patient
import com.example.projecttdm.data.model.Specialty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime

class AppointmentSummaryRepository  {

    // In a real app, this would fetch data from a remote API or local database
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAppointmentReviewData(appointmentId: String): Flow<AppointmentReviewData> = flow {
        // Simulate network delay in a real app
        // delay(1000)

//        emit(
//            AppointmentReviewData(
//                doctor = Doctor(
//                    id = "DR12345",
//                    name = "Dr. Jenny Watson",
//                    specialty = Specialty(
//                        id = "SPEC01",
//                        name = "Immunologist"
//                    ),
//                    hospital = "Christ Hospital in London, UK",
//                    rating = 4.8f,
//                    reviewCount = 124,
//                    imageResId = R.drawable.doctor_image
//                ),
//                appointment = Appointment(
//                    id = appointmentId,
//                    patientId = "PT-7829456",
//                    doctorId = "DR12345",
//                    date = LocalDate.of(2024, 12, 23),
//                    time = LocalTime.of(10, 0),
//                    status = AppointmentStatus.CONFIRMED,
//                    reason = "Follow-up check after treatment"
//                ),
//                patient = Patient(
//                    id = "PT-7829456",
//                    fullName = "John Smith",
//                    gender = "Male",
//                    age = 42,
//                    problemDescription = "Recurring cold and flu symptoms"
//                )
//            )
//        )

    }
}