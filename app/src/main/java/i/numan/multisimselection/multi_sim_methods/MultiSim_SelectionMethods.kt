package i.numan.multisimselection.multi_sim_methods

import android.Manifest
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat

class MultiSim_SelectionMethods {
    fun isSimAvailable(context: Context): Boolean {
        var isAvailable = false

        val manager = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val count = manager.phoneCount
        println("ffnet: Sim Count: $count")
        val simState = manager.getSimState()
        when (simState) {

            TelephonyManager.SIM_STATE_ABSENT -> {
                println("ffnet: NO SIM")

            }
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> {
                println("ffnet: Pin is  Required")
            }
            TelephonyManager.SIM_STATE_NOT_READY -> {
                println("ffnet: Sim Not Ready")
            }
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> {
                println("ffnet: Puk Requred")
            }
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> {
                println("ffnet: Network State Locked")
            }
            TelephonyManager.SIM_STATE_READY -> {
                println("ffnet: SIM, Available")
                isAvailable = true
            }

        }
        return isAvailable

    }

    // Sim Name
    fun getCarrierName(context: Context, index: Int): CharSequence? {
        val value: CharSequence = "false"
        val manager = SubscriptionManager.from(context)
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val CarrierInfo = manager.getActiveSubscriptionInfoForSimSlotIndex(index)
            return if (CarrierInfo != null) {
                println("ffnet: Carrier Name ${CarrierInfo.carrierName}")
                CarrierInfo.carrierName
            } else value
        }
        return value
    }

    // sim id
    fun getSubscriptionId(context: Context, index: Int): Int {
        val manager =
            SubscriptionManager.from(context)
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val SubId = manager.getActiveSubscriptionInfoForSimSlotIndex(index)
            return if (SubId != null) {
                Log.v("single", "" + SubId.subscriptionId)
                SubId.subscriptionId
            } else -1
        }
        return -1
    }

    // if single sim available
    fun callFromSims(context: Context, in_number: String) { // call function definition
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val uri = Uri.parse("tel:$in_number")
        val callIntent = Intent(Intent.ACTION_CALL, uri)
        context.startActivity(callIntent)
    }

    // if both sims available or if any one sim have service
    fun callFromSimsWithSubscriptionId(
        context: Context,
        in_number: String,
        id: Int
    ) { // call function definition
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val uri = Uri.parse("tel:$in_number")
        val callIntent = Intent(Intent.ACTION_CALL, uri)
        callIntent.putExtra("com.android.phone.force.slot", true)
        callIntent.putExtra("Cdma_Supp", true)
        callIntent.putExtra("simSlot", id) //0 or 1 according to sim
        context.startActivity(callIntent)
    }


}