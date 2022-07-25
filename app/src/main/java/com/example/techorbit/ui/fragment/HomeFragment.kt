package com.example.techorbit.ui.fragment

import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.techorbit.BuildConfig
import com.example.techorbit.R
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import kotlinx.android.synthetic.main.evoucher.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.recharge.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()

        wordl_recharge.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_countryFragment)
        }
//        eu_evoucher.setOnClickListener {
//            val bundle = bundleOf("value" to 0)
//            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
//        }
//        etisat_evoucher.setOnClickListener {
//            val bundle = bundleOf("value" to 1)
//            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
//        }
//        five_evoucher.setOnClickListener {
//            val bundle = bundleOf("value" to 2)
//            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
//        }
//        hello_evoucher.setOnClickListener {
//            val bundle = bundleOf("value" to 3)
//            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
//        }
//        salik_evoucher.setOnClickListener {
//            val bundle = bundleOf("value" to 4)
//            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
//        }
        eu_evoucher_.setOnClickListener {
            val bundle = bundleOf("value" to 0)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }

        etisat_evoucher_.setOnClickListener {
            val bundle = bundleOf("value" to 1)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }
        five_evoucher_.setOnClickListener {
            val bundle = bundleOf("value" to 2)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }
        hello_evoucher_.setOnClickListener {
            val bundle = bundleOf("value" to 3)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }

        salik.setOnClickListener {
            val bundle = bundleOf("value" to 4)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }


        virgin.setOnClickListener {
            val bundle = bundleOf("value" to 6)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }


        otar.setOnClickListener {
            val bundle = bundleOf("value" to 10)
            it.findNavController().navigate(R.id.action_homeFragment_to_otarFragment, bundle)
        }
        mpos.setOnClickListener {
            val bundle = bundleOf("value" to 11)
            it.findNavController().navigate(R.id.action_homeFragment_to_otarFragment, bundle)
        }

        games_giftcards.setOnClickListener {
            val bundle= bundleOf("value" to 13)
            it.findNavController().navigate(R.id.action_homeFragment_to_gamesAndGiftCards,bundle)
        }

        xbox.setOnClickListener {
            val bundle = bundleOf("value" to 7)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }
        itunes.setOnClickListener {
            val bundle = bundleOf("value" to 8)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }
        pubg.setOnClickListener {
            val bundle = bundleOf("value" to 9)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }

        google_play.setOnClickListener {
            val bundle = bundleOf("value" to 12)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }

        playstation.setOnClickListener {
            val bundle = bundleOf("value" to 5)
            it.findNavController().navigate(R.id.action_homeFragment_to_evoucherFragment, bundle)
        }
    }

    private fun setUpData() {
        merchant_details.text = TechorbitSharedPreference(requireContext()).getValue(KEYS.NAME)
        terminal_id.text =
            "TERMINAL ID:" + TechorbitSharedPreference(requireContext()).getValue(KEYS.USERNAME)

       val versioncode=BuildConfig.VERSION_NAME;
        version_code.text="V. $versioncode"

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}