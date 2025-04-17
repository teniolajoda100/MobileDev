package com.example.ouluproject

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Retrofit instance with dynamic API key support
    private fun getInstance(apiKey: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")  // Use the API key passed from ChatActivity
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")  // The OpenAI API base URL
            .client(okHttpClient)  // Set up the OkHttp client with the interceptor
            .addConverterFactory(GsonConverterFactory.create())  // Add Gson converter for parsing JSON
            .build()
    }

    // Function to get OpenAI service, passing API key dynamically
    fun getOpenAIService(apiKey: String): OpenAIService {
        return getInstance(apiKey).create(OpenAIService::class.java)  // Return the service with dynamic API key
    }
}
