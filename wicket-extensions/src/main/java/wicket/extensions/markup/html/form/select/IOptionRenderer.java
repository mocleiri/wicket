/*
 * $Id$ $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.extensions.markup.html.form.select;

import java.io.Serializable;

import wicket.model.IModel;

/**
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public interface IOptionRenderer extends Serializable
{
	/**
	 * Get the value for displaying to the user.
	 * 
	 * @param object
	 *            SelectOption model object
	 * @return the value for displaying to the user.
	 */
	public String getDisplayValue(Object object);

	/**
	 * Gets the model that will be used to represent the value object.
	 * 
	 * This is a good place to wrap the value object with a detachable model one
	 * is desired
	 * 
	 * @param value
	 * @return model that will contain the value object
	 */
	public IModel getModel(Object value);
}