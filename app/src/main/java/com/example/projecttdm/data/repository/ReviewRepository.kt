package com.example.projecttdm.data.repository

import com.example.projecttdm.data.local.ReviewData
import com.example.projecttdm.data.model.Review

class ReviewRepository {
    fun getReviewOfDoctor(id:String) :List<Review> = ReviewData.reviews
}