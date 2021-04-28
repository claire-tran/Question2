import java.util.List;

public class SMSResponse {
    private String messageCount;
    private List<Message> messages;

    public SMSResponse(String messageCount, List<Message> messages) {
        this.messageCount = messageCount;
        this.messages = messages;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public class Message {
        private String to;
        private String status;
        private String errorText;

        public Message(String to, String status, String errorText) {
            this.to = to;
            this.status = status;
            this.errorText = errorText;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }
    }
}
