package entity.webui.common.omnifaces;

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


import javax.faces.component.UISelectItem;
import javax.faces.model.SelectItem;

/**
 * This class extends the default {@link SelectItem} with several convenience methods.
 *
 * @author Arjan Tijms
 *
 */
public class ExtendedSelectItem extends SelectItem {

	private static final long serialVersionUID = -3266902732567582732L;

	/**
	 * <p>Construct a <code>SelectItem</code> with no initialized property
	 * values.</p>
	 */
	public ExtendedSelectItem() {}

	/**
	 * <p>Construct a <code>SelectItem</code> with property values initialized from the corresponding
	 * properties on the <code>UISelectItem</code>.
	 * </p>
	 * @param uiSelectItem The UI select item.
	 */
	public ExtendedSelectItem(UISelectItem uiSelectItem) {
		super(
			uiSelectItem.getItemValue(),
			uiSelectItem.getItemLabel() != null ? uiSelectItem.getItemLabel() : uiSelectItem.getItemValue() != null ? uiSelectItem.getItemValue().toString() : null,
			uiSelectItem.getItemDescription(),
			uiSelectItem.isItemDisabled(),
			uiSelectItem.isItemEscaped(),
			uiSelectItem.isNoSelectionOption()
		);
	}

}
