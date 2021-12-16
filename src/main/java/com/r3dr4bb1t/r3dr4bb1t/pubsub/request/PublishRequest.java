package com.r3dr4bb1t.merpay.pubsub.request;

import javax.validation.constraints.NotEmpty;

public class PublishRequest {
    @NotEmpty
    private String publisherName;

    @NotEmpty
    private String topicName;

    @NotEmpty
    private String messageContents;

    public String getPublisherName() {
        return publisherName;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(String messageContents) {
        this.messageContents = messageContents;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
