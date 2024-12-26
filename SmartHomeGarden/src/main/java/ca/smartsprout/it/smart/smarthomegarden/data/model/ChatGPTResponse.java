package ca.smartsprout.it.smart.smarthomegarden.data.model;

import java.util.List;

public class ChatGPTResponse {
    private List<Choice> choices;

    public String getGeneratedText() {
        return choices.get(0).message.getContent();
    }

    public static class Choice {
        private Message message;

        public static class Message {
            private String content;

            public String getContent() {
                return content;
            }
        }
    }
}
