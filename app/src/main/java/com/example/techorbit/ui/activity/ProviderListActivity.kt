package com.example.techorbit.ui.activity

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.techorbit.R
import com.example.techorbit.model.operator.Data
import com.example.techorbit.model.operator.Operators
import com.example.techorbit.services.ResultOf
import com.example.techorbit.utils.KEYS
import com.example.techorbit.utils.TechorbitSharedPreference
import com.example.techorbit.utils.showToast
import com.example.techorbit.viewmodel.CountryViewmodel
import kotlinx.android.synthetic.main.activity_provider_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProviderListActivity : AppCompatActivity() {

    private val viewmodel: CountryViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_list)

        val getintent = intent
        val id = getintent.getStringExtra("id")

        val header =
            "Bearer " + TechorbitSharedPreference(this).getValue(KEYS.TOKEN)

        viewmodel.getPrepaidOperatorList(header, id!!)

        progress_circular.visibility=View.VISIBLE
        viewmodel.operatorList.observe(this, Observer {
            when (it) {

                is ResultOf.Succes -> {
                    progress_circular.visibility=View.GONE
                    setAdapter(it.result)
                }
                is ResultOf.Failiure ->{
                    progress_circular.visibility=View.GONE
                }
            }

        })
    }

    private fun setAdapter(result: Operators) {

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, result.data)
        providerlist.adapter = adapter

        providerlist.setOnItemClickListener { adapterView, view, position, id ->

            val data = adapterView.getItemAtPosition(position) as Data
            val intent = intent
            intent.putExtra("name", data.name)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }


    }
}