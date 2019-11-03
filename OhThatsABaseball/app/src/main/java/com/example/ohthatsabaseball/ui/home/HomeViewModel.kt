package com.example.ohthatsabaseball.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val _acc = MutableLiveData<Double>().apply {
        value = 0.0
    }

    val text: LiveData<String> = _text

    fun observeAcc(): LiveData<Double> {
        return _acc
    }

    fun setAcc(a : Double) {
        if(a > _acc.value?:0.0)
            _acc.postValue(a)
    }

    fun resetAcc() {
        _acc.postValue(0.0)
    }

}