package com.example.techorbit.repositary

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseClass<V : ViewModel, R : BaseRepositary> : AppCompatActivity() {



    protected lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory=ViewModelFactory(getRepositary())
        viewModel= ViewModelProvider(this,factory).get(getViewmodel())
        main()
        observeData()
    }

    abstract fun main()
    abstract fun observeData()



    abstract fun getViewmodel():Class<V>

    abstract fun getRepositary():R
}