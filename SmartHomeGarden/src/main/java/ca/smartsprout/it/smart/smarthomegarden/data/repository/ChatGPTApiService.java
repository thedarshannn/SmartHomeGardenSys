package ca.smartsprout.it.smart.smarthomegarden.data.repository;


import ca.smartsprout.it.smart.smarthomegarden.data.model.ChatGPTRequest;
import ca.smartsprout.it.smart.smarthomegarden.data.model.ChatGPTResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatGPTApiService {

    @Headers({
            "Authorization: Bearer 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",
            "Content-Type: application/json"
    })
    @POST("v1/chat/completions")
    Call<ChatGPTResponse> generatePlantInfo(@Body ChatGPTRequest request);
}
