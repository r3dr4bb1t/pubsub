package com.r3dr4bb1t.merpay.pubsub.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.r3dr4bb1t.merpay.pubsub.model.Message;
import com.r3dr4bb1t.merpay.pubsub.model.Subscription;
import com.r3dr4bb1t.merpay.pubsub.model.Topic;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.joining;

@Component
public class TopicSubscriptionMap {

    private Map<Topic, Set<Subscription>> topicSubscriptionMap;

    @Autowired
    MessageWarehouse messageWarehouse;

    public TopicSubscriptionMap() {
        topicSubscriptionMap = new HashMap<Topic, Set<Subscription>>();
    }

    public boolean isEmpty() {
        return this.topicSubscriptionMap.isEmpty();
    }

    public void addTopicSubscriberPair(Topic topic, String subscriberName) {
        Set<Subscription> subscriptions = this.isSubscriberExistsFor(topic) ? getSubscriptionsFor(topic)
                : new HashSet<Subscription>();
        Subscription newSubs = new Subscription(subscriberName, new Date());
        subscriptions.add(newSubs);
        this.topicSubscriptionMap.put(topic, subscriptions);
    }

    public Set<Subscription> getSubscriptionsFor(Topic topic) {
        return this.topicSubscriptionMap.get(topic);
    }

    public void removeFromUnreadMessages(Long messageId, Topic topic, String subscriberName) throws Exception {
        Set<Subscription> subscriptions = getSubscriptionsFor(topic);
        if (subscriptions == null) {
            throw new Exception("SUBSCRIPTION_NOT_FOUND");
        }

        for (Subscription subs : subscriptions) {
            if (subs.getMessageIds().peek() == messageId && subs.getSubscriberName().equals(subscriberName)) {
                subs.getMessageIds().poll();
                return;
            }
        }
    }

    public boolean isSubscriberExistsFor(Topic topic) {
        return this.topicSubscriptionMap.containsKey(topic);
    }

    public boolean hasAlreadySubscribed(Topic topic, String subscriberName) {
        Set<Subscription> subscribers = this.getSubscriptionsFor(topic);
        if (subscribers == null) {
            return false;
        }
        return subscribers.stream().anyMatch(s -> s.getSubscriberName().equals(subscriberName));
    }

    public void pushToSubscriptions(Message message) {
        Set<Subscription> subscriptions = getSubscriptionsFor(message.getTopic());
        if (subscriptions == null) {
            return;
        }

        for (Subscription subs : subscriptions) {
            subs.getMessageIds().add(message.getId());
        }
    }

    public Map<String, String> getMessageContentsForSubscription(Topic topic, String subscriberName) throws Exception {
        Set<Subscription> subscriptions = getSubscriptionsFor(topic);
        if (subscriptions == null) {
            throw new Exception("SUBSCRIPTION_NOT_FOUND");
        }

        for (Subscription sub : subscriptions) {
            if (!sub.getSubscriberName().equals(subscriberName)) {
                continue;
            }

            Long headMessageId = sub.getMessageIds().peek();
            if (headMessageId == null) {
                throw new Exception("NO_NEW_MESSAGE");
            }

            return new HashMap<String, String>() {
                {
                    put("messageId", String.valueOf(headMessageId));
                    put("contents", messageWarehouse.find(headMessageId).getContents());
                }
            };
        }

        throw new Exception("SUBSCRIPTION_NOT_EXIST_FOR_GIVEN_NAME");
    }

    public String beautify() {
        return this.topicSubscriptionMap.entrySet().stream()
                .map(e -> e.getKey().getName() + "="
                        + e.getValue().stream().map(Subscription::getSubscriberName).collect(Collectors.joining(", ")))
                .collect(joining("&"));
    }
}
