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
package au.gov.ga.earthsci.layer.ui.handlers;

import java.util.Set;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.sapphire.Event;
import org.eclipse.sapphire.Listener;
import org.eclipse.sapphire.Property;
import org.eclipse.sapphire.PropertyContentEvent;
import org.eclipse.sapphire.ui.def.DefinitionLoader.Reference;
import org.eclipse.sapphire.ui.forms.DialogDef;
import org.eclipse.sapphire.ui.forms.swt.SapphireDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Shell;

import au.gov.ga.earthsci.editable.EditableManager;
import au.gov.ga.earthsci.editable.ElementAndDefinition;
import au.gov.ga.earthsci.layer.IPersistentLayer;
import au.gov.ga.earthsci.layer.tree.ILayerNode;
import au.gov.ga.earthsci.layer.tree.ILayerTreeNode;
import au.gov.ga.earthsci.worldwind.common.WorldWindowRegistry;

/**
 * Handles properties action.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class PropertiesHandler
{
	@Named(IServiceConstants.ACTIVE_SHELL)
	private Shell shell;

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ILayerTreeNode layer, Clipboard clipboard)
	{
		execute(new ILayerTreeNode[] { layer }, clipboard);
	}

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ILayerTreeNode[] layers, Clipboard clipboard)
	{
		if (layers == null || layers.length == 0 || layers[0] == null)
		{
			return;
		}

		final ILayerTreeNode node = layers[0];
		if (node instanceof ILayerNode)
		{
			ILayerNode layerNode = (ILayerNode) node;
			IPersistentLayer layer = layerNode.getLayer();
			ElementAndDefinition editor = EditableManager.getInstance().edit(layer);

			Listener listener = new Listener()
			{
				@Override
				public void handle(Event event)
				{
					if (event instanceof PropertyContentEvent)
					{
						//a layer property changed, redraw the world windows
						WorldWindowRegistry.INSTANCE.redraw();
					}
				}
			};
			editor.getElement().attach(listener);
			//XXX in sapphire 0.6.x attaching to the model (as above) was enough to receive property
			//change events, but 0.7.0 requires attaching to properties individually: (is this a bug?)
			Set<Property> properties = editor.getElement().properties();
			for (Property property : properties)
			{
				property.attach(listener);
			}

			Reference<DialogDef> definition = editor.getLoader().dialog();
			SapphireDialog dialog = new SapphireDialog(shell, editor.getElement(), definition);
			if (dialog.open() != Dialog.OK)
			{
				editor.revert();
				WorldWindowRegistry.INSTANCE.redraw();
			}
		}
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ILayerTreeNode layer)
	{
		return layer != null && layer instanceof ILayerNode;
	}

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ILayerTreeNode[] layers)
	{
		return layers != null && layers.length == 1 && layers[0] instanceof ILayerNode;
	}
}
