package com.mcmatica.entity.webui.model;

public class EventModel {

	private String eventUpdateExpression;
	private String eventListenerExpression;
	private String event;
	
	
	public EventModel(String eventUpdateExpression, String eventListenerExpression, String event) {
		super();
		this.eventUpdateExpression = eventUpdateExpression;
		this.eventListenerExpression = eventListenerExpression;
		this.event = event;
	}
	
	
	
	public String getEventUpdateExpression() {
		return eventUpdateExpression;
	}
	public void setEventUpdateExpression(String eventUpdateExpression) {
		this.eventUpdateExpression = eventUpdateExpression;
	}
	public String getEventListenerExpression() {
		return eventListenerExpression;
	}
	public void setEventListenerExpression(String eventListenerExpression) {
		this.eventListenerExpression = eventListenerExpression;
	}
	public String getEventName() {
		return event;
	}
	public void setEventName(String event) {
		this.event = event;
	}

}
