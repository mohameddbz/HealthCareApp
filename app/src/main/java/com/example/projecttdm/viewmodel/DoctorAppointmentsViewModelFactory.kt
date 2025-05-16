package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projecttdm.data.endpoint.DoctorEndPoint
import com.example.projecttdm.data.repository.DoctorRepository
import com.example.projecttdm.data.repository.RepositoryHolder

class AppointmentsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoctorAppointmentsViewModel::class.java)) {
            return DoctorAppointmentsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


