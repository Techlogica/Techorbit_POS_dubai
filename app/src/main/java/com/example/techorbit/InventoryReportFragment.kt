package com.example.techorbit

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.techorbit.adapter.InventoryAdapter
import com.example.techorbit.databinding.FragmentInventoryReportBinding
import com.example.techorbit.model.reports.inventory.Inventory
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.*
import com.example.techorbit.viewmodel.InventoryViemodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_inventory_report.*
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel


class InventoryReportFragment : Fragment() {

    //    private var progressDialog=ProgressDialog(requireContext())
    private val viemodel: InventoryViemodel by viewModel()
    private var inventoryList: List<Inventory>? = null
    private val BLUETOOTH_ENABLE_REQUEST = 100
    private lateinit var binding: FragmentInventoryReportBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_inventory_report, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val head = "Bearer " + TechorbitSharedPreference(requireContext()).getValue(KEYS.TOKEN)
        viemodel.getInventory(head)
        observeData()

        print_button.setOnClickListener {
            if (BluetoothUtil().is_bluetooth_enable()) {
                Log.d("ready_to", "Print")
                PrinterFunction(requireContext()).call_inventory_report_receipt_printer_bluetooth(
                    inventoryList!!
                )
            } else {
                enable_bluetooth()
            }
        }

    }

    private fun enable_bluetooth() {
        val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(bluetoothIntent, BLUETOOTH_ENABLE_REQUEST)
    }

    private fun observeData() {
//        progressDialog.show()
//        progressDialog.setCanceledOnTouchOutside(false)
        binding.progressbar.visibility = View.VISIBLE
        viemodel.liveData.observe(requireActivity(), Observer {

            when (it) {
                is ResultOf.Succes -> {
                    binding.progressbar.visibility = View.GONE
                    setData(it.result)
                }
                is ResultOf.Failiure -> {
//                    progressDialog.dismiss()
                    binding.progressbar.visibility = View.GONE
                    showToast("Loading Failed")
                }
            }

        })
    }

    private fun setData(it: Any) {

        val v = Gson().toJson(it)
        val jsonObject = JSONObject(v)
        if (jsonObject.getInt("response_code") == 100) {
            val products = jsonObject.getJSONArray("products")
            if (products.length() > 0) {
                binding.printButton.visibility = View.VISIBLE
                setList(products, jsonObject)
            } else {
               binding.printButton.visibility = View.GONE
            }


        } else if (jsonObject.getInt("response_code") == 400) {
            showToast(jsonObject.getString("error_message"))
        }


    }

    private fun setList(products: JSONArray, objects: JSONObject) {
        inventoryList = ArrayList()
        try {
            for (i in 0 until products.length()) {
//                Log.d("product_$i", products.getString(i))

                val inner_obj: JSONObject = objects.getJSONObject(products.getString(i))
                val data_array = inner_obj.getJSONArray("data")
                for (j in 0 until data_array.length()) {
                    val data_object = data_array.getJSONObject(j)
                    (inventoryList as ArrayList<Inventory>).add(
                        Inventory(
                            data_object.getString("name"),
                            data_object.getString("mrp"),
                            data_object.getString("stock")
                        )
                    )
                }
            }
            showReport()

        } catch (ex: Exception) {
        }
    }

    private fun showReport() {
        val adapter = InventoryAdapter()
        RecyclerView.adapter = adapter
        adapter.bindData(inventoryList)
    }
}

