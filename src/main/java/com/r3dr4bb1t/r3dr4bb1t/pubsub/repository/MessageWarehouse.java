package com.r3dr4bb1t.merpay.pubsub.repository;

import java.util.HashMap;
import java.util.stream.Collectors;

import com.r3dr4bb1t.merpay.pubsub.model.Message;

import org.springframework.stereotype.Component;

@Component
public class MessageWarehouse {

    HashMap<Long, Message> messageWarehouse;

    public MessageWarehouse() {
        messageWarehouse = new HashMap<Long, Message>();
    }

    public void add(Message message) {
        messageWarehouse.put(message.getId(), message);
    }

    public Message find(Long messageId) {
        return messageWarehouse.get(messageId);
    }

    public String beautify() {
        return this.messageWarehouse.entrySet().stream()
                .map(e -> "Id: " + e.getKey() + " Message: " + e.getValue().getContents()).collect(Collectors.toList())
                .toString();
    }
}
