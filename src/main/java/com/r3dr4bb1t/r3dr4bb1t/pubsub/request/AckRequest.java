package com.r3dr4bb1t.merpay.pubsub.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AckRequest {
    @NotNull
    private Long messageId;

    @NotEmpty
    private String subscriberName;

    public Long getMessageId() {
        return messageId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }
}
