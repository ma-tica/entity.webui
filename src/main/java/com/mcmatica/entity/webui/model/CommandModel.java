package com.mcmatica.entity.webui.model;

public class CommandModel {

	private String label;
	private String memberExpression;
	private int sequence;

	
	public CommandModel(String label, String memberExpression, int sequence) {
		super();
		this.label = label;
		this.memberExpression = memberExpression;
		this.sequence = sequence;
	}
		
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getMemberExpression() {
		return memberExpression;
	}
	public void setMemberExpression(String memberExpression) {
		this.memberExpression = memberExpression;
	}


	public int getSequence() {
		return sequence;
	}


	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	
	
	
}
