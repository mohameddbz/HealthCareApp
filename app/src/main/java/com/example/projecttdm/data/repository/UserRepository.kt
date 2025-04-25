package com.example.projecttdm.data.repository




import com.example.projecttdm.data.endpoint.UserEndPoint
import com.example.projecttdm.data.model.User


class UserRepository(private val endpoint: UserEndPoint) {


    suspend fun getCurrentUser():User {
        return endpoint.getCurrentUser();
    }


}