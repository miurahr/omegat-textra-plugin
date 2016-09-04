/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2010 Alex Buloichik
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 **************************************************************************/

package org.omegat.filters2;

import org.omegat.core.data.ProjectProperties;
import org.omegat.util.Language;

/**
 * This class is a stub to allow compilation.
 * It is based on the original OmagT class.
 * It is not distributed.
 */
public class FilterContext {

	private Language srcLang = new Language("en-us");
	private Language trgLang = new Language("fr-fr");
	private ProjectProperties pp = new ProjectProperties(); 
	
    public Language getSourceLang() {
        return srcLang;
    }

    public Language getTargetLang() {
    	return trgLang;
    }

    public String getInEncoding() {
    	return null;
    }

    public void setInEncoding(String inEncoding) {
    }

    public String getOutEncoding() {
    	return null;
    }

    public void setOutEncoding(String outEncoding) {
    }

    public boolean isSentenceSegmentingEnabled() {
        return false;
    }
    
    public ProjectProperties getProjectProperties () {
    	return pp;
    }

}
