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
package org.apache.wicket.markup.html.form;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.WicketTester;

/**
 * @author Pedro Santos
 */
public class FormWithMultipleButtonsTest extends TestCase
{
	private WicketTester tester;

	@Override
	protected void setUp() throws Exception
	{
		tester = new WicketTester();
	}

	/**
	 * Testing if the correct submit button is invoked in an form with multiple submit buttons. The
	 * browser set the clicked button input name as parameter on the HTTP request.
	 */
	public void testFindSubmittingButton()
	{
		TestPage testPage = new TestPage();
		tester.startPage(testPage);
		tester.getRequest()
			.getPostParameters()
			.addParameterValue(testPage.ajaxFallbackButton.getInputName(), "");
		tester.submitForm(testPage.form);
		assertFalse(testPage.submitSequence.contains(testPage.button));
		assertTrue(testPage.submitSequence.contains(testPage.ajaxFallbackButton));
	}

	/**
	 * @see href https://issues.apache.org/jira/browse/WICKET-1894
	 */
	public void testAjaxFallbackButtonInvokedFirst()
	{
		TestPage testPage = new TestPage();
		tester.startPage(testPage);
		tester.executeAjaxEvent(testPage.ajaxFallbackButton, "onclick");
		assertEquals(0, testPage.submitSequence.indexOf(testPage.ajaxFallbackButton));
		assertEquals(1, testPage.submitSequence.indexOf(testPage.form));
	}

	public void testButtonInvokedFirst()
	{
		TestPage testPage = new TestPage();
		tester.startPage(testPage);
		tester.getRequest()
			.getPostParameters()
			.addParameterValue(testPage.button.getInputName(), "");
		tester.submitForm(testPage.form);
		assertEquals(0, testPage.submitSequence.indexOf(testPage.button));
		assertEquals(1, testPage.submitSequence.indexOf(testPage.form));
	}

	@Override
	protected void tearDown() throws Exception
	{
		tester.destroy();
	}

	private static class TestPage extends WebPage implements IMarkupResourceStreamProvider
	{
		List<Component> submitSequence = new ArrayList<Component>();
		Form form;
		Button button;
		AjaxFallbackButton ajaxFallbackButton;

		public TestPage()
		{
			add(form = new Form("form")
			{
				@Override
				protected void onSubmit()
				{
					submitSequence.add(this);
				};
			});
			form.add(button = new Button("b1")
			{
				@Override
				public void onSubmit()
				{
					submitSequence.add(this);
				};
			});
			form.add(ajaxFallbackButton = new AjaxFallbackButton("b2", form)
			{
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					submitSequence.add(this);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form)
				{
				}
			});
		}

		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<html><body><form wicket:id=\"form\"><button wicket:id=\"b1\"></button><button wicket:id=\"b2\"></button></form></body></html>");
		}
	}

}
