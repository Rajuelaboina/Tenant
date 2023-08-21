package com.tenant.mytenant.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.NavController
import com.tenant.mytenant.MyBroadcastReceiver
import com.tenant.mytenant.R
import com.tenant.mytenant.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

         navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        /*binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

        }*/

        /*var alarmMgr: AlarmManager? = null
        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?*/
        val calendar = Calendar.getInstance()
        // calendar.set(Calendar.YEAR,2023)
        // calendar.set(Calendar.MONTH,7)
        //  calendar.set(Calendar.DAY_OF_MONTH,18)
        calendar.set(Calendar.HOUR_OF_DAY,7)
        calendar.set(Calendar.MINUTE,58)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.AM_PM, Calendar.PM)

        val i: Int = 50
        val intent = Intent(applicationContext, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext.applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)
        var alarmMgr: AlarmManager? = null
        alarmMgr = applicationContext?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
        if (alarmMgr != null) {
            // alarmMgr.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + (i * 1000), pendingIntent)
            // alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,AlarmManager.INTERVAL_DAY,calendar.getTimeInMillis(), pendingIntent)
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent)
        }

        Toast.makeText(applicationContext, "Alarm set in $i seconds", Toast.LENGTH_LONG).show()
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {}
            R.id.action_addUser -> {
                navController.navigate(R.id.action_HomeFragment_to_RegistrationFragment)
            }

        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}