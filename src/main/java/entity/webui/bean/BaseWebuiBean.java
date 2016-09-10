package entity.webui.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

import org.primefaces.component.panelgrid.PanelGrid;

import entity.webui.model.BaseEntityModel;
import entity.webui.model.FieldModel;
import entity.webui.service.BaseWebuiService;

//public abstract class  BaseWebuiBean<T extends BaseEntityModel> {
public abstract class  BaseWebuiBean {

	
	private static final String XHTML_ROWID = "rowId";
	
	//protected BaseWebuiService<BaseEntityModel> service;
	protected BaseWebuiService service;
	
	protected List<FieldModel> columns = new ArrayList<FieldModel>();
	
	protected List<BaseEntityModel> list;
	
	protected PanelGrid formPanel;

	public abstract void init();
	
	
/*
 * ---------------------------------
 * B I N D I N G S
 */
	
	public void onCellClick() {
		
		FacesContext context = FacesContext.getCurrentInstance();

		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
				
		this.service.setSelected(this.service.getById(params.get(XHTML_ROWID)));
	}	


	public List<FieldModel> getColumns() {
		if (this.columns == null || this.columns.isEmpty())
		{
			this.columns = this.service.buildShortListFields();
		}
		return columns;
	}



	public void setColumns(List<FieldModel> columns) {
		this.columns = columns;
	}



	public List<BaseEntityModel> getList() {
		if (this.list == null)
		{
			this.list = this.service.buildList();
		}
		return list;
	}



	public void setList(List<BaseEntityModel> list) {
		this.list = list;
	}
	
	public PanelGrid getPanel()
	{
		if (formPanel == null)
		{
			this.formPanel = this.service.buildPanelGrid();
		}
		return formPanel;
	}

	public void setPanel(PanelGrid panel)
	{
		this.formPanel = panel;
	}	
	
	public BaseEntityModel getSelected() {
		return service.getSelected();
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(BaseEntityModel selected) {
		this.service.setSelected(selected);
	}

/*
 * ---------------------------------
 * C O M M A N D
 */
	
	
	public void save()
	{
		this.service.save();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "save"));
	}
	
	public void create()
	{
		this.service.create();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "addnew"));

	}
	
	public void delete()
	{
		this.service.delete(this.service.getSelected());
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "delete"));
		
	}
	
	public void refresh()
	{
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "refresh"));
		
	}
	

	public abstract String goToHome();
}
