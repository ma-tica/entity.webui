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
	
	private List<FieldModel> fields;
	
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
		this.fields = scanner.getFields();
		
		Panel panel = new Panel();
		String title = scanner.getClazzAnnotation().title().isEmpty() ? scanner.getClazzAnnotation().beanControllerName() : scanner.getClazzAnnotation().title(); 
		panel.setHeader(Utility.createValue(title, String.class));
		
		/*
		 * Main header Panel
		 */
		WebuiPanelProvider panelProvider = new WebuiPanelProvider(this.fields, scanner.getClazzAnnotation().panelColumns(), 
																null, null, this.getLabels());
		
		panel.getChildren().add(panelProvider.buildPanelGrid());
		
				
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

	@Override
	public List<FieldModel> getFields()
	{
		return this.fields;
	}
	

/*
 * --------------------------
 * P R I V A T E	
 */
	
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
