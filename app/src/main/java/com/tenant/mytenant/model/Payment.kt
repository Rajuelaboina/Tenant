package com.tenant.mytenant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "rentPayment")
data class Payment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "month")
    var month: String,
    @ColumnInfo(name = "year")
    var year: String,
    @ColumnInfo(name = "mobileNumber")
    var mobileNumber: String,
    @ColumnInfo(name = "rentAmount")
    var rentAmount: Double,
    @ColumnInfo(name = "paidAmount")
    var paidAmount: Double,
    @ColumnInfo(name = "dueAmount")
    var dueAmount: Double

) : Serializable
