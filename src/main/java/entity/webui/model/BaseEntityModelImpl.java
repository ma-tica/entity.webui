package entity.webui.model;

import java.util.Date;

import org.springframework.data.annotation.Transient;

public abstract class BaseEntityModelImpl implements BaseEntityModel {

	@Transient
	private boolean newInstance;
	
	


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
		return this.getId();
	}
}
