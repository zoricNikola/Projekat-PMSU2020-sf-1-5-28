package com.example.projekat_pmsu2020_sf_1_5_28.model;

import java.io.Serializable;
import java.util.List;

public class MessageData implements Serializable {

    private static final long serialVersionUID = -2159863338969384284L;

    private Message message;

    private List<Attachment> attachments;

    public MessageData() {}

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
