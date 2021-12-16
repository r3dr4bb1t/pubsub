package com.r3dr4bb1t.merpay.pubsub.repository;

import java.util.HashMap;
import java.util.Map;

import com.r3dr4bb1t.merpay.pubsub.model.Topic;

import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.joining;

@Component
public class TopicPublisherMap {

    private Map<Topic, String> topicPublishMap;

    public TopicPublisherMap() {
        topicPublishMap = new HashMap<>();
    }

    public boolean isEmpty() {
        return this.topicPublishMap.isEmpty();
    }

    public void addTopicPublisherPair(Topic topic, String publisherName) {
        this.topicPublishMap.put(topic, publisherName);
    }

    public String getPublisherFor(Topic topic) {
        return this.topicPublishMap.get(topic);
    }

    public boolean isTopicAlreadyExists(Topic topic) {
        return this.topicPublishMap.containsKey(topic);
    }

    public String beautify() {
        return this.topicPublishMap.entrySet().stream().map(e -> e.getKey().getName() + "=" + e.getValue())
                .collect(joining("&"));
    }
}
