
package com.example.foobook_android.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Provides a singleton Retrofit client for making HTTP requests. Configured for logging and JSON conversion.
public class RetrofitClient {
    private static Retrofit retrofit = null; // Singleton instance of Retrofit.
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Base URL for the API.

    // Returns the single instance of Retrofit, creating it if necessary.
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Interceptor for logging HTTP requests and responses.
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            // Builds the Retrofit instance with the base URL and converter factory for handling JSON.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
