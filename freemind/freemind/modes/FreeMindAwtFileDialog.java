/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2012 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
*
*See COPYING for Details
*
*This program is free software; you can redistribute it and/or
*modify it under the terms of the GNU General Public License
*as published by the Free Software Foundation; either version 2
*of the License, or (at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program; if not, write to the Free Software
*Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package freemind.modes;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author foltin
 * @date 23.02.2012
 */
public class FreeMindAwtFileDialog extends FileDialog implements
		FreeMindFileDialog {


	private final static class NullFilter extends FileFilter {

		/* (non-Javadoc)
		 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
		 */
		public boolean accept(File pF) {
			return true;
		}

		/* (non-Javadoc)
		 * @see javax.swing.filechooser.FileFilter#getDescription()
		 */
		public String getDescription() {
			return "NullFilter";
		}
		
	}
	
	private final class DirFilter extends FileFilter {

		public boolean accept(File pF) {
			return pF.isDirectory();
		}

		/* (non-Javadoc)
		 * @see javax.swing.filechooser.FileFilter#getDescription()
		 */
		public String getDescription() {
			return "DirFilter";
		}
		
	}
	
	private final class FileOnlyFilter extends FileFilter {
		
		public boolean accept(File pF) {
			return pF.isFile();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.filechooser.FileFilter#getDescription()
		 */
		public String getDescription() {
			return "FileFilter";
		}
		
	}
	
	private final class FileAndDirFilter extends FileFilter {
		
		public boolean accept(File pF) {
			return pF.isFile() || pF.isDirectory();
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.filechooser.FileFilter#getDescription()
		 */
		public String getDescription() {
			return "FileAndDirFilter";
		}
		
	}
	
	private FreeMindFilenameFilter mFilter;

	/**
	 * @author foltin
	 * @date 27.02.2012
	 */
	private final class FreeMindFilenameFilter implements FilenameFilter {
		/**
		 * 
		 */
		private FileFilter mCustomFilter = new NullFilter();
		/**
		 * Filter for dirs, files or both.
		 */
		private FileFilter mPrincipalFilter = new NullFilter();

		/**
		 * @param pFilter
		 */
		private FreeMindFilenameFilter() {
		}

		public boolean accept(File pDir, String pName) {
			File file = new File(pDir, pName);
			return mPrincipalFilter.accept(file) && mCustomFilter.accept(file);
		}

		public FileFilter getCustomFilter() {
			return mCustomFilter;
		}

		public void setCustomFilter(FileFilter pFilter) {
			mCustomFilter = pFilter;
		}

		public FileFilter getPrincipalFilter() {
			return mPrincipalFilter;
		}

		public void setPrincipalFilter(FileFilter pPrincipalFilter) {
			mPrincipalFilter = pPrincipalFilter;
		}
	}

	/**
	 * 
	 */
	public FreeMindAwtFileDialog() {
		super((Frame) null);
		mFilter = new FreeMindFilenameFilter();
		super.setFilenameFilter(mFilter);
		System.setProperty("apple.awt.fileDialogForDirectories", "false");

	}
	
	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#showOpenDialog(java.awt.Component)
	 */
	public int showOpenDialog(Component pParent) throws HeadlessException {
		setMode(LOAD);
		show();
		return (getFile() == null)?JFileChooser.CANCEL_OPTION:JFileChooser.APPROVE_OPTION;
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#showSaveDialog(java.awt.Component)
	 */
	public int showSaveDialog(Component pParent) throws HeadlessException {
		setMode(SAVE);
		show();
		return (getFile() == null)?JFileChooser.CANCEL_OPTION:JFileChooser.APPROVE_OPTION;
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#setDialogTitle(java.lang.String)
	 */
	public void setDialogTitle(String pDialogTitle) {
		setTitle(pDialogTitle);
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#addChoosableFileFilter(javax.swing.filechooser.FileFilter)
	 */
	public void addChoosableFileFilter(FileFilter pFilter) {
		mFilter.setCustomFilter(pFilter);
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#setFileSelectionMode(int)
	 */
	public void setFileSelectionMode(int pMode) {
		switch(pMode) {
		case JFileChooser.DIRECTORIES_ONLY:
			mFilter.setPrincipalFilter(new DirFilter());
			System.setProperty("apple.awt.fileDialogForDirectories", "true");
			break;
		case JFileChooser.FILES_ONLY:
			mFilter.setPrincipalFilter(new FileOnlyFilter());
			break;
		case JFileChooser.FILES_AND_DIRECTORIES:
			mFilter.setPrincipalFilter(new FileAndDirFilter());
			break;
		default:
			mFilter.setPrincipalFilter(new NullFilter());
			break;
		}
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#setMultiSelectionEnabled(boolean)
	 */
	public void setMultiSelectionEnabled(boolean pB) {
		setMultipleMode(pB);
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#isMultiSelectionEnabled()
	 */
	public boolean isMultiSelectionEnabled() {
		return isMultipleMode();
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#getSelectedFiles()
	 */
	public File[] getSelectedFiles() {
		return getFiles();
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#getSelectedFile()
	 */
	public File getSelectedFile() {
		File[] files = getFiles();
		if(files.length > 0)
			return files[0];
		return null;
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#setCurrentDirectory(java.io.File)
	 */
	public void setCurrentDirectory(File pLastCurrentDir) {
		super.setDirectory(pLastCurrentDir.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see freemind.modes.FreeMindFileDialog#setSelectedFile(java.io.File)
	 */
	public void setSelectedFile(File pFile) {
		super.setFile(pFile.getAbsolutePath());
	}

}