package entity.webui.factory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.annotation.Transient;

import entity.webui.annotation.Webui;
import entity.webui.annotation.WebuiField;
import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;
import entity.webui.model.FieldModel.EditorComponent;
import entity.webui.provider.ChildType;

class ClassScanner<T extends BaseEntityModel> {

	private List<FieldModel> fields;
	
	private List<FieldModel> shortListFields;

	private List<ChildType> children;
	
	private Class<T> clazz;

	
	private Webui clazzAnnotation;
	
	/**
	 * Constructor
	 * 
	 * @param clazz
	 * 			Class to scan
	 */
	public ClassScanner(Class<T> clazz)
	{
		this.clazz = clazz;
		this.clazzAnnotation =clazz.getAnnotation(Webui.class);
	
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
	public Webui getClazzAnnotation() {
		return clazzAnnotation;
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
		
		Webui webui = clazz.getAnnotation(Webui.class);
		
		for (Field field : this.clazz.getDeclaredFields())
		{
			WebuiField webuifield = field.getAnnotation(WebuiField.class);
			if (webuifield != null)
			{
				FieldModel fmodel = new FieldModel();
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
				this.fields.add(fmodel);				
				if (fmodel.getShortListPosition() > 0)
				{
					this.shortListFields.add(fmodel);
				}
			}else{
				if (field.getAnnotation(Transient.class ) == null)
				{
					/*
					 * Children data
					 */
					if (field.getType().equals(List.class))
					{
						Type elementType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
						//if (listType instanceof ParameterizedType)
						//{
							//Type elementType = ((ParameterizedType) listType).getActualTypeArguments()[0];
//						Class.forName(elementType.getTypeName()
							this.children.add(new ChildType(webui.beanControllerName(), field.getName(), (Class)elementType ));
						
						//}
						
						
					}
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
	private EditorComponent retrieveEditorComponentField(WebuiField webuifield, Field field)
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
			Webui relwebui =  field.getType().getAnnotation(Webui.class);
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
