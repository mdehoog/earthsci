/*******************************************************************************
 * Copyright 2012 Geoscience Australia
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
package au.gov.ga.earthsci.core.model.layer.uri.handler;

import gov.nasa.worldwind.layers.Layer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;

import au.gov.ga.earthsci.core.retrieve.IRetrieval;
import au.gov.ga.earthsci.core.retrieve.IRetrievalResult;
import au.gov.ga.earthsci.core.retrieve.RetrievalServiceFactory;

/**
 * Abstract {@link ILayerURIHandler} implementation for URIs that are actually
 * URLs (file, http, etc).
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public abstract class AbstractURLURIHandler extends AbstractInputStreamURIHandler
{
	@Override
	public Layer createLayerFromURI(Object caller, URI uri, IProgressMonitor monitor) throws LayerURIHandlerException
	{
		monitor.beginTask("Loading layer from URL", IProgressMonitor.UNKNOWN);
		InputStream is;
		try
		{
			URL url = uri.toURL();
			IRetrieval retrieval = RetrievalServiceFactory.getServiceInstance().retrieve(caller, url);
			retrieval.start();
			IRetrievalResult result = retrieval.waitAndGetResult();
			is = result.getData().getInputStream();
		}
		catch (Exception e)
		{
			throw new LayerURIHandlerException(e);
		}
		finally
		{
			monitor.done();
		}
		try
		{
			return createLayer(is, uri);
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
			}
		}
	}
}
