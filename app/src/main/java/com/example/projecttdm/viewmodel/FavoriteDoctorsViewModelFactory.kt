package com.example.projecttdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FavoriteDoctorsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteDoctorsViewModel::class.java)) {
            return FavoriteDoctorsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}