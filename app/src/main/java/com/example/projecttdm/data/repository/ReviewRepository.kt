package com.example.projecttdm.data.repository

import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.data.endpoint.ReviewEndPoint
import com.example.projecttdm.data.model.Review
import com.example.projecttdm.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ReviewRepository {

    private val endpoint = ApiClient.create(ReviewEndPoint::class.java)

    fun getReviewOfDoctor(doctorId: String): Flow<UiState<List<Review>>> = flow {
        emit(UiState.Loading)
        try {
            val reviews = endpoint.getReviewsByDoctor(doctorId)
            emit(UiState.Success(reviews))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}