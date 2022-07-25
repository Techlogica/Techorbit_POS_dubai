package com.example.techorbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.techorbit.adapter.SearchAdapter
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.SearchViewmodel
import kotlinx.android.synthetic.main.fragment_search_transaction.*
import kotlinx.android.synthetic.main.fragment_search_transaction.phone_number
import kotlinx.android.synthetic.main.fragment_search_transaction_inner.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchTransactionInnerFragment : Fragment() {

    private val viewmodel: SearchViewmodel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_transaction_inner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()

        viewmodel.liveData.observe(requireActivity(), Observer {
            progress_circular.visibility=View.GONE
            when (it) {
                is ResultOf.Succes -> {
                    val adapter = SearchAdapter()
                    adapter.bind(it.result.data)
                    recycler_view.adapter = adapter
                }
                is ResultOf.Failiure -> {
                    showToast(it.errorBody.toString())
                }

            }
        })

    }

    private fun setData() {
        progress_circular.visibility=View.VISIBLE
        val value:String = arguments?.get("value").toString()
        contact.text=value
        val header = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viewmodel.searchData(header, value.toString())
    }
}