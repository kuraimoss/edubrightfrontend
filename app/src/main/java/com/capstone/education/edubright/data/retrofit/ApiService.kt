package com.capstone.education.edubright.data.retrofit
import com.capstone.education.edubright.data.response.LoginRequest
import com.capstone.education.edubright.data.response.LoginResponse
import com.capstone.education.edubright.data.response.RegisterRequest
import com.capstone.education.edubright.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Model untuk request dan response
data class RegisterRequest(val name: String, val email: String, val password: String)
data class RegisterResponse(val status: String, val message: String, val registerResult: User?)
data class User(val userId: String, val name: String, val email: String)

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val message: String, val loginResult: LoginResult?)
data class LoginResult(val token: String, val userId: String, val name: String, val email: String)

interface ApiService {
    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
