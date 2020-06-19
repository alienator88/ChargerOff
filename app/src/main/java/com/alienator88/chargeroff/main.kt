package com.alienator88.chargeroff

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.BLACK
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //region Load
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        setSupportActionBar(findViewById(R.id.toolbar))
        //endregion

        //region Values
        val battview = findViewById<ImageView>(R.id.battview)
        val statusTv = findViewById<TextView>(R.id.statusTv)
        val sw = findViewById<Switch>(R.id.switch1)
        val sp = getSharedPreferences("SP_Data", Context.MODE_PRIVATE)
        val editor = sp.edit()
        val power = sp.getString("POWER", "ENABLED")
        //endregion

        //region Commands
        val su = "su"
        val clr = "-c"
        val reboot = "reboot now"
        val yes = "echo 1 >"
        val no = "echo 0 >"
        val enb = "echo enabled >"
        val dis = "echo disabled >"
        val inputsuspend = "/sys/class/power_supply/battery/input_suspend"                      //0 yes, 1 no
        val pm8921charger = "/sys/module/pm8921_charger/parameters/disabled"                    //0 yes, 1 no
        val storemode = "/sys/class/power_supply/battery/store_mode"                            //0 yes, 1 no
        val slatemode = "/sys/class/power_supply/battery/slate_mode"                            //0 yes, 1 no
        val battslatemode = "/sys/class/power_supply/battery/batt_slate_mode"                   //0 yes, 1 no
        val hwpower = "/sys/class/hw_power/charger/charge_data/enable_charger"                  //1 yes, 0 no
        val chargingenable = "/sys/class/power_supply/battery/device/Charging_Enable"           //1 yes, 0 no
        val chargingenabled = "/sys/class/power_supply/battery/charging_enabled"                //1 yes, 0 no
        val battlpcharging = "/sys/class/power_supply/battery/batt_lp_charging"                 //1 yes, 0 no
        val batterychargingenabled = "/sys/class/power_supply/battery/battery_charging_enabled" //1 yes, 0 no
        val bq2589x = "/sys/class/power_supply/bq2589x_charger/enable_charging"                 //1 yes, 0 no
        val acchargingenabled = "/sys/class/power_supply/ac/charging_enabled"                   //1 yes, 0 no
        val chargeenabled = "/sys/class/power_supply/ac/charging_enabled"                       //1 yes, 0 no
        val chargerenable = "/sys/devices/platform/battery/ChargerEnable"                       //1 yes, 0 no
        val hvcharger = "/sys/class/power_supply/battery/hv_charger_set"                        //1 yes, 0 no
        val chargenow = "/sys/class/power_supply/battery/charge_now"                            //1 yes, 0 no
        val tegra = "/sys/devices/platform/tegra12-i2c.0/i2c-0/0-006b/charging_state"           //disabled no, enabled yes
        //endregion

        //region Load Status
        statusTv.text = power

        if(power != "DISABLED") {
            statusTv.setTextColor(ContextCompat.getColor(this, R.color.enabled))
            sw.isChecked = true
            battview.setImageResource(R.drawable.ic_battery)
        } else {
            statusTv.setTextColor(ContextCompat.getColor(this, R.color.disabled))
            sw.isChecked = false
            battview.setImageResource(R.drawable.ic_no_battery)
        }
        //endregion

        //region Get Root
        try {
            Runtime.getRuntime().exec("su")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    //endregion

        //region Switch
        sw.setOnClickListener {
            if (sw.isChecked) {
                try {
                    val proc = Runtime.getRuntime().exec(arrayOf(su, clr, no, inputsuspend))
                    val proc2 = Runtime.getRuntime().exec(arrayOf(su, clr, no, pm8921charger))
                    val proc3 = Runtime.getRuntime().exec(arrayOf(su, clr, no, storemode))
                    val proc4 = Runtime.getRuntime().exec(arrayOf(su, clr, no, slatemode))
                    val proc5 = Runtime.getRuntime().exec(arrayOf(su, clr, no, battslatemode))
                    val proc6 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, hwpower))
                    val proc7 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, chargingenable))
                    val proc8 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, chargingenabled))
                    val proc9 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, battlpcharging))
                    val proc10 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, batterychargingenabled))
                    val proc11 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, bq2589x))
                    val proc12 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, acchargingenabled))
                    val proc13 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, chargeenabled))
                    val proc14 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, chargerenable))
                    val proc15 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, hvcharger))
                    val proc16 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, chargenow))
                    val proc17 = Runtime.getRuntime().exec(arrayOf(su, clr, enb, tegra))
                    //proc17.waitFor()
                } catch (e: Exception) { Log.d("Exceptions", "Exception dropping caches: $e") }
                statusTv.setTextColor(ContextCompat.getColor(this, R.color.enabled))
                statusTv.text = "ENABLED"
                battview.setImageResource(R.drawable.ic_battery)
                editor.clear()
                editor.putString("POWER", "ENABLED")
                editor.apply()
                val snack = Snackbar.make(it,"If the battery state gets stuck, tap the battery above to reboot the device", Snackbar.LENGTH_LONG )
                val sbView: View = snack.view
                val textView = sbView.findViewById(R.id.snackbar_text) as TextView
                //textView.setTextColor(Color.parseColor("#4444DD"))
                sbView.setBackgroundColor(Color.parseColor("#243447"))
                snack.show()
            }else{
                try { //Stop charging
                    val proc = Runtime.getRuntime().exec(arrayOf(su, clr, yes, inputsuspend))
                    val proc2 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, pm8921charger))
                    val proc3 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, storemode))
                    val proc4 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, slatemode))
                    val proc5 = Runtime.getRuntime().exec(arrayOf(su, clr, yes, battslatemode))
                    val proc6 = Runtime.getRuntime().exec(arrayOf(su, clr, no, hwpower))
                    val proc7 = Runtime.getRuntime().exec(arrayOf(su, clr, no, chargingenable))
                    val proc8 = Runtime.getRuntime().exec(arrayOf(su, clr, no, chargingenabled))
                    val proc9 = Runtime.getRuntime().exec(arrayOf(su, clr, no, battlpcharging))
                    val proc10 = Runtime.getRuntime().exec(arrayOf(su, clr, no, batterychargingenabled))
                    val proc11 = Runtime.getRuntime().exec(arrayOf(su, clr, no, bq2589x))
                    val proc12 = Runtime.getRuntime().exec(arrayOf(su, clr, no, acchargingenabled))
                    val proc13 = Runtime.getRuntime().exec(arrayOf(su, clr, no, chargeenabled))
                    val proc14 = Runtime.getRuntime().exec(arrayOf(su, clr, no, chargerenable))
                    val proc15 = Runtime.getRuntime().exec(arrayOf(su, clr, no, hvcharger))
                    val proc16 = Runtime.getRuntime().exec(arrayOf(su, clr, no, chargenow))
                    val proc17 = Runtime.getRuntime().exec(arrayOf(su, clr, dis, tegra))
                    //proc17.waitFor()
                } catch (e: Exception) { Log.d("Exceptions", "Exception dropping caches: $e") }
                statusTv.setTextColor(ContextCompat.getColor(this, R.color.disabled))
                statusTv.text = "DISABLED"
                battview.setImageResource(R.drawable.ic_no_battery)
                editor.clear()
                editor.putString("POWER", "DISABLED")
                editor.apply()
            }
        }
        //endregion

        //region Reboot
        battview.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setMessage("This will force reboot your phone in order to restore the charging state")
                mBuilder.setPositiveButton("Reboot"){dialog, which ->
                    try {
                        editor.putString("POWER", "ENABLED")
                        editor.apply()
                        val proc = Runtime.getRuntime().exec(arrayOf(su, clr, reboot))
                    } catch (e: Exception) { Log.d("Exceptions", "Exception dropping caches: $e") } }
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                mBuilder.show()
        }




        //endregion

    }

        //region Info
    fun infoGo(){
        val intent = Intent(this, com.alienator88.chargeroff.info::class.java)
        startActivity(intent)
    }
    //endregion

       //region Settings
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up track, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_about -> {
                infoGo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //endregion

}