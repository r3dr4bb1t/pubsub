package com.r3dr4bb1t.merpay.pubsub.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
// import java.util.concurrent.TransferQueue;

import javax.validation.constraints.NotEmpty;

public class Subscription {

    @NotEmpty
    private String subscriberName;

    @NotEmpty
    private Date subscribedDate;

    private Queue<Long> messageIds;

    public Subscription(String subscriberName, Date subscribedDate) {
        this.subscriberName = subscriberName;
        this.subscribedDate = subscribedDate;
        messageIds = new LinkedList<>();
    }

    public Queue<Long> getMessageIds() {
        return messageIds;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public Date getSubscribedDate() {
        return subscribedDate;
    }

    public void setSubscribedDate(Date subscribedDate) {
        this.subscribedDate = subscribedDate;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

}
