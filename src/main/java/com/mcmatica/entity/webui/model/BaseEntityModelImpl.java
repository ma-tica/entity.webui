package com.mcmatica.entity.webui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.mcmatica.entity.webui.common.Constant;
import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.service.LoadLazyProvider;

public abstract class BaseEntityModelImpl implements BaseEntityModel, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -894880056866237573L;

	
	public BaseEntityModelImpl(){
		this.setId(Utility.randomId(10));
	}
	
	@Transient
	private boolean newInstance;
	
	/**
	 * contains the children items deleted
	 */
	@Transient
	private HashMap<String, List<BaseEntityModel>> fieldListItemRemoved;

	@Transient
	private List<String> filedListLazyLoaded;

	@Override
	public Long getIdValue() {
		if (this.getId() != null)
		{
			return Long.parseLong(this.getId());
		}
		return null;
	}
	
	@Override
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

//	@Override
//	public String getSelectionLabel() {
//		return Long.toString(this.getIdValue());
//	}
	
	/**
	 * Return the List of removed children
	 * 
	 * @param childListName
	 * @return
	 */
	@Override
	public List<BaseEntityModel> getFieldListItemsRemoved(String childListName)
	{
		if (this.fieldListItemRemoved == null)
		{
			this.fieldListItemRemoved = new HashMap<String, List<BaseEntityModel>>();
		}
		if (!this.fieldListItemRemoved.containsKey(childListName) )
		{
			this.fieldListItemRemoved.put(childListName, new ArrayList<BaseEntityModel>());
		}
	
		return this.fieldListItemRemoved.get(childListName);
	}

	@Override
	public void fullLoad() {
	
		LoadLazyProvider.instance.fullLoad(this);
		
	}

	
	
}
