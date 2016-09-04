/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2007-2008 Didier Briel
               2013 Didier Briel, Alex Buloichik
               2015 Aaron Madlon-Kay
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.filters3.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.omegat.core.data.ProtectedPart;
import org.omegat.filters2.AbstractFilter;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.TranslationException;
import org.omegat.util.Language;
import org.xml.sax.Attributes;


/**
 * stub only
 */
public abstract class XMLFilter extends AbstractFilter implements Translator {

    public XMLFilter(XMLDialect dialect) {
    }

    public XMLDialect getDialect() {
        return null;
    }

    @Override
    public BufferedReader createReader(File inFile, String inEncoding) throws UnsupportedEncodingException,
            IOException {
        return null;
    }

    @Override
    public BufferedWriter createWriter(File outFile, String outEncoding) throws UnsupportedEncodingException,
            IOException {
        return null; 
    }

    @Override
    public Language getTargetLanguage() {
        return null;
    }

    @Override
    public void processFile(File inFile, File outFile, FilterContext fc) throws IOException,
            TranslationException {
    }

    @Override
    protected void processFile(BufferedReader inFile, BufferedWriter outFile, FilterContext fc) throws IOException,
            TranslationException {
    }

    @Override
    public boolean isSourceEncodingVariable() {
        return false;
    }

    @Override
    public boolean isTargetEncodingVariable() {
        return true;
    }

    @Override
    public String translate(String entry, List<ProtectedPart> protectedParts) {
        return ""; 
    }

    @Override
    public boolean isFileSupported(BufferedReader reader) {
        return true;
    }
    
    @Override
    public void tagStart(String path, Attributes atts) {
    }
    
    @Override
    public void tagEnd(String path) {
    }

    @Override
    public void comment(String comment) {
    }

    @Override
    public void text(String text) {
    }

    @Override
    public boolean isInIgnored() {
        return false;
    }
}
