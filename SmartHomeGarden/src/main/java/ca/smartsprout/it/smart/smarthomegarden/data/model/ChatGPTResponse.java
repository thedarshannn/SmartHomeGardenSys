package ca.smartsprout.it.smart.smarthomegarden.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ChatGPTResponse {
    @SerializedName("choices")
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public static class Choice {
        @SerializedName("message")
        private Message message;

        public Message getMessage() {
            return message;
        }
    }

    public static class Message {
        @SerializedName("content")
        private String content;

        public String getContent() {
            return content;
        }
    }
}
