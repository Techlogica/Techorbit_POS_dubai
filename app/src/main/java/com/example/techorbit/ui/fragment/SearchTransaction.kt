package com.example.techorbit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.techorbit.R
import com.example.techorbit.utils.showToast
import kotlinx.android.synthetic.main.fragment_search_transaction.*

class SearchTransaction : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search.setOnClickListener {
            if (phone_number.text.isEmpty()){
                showToast("Enter beneficiary number")
            }else{
                val bundle=Bundle()
                bundle.putString("value",phone_number.text.toString().trim())
                it.findNavController().navigate(R.id.action_searchTransaction_to_searchTransactionInnerFragment,bundle)
            }
        }
    }


}