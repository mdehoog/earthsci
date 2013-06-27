/*******************************************************************************
 * Copyright 2013 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package au.gov.ga.earthsci.application.compatibility;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.renderers.swt.MenuManagerRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

/**
 * {@link WorkbenchRendererFactory} subclass that creates the
 * {@link MenuManagerRendererFixed} instead of the {@link MenuManagerRenderer}
 * as the renderer for {@link MMenu} elements.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class WorkbenchRendererFactoryFixed extends WorkbenchRendererFactory
{
	private MenuManagerRenderer menuRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent)
	{
		if (uiElement instanceof MMenu)
		{
			if (menuRenderer == null)
			{
				menuRenderer = new MenuManagerRendererFixed();
				initRenderer(menuRenderer);
			}
			return menuRenderer;
		}

		return super.getRenderer(uiElement, parent);
	}
}
