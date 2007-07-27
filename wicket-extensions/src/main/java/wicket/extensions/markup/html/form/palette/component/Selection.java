/*
 * $Id$
 * $Revision$ $Date$
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

package wicket.extensions.markup.html.form.palette.component;

import java.util.Iterator;

import wicket.extensions.markup.html.form.palette.Palette;
import wicket.markup.ComponentTag;
import wicket.util.value.ValueMap;

/**
 * select box containg selected choices of the palette
 * 
 * @author Igor Vaynberg ( ivaynberg )
 * 
 */
public class Selection extends AbstractOptions
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id component id
	 * @param palette parent palette
	 */
	public Selection(String id, Palette palette)
	{
		super(id, palette);
	}

	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		ValueMap attrs=tag.getAttributes();
		
		String onFocus=getPalette().getSelectionOnFocusJS();
		if (onFocus!=null) {
			attrs.put("onFocus", onFocus);
		}
		
		tag.getAttributes().put("ondblclick", getPalette().getRemoveOnClickJS()); 
	}
	
	protected Iterator getOptionsIterator()
	{
		return getPalette().getSelectedChoices();
	}
	
}