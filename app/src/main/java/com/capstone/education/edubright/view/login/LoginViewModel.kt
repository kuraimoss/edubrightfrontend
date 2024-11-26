package com.capstone.education.edubright.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.education.edubright.data.UserRepository
import com.capstone.education.edubright.data.pref.Result
import com.capstone.education.edubright.data.pref.UserModel
import com.capstone.education.edubright.data.response.LoginRequest
import com.capstone.education.edubright.data.response.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResult?>>()
    val loginResult: LiveData<Result<LoginResult?>> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>() // Tambahan untuk status loading
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>() // Tambahan untuk pesan error
    val errorMessage: LiveData<String?> = _errorMessage

    // Fungsi login user
    fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            repository.loginUser(loginRequest).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _isLoading.postValue(true)
                        _loginResult.postValue(Result.Loading)
                    }
                    is Result.Success -> {
                        val loginResult = result.data?.loginResult // Ambil LoginResult
                        _isLoading.postValue(false)
                        _loginResult.postValue(Result.Success(loginResult))
                    }
                    is Result.Error -> {
                        _isLoading.postValue(false)
                        _loginResult.postValue(Result.Error(result.error))
                        _errorMessage.postValue(result.error)
                    }
                }
            }
        }
    }

    // Fungsi untuk menyimpan sesi user
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    // Tambahan untuk menghapus pesan error setelah ditampilkan
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
