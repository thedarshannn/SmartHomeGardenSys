package ca.smartsprout.it.smart.smarthomegarden.data.model;

import java.util.ArrayList;
import java.util.List;

public class ChatGPTRequest {
    private static final String DEFAULT_MODEL = "gpt-3.5-turbo";
    private static final String USER_ROLE = "user";

    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String plantName) {
        this.model = DEFAULT_MODEL;
        this.messages = new ArrayList<>();
        messages.add(new Message( USER_ROLE, "You are a JSON generator. Strictly respond in JSON format, without extra text or explanations. Provide a JSON response with the following fields for a plant named \"" + plantName + "\": {\n" +
                "  \"description\": \"One line concise description.\",\n" +
                "  \"wateringPeriod\": \"2-3 words (e.g., Weekly, Twice daily).\",\n" +
                "  \"toxicity\": \"2-3 words (e.g., Non-toxic, Mildly toxic).\",\n" +
                "  \"suitability\": \"2-3 words (e.g., Indoor, Outdoor).\"\n" +
                "}\n" +
                "Do not include anything outside this JSON structure.If you don't know the details of the plant, respond with an empty JSON object {}.\""
        ));
    }

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
