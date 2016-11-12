package com.mcmatica.entity.webui.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;
import org.springframework.util.CollectionUtils;


public class BaseEntityDataModel<E extends BaseEntityModel> extends ListDataModel<E> implements SelectableDataModel<E> {

	public BaseEntityDataModel() {
		super();

	}

	private List<E> getData() {
		return (List<E>) getWrappedData();
	}


	public BaseEntityDataModel(List<E> list) {
		super(list);

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

}
