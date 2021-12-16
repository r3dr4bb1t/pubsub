package com.r3dr4bb1t.merpay.pubsub.model;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.NotEmpty;

public class Message {
	private static final AtomicLong count = new AtomicLong(0);

	@NotEmpty
	private Long messageId;

	@NotEmpty
	private Topic topic;

	@NotEmpty
	private String contents;

	@NotEmpty
	private Date publishedDateTime;

	public Message() {
	}

	public Message(Topic topic, String contents, Date publishedDateTime) {
		this.topic = topic;
		this.contents = contents;
		this.publishedDateTime = publishedDateTime;
		this.messageId = count.incrementAndGet();
	}

	public Long getId() {
		return messageId;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Date getPublishedDateTime() {
		return publishedDateTime;
	}

	public void setPublishedDateTime(Date publishedDateTime) {
		this.publishedDateTime = publishedDateTime;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
