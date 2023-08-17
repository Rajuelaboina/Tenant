package com.tenant.mytenant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tenant.mytenant.model.Payment
import com.tenant.mytenant.model.PowerWaterPayment
import com.tenant.mytenant.model.UserRegistration

@Database(entities = [UserRegistration::class,Payment::class,PowerWaterPayment::class], version = 2)
abstract class UserDataBase:RoomDatabase() {
   abstract fun userDao(): UserRegistrationDao

   companion object{
       var instance:UserDataBase?=null
       fun getInstance(context: Context):UserDataBase{
           if (instance==null){
               instance = Room.databaseBuilder(context.applicationContext,UserDataBase::class.java,"tenant_dataBase")
                   .fallbackToDestructiveMigration().build()

           }
           return instance!!
       }
   }
}