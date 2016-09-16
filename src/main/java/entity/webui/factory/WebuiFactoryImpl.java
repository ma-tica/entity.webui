package entity.webui.factory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

import entity.webui.common.Utility;
import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;
import entity.webui.provider.ChildType;
import entity.webui.provider.WebuiDatatableProvider;
import entity.webui.provider.WebuiPanelProvider;

public class WebuiFactoryImpl<T extends BaseEntityModel> implements WebuiFactory {

	//private Class<T> clazz;
	
	//private List<FieldModel> fields;
	
	private List<FieldModel> shortListFields;
	
	//private List<ChildType> children;
	
	//private String beanName;
	
	//private Webui clazzAnnotation;
	
	
	private Class<T> clazz;
	
	private ResourceBundle labels; 
	
	/**
	 * Constructor
	 * 
	 * @param clazz
	 * @param beanName
	 */
//	public WebuiFactoryImpl(Class<T> clazz, String managedBeanName)
//	{
//		
//		//this.clazzAnnotation = this.readClassAnnotation(clazz);
//		this.beanName = managedBeanName;
//		this.fillFieldList(clazz, beanName);
//		
//	}

	public WebuiFactoryImpl(Class<T> clazz)
	{
		
		//this.clazzAnnotation = this.readClassAnnotation(clazz);
		//this.beanName = this.clazzAnnotation.beanControllerName();
		//this.fillFieldList(clazz, beanName);
		this.clazz = clazz;
		
	}


	
	public ResourceBundle getLabels() {
		if (this.labels == null)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			this.labels = context.getApplication().getResourceBundle(context, "mclbl");

		}
		return this.labels;
	}

	@Override
	public Panel buildPanelGrid()
	{

		/*
		 * scan the Entity class
		 */
		ClassScanner<T> scanner = new ClassScanner<T>(this.clazz);
			
		this.shortListFields = scanner.getShortListFields();
		
		Panel panel = new Panel();
		panel.setHeader(scanner.getClazzAnnotation().title());
		
		/*
		 * Main header Panel
		 */
//		LayoutUnit north = new LayoutUnit();
//		north.setPosition("center");
//		north.setSize("200");
			
		
		WebuiPanelProvider panelProvider = new WebuiPanelProvider(scanner.getFields(), scanner.getClazzAnnotation().panelColumns(), 
																null, null, this.getLabels());
		
		panel.getChildren().add(panelProvider.buildPanelGrid());
		
//		north.getChildren().add(panelProvider.buildPanelGrid());
		
		
		/*
		 * Children panels
		 */
		if (scanner.getChildren() != null && !scanner.getChildren().isEmpty())
		{
			
			panel.getChildren().add(this.buildChildrenPanels(scanner.getChildren()));
			
		}
		
		
		
		return panel;
	}
	
	
	@Override
	public List<FieldModel> buildShortListFields()
	{
		if (this.shortListFields != null )
		{
		
			Collections.sort(this.shortListFields, new Comparator<FieldModel>() {
	
				@Override
				public int compare(FieldModel o1, FieldModel o2) {
					if (o1.getShortListPosition() == o2.getShortListPosition()) {
						return 0;
					} else if (o1.getShortListPosition() > o2.getShortListPosition()) {
						return 1;
					} else {
						return -1;
					}
	
				}
			});
		}
		return this.shortListFields;
	}
	

/*
 * --------------------------
 * P R I V A T E	
 */
//	private Webui readClassAnnotation(Class<T> clazz)
//	{	
//		return clazz.getAnnotation(Webui.class);
//	}
//	
//	private void fillFieldList(Class<T> clazz, String beanControllerName)
//	{
//		this.fields = new ArrayList<FieldModel>();
//		this.shortListFields = new ArrayList<FieldModel>();
//		this.children = new ArrayList<ChildType>();
//		for (Field field : clazz.getDeclaredFields())
//		{
//			WebuiField webui = field.getAnnotation(WebuiField.class);
//			if (webui != null)
//			{
//				FieldModel fmodel = new FieldModel();
//				fmodel.setCaption(webui.caption());
//				
//				fmodel.setController(webui.controller());
//				fmodel.setPropertyName(field.getName());
//				fmodel.setBeanControllerName(beanControllerName);
//				fmodel.setClazz(field.getType());
//				fmodel.setRequired(webui.required());
//				fmodel.setShortListPosition(webui.shortListPosition());
//				fmodel.setColSpan(webui.colSpan());
//				fmodel.setFormPosition(webui.formPosition());
//				this.fields.add(fmodel);				
//				if (fmodel.getShortListPosition() > 0)
//				{
//					this.shortListFields.add(fmodel);
//				}
//			}else{
//				if (field.getAnnotation(Transient.class ) == null)
//				{
//					/*
//					 * Children data
//					 */
//					if (field.getType().equals(List.class))
//					{
//						Type listType = field.getType(); 
//						if (listType instanceof ParameterizedType)
//						{
//							Type elementType = ((ParameterizedType) listType).getActualTypeArguments()[0];
//							this.children.add(new ChildType(field.getName(), elementType) );
//						
//						}
//						
//						
//					}
//				}
//			}
//		}
//	}
//	
	
	
	
	private TabView buildChildrenPanels(List<ChildType> children)
	{
		TabView tabview = new TabView();

		for (ChildType childType : children)
		{
			tabview.getChildren().add(this.buildChildrenTab(childType));
			
		}
		
		return tabview;
	}
	
	private Tab buildChildrenTab(ChildType childType)
	{
		Tab tab = new Tab();
		ClassScanner<BaseEntityModel> childScanner = new ClassScanner<BaseEntityModel>(childType.getPropertyType());
		
		
		tab.setTitle(childScanner.getClazzAnnotation().title());
		
		String listValueGetterSetter = String.format("#{%s}" , childType.getGetterSetterValueName()); 
		WebuiDatatableProvider<BaseEntityModel> dataTableProvider = new WebuiDatatableProvider<BaseEntityModel>(childScanner.getFields(), 
																	this.getLabels(), listValueGetterSetter, childType);

//		WebuiPanelProvider panelProvider = new WebuiPanelProvider(childScanner.getFields(), childScanner.getClazzAnnotation().panelColumns(), 
//																  null, child.getParentField(), this.getLabels());

		//Bottone +
		CommandButton plus = new CommandButton();
		plus.setValue("ADD");
		String addmethod = String.format("#{%s}" , childType.getAddValueMethodName());
		MethodExpression actionListener = Utility.createMethodExp(addmethod);
		plus.setActionExpression(actionListener);
		plus.setUpdate(childType.getName()+"_datatable");
		tab.getChildren().add(plus);
		
		
		tab.getChildren().add( dataTableProvider.buildDataTable());
		
		
		return tab;
				
	}
	
	
	
}
