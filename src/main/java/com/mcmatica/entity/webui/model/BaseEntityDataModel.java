package com.mcmatica.entity.webui.model;

import java.util.ArrayList;
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
	public List<E> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)  {
		
		List<E> page;

		if (filters != null && !filters.isEmpty())
		{
			Jqb jqb = new Jqb(JqbDialect.MONGODB);
			JqbWhereBuilder where = jqb.getWhere();
			Iterator<String> iterator = filters.keySet().iterator();
			
			List<JoinModel> joins = null;
			while(iterator.hasNext()) {
				String key = iterator.next();
				
				/*
				 * if key contains dot means we need a lookup
				 */
				if (!key.contains(".")) {
					if (where == null) {
						where = jqb.where(jqb.property(key).contains(filters.get(key)+""));
					}else{
						where = jqb.getWhere().and(jqb.property(key).contains(filters.get(key)+""));
					}
				}else{
					if (joins == null)
					{
						joins = new ArrayList<JoinModel>();
					}
					String[] composedFilterKey = key.split("\\.");

					JoinModel join = new JoinModel(composedFilterKey[1].replace("_", "."), "_id", composedFilterKey[0], composedFilterKey[0]);
					joins.add(join);
					
					if (where == null) {
						where = jqb.where(jqb.property(composedFilterKey[0].concat(".").concat(composedFilterKey[2])).contains(filters.get(key)+""));
					}else{
						where = jqb.getWhere().and(jqb.property(composedFilterKey[0].concat(".").concat(composedFilterKey[2])).contains(filters.get(key)+""));
					}
					
				}
			}
			
			int pageIndex = first / pageSize;
			if (joins == null) 
			{
				page = this.repository.find(where.text(), pageIndex, pageSize);
				this.listSize = this.repository.count(where.text());
			}else{
				try {
					page = this.repository.find(where.text(), pageIndex, pageSize, joins);
					this.listSize = this.repository.count(where.text(), joins);
				} catch (ClassNotFoundException e) {
					page = null;
					e.printStackTrace();
				}
			}
			
			
			
		}else{
			
			int pageIndex = first / pageSize;
			
			page = this.repository.findAll(pageIndex , pageSize);
			this.listSize = this.repository.count();
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
