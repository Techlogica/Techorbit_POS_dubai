package com.example.techorbit.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.techorbit.R
import com.example.techorbit.utils.DeviceId
import com.example.techorbit.utils.Utils
import com.example.techorbit.utils.getUniqueIMEIId
import kotlinx.android.synthetic.main.signup_dialog.*

class SignupDialogFragment : DialogFragment() {
    var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE
    )
    val MULTIPLE_PERMISSIONS = 1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener(view)


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            setPermissions()
        }else{
            deviceid.setText("My Device Id: ${DeviceId().serialNumber}")
        }

    }
    private fun setPermissions(){
        if (!Utils.checkPermissions(permissions,requireActivity())){
            requestPermissions(permissions,MULTIPLE_PERMISSIONS)
        }else{
            val deviceId= getUniqueIMEIId(requireContext())
            deviceid.setText("My Device Id: $deviceId")
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    private fun setupClickListener(view: View) {
        back.setOnClickListener {
            dismiss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            MULTIPLE_PERMISSIONS->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    val deviceId= getUniqueIMEIId(requireContext())
                    deviceid.setText("My Device Id: $deviceId")
                }
            }
        }


    }
}