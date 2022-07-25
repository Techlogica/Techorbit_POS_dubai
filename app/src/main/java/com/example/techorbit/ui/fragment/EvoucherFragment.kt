package com.example.techorbit.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.techorbit.R
import com.example.techorbit.model.Plans
import com.example.techorbit.model.five.Recharge
import com.example.techorbit.ui.activity.EvoucherConfirmation
import com.example.techorbit.ui.activity.EvoucherPlans
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_evoucher.*

class EvoucherFragment : Fragment() {

    private lateinit var value: String
    private var recharge:Plans?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evoucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        value = arguments?.get("value").toString()
        when (value) {
            "0" -> {
                voucher_imageview.setImageResource(R.drawable.du)
                voucher_heading.setText("Du Voucher")
                evoucher_top.setText("Du\nE-Voucher")

            }
            "1" -> {
                voucher_imageview.setImageResource(R.drawable.etisalat)
                voucher_heading.setText("EtisalatE-Voucher")
                evoucher_top.setText("Etisat\nE-Voucher")
            }
            "2" -> {
                voucher_imageview.setImageResource(R.drawable.five)
                voucher_heading.setText("FiveE-Voucher")
                evoucher_top.setText("Five\nE-Voucher")
            }
            "3" -> {
                voucher_imageview.setImageResource(R.drawable.hello)
                voucher_heading.setText("HelloE-Voucher")
                evoucher_top.setText("Hello\nE-Voucher")
            }
            "4" -> {
                voucher_imageview.setImageResource(R.drawable.salik)
                voucher_heading.setText("Salik-E-Voucher")
                evoucher_top.setText("Salik\nE-Voucher")
            }
            "5" -> {
                voucher_imageview.setImageResource(R.drawable.ic_play_station)
                voucher_heading.setText("PlayStation-Voucher")
                evoucher_top.setText("PlayStation\nE-Voucher")
            }
            "6" -> {
                voucher_imageview.setImageResource(R.drawable.virgin_mobiles)
                voucher_heading.setText("Virigin-Voucher")
                evoucher_top.setText("Virigin\nE-Voucher")
            }
            "7" -> {
                voucher_imageview.setImageResource(R.drawable.xbox)
                voucher_heading.setText("Xbox-Voucher")
                evoucher_top.setText("Xbox\nE-Voucher")
            }
            "8" -> {
                voucher_imageview.setImageResource(R.drawable.itunes)
                voucher_heading.setText("Itunes-Voucher")
                evoucher_top.setText("Itunes\nE-Voucher")
            }
            "9" -> {
                voucher_imageview.setImageResource(R.drawable.pubg)
                voucher_heading.setText("Pubg-Voucher")
                evoucher_top.setText("Pubg\nE-Voucher")
            }
            "12" -> {
                voucher_imageview.setImageResource(R.drawable.play)
                voucher_heading.setText("G-Play-Voucher")
                evoucher_top.setText("G-Play\nE-Voucher")
            }
            "14"->{
                voucher_imageview.setImageResource(R.drawable.freefire)
                voucher_heading.setText("FreeFire-Voucher")
                evoucher_top.setText("FreeFire\nE-Voucher")
            }
            "15"->{
                voucher_imageview.setImageResource(R.drawable.xbox)
                voucher_heading.setText("Xbox-US-Voucher")
                evoucher_top.setText("Xbox-Us\nE-Voucher")
            }
            "16"->{
                voucher_imageview.setImageResource(R.drawable.ic_play_station)
                voucher_heading.setText("Playstation-Us-Voucher")
                evoucher_top.setText("Playstation-US\nE-Voucher")
            }
            "17"->{
                voucher_imageview.setImageResource(R.drawable.ic_play_station)
                voucher_heading.setText("Playstation-Plus-UAE-Voucher")
                evoucher_top.setText("Playstation-Plus-UAE\nE-Voucher")
            }
            "18"->{
                voucher_imageview.setImageResource(R.drawable.ic_play_station)
                voucher_heading.setText("Playstation-Plus-US-Voucher")
                evoucher_top.setText("Playstation-Plus-US\nE-Voucher")
            }
            "19"->{
                voucher_imageview.setImageResource(R.drawable.itunes)
                voucher_heading.setText("Itunes-US-Voucher")
                evoucher_top.setText("Itunes-US\nE-Voucher")
            }
            "20" -> {
                voucher_imageview.setImageResource(R.drawable.play)
                voucher_heading.setText("G-Play-US-Voucher")
                evoucher_top.setText("G-Play-US\nE-Voucher")
            }
            "21" -> {
                voucher_imageview.setImageResource(R.drawable.amazon)
                voucher_heading.setText("Amazon-Voucher")
                evoucher_top.setText("Amazon\nE-Voucher")
            }
            "22" -> {
                voucher_imageview.setImageResource(R.drawable.amazon)
                voucher_heading.setText("Amazon-US-Voucher")
                evoucher_top.setText("Amazon-US\nE-Voucher")
            }
            "23" -> {
                voucher_imageview.setImageResource(R.drawable.netflix)
                voucher_heading.setText("Netflix")
                evoucher_top.setText("Netflix\nE-Voucher")
            }
            "24" -> {
                voucher_imageview.setImageResource(R.drawable.netflix)
                voucher_heading.setText("Netflix-US-Voucher")
                evoucher_top.setText("Netflix-US\nE-Voucher")
            }
            "25" -> {
                voucher_imageview.setImageResource(R.drawable.spotify)
                voucher_heading.setText("Spotify-Voucher")
                evoucher_top.setText("Spotify\nE-Voucher")
            }


        }

        denomination.setOnClickListener {

            val intent = Intent(activity, EvoucherPlans::class.java)
            intent.putExtra("value",arguments?.get("value").toString())
            startActivityForResult(intent, 1)
        }
        submit.setOnClickListener {
            when{
                denomination.text.isEmpty()->showToast("select denomination")
                count.text.isEmpty()->showToast("Enter count")
                else->{
                    recharge?.let {
                        val intent = Intent(requireContext(), EvoucherConfirmation::class.java)
                        intent.putExtra("value", it)
                        intent.putExtra("head",arguments?.get("value").toString())
                        intent.putExtra("count",count.text.toString().trim())
                        startActivity(intent)
                    }

                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            data?.let {
                recharge=data?.getParcelableExtra<Plans>("key")
                denomination.setText("AED"+recharge?.mrp?.toString())
                count.setText("1")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.wallet?.text= TechorbitSharedPreference(requireContext()).getValue(KEYS.BALANCE)
    }

}