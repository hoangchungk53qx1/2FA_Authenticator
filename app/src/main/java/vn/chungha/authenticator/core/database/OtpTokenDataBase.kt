package vn.chungha.authenticator.core.database

import androidx.room.RoomDatabase

abstract class OtpTokenDataBase : RoomDatabase() {
    abstract fun otpTokenDao() : OtpTokenDao
}