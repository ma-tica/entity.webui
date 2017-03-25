package com.mcmatica.entity.webui.model.scanner;

public class CommandModel {

	private String label;
	private String commandExpression;
	private String disabledExpression;
	private int sequence;
	private String clientUpdate;

	
	public CommandModel(String label, String commandExpression) {
		super();
		this.label = label;
		this.commandExpression = commandExpression;
	}
		
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}


	public int getSequence() {
		return sequence;
	}


	public void setSequence(int sequence) {
		this.sequence = sequence;
	}


	public String getCommandExpression() {
		return commandExpression;
	}


	public void setCommandExpression(String commandExpression) {
		this.commandExpression = commandExpression;
	}


	public String getDisabledExpression() {
		return disabledExpression;
	}


	public void setDisabledExpression(String disabledExpression) {
		this.disabledExpression = disabledExpression;
	}


	public String getClientUpdate() {
		return clientUpdate;
	}


	public void setClientUpdate(String clientUpdate) {
		this.clientUpdate = clientUpdate;
	}
	
	
	
	
}
