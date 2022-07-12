package com.models.DTO;

import java.io.Serial;
import java.io.Serializable;

public class MessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String messageContent;
    private String messageHeader;

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public MessageDTO() {
    }

    public MessageDTO(String messageContent, String messageHeader) {
        this.messageContent = messageContent;
        this.messageHeader = messageHeader;
    }
}
