package com.mcmatica.entity.webui.factory;

import java.util.List;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.menubutton.MenuButton;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.component.tooltip.Tooltip;

import com.mcmatica.entity.webui.bean.BaseUi;
import com.mcmatica.entity.webui.common.Utility;
import com.mcmatica.entity.webui.model.BaseEntityModel;
import com.mcmatica.entity.webui.model.scanner.CommandModel;
import com.mcmatica.entity.webui.model.DetailListModel;
import com.mcmatica.entity.webui.model.scanner.FieldModel;
import com.mcmatica.entity.webui.provider.WebuiDatatableProvider;
import com.mcmatica.entity.webui.provider.WebuiFormProvider;
import com.mcmatica.entity.webui.provider.WebuiPanelProvider;

public class WebuiFactoryImpl<T extends BaseUi> implements WebuiFactory {

	
	private List<FieldModel> fields;
	
	private List<FieldModel> shortListFields;

	private List<CommandModel> commands;
	
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
		/*
		 * scan the Entity class
		 */
		this.scanner = new ClassScanner<T>(clazz);
			
		this.shortListFields = scanner.getShortListFields();
				
		this.fields = scanner.getFields();

		this.commands = scanner.getCommands();
		
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
	public HtmlPanelGroup buildPanelGrid()
	{

		
		Panel panel = new Panel();
		String title = this.scanner.getClazzAnnotation().title().isEmpty() ? scanner.getClazzAnnotation().beanControllerName() : scanner.getClazzAnnotation().title(); 
		panel.setHeader(Utility.createValue(title, String.class));
		
		/*
		 * Main header Panel
		 */
		WebuiPanelProvider panelProvider = new WebuiPanelProvider(this.fields, scanner.getClazzAnnotation().panelColumns(),
																  scanner.getClazzAnnotation().columnClasses(),	
																  null, null, this.getLabels());
		
		panel.getChildren().add(panelProvider.buildPanelGrid());
		
				
		/*
		 * Children panels
		 */
		Panel childrenpanel = null;
		if (this.scanner.getChildren() != null && !scanner.getChildren().isEmpty())
		{
			childrenpanel = new Panel();
			
			childrenpanel.getChildren().add(this.buildChildrenPanels(scanner.getChildren()));
			

			
		}
		
		HtmlPanelGroup mainpanel = new HtmlPanelGroup();
		mainpanel.getChildren().add(panel);
		
		if (childrenpanel != null)
		{
			panel.setToggleable(true);
			
			childrenpanel.setToggleable(true);
			mainpanel.getChildren().add(childrenpanel);
			
		}
				
		return mainpanel;
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
//		table.setValueExpression("tableStyle", Utility.createExpression("width: auto", String.class));
		
		/* skipChildren */
		table.setValueExpression("skipChildren", Utility.createExpression("true", Boolean.class));
		
		
		/* lazy */
		table.setValueExpression("lazy", Utility.createExpression("true", Boolean.class));
		
		/*
		 * columns
		 */
		for (FieldModel fmodel : this.shortListFields)
		{
			Column column = new Column();
			
//			HtmlOutputText caption = new HtmlOutputText();
//			caption.setValueExpression("value", Utility.createExpression(fmodel.getGridCaption(), String.class));			
//			column.getFacets().put("header", caption);
			
			/*
			 * Header
			 */
			
			/* caption */
			String caption = fmodel.getGridCaption();
			column.setValueExpression("headerText", Utility.createExpression(caption, String.class));
			
			/* width */
			column.setValueExpression("width", Utility.createExpression(fmodel.getGridWidth(), String.class));
			
			
			/*
			 * Cell
			 */			
			
			/* cell value */
			HtmlOutputText cell = new HtmlOutputText();
			String cellvalue = String.format("#{%s.%s}", table.getVar(), fmodel.getPropertyName());
						
			if (fmodel.getLinkedParentField() != null && !fmodel.getLinkedParentField().isEmpty())
			{
				cellvalue = String.format("#{%s.%s.%s}", table.getVar(), fmodel.getLinkedParentField(), fmodel.getLinkedValueExpression());
			}
			
			cell.setValueExpression("value", Utility.createExpression(cellvalue, fmodel.getPropertyType()));
			column.getChildren().add(cell);
			
			/* filter by */
			
			if (fmodel.getLinkedParentField() == null || fmodel.getLinkedParentField().isEmpty())
			{
				//cellvalue = String.format("#{%s.%s.%s}", table.getVar(), fmodel.getLinkedParentField(), fmodel.getLinkedValueExpression());
				cellvalue = String.format("#{%s.%s}", table.getVar(), fmodel.getDbFieldName());
			}else{
				cellvalue = String.format("#{%s.%s.%s.%s}", table.getVar(), 
														 fmodel.getLinkedParentField(),
														 fmodel.getLinkedParentType().getName().replace(".", "_"),
														 fmodel.getLinkedValueExpression());
			}
			
			column.setValueExpression("filterBy", Utility.createExpression(cellvalue, String.class));
			/* filter match mode */
			//column.setValueExpression("filterMatchMode", Utility.createExpression("contains", String.class));
			
			
			table.getChildren().add(column);
			
		}
		
		
		
		
		return table;
		
	}
	
	@Override
	public List<FieldModel> getFields()
	{
		return this.fields;
	}
	

	@Override
	public Panel buildEditFormDialogs()
	{
		
		Panel panel = new Panel();
		
				
		/*
		 * Children panels
		 */
		if (this.scanner.getChildren() != null && !scanner.getChildren().isEmpty())
		{
			this.buildChildrenForms(scanner.getChildren(), panel);
		}

		return panel;
	}
	
	
	@Override
	public MenuButton buildMenuFunctions() 
	{
		MenuButton menuButton = new MenuButton();

		List<CommandModel> commands = this.scanner.getCommands(); 
		if (commands != null && !commands.isEmpty() )
		{


			UIMenuItem item;


			for (CommandModel command : commands)
			{

				item = new UIMenuItem();

				item.setValue(command.getLabel());
				item.setActionExpression(Utility.createMethodExp(command.getCommandExpression()));
				item.setValueExpression("disabled", Utility.createExpression(command.getDisabledExpression(), Boolean.class));
				item.setUpdate(command.getClientUpdate());
				
				
				menuButton.getChildren().add(item);


			}


		}
		
		
		return menuButton;
	}
	
	
/*
 * --------------------------
 * P R I V A T E	
 */
	
	private void buildChildrenForms(List<DetailListModel> children, Panel panelContainer)
	{

		for (DetailListModel childType : children)
		{
			String listValueGetterSetter = String.format("#{%s}" , childType.getGetterSetterValueName()); 
			ClassScanner<BaseUi> childScanner = new ClassScanner<BaseUi>((Class<BaseUi>) childType.getUiClassType());

			WebuiFormProvider formProvider = new WebuiFormProvider(childScanner.getFields(),
																  childScanner.getClazzAnnotation(),
																  this.getLabels(), 
																  listValueGetterSetter, 
																  childType);

			panelContainer.getChildren().add(formProvider.buildForm());
			
			this.buildChildrenForms(childScanner.getChildren(), panelContainer);		
		

			
		}
		
	}



	
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
		ClassScanner<BaseUi> childScanner = new ClassScanner(childType.getUiClassType());
		
		
		tab.setValueExpression("title", Utility.createExpression(childScanner.getClazzAnnotation().title(), String.class));		
		
		tab.getChildren().add(this.buildChildrenPanel(childType, childScanner, null));
				
		return tab;
				
	}
	
	
	private Panel buildChildrenPanel(DetailListModel detailList, ClassScanner<BaseUi> childScanner, String title)
	{
		String listValueGetterSetter = String.format("#{%s}" , detailList.getGetterSetterValueName()); 
		WebuiDatatableProvider<BaseEntityModel> dataTableProvider = new WebuiDatatableProvider<BaseEntityModel>(childScanner.getFields(), 
																	this.getLabels(), listValueGetterSetter, detailList);


		/*
		 * Bottone +
		 */
		CommandButton plus = new CommandButton();
		plus.setProcess("@this");
		plus.setIcon("fa fa-plus");
		plus.setId(detailList.getPropertyName() + "_plus_button");
		String addmethod = String.format("#{%s}" , detailList.getAddValueMethodName());
		MethodExpression actionListener = Utility.createMethodExp(addmethod);
		plus.setActionExpression(actionListener);
		//plus.setUpdate(detailList.getPropertyName()+"_datatable");
		plus.setUpdate(":" + detailList.getPropertyName()+"_form:" + detailList.getPropertyName() + "_dlg");
		plus.setOncomplete("PF('" + detailList.getPropertyName()+"_dlg" + "').show()");


		/*
		 * Tooltip bottone +
		 */
		Tooltip plustooltip = new Tooltip();
		plustooltip.setFor(plus.getId());
		plustooltip.setValue("Add new " + detailList.getPropertyName()) ;

		
		/*
		 * Bottone -
		 */
		String disableExpression = String.format("#{%s eq null}" , detailList.getSelection());
		
		CommandButton remove = new CommandButton();
		remove.setProcess("@this");
		remove.setIcon("fa fa-remove");
		remove.setId(detailList.getPropertyName() + "_remove_button");
		String deleteMethod = String.format("#{%s}", detailList.getDeleteValueMethodName());
		MethodExpression removeactionListener = Utility.createMethodExp(deleteMethod,
				new Class[] { detailList.getPropertyType() });
		remove.setActionExpression(removeactionListener);
		remove.setUpdate(detailList.getPropertyName()+"_datatable");
		
		remove.setValueExpression("disabled", Utility.createExpression(disableExpression, Boolean.class));

		/*
		 * Tooltip bottone -
		 */
		Tooltip removetooltip = new Tooltip();
		removetooltip.setFor(remove.getId());
		removetooltip.setValue("Delete " + detailList.getPropertyName()) ;
		
		/*
		 * Bottone Editing
		 */
		CommandButton edit = new CommandButton();
		edit.setProcess("@this");
		edit.setIcon("fa fa-edit");
		edit.setId(detailList.getPropertyName() + "_edit_button");
		edit.setUpdate(":" + detailList.getPropertyName()+"_form:" + detailList.getPropertyName() + "_dlg");		
		edit.setOncomplete("PF('" + detailList.getPropertyName()+"_dlg" + "').show()");		
		edit.setValueExpression("disabled", Utility.createExpression(disableExpression, Boolean.class));
		/*
		 * Tooltip bottone edit
		 */
		Tooltip edittooltip = new Tooltip();
		edittooltip.setFor(edit.getId());
		edittooltip.setValue("Edit " + detailList.getPropertyName()) ;
		
		
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
		
		panel.getChildren().add(remove);
		panel.getChildren().add(removetooltip);
		
		panel.getChildren().add(edit);
		panel.getChildren().add(edittooltip);

		
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
