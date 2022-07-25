package com.example.techorbit.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.techorbit.R
import com.example.techorbit.adapter.CountryAdapter
import com.example.techorbit.model.country.CountryList
import com.example.techorbit.model.country.Data
import com.example.techorbit.model.signup.Country
import com.example.techorbit.repositary.TechorbitRepositary
import com.example.techorbit.repositary.ViewModelFactory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.getJsonDataFromAsset
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.CountryViewmodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_country.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CountryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: CountryViewmodel
    private lateinit var adapter: CountryAdapter
    private lateinit var list: List<Data>
    private lateinit var progress: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress= ProgressDialog(context)
        progress.setCanceledOnTouchOutside(false)
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
        return inflater.inflate(R.layout.fragment_country, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()
        observeData()

    }

    private fun observeData() {
//        progress_circular.visibility=View.VISIBLE
//        viewModel.liveData.observe(requireActivity(), Observer {
//            when (it) {
//                is ResultOf.Succes -> {
////                    progress_circular.visibility=View.GONE
//                    progress.dismiss()
//                    linear.visibility=View.VISIBLE
//                    list = it.result.data
//                    adapter = CountryAdapter { selectd: Data -> selectedItem(selectd) }
//                    recycler_view.adapter = adapter
//                    adapter.bindData(it.result.data)
//                }
//                is ResultOf.Failiure->{
////                    progress_circular.visibility=View.GONE
//                    progress.dismiss()
//                    showToast("Failed to load")
//                }
//            }
//
//
//        })

        val jsondata = getJsonDataFromAsset(requireContext(), "countries.json")
        val livedata = Gson().fromJson(jsondata, CountryList::class.java)
        list=livedata.data

        for ((index, value) in list.withIndex()) {
            if (value.abbreviation=="IN"){
//                list.drop(index)
            }
        }

        adapter= CountryAdapter { selectd: Data -> selectedItem(selectd) }
        recycler_view.adapter=adapter
        adapter.bindData(list)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CountryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setUpData() {
//
//        val repositary =
//            TechorbitRepositary(RemoteDataSource().buildApi(TechorbitClient::class.java))
//        val factory = ViewModelFactory(repositary)
//        viewModel = ViewModelProvider(this, factory).get(CountryViewmodel::class.java)
//        val header = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
//        val hashMap = HashMap<String, String>().apply {
//            put("rechargeType", "string")
//            put("deviceId", "string")
//        }
//        viewModel.getCountries(header!!, hashMap)
//        progress.show()

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText!!)
                return false
            }
        })

    }

    private fun selectedItem(data: Data) {

        val bundle = Bundle().also {
            it.putString("id", data.id.toString())
            it.putString("abrevation", data.extension.toString())
            it.putString("numberLength", data.numberLength.toString())
            it.putString("name",data.name.toString())

        }

//           bundleOf("id" to data.id.toString())


//           bundleOf("abrevation" to data.abbreviation.toString())

        findNavController().navigate(R.id.action_countryFragment_to_internationalRecharge, bundle)
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filterdNames: ArrayList<Data> = ArrayList()

        //looping through existing elements
        for (s in list) {
            //if the existing elements contains the search input
            if (s.name.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames)
    }
}