package com.alwinsden.dino.googleAuthn.serverManager.tables

import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

//this is the file for all Exposed transactions.
class UserInfoDbActions {
    suspend fun userExists(googleSubjectId: String) = suspendTransaction {
        UserInfo.selectAll().any { it[UserInfo.googleSubjectId] == googleSubjectId }
    }

    suspend fun createNewUser(userData: UserInfoDataClass) = suspendTransaction {
        UserInfo.insert {
            it[googleSubjectId] = userData.googleSubjectId
            it[userEmail] = userData.userEmail
            it[userFullName] = userData.userFullName
            it[userGoogleProfile] = userData.userGoogleProfile
        }
    }
}