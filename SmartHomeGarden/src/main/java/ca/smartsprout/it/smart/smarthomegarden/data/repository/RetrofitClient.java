package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import androidx.annotation.NonNull;

import ca.smartsprout.it.smart.smarthomegarden.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            // Create OkHttpClient to add the Authorization header dynamically
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Authorization", "Bearer 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")
                                    .header("Content-Type", "application/json")
                                    .method(original.method(), original.body())
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            // Build the Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openai.com") // Base URL for the OpenAI API
                    .client(okHttpClient) // Attach OkHttpClient with interceptor
                    .addConverterFactory(GsonConverterFactory.create()) // Gson for JSON parsing
                    .build();
        }
        return retrofit;
    }
}
