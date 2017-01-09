package com.mcmatica.entity.webui.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.component.tooltip.Tooltip;

import com.mcmatica.entity.webui.bean.BaseUi;
import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.DetailListModel;
import com.mcmatica.entity.webui.model.FieldModel;
import com.mcmatica.entity.webui.provider.WebuiDatatableProvider;
import com.mcmatica.entity.webui.provider.WebuiPanelProvider;

public class WebuiFactoryImpl<T extends BaseUi> implements WebuiFactory {

	
	private List<FieldModel> fields;
	
	private List<FieldModel> shortListFields;
	
	
	//private Class<T> clazz;
	
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
		
		//this.clazz = clazz;

		/*
		 * scan the Entity class
		 */
		this.scanner = new ClassScanner<T>(clazz);
			
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
		
		/* value */
		String expr = String.format("#{%s.%s}", scanner.getClazzAnnotation().beanControllerName(), "list");
		table.setValueExpression("value", Utility.createExpression( expr, DataModel.class));
		
		/* var */
		table.setVar("item");
		table.setSelectionMode("single");
		
		
		/* filtered value */
		expr = String.format("#{%s.%s}", scanner.getClazzAnnotation().beanControllerName(), "listFiltered");
		table.setValueExpression("filteredValue", Utility.createExpression( expr, List.class));
		
		//table.setValueExpression("rowKey", Utility.createExpression("#{item.id}", String.class));
		
		/* selection */
		expr = String.format("#{%s.%s}", scanner.getClazzAnnotation().beanControllerName(), "selected" );
		table.setValueExpression("selection", Utility.createExpression(expr, scanner.getAnnotatedCalssType()));
		
		/* table style */
		table.setValueExpression("tableStyle", Utility.createExpression("width: auto", String.class));
		
		/* lazy */
		table.setValueExpression("lazy", Utility.createExpression("true", Boolean.class));
		
		/*
		 * columns
		 */
		for (FieldModel fmodel : this.shortListFields)
		{
			Column column = new Column();
			
			
			/*
			 * Header
			 */
			
			/* caption */
			String caption = fmodel.getGridCaption();
			column.setValueExpression("headerText", Utility.createExpression(caption, String.class));
			
			/* width */
//			column.setValueExpression("width", Utility.createExpression(fmodel.getGridWidth(), String.class));
			
			
			/*
			 * Cell
			 */			
			
			/* cell value */
			HtmlOutputText cell = new HtmlOutputText();
			String cellvalue = String.format("#{%s.%s}", table.getVar(), fmodel.getPropertyName());
			cell.setValueExpression("value", Utility.createExpression(cellvalue, fmodel.getPropertyType()));
			column.getChildren().add(cell);
			
			/* filter by */
			cellvalue = String.format("#{%s.%s}", table.getVar(), fmodel.getDbFieldName());
			column.setValueExpression("filterBy", Utility.createExpression(cellvalue, String.class));
			
			/* filter match mode */
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
		//ClassScanner<BaseUi> childScanner = new ClassScanner(childType.getPropertyType());
		ClassScanner<BaseUi> childScanner = new ClassScanner(childType.getUiClassType());
		
		
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
	
	
	private Panel buildChildrenPanel(DetailListModel detailList, ClassScanner<BaseUi> childScanner, String title)
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
		 * Loader gif
		 */
		// TODO - da migliorare
		
//		GraphicImage img = new GraphicImage();
//		img.setName("img/ajax-loader.gif");
//		img.setId(detailList.getPropertyName() + "_jobber_gif");
//		img.setStyle("visibility: hidden");		
//		img.setStyleClass("detail_tabview_jobber_gif");
	
		
		
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
		//panel.getChildren().add(img);
		panel.getChildren().add(dataTableProvider.buildDataTable());

		/*
		 * Sub Children panels
		 */
		for (DetailListModel subDetailModel : childScanner.getChildren())
		{
					
			
			//ClassScanner<BaseUi> subChildScanner = new ClassScanner<BaseUi>((Class<BaseUi>) subDetailModel.getPropertyType());
			ClassScanner<BaseUi> subChildScanner = new ClassScanner<BaseUi>((Class<BaseUi>) subDetailModel.getUiClassType());
			panel.getChildren().add(this.buildChildrenPanel(subDetailModel, subChildScanner, subDetailModel.getPropertyName()));
					
			
			

		}
		

		
		return panel;
		
		
	}
	
	
}
