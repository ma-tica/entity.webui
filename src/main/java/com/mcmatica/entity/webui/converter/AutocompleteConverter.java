/*
 * Copyright 2016 OmniFaces
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

/*
 * Copyright 2016 OmniFaces
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.mcmatica.entity.webui.converter;

//import static org.omnifaces.util.Messages.createError;

import static  com.mcmatica.entity.webui.common.omnifaces.Messages.createError;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

import org.primefaces.component.autocomplete.AutoComplete;

import com.mcmatica.entity.webui.model.BaseEntityModel;

/**
 * <p>
 * The <code>omnifaces.ListIndexConverter</code> is a variant of the {@link ListConverter} which automatically converts
 * based on the position (index) of the selected item in the list instead of the {@link #toString()} of the selected
 * item.
 *
 * <h3>Usage</h3>
 * <p>
 * This converter is available by converter ID <code>omnifaces.ListIndexConverter</code> and should be used in
 * combination with <code>&lt;o:converter&gt;</code> in order to be able to pass the {@link List} source to it, which it
 * can use for conversion. Here's a basic usage example with PrimeFaces <code>&lt;p:pickList&gt;</code>, which is one of
 * the few select components which doesn't use {@link SelectItem}s as the source, but work directly via a {@link List}.
 * <pre>
 * &lt;p:pickList value="#{bean.dualListModel}" var="entity" itemValue="#{entity}" itemLabel="#{entity.someProperty}"&gt;
 *     &lt;o:converter converterId="omnifaces.ListIndexConverter" list="#{bean.dualListModel.source}" /&gt;
 * &lt;/p:pickList&gt;
 * </pre>
 *
 * <h3>Pros and cons as compared to {@link ListConverter}</h3>
 * <p>
 * For detail, refer the javadoc of {@link SelectItemsIndexConverter} and substitute
 * "<code>SelectItemsIndexConverter</code>" by "<code>ListIndexConverter</code>" and "<code>SelectItemsConverter</code>"
 * by "<code>ListConverter</code>".
 *
 * @author Arjan Tijms
 */
@FacesConverter("mcmatica.AutocompleteConverter")
public class AutocompleteConverter implements Converter {

	

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		AutoComplete autocompleteui = (AutoComplete) component;
		List<BaseEntityModel> suggestions = autocompleteui.getSuggestions();
		if (suggestions != null) {
			for(BaseEntityModel entity : suggestions)
			{
				if (value.equals(entity.toString())) {
					return entity;
				}
			}
		
		}
		

		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return "";
		}

		return value.toString();
	}

	


}