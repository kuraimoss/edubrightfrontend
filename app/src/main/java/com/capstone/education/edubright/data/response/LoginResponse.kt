package com.capstone.education.edubright.data.response

import com.google.gson.annotations.SerializedName

// Data class untuk respons login
data class LoginResponse(
	@field:SerializedName("loginResult")
	val loginResult: LoginResult?, // **Pastikan null safety di sini**

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

// Data class untuk hasil login
data class LoginResult(
	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)

// Data class untuk permintaan login
data class LoginRequest(
	val email: String = "",
	val password: String = ""
)

// Fungsi ekstensi untuk memvalidasi respons login
fun LoginResponse.isSuccessful(): Boolean {
	return status.equals("success", ignoreCase = true)
}

// Fungsi untuk mendapatkan detail error dari respons login
fun LoginResponse.getErrorMessage(): String {
	return if (!isSuccessful()) message else ""
}

// Fungsi untuk memvalidasi token hasil login
fun LoginResult.isTokenValid(): Boolean {
	return token.isNotEmpty() && token.length > 10
}
