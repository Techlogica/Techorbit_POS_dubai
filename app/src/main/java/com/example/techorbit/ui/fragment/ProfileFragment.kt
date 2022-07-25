package com.example.techorbit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.techorbit.R
import com.example.techorbit.ui.activity.HomeActivity
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import kotlinx.android.synthetic.main.profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()
        commisionprofile.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_commisionFragment)
        }

    }

    private fun setUpData() {
        owner_name.text=TechorbitSharedPreference(requireContext()).getValue(KEYS.NAME)
//        merchant_id.text=TechorbitSharedPreference(requireContext()).getValue(KEYS.NAME)
        shope_name.text=TechorbitSharedPreference(requireContext()).getValue(KEYS.SHOPENAME)
        mobile_no.text=TechorbitSharedPreference(requireContext()).getValue(KEYS.MOBILE)
        landline_no.text=TechorbitSharedPreference(requireContext()).getValue(KEYS.LANDNO)
        m2m_id.text=TechorbitSharedPreference(requireContext()).getValue(KEYS.M2MID)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}