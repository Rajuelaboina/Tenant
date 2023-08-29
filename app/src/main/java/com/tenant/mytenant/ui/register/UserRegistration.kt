package com.tenant.mytenant.ui.register

import android.os.Parcelable
import android.text.TextUtils
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Registration")

data class UserRegistration (
    @ColumnInfo(name = "userName")
    var userName: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mobileNumber")
    var mobileNumber: Long,
    @ColumnInfo(name = "aadharNumber")
    var aadharNumber: String,
    @ColumnInfo(name = "roomNumber")
    var roomNumber: String,
    @ColumnInfo(name = "rentAmount")
    var rentAmount: Double,
    @ColumnInfo(name = "joinDate")
    var joinDate: String,
    @ColumnInfo(name = "status")
    var status: Boolean
        ): BaseObservable(), Serializable {
    fun isValidUser():Int{
        return if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(mobileNumber.toString()) && !TextUtils.isEmpty(aadharNumber) && !TextUtils.isEmpty(roomNumber.toString())
            && !TextUtils.isEmpty(rentAmount.toString()) && !TextUtils.isEmpty(joinDate) ){
            -1   //success
        }else{
            1  // all textFields are  is empty
        }
    }
}