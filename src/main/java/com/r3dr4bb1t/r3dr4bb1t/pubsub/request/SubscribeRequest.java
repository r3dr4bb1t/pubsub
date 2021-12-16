package com.r3dr4bb1t.merpay.pubsub.request;

import javax.validation.constraints.NotEmpty;

public class SubscribeRequest {

    @NotEmpty
    private String subscriberName;

    @NotEmpty
    private String topicName;

    public String getTopicName() {
        return topicName;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setSubscriberName(String publisherName) {
        this.subscriberName = publisherName;
    }
}
