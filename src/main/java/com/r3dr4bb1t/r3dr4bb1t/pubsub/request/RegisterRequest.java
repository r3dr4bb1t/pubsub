package com.r3dr4bb1t.merpay.pubsub.request;

import javax.validation.constraints.NotEmpty;

public class RegisterRequest {
    @NotEmpty
    private String publisherName;

    @NotEmpty
    private String topicName;

    public String getPublisherName() {
        return publisherName;
    }

    public String getTopicName() {
        return topicName;
    }
}
