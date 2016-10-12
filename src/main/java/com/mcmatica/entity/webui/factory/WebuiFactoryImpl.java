package com.mcmatica.entity.webui.factory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;
import javax.xml.crypto.Data;

import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.component.tooltip.Tooltip;

import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.DetailListModel;
import com.mcmatica.entity.webui.model.FieldModel;
import com.mcmatica.entity.webui.provider.WebuiDatatableProvider;
import com.mcmatica.entity.webui.provider.WebuiPanelProvider;

public class WebuiFactoryImpl<T extends BaseEntityModel> implements WebuiFactory {

	
	private List<FieldModel> fields;
	
	private List<FieldModel> shortListFields;
	
	
	private Class<T> clazz;
	
	private ResourceBundle labels; 
	
	private ClassScanner<T> scanner;
	
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

	/**
	 * Constructor
	 * 
	 * @param clazz
	 * 		Entity model class
	 */
	public WebuiFactoryImpl(Class<T> clazz)
	{
		
		this.clazz = clazz;

		/*
		 * scan the Entity class
		 */
		this.scanner = new ClassScanner<T>(this.clazz);
			
		this.shortListFields = scanner.getShortListFields();
		this.fields = scanner.getFields();

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

		
		Panel panel = new Panel();
		String title = this.scanner.getClazzAnnotation().title().isEmpty() ? scanner.getClazzAnnotation().beanControllerName() : scanner.getClazzAnnotation().title(); 
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
		if (this.scanner.getChildren() != null && !scanner.getChildren().isEmpty())
		{
			
			panel.getChildren().add(this.buildChildrenPanels(scanner.getChildren()));
			
		}
		
		
		
		return panel;
	}
	
	@Override
	public DataTable buildSelectionGrid()
	{
		DataTable table = new DataTable();
		
		String expr = String.format("#{%s.%s}", scanner.getClazzAnnotation().beanControllerName(), "list");
		table.setValueExpression("value", Utility.createExpression( expr, List.class));
		table.setVar("item");
		table.setSelectionMode("single");
		expr = String.format("#{%s.%s}", scanner.getClazzAnnotation().beanControllerName(), "listFiltered");
		table.setValueExpression("filteredValue", Utility.createExpression( expr, List.class));
		table.setValueExpression("rowKey", Utility.createExpression("#{item.id}", String.class));
		expr = String.format("#{%s.%s}", scanner.getClazzAnnotation().beanControllerName(), "selected" );
		table.setValueExpression("selection", Utility.createExpression(expr, scanner.getAnnotatedCalssType()));
		
		/*
		 * Checkbox
		 */
//		<p:column selectionMode="single" style="width:16px;text-align:center"/>
//		Column chkbox = new Column();
//		chkbox.setSelectionMode("single");
//		table.getChildren().add(chkbox);
		
		/*
		 * columns
		 */
		for (FieldModel fmodel : this.shortListFields)
		{
			Column column = new Column();
			String cellvalue = String.format("#{%s.%s}", table.getVar(), fmodel.getPropertyName());
			
			/*
			 * Header
			 */
//			HtmlOutputText header = new HtmlOutputText();
			String caption = fmodel.getCaption().isEmpty() ? fmodel.getPropertyName() : fmodel.getCaption();
//			header.setValueExpression("value", Utility.createExpression(caption, String.class));						
//			column.setHeader(header);
			column.setValueExpression("headerText", Utility.createExpression(caption, String.class));
			
			
			/*
			 * Cell
			 */			
			HtmlOutputText cell = new HtmlOutputText();						
			cell.setValueExpression("value", Utility.createExpression(cellvalue, fmodel.getPropertyType()));									
			column.getChildren().add(cell);
			
			column.setValueExpression("filterBy", Utility.createExpression(cellvalue, String.class));
			column.setValueExpression("filterMatchMode", Utility.createExpression("contains", String.class));
			
			table.getChildren().add(column);
			
		}
		
		
		
		
		return table;
		
	}
	
//	@Override
//	public List<FieldModel> buildShortListFields()
//	{
//		if (this.shortListFields != null )
//		{
//		
//			Collections.sort(this.shortListFields, new Comparator<FieldModel>() {
//	
//				@Override
//				public int compare(FieldModel o1, FieldModel o2) {
//					if (o1.getShortListPosition() == o2.getShortListPosition()) {
//						return 0;
//					} else if (o1.getShortListPosition() > o2.getShortListPosition()) {
//						return 1;
//					} else {
//						return -1;
//					}
//	
//				}
//			});
//		}
//		return this.shortListFields;
//	}

	@Override
	public List<FieldModel> getFields()
	{
		return this.fields;
	}
	

/*
 * --------------------------
 * P R I V A T E	
 */
	
	private TabView buildChildrenPanels(List<DetailListModel> children)
	{
		TabView tabview = new TabView();
		tabview.setId("detail_tabview");

		for (DetailListModel childType : children)
		{
			tabview.getChildren().add(this.buildChildrenTab(childType));
			
		}
		
		return tabview;
	}
	
	private Tab buildChildrenTab(DetailListModel childType)
	{
		Tab tab = new Tab();
		ClassScanner<BaseEntityModel> childScanner = new ClassScanner(childType.getPropertyType());
		
		
		//tab.setTitle(childScanner.getClazzAnnotation().title());
		tab.setValueExpression("title", Utility.createExpression(childScanner.getClazzAnnotation().title(), String.class));
		
		
//		String listValueGetterSetter = String.format("#{%s}" , childType.getGetterSetterValueName()); 
//		WebuiDatatableProvider<BaseEntityModel> dataTableProvider = new WebuiDatatableProvider<BaseEntityModel>(childScanner.getFields(), 
//																	this.getLabels(), listValueGetterSetter, childType);
//
//
//		//Bottone +
//		CommandButton plus = new CommandButton();
//		//plus.setValue("ADD");
//		plus.setIcon("fa fa-plus");
//		plus.setId(childType.getName() + "_plus_button");
//		String addmethod = String.format("#{%s}" , childType.getAddValueMethodName());
//		MethodExpression actionListener = Utility.createMethodExp(addmethod);
//		plus.setActionExpression(actionListener);
//		plus.setUpdate(childType.getName()+"_datatable");
//		
//		Tooltip plustooltip = new Tooltip();
//		plustooltip.setFor(plus.getId());
//		plustooltip.setValue("Add new " + childType.getName()) ;
//		
//		tab.getChildren().add(plus);
//		tab.getChildren().add(plustooltip);
//		
//		
//		tab.getChildren().add( dataTableProvider.buildDataTable());
		
		tab.getChildren().add(this.buildChildrenPanel(childType, childScanner, null));
		
		
		
		
		return tab;
				
	}
	
	
	private Panel buildChildrenPanel(DetailListModel detailList, ClassScanner<BaseEntityModel> childScanner, String title)
	{
		String listValueGetterSetter = String.format("#{%s}" , detailList.getGetterSetterValueName()); 
		WebuiDatatableProvider<BaseEntityModel> dataTableProvider = new WebuiDatatableProvider<BaseEntityModel>(childScanner.getFields(), 
																	this.getLabels(), listValueGetterSetter, detailList);


		//Bottone +
		CommandButton plus = new CommandButton();
		plus.setIcon("fa fa-plus");
		plus.setId(detailList.getPropertyName() + "_plus_button");
		String addmethod = String.format("#{%s}" , detailList.getAddValueMethodName());
		MethodExpression actionListener = Utility.createMethodExp(addmethod);
		plus.setActionExpression(actionListener);
		plus.setUpdate(detailList.getPropertyName()+"_datatable");

		/*
		 * Tooltip bottone
		 */
		Tooltip plustooltip = new Tooltip();
		plustooltip.setFor(plus.getId());
		plustooltip.setValue("Add new " + detailList.getPropertyName()) ;

		/*
		 * Panel
		 */
		
		Panel panel = new Panel();
		panel.setId(detailList.getPropertyName() + "_panel");
		if (title != null)
		{
			panel.setValueExpression("header", Utility.createExpression(title, String.class));
		}
		panel.getChildren().add(plus);
		panel.getChildren().add(plustooltip);
		panel.getChildren().add(dataTableProvider.buildDataTable());

		/*
		 * Sub Children panels
		 */
		for (DetailListModel subDetailModel : childScanner.getChildren())
		{
					
			
			ClassScanner<BaseEntityModel> subChildScanner = new ClassScanner<BaseEntityModel>((Class<BaseEntityModel>) subDetailModel.getPropertyType());
			panel.getChildren().add(this.buildChildrenPanel(subDetailModel, subChildScanner, subDetailModel.getPropertyName()));
					
			
			

		}
		

		
		return panel;
		
		
	}
	
	
}
