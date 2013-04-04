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
package au.gov.ga.earthsci.catalog.ui.handler;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import au.gov.ga.earthsci.application.parts.info.InfoPart;
import au.gov.ga.earthsci.catalog.ICatalogTreeNode;
import au.gov.ga.earthsci.catalog.ui.CatalogLabelProviderRegistry;

/**
 * Handler used to show the information related to a catalog tree node.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class InformationHandler
{
	@Inject
	private EPartService partService;

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ICatalogTreeNode[] nodes)
	{
		MPart part = partService.showPart(InfoPart.PART_ID, PartState.VISIBLE);
		part.getContext().modify(InfoPart.INPUT_NAME, nodes[0]);
		part.getContext().declareModifiable(InfoPart.INPUT_NAME);
	}

	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) ICatalogTreeNode[] nodes)
	{
		if (nodes == null || nodes.length != 1)
		{
			return false;
		}

		ICatalogTreeNode node = nodes[0];
		URL infoURL = CatalogLabelProviderRegistry.getProvider(node).getInfoURL(node);
		return infoURL != null;
	}
}
