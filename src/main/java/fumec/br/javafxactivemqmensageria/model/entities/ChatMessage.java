package fumec.br.javafxactivemqmensageria.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {

    private String senderCode;
    private String recipientCode;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime timestamp;

    private MessageType type;

    @JsonIgnore
    private transient String formattedMessage;

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(String senderCode, String content) {
        this.senderCode = senderCode;
        this.recipientCode = null;
        this.content = content;
        this.type = MessageType.BROADCAST;
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(String senderCode, String recipientCode, String content) {
        this.senderCode = senderCode;
        this.recipientCode = recipientCode;
        this.content = content;
        this.type = MessageType.PRIVATE;
        this.timestamp = LocalDateTime.now();
    }

    public String getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(String senderCode) {
        this.senderCode = senderCode;
    }

    public String getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(String recipientCode) {
        this.recipientCode = recipientCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @JsonIgnore
    public String getFormattedMessage() {
        if (formattedMessage == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String timeStr = timestamp.format(formatter);

            switch (type) {
                case PRIVATE:
                    formattedMessage = String.format("[%s] %s -> %s: %s", timeStr, senderCode, recipientCode, content);
                    break;
                case SYSTEM:
                    formattedMessage = String.format("[%s] SISTEMA: %s", timeStr, content);
                    break;
                default:
                    formattedMessage = String.format("[%s] %s: %s", timeStr, senderCode, content);
            }
        }
        return formattedMessage;
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }

}