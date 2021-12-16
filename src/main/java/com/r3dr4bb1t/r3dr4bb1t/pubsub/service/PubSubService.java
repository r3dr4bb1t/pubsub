package com.r3dr4bb1t.merpay.pubsub.service;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @todo add Custom Exception
 */
@Service
public interface PubSubService {
	void registerTopic(String topicName, String publihserName) throws Exception;

	void subscribeTopic(String topicName, String subscriberName) throws Exception;

	void publish(String topicName, String messageName, String publisherName) throws Exception;

	Map<String, String> getMessageContents(String topicName, String subscriberName) throws Exception;

	void ackMessage(Long messageId, String subscriberName) throws Exception;
}
