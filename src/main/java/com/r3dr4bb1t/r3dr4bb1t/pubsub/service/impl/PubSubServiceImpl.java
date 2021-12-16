package com.r3dr4bb1t.merpay.pubsub.service.impl;

import java.util.Date;
import java.util.Map;

import com.r3dr4bb1t.merpay.pubsub.model.Message;
import com.r3dr4bb1t.merpay.pubsub.model.Topic;
import com.r3dr4bb1t.merpay.pubsub.repository.MessageWarehouse;
import com.r3dr4bb1t.merpay.pubsub.repository.TopicPublisherMap;
import com.r3dr4bb1t.merpay.pubsub.repository.TopicSubscriptionMap;
import com.r3dr4bb1t.merpay.pubsub.service.PubSubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PubSubServiceImpl implements PubSubService {

    private final Integer MESSAGE_SIZE_LIMIT = 128000;

    @Autowired
    MessageWarehouse messageWarehouse;

    @Autowired
    TopicPublisherMap topicPublisherMap;

    @Autowired
    TopicSubscriptionMap topicSubscriptionMap;

    @Override
    public void registerTopic(String topicName, String publisherName) throws Exception {
        Topic topic = new Topic(topicName);
        if (topicPublisherMap.isTopicAlreadyExists(topic)) {
            throw new Exception("TOPIC_ALREADY_EXISTS");
        }

        topicPublisherMap.addTopicPublisherPair(topic, publisherName);
    }

    @Override
    public void subscribeTopic(String topicName, String subscriberName) throws Exception {
        Topic topic = new Topic(topicName);
        if (!topicPublisherMap.isTopicAlreadyExists(topic)) {
            throw new Exception("TOPIC_NOT_FOUND");
        }

        if (topicSubscriptionMap.hasAlreadySubscribed(topic, subscriberName)) {
            throw new Exception("TOPIC_ALREADY_SUBSCRIBED");
        }

        topicSubscriptionMap.addTopicSubscriberPair(topic, subscriberName);
    }

    @Override
    public void publish(String topicName, String messageContents, String publisherName) throws Exception {
        Topic topic = new Topic(topicName);
        String topicOwner = topicPublisherMap.getPublisherFor(topic);

        if (!topicPublisherMap.isTopicAlreadyExists(topic)) {
            throw new Exception("TOPIC_NOT_FOUND");
        }

        if (!topicOwner.equals(publisherName)) {
            throw new Exception("TOPIC_UNAUTHORIZED");
        }

        if (messageContents.getBytes("UTF-8").length > MESSAGE_SIZE_LIMIT) {
            throw new Exception("MESSAGE_MAX_SIZE_LIMIT_EXCEEDED");
        }

        Message message = new Message(topic, messageContents, new Date());
        messageWarehouse.add(message);
        topicSubscriptionMap.pushToSubscriptions(message);
    }

    @Override
    public Map<String, String> getMessageContents(String topicName, String subscriberName) throws Exception {
        Topic topic = new Topic(topicName);
        if (!topicPublisherMap.isTopicAlreadyExists(topic)) {
            throw new Exception("TOPIC_NOT_FOUND");
        }

        if (!topicSubscriptionMap.hasAlreadySubscribed(topic, subscriberName)) {
            throw new Exception("TOPIC_NOT_SUBSCRIBED");
        }

        return topicSubscriptionMap.getMessageContentsForSubscription(topic, subscriberName);
    }

    @Override
    public void ackMessage(Long messageId, String subscriberName) throws Exception {
        Message message = messageWarehouse.find(messageId);
        if (message == null) {
            throw new Exception("MESSAGE_NOT_EXISTS");
        }

        Topic topicForMessage = message.getTopic();
        if (!topicSubscriptionMap.hasAlreadySubscribed(topicForMessage, subscriberName)) {
            throw new Exception("TOPIC_NOT_SUBSCRIBED");
        }

        topicSubscriptionMap.removeFromUnreadMessages(messageId, topicForMessage, subscriberName);
    }
}
