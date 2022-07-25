package com.example.techorbit.ui.fragment.live

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.techorbit.R
import com.example.techorbit.adapter.LiveAdapter
import com.example.techorbit.db.ScratchCards
import com.example.techorbit.model.five.Live
import com.example.techorbit.model.five.Recharge
import com.example.techorbit.model.reports.inventory.Inventory
import com.example.techorbit.services.RemoteDataSource
import com.example.techorbit.services.ResultOf
import com.example.techorbit.services.TechorbitClient
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.getJsonDataFromAsset
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.EvoucherPlanViewmodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_inventory_report.*
import kotlinx.android.synthetic.main.fragment_live.*
import kotlinx.android.synthetic.main.fragment_live.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LiveFragment : Fragment() {
    private lateinit var viewmodel: EvoucherPlanViewmodel
    private lateinit var progressDialog: ProgressDialog
    private var rechargelist: ArrayList<ScratchCards>? = null
    var jsonvalue: String = ""

    private val liveViewmodel: LiveViewmodel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        progressDialog = ProgressDialog(requireContext())
        viewmodel = ViewModelProvider(requireActivity()).get(EvoucherPlanViewmodel::class.java)
        return inflater.inflate(R.layout.fragment_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        when (arguments?.get("value")) {
            "0" -> {
                jsonvalue = "DU-EVC"
            }
            "1" -> {
                jsonvalue = "etisalat"
            }
            "2" -> {
                jsonvalue = "five"
            }
            "3" -> {
                jsonvalue = "hello"
            }
            "4" -> {
                jsonvalue = "salik"
            }
            "5" -> {
                jsonvalue = "PLAYSTATION"
            }
            "6" -> {
                jsonvalue = "VIRGIN"
            }
            "7" -> {
                jsonvalue = "XBOX-UAE"
            }
            "8" -> {
                jsonvalue = "ITUNES-UAE"
            }
            "9" -> {
                jsonvalue = "pubg"
            }
            "12" -> {
                jsonvalue = "GOOGLEPLAY"
            }
            "14" -> {
                jsonvalue = "FREEFIRE"
            }
            "15" -> {
                jsonvalue = "XBOX-US"
            }
            "16" -> {
                jsonvalue = "PLAYSTATION-US"
            }
            "17" -> {
                jsonvalue = "PLAYSTATION-PLUS-UAE"
            }
            "18" -> {
                jsonvalue = "PLAYSTATION-PLUS-US"
            }
            "19" -> {
                jsonvalue = "ITUNES-US"
            }
            "20" -> {
                jsonvalue = "GOOGLEPLAY-US"
            }
            "21" -> {
                jsonvalue = "AMAZON-UAE"
            }
            "22" -> {
                jsonvalue = "AMAZON-US"
            }
            "23" -> {
                jsonvalue = "NETFLIX-UAE"
            }
            "24" -> {
                jsonvalue = "NETFLIX-US"
            }
            "25" -> {
                jsonvalue = "SPOTIFY"
            }
            else -> {
                jsonvalue = "etisalat"
            }
        }





        val calender1 = Calendar.getInstance()
        val date = calender1.time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatdate = sdf.format(date)

        val sharedDate = TechorbitSharedPreference(requireContext()).getValue(KEYS.DATE)

        when {
            sharedDate == null -> {
                liveViewmodel.clearAll()
                TechorbitSharedPreference(requireContext()).save(KEYS.DATE, formatdate)
            }
            sharedDate.equals(formatdate) -> {

            }
            else -> {
                liveViewmodel.clearAll()
                TechorbitSharedPreference(requireContext()).save(KEYS.DATE, formatdate)
            }
        }


     observeData()






     /*  un comment to use

        if (jsonvalue == "etisalat" || jsonvalue == "salik" || jsonvalue == "hello" || jsonvalue == "five") {

            liveViewmodel.dblivedata.observe(requireActivity(), Observer {
                if (it.isEmpty()) {
                    val head =
                        "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
                    liveViewmodel.getInventory(head)
//                    checkApi()

                    progressDialog.show()
                    progressDialog.setCanceledOnTouchOutside(false)
                } else {
                    setLocalData(it)
                }
            })

            liveViewmodel.scrachcarLiveData.observe(requireActivity(), Observer {
                progressDialog.dismiss()
                when (it) {
                    is ResultOf.Succes -> {
                        setData(it.result)


                    }
                    is ResultOf.Failiure -> {

                        showToast(it.message)
//                        showToast("Loading Failed")
                    }
                }
            })


        }else{
            liveViewmodel.dblivedata.observe(requireActivity(), Observer {
                if (it.isEmpty()) {
                    val head =
                        "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
                    liveViewmodel.getInventory(head)
//                    checkApi()

                    progressDialog.show()
                    progressDialog.setCanceledOnTouchOutside(false)
                } else {
                    setLocalData(it)
                }
            })
        }

        */

        /** no need */

//        else {
//
//            val header =
//                "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
//            liveViewmodel.getLiveData(header, jsonvalue)
//            progressDialog.show()
//            progressDialog.setCanceledOnTouchOutside(false)
//
//            liveViewmodel.liveData.observe(requireActivity(), Observer {
//                progressDialog.dismiss()
//                when (it) {
//                    is ResultOf.Succes -> {
//
//                        if(it.result.response_code==200){
//                            val adapter = LiveAdapter { it -> slectdata(it) }
//                            adapter.BindData(it.result.data.recharge)
//                            view.recycler_view.adapter = adapter
//                        }else{
//                            showToast(it.result.error_message)
//                        }
//
//
//                    }
//                    is ResultOf.Failiure -> {
//                        showToast("Something went Wrong! Try agin.")
//                    }
//                }
//            })
//
//
//        }

    }

    private fun observeData() {
        liveViewmodel.dblivedata.observe(requireActivity(), Observer {
            if (it.isEmpty()) {
                val head =
                    "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
                liveViewmodel.getInventory(head)
//                    checkApi()

                showToast("Synching Data")
                progressDialog.show()
                progressDialog.setCanceledOnTouchOutside(false)
            } else {
                setLocalData(it)
            }
        })

        liveViewmodel.scrachcarLiveData.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            when (it) {
                is ResultOf.Succes -> {
                    setData(it.result)


                }
                is ResultOf.Failiure -> {

                    showToast(it.message)
//                        showToast("Loading Failed")
                }
            }
        })


    }

    private fun setLocalData(it: List<ScratchCards>?) {
        val list = ArrayList<Recharge>()
        for (i in it!!) {
            val recharge = Recharge(
                benefits = i.benefits, localAmount = i.localAmount, localCurrency = i.localCurrency
                , mrp = i.mrp, planId = i.planId, downlodedCount = i.downlodedCount, name = i.name
            )
            list.add(recharge)
        }
        setAdapter(list)
    }

    private fun setAdapter(list: ArrayList<Recharge>) {

        val newlist = ArrayList<Recharge>()
        for (i in list) {
            if (i.name == jsonvalue.toUpperCase()) {
                newlist.add(i)
            }
        }

        newlist.sortBy {
            it.mrp?.toDouble()
        }

        Log.d("TAG", "setAdapter: "+newlist.toString())
        val adapter = LiveAdapter { it -> slectdata(it) }
        adapter.BindData(newlist)
        recycler_view.adapter = adapter
    }

    private fun setData(result: Any) {

        val v = Gson().toJson(result)
        val jsonObject = JSONObject(v)
        if (jsonObject.getInt("response_code") == 100) {
            val products = jsonObject.getJSONArray("products")
            setList(products, jsonObject)

        } else if (jsonObject.getInt("response_code") == 400) {
            showToast(jsonObject.getString("error_message"))
        }
    }

    private fun setList(products: JSONArray, jsonObject: JSONObject) {

        rechargelist = ArrayList()
        try {
            for (i in 0 until products.length()) {

                var inner_obj: JSONObject = jsonObject.getJSONObject(products.getString(i))

                val data_array = inner_obj.getJSONArray("data")

                for (j in 0 until data_array.length()) {
                    val data_object = data_array.getJSONObject(j)
                    val name = data_object.getString("name")
                    val mrp = data_object.getString("mrp")
                    val stock = data_object.getString("stock")
                    val planid = data_object.getInt("planId")
                    val available = data_object.getString("available")
                    val localAmount = data_object.getString("localAmount")
                    val benefits = data_object.getString("benefits")
                    val localCurrency = data_object.getString("localCurrency")

//                    Log.d("TAG", "$name")

                    (rechargelist as ArrayList<ScratchCards>).add(
                        ScratchCards(
                            benefits = benefits,
                            localAmount = localAmount,
                            planId = planid,
                            mrp = mrp
                            ,
                            localCurrency = localCurrency,
                            name = name
                        )
                    )
                }
            }
            liveViewmodel.saveData(rechargelist as ArrayList<ScratchCards>)

        } catch (ex: Exception) {
        }

    }


    private fun slectdata(it: Recharge) {
        viewmodel.setData(it)
    }

    fun checkApi() {
        val header =
            "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        val baseUrl = "http://recharge.techorbit.net:44396/api/v1/"
        val retrofit =
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .build()
        val client = retrofit.create(TechorbitClient::class.java)
        val call = client.getTest(header)
        call.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("TAG", "$response")
            }

        })

    }

}