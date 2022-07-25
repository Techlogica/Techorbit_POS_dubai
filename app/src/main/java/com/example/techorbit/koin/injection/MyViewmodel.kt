package com.example.techorbit.koin.injection

import androidx.lifecycle.ViewModel

class MyViewmodel(val helloRepositary: HelloRepositary):ViewModel() {
    fun  sayHello()="${helloRepositary.sayHello()}.from $this"
}