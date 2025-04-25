package com.example.projecttdm.data.repository

import com.example.projecttdm.data.local.StaticData
import com.example.projecttdm.data.model.Notification
import com.example.projecttdm.data.model.User

class NotificationRepository {

  suspend  fun getNotificationsOfUser(): List<Notification> = StaticData.notifications

}