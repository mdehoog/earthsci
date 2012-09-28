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

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Abstract {@link ILayerURIHandler} implementation for URIs that are actually
 * URLs (file, http, etc).
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public abstract class AbstractURLURIHandler extends AbstractInputStreamURIHandler
{
	@Override
	public Layer createLayerFromURI(URI uri) throws LayerURIHandlerException
	{
		InputStream is;
		try
		{
			URL url = uri.toURL();
			is = url.openStream();
		}
		catch (Exception e)
		{
			throw new LayerURIHandlerException(e);
		}
		return createLayer(is, uri);
	}
}
