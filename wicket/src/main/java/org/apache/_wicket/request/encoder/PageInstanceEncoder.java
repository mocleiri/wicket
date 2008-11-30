/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache._wicket.request.encoder;

import org.apache._wicket.IComponent;
import org.apache._wicket.IPage;
import org.apache._wicket.request.RequestHandler;
import org.apache._wicket.request.Url;
import org.apache._wicket.request.encoder.info.ComponentInfo;
import org.apache._wicket.request.encoder.info.PageComponentInfo;
import org.apache._wicket.request.encoder.info.PageInfo;
import org.apache._wicket.request.handler.impl.ListenerInterfaceRequestHandler;
import org.apache._wicket.request.handler.impl.RenderPageRequestHandler;
import org.apache._wicket.request.request.Request;
import org.apache.wicket.RequestListenerInterface;

/**
 * Decodes and encodes the following URLs:
 * 
 * <pre>
 *  Page Instance - Render (RenderPageRequestHandler)
 *  /wicket/page?2
 *  /wicket/page?2.4
 *  /wicket/page?abc.2.4
 * 
 *  Page Instance - Listener (ListenerInterfaceRequestHandler)
 *  /wicket/page?2-click-foo-bar-baz
 *  /wicket/page?2.4-click-foo-bar-baz
 *  /wicket/page?pageMap.2.4-click-foo-bar-bazr-baz
 *  /wicket/page?2.4-click.1-foo-bar-baz (1 is behavior index)  
 * </pre>
 * 
 * @author Matej Knopp
 */
public class PageInstanceEncoder extends AbstractEncoder
{

	/**
	 * Construct.
	 */
	public PageInstanceEncoder()
	{
	}


	public RequestHandler decode(Request request)
	{
		Url url = request.getUrl();
		if (urlStartsWith(url, getContext().getNamespace(), getContext().getPageIdentifier()))
		{
			PageComponentInfo info = getPageComponentInfo(url);
			if (info != null && info.getPageInfo().getPageId() != null)
			{
				IPage page = getPageInstance(info.getPageInfo());
				if (info.getComponentInfo() == null)
				{
					// render page
					return new RenderPageRequestHandler(page);
				}
				else
				{
					ComponentInfo componentInfo = info.getComponentInfo();
					// listener interface
					IComponent component = getComponent(page, componentInfo.getComponentPath());
					RequestListenerInterface listenerInterface = requestListenerInterfaceFromString(componentInfo.getListenerInterface());

					return new ListenerInterfaceRequestHandler(page, component, listenerInterface,
						componentInfo.getBehaviorIndex());
				}
			}
		}
		return null;
	}

	public Url encode(RequestHandler requestHandler)
	{
		PageComponentInfo info = null;

		if (requestHandler instanceof RenderPageRequestHandler)
		{
			IPage page = ((RenderPageRequestHandler)requestHandler).getPage();

			PageInfo i = new PageInfo(page);
			info = new PageComponentInfo(i, null);
		}
		else if (requestHandler instanceof ListenerInterfaceRequestHandler)
		{
			ListenerInterfaceRequestHandler handler = (ListenerInterfaceRequestHandler)requestHandler;
			IPage page = handler.getPage();
			String componentPath = handler.getComponent().getPath();
			RequestListenerInterface listenerInterface = handler.getListenerInterface();

			PageInfo pageInfo = new PageInfo(page);
			ComponentInfo componentInfo = new ComponentInfo(
				requestListenerInterfaceToString(listenerInterface), componentPath,
				handler.getBehaviorIndex());
			info = new PageComponentInfo(pageInfo, componentInfo);
		}

		if (info != null)
		{
			Url url = new Url();
			url.getSegments().add(getContext().getNamespace());
			url.getSegments().add(getContext().getPageIdentifier());
			encodePageComponentInfo(url, info);
			return url;
		}
		else
		{
			return null;
		}
	}

	public int getMachingSegmentsCount(Request request)
	{
		// always return 0 here so that the mounts have higher priority
		return 0;
	}
}