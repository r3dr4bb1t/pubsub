package com.r3dr4bb1t.merpay.pubsub.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.r3dr4bb1t.merpay.pubsub.repository.MessageWarehouse;
import com.r3dr4bb1t.merpay.pubsub.repository.TopicPublisherMap;
import com.r3dr4bb1t.merpay.pubsub.repository.TopicSubscriptionMap;
import com.r3dr4bb1t.merpay.pubsub.request.AckRequest;
import com.r3dr4bb1t.merpay.pubsub.request.PublishRequest;
import com.r3dr4bb1t.merpay.pubsub.request.RegisterRequest;
import com.r3dr4bb1t.merpay.pubsub.request.SubscribeRequest;
import com.r3dr4bb1t.merpay.pubsub.service.PubSubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class PubSubController {

	@Autowired
	PubSubService pubSubService;

	@Autowired
	TopicPublisherMap topicPublisherMap;

	@Autowired
	TopicSubscriptionMap topicSubscriptionMap;

	@Autowired
	MessageWarehouse messageWarehouse;

	@GetMapping("/healthcheck")
	public String healthcheck() {
		return "Alive";
	}

	/**
	 * @todo return JSON later
	 */
	@GetMapping("/topic/get/all")
	public String handleAllTopicsPull() {
		return topicPublisherMap.beautify().isEmpty() ? "TOPIC_NOT_FOUND" : topicPublisherMap.beautify();
	}

	@PostMapping("/topic/register")
	public ResponseEntity<String> handleTopicRegister(@Valid @RequestBody RegisterRequest registerRequest) {
		String topicName = registerRequest.getTopicName();
		String publisherName = registerRequest.getPublisherName();

		try {
			pubSubService.registerTopic(topicName, publisherName);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>("Registered", HttpStatus.ACCEPTED);
	}

	@PostMapping("/topic/subscribe")
	public ResponseEntity<String> handleTopicSubscribe(@Valid @RequestBody SubscribeRequest subscribeRequest) {
		String topicName = subscribeRequest.getTopicName();
		String subscriberName = subscribeRequest.getSubscriberName();

		try {
			pubSubService.subscribeTopic(topicName, subscriberName);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>("Subscribed", HttpStatus.ACCEPTED);
	}

	@GetMapping("/subscriber/get/all")
	public String handleAllSubscribersPull() {
		return topicSubscriptionMap.beautify().isEmpty() ? "SUBSCRIBER_NOT_FOUND" : topicSubscriptionMap.beautify();
	}

	@GetMapping("/message/get/")
	public ResponseEntity<?> handleMessagePull(@RequestParam String topicName, @RequestParam String subscriberName) {
		Map<String, String> messageData = new HashMap<>();
		try {
			messageData = pubSubService.getMessageContents(topicName, subscriberName);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<Map<String, String>>(messageData, HttpStatus.OK);
	}

	@GetMapping("/message/get/all")
	public String handleAllMessagesPull() {
		return messageWarehouse.beautify().isEmpty() ? "MESSAGE_NOT_FOUND" : messageWarehouse.beautify();
	}

	@PostMapping("/message/ack")
	public ResponseEntity<String> handleMessageAck(@Valid @RequestBody AckRequest ackRequest) {
		Long messageId = ackRequest.getMessageId();
		String subscriberName = ackRequest.getSubscriberName();

		try {
			pubSubService.ackMessage(messageId, subscriberName);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<String>("Acked", HttpStatus.OK);
	}

	@PostMapping("/message/publish")
	public ResponseEntity<String> handleMessagePublish(@Valid @RequestBody PublishRequest publishRequest) {
		String topicName = publishRequest.getTopicName();
		String messageContents = publishRequest.getMessageContents();
		String publisherName = publishRequest.getPublisherName();

		try {
			pubSubService.publish(topicName, messageContents, publisherName);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<>("Published", HttpStatus.ACCEPTED);
	}
}