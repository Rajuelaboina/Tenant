package com.tenant.mytenant.database

import androidx.room.*
import com.tenant.mytenant.ui.rentpayment.Payment
import com.tenant.mytenant.ui.billpayment.PowerWaterPayment
import com.tenant.mytenant.ui.register.UserRegistration

@Dao
interface UserRegistrationDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertUser(userRegistration: UserRegistration)
    @Query("SELECT * FROM Registration")
     fun getAllUsers(): List<UserRegistration>

    /*@Query("SELECT EXISTS(SELECT * FROM Registration WHERE mobileNumber = :mobileNumber)")
    fun isRecordExistsUserId(mobileNumber: String): Boolean*/
    @Update
    fun getUpdate(userRegistration: UserRegistration)
    //delete
    @Query("DELETE FROM Registration WHERE mobileNumber = :mobileNumber")
    fun deleteUser(mobileNumber: String)

    //  payment table  -- ///
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPayment(payment: Payment)

    @Query("SELECT * FROM rentPayment WHERE mobileNumber = :mobileNumber ORDER BY month")
    fun getUserAllPayments(mobileNumber: String):List<Payment>

    @Update
    fun paymentUpdate(payment: Payment)

    @Query("SELECT EXISTS(SELECT * FROM rentPayment WHERE month = :month AND year = :year AND mobileNumber = :mobileNumber )")
    fun isRecordExistsUserId(month: String,year:String,mobileNumber:String): Boolean

    //delete
    @Query("DELETE FROM rentPayment WHERE mobileNumber = :mobileNumber")
    fun deletepayUser(mobileNumber: String)

    // powerWater bill table Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPowerWaterPayment(powerWaterPayment: PowerWaterPayment)

   @Query("SELECT EXISTS(SELECT * FROM powerWaterBill WHERE month = :month AND year = :year AND mobileNumber = :mobileNumber )")
   fun isRecordExistsPowerBill(month: String,year:String,mobileNumber:String): Boolean

    @Query("SELECT * FROM powerWaterBill WHERE mobileNumber = :mobileNumber ORDER BY month")
    fun getUserAllPowerPayments(mobileNumber: String):List<PowerWaterPayment>

    @Update
    fun powerBillUpdate(payment: PowerWaterPayment)

    @Query("DELETE FROM powerWaterBill WHERE mobileNumber = :mobileNumber")
    fun deletePowerBillUser(mobileNumber: String)

}