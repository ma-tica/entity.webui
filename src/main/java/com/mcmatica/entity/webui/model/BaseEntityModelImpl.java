package com.mcmatica.entity.webui.model;

import org.springframework.data.annotation.Transient;

import com.mcmatica.entity.webui.common.Constant;

public abstract class BaseEntityModelImpl implements BaseEntityModel {

	@Transient
	private boolean newInstance;
	
	@Override
	public Long getIdValue() {
		if (this.getId() != null)
		{
			return Long.parseLong(this.getId());
		}
		return null;
	}
	
	public void setIdValue(Long idValue) {
		if (idValue != null)
		{
			this.setId(String.format("%0" + Constant.ID_LENGTH + "d", idValue));
		}
	}

	@Override
	public boolean isNewInstance() {
		return newInstance;
	}

	@Override
	public void setNewInstanceState(boolean value) {
		newInstance = value;

	}

	@Override
	public String getSelectionLabel() {
		return Long.toString(this.getIdValue());
	}
	
}
