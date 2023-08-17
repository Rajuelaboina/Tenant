package com.tenant.mytenant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "powerWaterBill")
data class PowerWaterPayment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "month")
    var month: String,
    @ColumnInfo(name = "year")
    var year: String,
    @ColumnInfo(name = "mobileNumber")
    var mobileNumber: String,
    @ColumnInfo(name = "powerWaterBill")
    var powerWaterBill: String,
    @ColumnInfo(name = "powerWaterPaid")
    var powerWaterPaid: String,
    @ColumnInfo(name = "powerWaterDue")
    var powerWaterDue: String

)
