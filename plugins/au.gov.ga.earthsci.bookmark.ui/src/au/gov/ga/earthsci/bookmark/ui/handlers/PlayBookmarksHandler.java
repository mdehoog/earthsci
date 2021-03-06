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
package au.gov.ga.earthsci.bookmark.ui.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.TableViewer;

import au.gov.ga.earthsci.bookmark.model.IBookmark;
import au.gov.ga.earthsci.bookmark.ui.IBookmarksController;

/**
 * A handler used to play the bookmark playlist
 * 
 * @author James Navin (james.navin@ga.gov.au)
 */
public class PlayBookmarksHandler
{
	@Inject
	private IBookmarksController controller;

	@Execute
	public void execute(TableViewer bookmarkListView,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) IBookmark[] selectedBookmarks)
	{
		controller.stop();
		controller.play((selectedBookmarks == null || selectedBookmarks.length == 0) ? null : selectedBookmarks[0]);
	}

	@CanExecute
	public boolean canExecute()
	{
		return !controller.isPlaying() && !controller.getCurrentList().getBookmarks().isEmpty();
	}
}
