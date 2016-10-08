package com.mcmatica.entity.webui.factory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.mcmatica.entity.webui.annotation.MCSelectable;
import com.mcmatica.entity.webui.annotation.MCWebui;
import com.mcmatica.entity.webui.annotation.MCWebuiField;
import com.mcmatica.entity.webui.annotation.MCWebuiFieldEvent;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.FieldModel;
import com.mcmatica.entity.webui.model.FieldModel.EditorComponent;
import com.mcmatica.entity.webui.provider.ChildType;

class ClassScanner<T extends BaseEntityModel> {

	private List<FieldModel> fields;
	
	private List<FieldModel> shortListFields;

	private List<ChildType> children;
	
	private Class<T> clazz;

	
	private MCWebui clazzAnnotation;
	
	/**
	 * Constructor
	 * 
	 * @param clazz
	 * 			Class to scan
	 */
	public ClassScanner(Class<T> clazz)
	{
		this.clazz = clazz;
		this.clazzAnnotation =clazz.getAnnotation(MCWebui.class);
	
		this.scan();
	}


	
	/**
	 * @return the fields
	 */
	public List<FieldModel> getFields() {
		return fields;
	}



	/**
	 * @return the shortListFields
	 */
	public List<FieldModel> getShortListFields() {
		return shortListFields;
	}



	/**
	 * @return the children
	 */
	public List<ChildType> getChildren() {
		return children;
	}




	/**
	 * @return the clazzAnnotation
	 */
	public MCWebui getClazzAnnotation() {
		return clazzAnnotation;
	}


	/**
	 * Return the annotated class Type
	 * @return
	 */
	public Class<T> getAnnotatedCalssType()
	{
		return this.clazz;
	}
	
	
	private void scan()
	{
		
		/*
		 * Fills the internal list of fields
		 */
		this.fillFieldList();

		/*
		 * Sorts fields
		 */
		this.sortFieldByFormPosition();

	}
	
	private void fillFieldList()
	{
		this.fields = new ArrayList<FieldModel>();
		this.shortListFields = new ArrayList<FieldModel>();
		this.children = new ArrayList<ChildType>();
		
		MCWebui webui = clazz.getAnnotation(MCWebui.class);
		
		for (Field field : this.clazz.getDeclaredFields())
		{
			FieldModel fmodel = new FieldModel();
			MCWebuiField webuifield = field.getAnnotation(MCWebuiField.class);
			if (webuifield != null)
			{				
				fmodel.setCaption(webuifield.caption());				
				fmodel.setEditorComponent(this.retrieveEditorComponentField(webuifield, field));
				fmodel.setPropertyName(field.getName());				
				fmodel.setBeanControllerName(webui.beanControllerName());
				fmodel.setRelatedBeanControllerName(this.retrieveRelatedBeanControllerName(field));
				fmodel.setPropertyType(field.getType());
				fmodel.setRequiredExpression(webuifield.required());
				fmodel.setShortListPosition(webuifield.shortListPosition());
				fmodel.setColSpan(webuifield.colSpan());
				fmodel.setFormPosition(webuifield.formPosition());
				fmodel.setDefaultValue(webuifield.defaultValue());
				fmodel.setVisibleExpression(webuifield.visible());
				fmodel.setReadonlyExpression(webuifield.readonly());
				fmodel.setFillSelectionListExpression(String.format("#{%s.%s}", fmodel.getRelatedBeanControllerName(), "findAll()"));
				
				this.fields.add(fmodel);				
				if (fmodel.getShortListPosition() > 0)
				{
					this.shortListFields.add(fmodel);
				}
			}
			
			MCSelectable selectable = field.getAnnotation(MCSelectable.class);
			if (selectable != null)
			{
				fmodel.setFillSelectionListExpression(selectable.value());
			}

			MCWebuiFieldEvent event = field.getAnnotation(MCWebuiFieldEvent.class);
			if (event != null)
			{
				fmodel.setEvent(event.event());
				fmodel.setEventUpdateExpression(event.update());
				fmodel.setEventListenerExpression(event.listener());
			}
			
			if (field.getAnnotation(Transient.class ) == null)
			{
				/*
				 * Children data
				 */
				if (field.getType().equals(List.class))
				{
					Type elementType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
					this.children.add(new ChildType(webui.beanControllerName(), field.getName(), (Class)elementType ));
				}
			}
					
		}
	}
	
	/**
	 * Choose the right controller for the field
	 * 
	 * @param webuifield
	 * @param field
	 * @return
	 */
	private EditorComponent retrieveEditorComponentField(MCWebuiField webuifield, Field field)
	{
		/*
		 * Not defined by developer
		 */
		if (webuifield.editorComponent().equals(EditorComponent.UNDEFINED))
		{
			if (ClassScanner.implementBaseEntityModel(field.getType()))
			{
				return EditorComponent.SELECTION_ONE_MENU;
			}else{
				
				return EditorComponent.INPUT_TEXT;
			}
		}else{
			/*
			 * define by developer
			 */
			return webuifield.editorComponent();
		}
		
	}
	
	
	private String retrieveRelatedBeanControllerName(Field field)
	{
		if (ClassScanner.implementBaseEntityModel(field.getType()))
		{
			MCWebui relwebui =  field.getType().getAnnotation(MCWebui.class);
			if (relwebui != null)
			{
				return relwebui.beanControllerName();
			}
		}
		
		return null;
	}
	
	/**
	 * Sorts the field list by form position attribute
	 */
	private void sortFieldByFormPosition()
	{
		Collections.sort(this.fields, new Comparator<FieldModel>() {

			@Override
			public int compare(FieldModel o1, FieldModel o2) {
				if (o1.getFormPosition() == o2.getFormPosition()) {
					return 0;
				} else if (o1.getFormPosition() > o2.getFormPosition()) {
					return 1;
				} else {
					return -1;
				}

			}
		});
	}
	
	private static boolean implementBaseEntityModel(Type type)
	{
		return Arrays.asList(((Class) type).getInterfaces()).contains(BaseEntityModel.class);
	}


}
