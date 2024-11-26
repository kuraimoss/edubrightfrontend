package com.capstone.education.edubright.data.response

import com.google.gson.annotations.SerializedName

// Data class untuk respons registrasi
data class RegisterResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("registerResult")
	val registerResult: RegisterResult,

	@field:SerializedName("status")
	val status: String
)

// Data class untuk hasil registrasi
data class RegisterResult(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String
)

data class RegisterRequest(
	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String
)

// **Tambahan**: Fungsi ekstensi untuk memeriksa apakah registrasi berhasil
fun RegisterResponse.isSuccessful(): Boolean {
	return status.equals("success", ignoreCase = true)
}

// **Tambahan**: Fungsi untuk mengambil pesan error (jika registrasi gagal)
fun RegisterResponse.getErrorMessage(): String {
	return if (!isSuccessful()) message else ""
}

// **Tambahan**: Fungsi validasi email pada hasil registrasi
fun RegisterResult.isEmailValid(): Boolean {
	return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// **Tambahan**: Fungsi untuk mendapatkan detail hasil dalam format string
fun RegisterResult.toFormattedString(): String {
	return "Name: $name\nEmail: $email\nUserID: $userId"
}
