package entity.webui.model;

public enum EPersistanceState {
	/**
	 * object just instantiated
	 */
	NEW,
	
	/**
	 * Object syncronized with persistent storage
	 */
	PERSISTED,
	
	/**
	 * Object not syncronized with persisten storage
	 */
	CHANGED
	
	;	
	
}
