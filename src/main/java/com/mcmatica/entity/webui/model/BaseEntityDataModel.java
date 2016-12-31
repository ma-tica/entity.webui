package com.mcmatica.entity.webui.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.util.CollectionUtils;

import com.mcmatica.entity.webui.repository.BaseRepository;
import com.mcmatica.jqb.Jqb;
import com.mcmatica.jqb.JqbDialect;
import com.mcmatica.jqb.JqbWhereBuilder;


public class BaseEntityDataModel<E extends BaseEntityModel> extends LazyDataModel<E> implements SelectableDataModel<E> {

	
	private BaseRepository<E> repository;
	private Long listSize;
	
	
	
	public  BaseEntityDataModel(BaseRepository<E> repository) {
		super();
		this.repository = repository;
		this.listSize = this.repository.count();
		
	}

	public List<E> getData() {
		return (List<E>) getWrappedData();
	}




	@Override
	public Object getRowKey(E object) {
		return object.getId();
		
	}


	@Override
	public E getRowData(String rowKey) {
		final String uniqueKey = rowKey;
		List<E> data = this.getData();

		if (!CollectionUtils.isEmpty(data)) {
            //E t = data.get(0);
            
            return data.stream().filter((keyed) -> String.valueOf(((E) keyed).getId()).equals(uniqueKey)).findFirst().orElse(null);
        }
		return null;
	}
	
	public void setRowData(E item)
	{
		final String uniqueKey = item.getId();
		List<E> data = this.getData();

		if (!CollectionUtils.isEmpty(data)) {
            //E t = data.get(0);
            
            data.stream().filter((keyed) -> String.valueOf(((E) keyed).getId()).equals(uniqueKey)).findFirst().of(item);
            
        }
		
	}
	
	
//	public void moveNext(E current)
//	{
//		List<E> data = this.getData();
//		final String uniqueKey = current.getId();
//		if (!CollectionUtils.isEmpty(data)) {
//            //E t = data.get(0);
//            
//             data.stream().filter((keyed) -> String.valueOf(((E) keyed).getId()).equals(uniqueKey)).findFirst().orElse(null);
//        }
//		
//	}
	
	
	@Override
	public int getRowIndex() {
		// TODO Auto-generated method stub
		return super.getRowIndex();
	}

	public void add(E item)
	{
//		this.getData().add(item);
		this.listSize = this.repository.count();
	}
	
	public void remove(E item)
	{
//		this.getData().remove(item);
//		this.listSize--;
		this.listSize = this.repository.count();
	}

	
	
	
	@Override
	public List<E> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		

//		int pagenum = 0;
//		if (first > 0 && pageSize > 0)
//		{
//			//pagenum = first-pageSize+1;
//			pagenum = first / pageSize;
//		}

		List<E> page;

		if (filters != null && !filters.isEmpty())
		{
			Jqb jqb = new Jqb(JqbDialect.MONGODB);
			JqbWhereBuilder where = jqb.getWhere();
			Iterator<String> iterator = filters.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				if (where == null) {
					where = jqb.where(jqb.property(key).contains(filters.get(key)+""));
				}else{
					where = where.and(jqb.where(jqb.property(key).contains(filters.get(key)+"")));
				}
			}
			
			int pageIndex = first / pageSize;
			page = this.repository.find(where.text(), pageIndex, pageSize);
			
			
			
			//this.setRowCount(page.size());
		}else{
			
			int pageIndex = first / pageSize;
			
			page = this.repository.findAll(pageIndex , pageSize);
			//this.setRowCount(this.listSize.intValue());
			//this.setRowCount(page.size());
		}
		
		
		
		this.setRowCount(this.listSize.intValue());
		
		
		
		
		
		return page;
	}

//	@Override
//	public List<E> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
//		
//		PageRequest pagerequest = new PageRequest(first, pageSize);
//		
//		Page<E> page =this.repository.findAll(pagerequest);
//		
//		
//		
//		this.setRowCount(page.getSize());
//		
//		
//		return page.getContent();
//		
//	}

	
	
}
