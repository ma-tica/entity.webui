package com.mcmatica.entity.webui.model;

public class JoinModel {

	private final String entityClassName;
	private final String foreignFieldName;
	private final String localFieldName;
	private final String aliasName;

	public JoinModel(String entityClassName, String foreignFieldName, String localFieldName, String aliasName) {
		super();
		this.entityClassName = entityClassName;
		this.foreignFieldName = foreignFieldName;
		this.localFieldName = localFieldName;
		this.aliasName = aliasName;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public String getForeignFieldName() {
		return foreignFieldName;
	}

	public String getLocalFieldName() {
		return localFieldName;
	}

	public String getAliasName() {
		return aliasName;
	}

	
}
