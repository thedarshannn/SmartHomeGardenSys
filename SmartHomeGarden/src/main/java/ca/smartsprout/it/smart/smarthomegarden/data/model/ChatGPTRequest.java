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
        messages.add(new Message( USER_ROLE, "You are a JSON generator. Strictly respond only in JSON format, without any extra text or explanations.\n" +
                "Generate a JSON response with the following fields for a plant named \"" + plantName + "\":\n" +
                "{\n" +
                "  \"description\": \"Provide a concise 2-3 line description of the plant.\",\n" +
                "  \"wateringPeriod\": \"Specify the watering period in one short sentence.\",\n" +
                "  \"toxicity\": \"Indicate if the plant is toxic to pets or humans in one short sentence.\",\n" +
                "  \"suitability\": \"State if the plant is suitable for indoor or outdoor use in one short sentence.\"\n" +
                "}\n\n" +
                "Do not include anything outside this JSON structure. If you don't know the details of the plant, respond with an empty JSON object {}."));
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
