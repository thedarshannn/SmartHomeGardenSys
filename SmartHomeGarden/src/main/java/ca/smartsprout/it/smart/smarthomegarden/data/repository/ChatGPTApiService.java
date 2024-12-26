package ca.smartsprout.it.smart.smarthomegarden.data.repository;

import ca.smartsprout.it.smart.smarthomegarden.BuildConfig;
import ca.smartsprout.it.smart.smarthomegarden.data.model.ChatGPTRequest;
import ca.smartsprout.it.smart.smarthomegarden.data.model.ChatGPTResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatGPTApiService {

    @Headers({
            "Authorization: Bearer " + BuildConfig.OPENAI_API_KEY,
            "Content-Type: application/json"
    })
    @POST("v1/chat/completions")
    Call<ChatGPTResponse> generatePlantInfo(@Body ChatGPTRequest request);
}
