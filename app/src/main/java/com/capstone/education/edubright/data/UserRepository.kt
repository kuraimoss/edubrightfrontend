package com.capstone.education.edubright.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.education.edubright.data.pref.Result
import com.capstone.education.edubright.data.pref.UserModel
import com.capstone.education.edubright.data.pref.UserPreference
import com.capstone.education.edubright.data.retrofit.ApiService
import com.capstone.education.edubright.data.response.LoginRequest
import com.capstone.education.edubright.data.response.LoginResponse
import com.capstone.education.edubright.data.response.LoginResult
import com.capstone.education.edubright.data.response.RegisterRequest
import com.capstone.education.edubright.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    private val _registerUser = MutableLiveData<RegisterResponse>()
    private val _successMessage = MutableLiveData<String?>()
    val successMessage: MutableLiveData<String?> = _successMessage
    private val _errorLiveData = MutableLiveData<String?>()
    val errorLiveData: MutableLiveData<String?> = _errorLiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Fungsi untuk mendaftarkan user baru
    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true

        val registerRequest = RegisterRequest(name, email, password) // Gunakan RegisterRequest
        val client = apiService.register(registerRequest)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerUser.value = response.body()
                    val successMessage = response.body()?.message
                    _successMessage.postValue(successMessage)
                } else {
                    val errorMessage = response.errorBody()?.string()
                    _errorLiveData.postValue(errorMessage)
                    Log.e("postRegister", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("postRegister", "onFailure: ${t.message}")
            }
        })
    }

    // Fungsi untuk login
    fun loginUser(loginRequest: LoginRequest): Flow<Result<LoginResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.login(loginRequest).execute()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!)) // Pastikan response.body() tidak null
            } else {
                emit(Result.Error(response.message()))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.message ?: "HTTP Error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO) // Pindahkan eksekusi ke thread I/O

    // **Tambahan**: Fungsi register user menggunakan Flow
    fun registerUserFlow(registerRequest: RegisterRequest): Flow<Result<RegisterResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.register(registerRequest).execute()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                emit(Result.Error(response.message()))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            emit(Result.Error(errorBody ?: e.message()))
        }
    }

    // Menyimpan sesi user
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    // Mendapatkan data sesi user
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    // Logout user
    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
