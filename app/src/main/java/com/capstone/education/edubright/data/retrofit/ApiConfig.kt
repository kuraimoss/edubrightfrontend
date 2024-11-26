package com.capstone.education.edubright.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

//    private const val BASE_URL = "http://34.101.88.162" // Gunakan IP ini untuk koneksi ke server cloud
//    private const val BASE_URL = "http://localhost:8080" // Jika pakai Emulator dari android studio
    private const val BASE_URL = "http://192.168.100.7:8080"// Jika pakai Perangkat Fisik ubah ip ini. ambil ip dari ipconfig dan ipv4 nya kamu paste disini note : dari saint
    // Konfigurasi OkHttpClient untuk logging (opsional, berguna untuk debugging)
    private val client: OkHttpClient
        get() {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

    // Fungsi untuk mendapatkan instance ApiService
    fun getApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Menggunakan IP yang dipilih langsung
            .client(client) // Menggunakan OkHttpClient dengan logging
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
