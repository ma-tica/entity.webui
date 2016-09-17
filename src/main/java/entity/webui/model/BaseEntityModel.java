package entity.webui.model;

public interface BaseEntityModel {

	String getId();
	
	boolean isNewInstance();
	
	void setNewInstanceState(boolean value);
	
	String getSelectionLabel();
	
}
