package com.example.userdetailsform.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userdetailsform.R
import com.example.userdetailsform.model.UserEntity
import com.example.userdetailsform.repository.UserRepo
import kotlinx.coroutines.launch

class UserViewModel : ViewModel(){
    private lateinit var userRepo: UserRepo
    val successLiveData = MutableLiveData<Int>()
    val errorLiveData = MutableLiveData<Int>()

    fun setRepository(repository: UserRepo) {
        this.userRepo = repository
    }

    fun insertBooking(model: UserEntity) {
        viewModelScope.launch {
            try {
                userRepo.insertUser(model)
                successLiveData.value = R.string.success_message
            } catch (e: Exception) {
                e.printStackTrace()
                errorLiveData.value = R.string.error_message
            }
        }
    }


}