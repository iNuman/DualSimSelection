package i.numan.multisimselection

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import i.numan.multisimselection.extension.ExtensionFunction.Companion.toast
import i.numan.multisimselection.multi_sim_methods.MultiSim_SelectionMethods
import kotlinx.android.synthetic.main.contact_history_acitivty.*

class MainActivity : AppCompatActivity() {


    lateinit var multisimSelectionmethods: MultiSim_SelectionMethods

    private val PERMISSIONS_REQUEST_CODE = 10
    private var permissionsArray = arrayOf(
        android.Manifest.permission.READ_PHONE_STATE,
        android.Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_history_acitivty)

        if (checkPermissions(permissionsArray)) {
//            showToast(this, "Granted")
            println("ffnet: Permission Granted")

        } else {
            requestPermissions(permissionsArray, PERMISSIONS_REQUEST_CODE)
        }


        multisimSelectionmethods = MultiSim_SelectionMethods()


        sim_selector_btn.setOnClickListener {
            makeCall(in_number = "03127746663")
        }
    }

    // make call
    private fun makeCall(in_number: String) {
        // conditions
        if (multisimSelectionmethods.isSimAvailable(this@MainActivity)) { //More than 1 sim is present

            if (!multisimSelectionmethods.getCarrierName(
                    this@MainActivity,
                    1
                ).toString().equals("No Service", ignoreCase = true) &&
                !multisimSelectionmethods.getCarrierName(
                    this@MainActivity,
                    0
                ).toString().equals("No Service", ignoreCase = true)
            ) {
                //both sims are in service show alert Dialog
                showSimSelectionDialog(in_number)
            } else {
                multisimSelectionmethods.callFromSims(
                    context = this@MainActivity,
                    in_number = "03127746663"
                )
            }
            // if there're No Sims
        } else
            toast(this@MainActivity, "Can't make a call right now")
    }


    // dialog for sim selection
    private fun showSimSelectionDialog(number: String?) {

        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.sim_selection_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Sim 1 init
        val sim1 = dialog.findViewById<RadioButton>(R.id.sim1)
        sim1.text = multisimSelectionmethods.getCarrierName(
            context = this@MainActivity,
            index   = Resources.getSIM0(context = this@MainActivity)
        ).toString()

        // Sim 2 init
        val sim2 = dialog.findViewById<RadioButton>(R.id.sim2)
        sim2.text= multisimSelectionmethods.getCarrierName(
                context = this@MainActivity,
                index   = Resources.getSIM1(context = this@MainActivity)
            ).toString()



        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        sim1.setOnClickListener {
            multisimSelectionmethods.callFromSimsWithSubscriptionId(
                context = this@MainActivity,
                in_number = number!!,
                id = multisimSelectionmethods.getSubscriptionId(
                    this@MainActivity,
                    0
                )
            )
            dialog.dismiss()
        }
        sim2.setOnClickListener {
            multisimSelectionmethods.callFromSimsWithSubscriptionId(
                context = this@MainActivity,
                in_number = number!!,
                id = multisimSelectionmethods.getSubscriptionId(
                    this@MainActivity,
                    1
                )
            )
            dialog.dismiss()
        }

    }

    // Boolean Check
    private fun checkPermissions(permissionsArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionsArray.indices) {
            for (i in permissionsArray.indices) {
                if (checkCallingOrSelfPermission(permissionsArray[i]) == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                }
            }
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) toast(
                        context = this@MainActivity,
                        message = "Permission hasn't been granted"
                    )
                    else
                        toast(
                            context = this@MainActivity,
                            message = "Set permissions by yourself!"
                        )
                }
            }
            if (allSuccess) toast(
                context = this@MainActivity,
                message = "Permission has been granted"
            )

        }
    }


}
