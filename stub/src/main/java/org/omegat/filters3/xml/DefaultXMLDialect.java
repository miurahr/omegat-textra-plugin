/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2008 Martin Fleurke
               2009 Didier Briel
               2010 Antonio Vilei
               2011 Didier Briel
               2013 Alex Buloichik
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.omegat.core.data.ProtectedPart;
import org.omegat.filters3.Attributes;
import org.omegat.filters3.Element;
import org.omegat.filters3.Tag;
import org.omegat.util.MultiMap;
import org.xml.sax.InputSource;

/**
 * This class is a stub to allow compilation.
 * It is based on the original OmegaT class.
 * It is not distributed.
 */
public class DefaultXMLDialect implements XMLDialect {

    /** Defines paragraph tag. Allows duplicates. */
    public void defineParagraphTag(String tag) {
    }

    /** Defines a set of paragraph tags from an array. Allows duplicates. */
    public void defineParagraphTags(String[] tags) {
    }

    public void defineContentBasedTag(String tag, Tag.Type type) {
    }

    /** Defines preformat tag. Allows duplicates. */
    public void definePreformatTag(String tag) {
    }

    /** Defines a set of preformat tags from an array. Allows duplicates. */
    public void definePreformatTags(String[] tags) {
    }

    /** Defines intact tag. Allows duplicates. */
    public void defineIntactTag(String tag) {
    }

    /** Defines a set of intact tags from an array. Allows duplicates. */
    public void defineIntactTags(String[] tags) {
    }

    /** Defines translatable attribute of a tag. */
    public void defineTranslatableTagAttribute(String tag, String attribute) {
    }

    /** Defines translatable attributes of a tag. */
    public void defineTranslatableTagAttributes(String tag, String[] attributes) {
    }

    /** Defines translatable attribute of several tags. */
    public void defineTranslatableTagsAttribute(String[] tags, String attribute) {
    }

    /**
     * Defines always translatable attribute (no matter what tag it belongs to).
     */
    public void defineTranslatableAttribute(String attribute) {
    }

    /**
     * Defines always translatable attributes (no matter what tag it belongs
     * to).
     */
    public void defineTranslatableAttributes(String[] attributes) {
    }

    /**
     * Defines out of turn tag. Such tag surrounds chunk of text that should be
     * translated separately, not breaking currently collected text.
     */
    public void defineOutOfTurnTag(String tag) {
    }

    /**
     * Defines out of turn tags. Such tags surround chunks of text that should
     * be translated separately, not breaking currently collected text.
     */
    public void defineOutOfTurnTags(String[] tags) {
    }

    Map<Integer, Pattern> constraints = new HashMap<Integer, Pattern>();

    /**
     * Defines a constraint to restrict supported subset of XML files. There can
     * be only one constraint of each type.
     * 
     * @param constraintType
     *            Type of constraint, see CONSTRAINT_... constants.
     * @param template
     *            Regular expression for a specified constrained string.
     */
    public void defineConstraint(Integer constraintType, Pattern template) {
    }

    Map<String, String> shortcuts = new HashMap<String, String>();

    /**
     * Defines a shortcut for a tag, useful for formatting tags. Shortcut is a
     * short form of a tag visible to translator, and stored in OmegaT's flavor
     * of TMX files.
     * 
     * @param tag
     *            Tag name.
     * @param shortcut
     *            The shortcut for a tag.
     */
    public void defineShortcut(String tag, String shortcut) {
    }

    /**
     * Defines shortcuts for formatting tags. An alternative to calling
     * {@link #defineShortcut(String,String)} multiple times.
     * 
     * @param mappings
     *            Array of strings, where even elements (0th, 2nd, etc) are
     *            tags, and odd elements are their corresponding shortcuts.
     */
    public void defineShortcuts(String[] mappings) {
    }

    // /////////////////////////////////////////////////////////////////////////
    // XMLDialect Interface Implementation
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Returns the set of defined paragraph tags.
     * <p>
     * Each entry in a set should be a String class.
     */
    @Override
    public Set<String> getParagraphTags() {
        return null;
    }

    /**
     * Returns the set of content based tags.
     */
    @Override
    public Map<String, Tag.Type> getContentBasedTags() {
        return null;
    }

    /**
     * Returns the set of tags that surround preformatted text.
     * <p>
     * Each entry in a set should be a String class.
     */
    @Override
    public Set<String> getPreformatTags() {
        return null;
    }

    /**
     * Returns the set of tags that surround intact portions of document, that
     * should not be translated at all.
     * <p>
     * Each entry in a set should be a String class.
     */
    @Override
    public Set<String> getIntactTags() {
        return null;
    }

    /**
     * Returns the multimap of translatable attributes of each tag.
     * <p>
     * Each entry should map from a String to a set of Strings.
     */
    @Override
    public MultiMap<String, String> getTranslatableTagAttributes() {
        return null;
    }

    /**
     * Returns for a given attribute of a given tag if the attribute should be
     * translated with the given other attributes present. If the tagAttribute
     * is returned by getTranslatable(Tag)Attributes(), this function is called
     * to further test the attribute within its context. This allows for example
     * the XHTML filter to not translate the value attribute of an
     * input-element, except when it is a button or submit or reset.
     */
    @Override
    public Boolean validateTranslatableTagAttribute(String tag, String attribute, Attributes atts) {
        return true;
    }

    /**
     * For a given tag, return wether the content of this tag should be
     * translated, depending on the content of one attribute and the presence or
     * absence of other attributes. For instance, in the ResX filter, tags
     * should not be translated when they contain the attribute "type", or when
     * the attribute "name" starts with "&amp;gt";
     * 
     * @param tag
     *            The tag that could be translated
     * @param atts
     *            The list of the tag attributes
     * @return <code>true</code> or <code>false</code>
     */
    @Override
    public Boolean validateIntactTag(String tag, Attributes atts) {
        return false;
    }

    @Override
    public Boolean validateContentBasedTag(String tag, Attributes atts) {
        return false;
    }

    /**
     * For a given tag, return wether the content of this tag should be
     * translated, depending on the content of one attribute and the presence or
     * absence of other attributes. For instance, in the Typo3 filter, tags
     * should be translated when the attribute locazible="1". Contrary to
     * validateIntactTag, this applies only to the current tag, and the tags
     * contained in it are not affected.
     * 
     * @param tag
     *            The tag that could be translated
     * @param atts
     *            The list of the tag attributes
     * @return <code>true</code> or <code>false</code>
     */
    @Override
    public Boolean validateTranslatableTag(String tag, Attributes atts) {
        return true;
    }

    /**
     * For a given tag, return wether the content of this tag is a paragraph
     * tag, depending on the content of one attribute (and/or the presence or
     * absence of other attributes). For instance, in the XLIFF filter, the
     * &lt;mark&gt; tag should start a new paragraph when the attribute "mtype"
     * contains "seg".
     * 
     * @param tag
     *            The tag that could be a paragraph tag
     * @param atts
     *            The list of the tag attributes
     * @return <code>true</code> or <code>false</code>
     */
    @Override
    public Boolean validateParagraphTag(String tag, Attributes atts) {
        return false;
    }

    /**
     * For a given tag, return wether the content of this tag is a preformat
     * tag, depending on the content of one attribute (and/or the presence or
     * absence of other attributes). For instance, in the XLIFF filter, the
     * &lt;mark&gt; tag should be a preformat tag when the attribute "mtype"
     * contains "seg".
     * 
     * @param tag
     *            The tag that could be a preformat tag
     * @param atts
     *            The list of the tag attributes
     * @return <code>true</code> or <code>false</code>
     */
    @Override
    public Boolean validatePreformatTag(String tag, Attributes atts) {
        return false;
    }

    /**
     * Returns the set of translatable attributes (no matter what tag they
     * belong to).
     * <p>
     * Each entry in a set should be a String class.
     */
    @Override
    public Set<String> getTranslatableAttributes() {
        return null;
    }

    /**
     * Returns the set of "out-of-turn" tags. Such tags specify chunks of text
     * that should be translated separately, not breaking currently collected
     * text entry. For example, footnotes in OpenDocument.
     * <p>
     * Each entry in a set should be a String class.
     */
    @Override
    public Set<String> getOutOfTurnTags() {
        return null;
    }

    /**
     * Returns defined constraints to restrict supported subset of XML files.
     * There can be only one constraint of each type, see CONSTRAINT_...
     * constants.
     * <p>
     * Each entry should map an {@link Integer} to a {@link Pattern} -- regular
     * expression for a specified constrained string.
     */
    @Override
    public Map<Integer, Pattern> getConstraints() {
        return null;
    }

    /**
     * Resolves external entites if child filter needs it. Default
     * implementation returns <code>null</code>.
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return null;
    }

    /**
     * Returns the map of tags to their shortcuts.
     * <p>
     * Each entry should map a {@link String} to a {@link String} -- a tag to
     * its shortcut.
     */
    @Override
    public Map<String, String> getShortcuts() {
        return null;
    }

    /**
     * Sets closingTag to <code>true</code> or <code>false</code>
     * 
     * @param onOff
     *            The parameter setting wether closing tags should be used or
     *            not for empty tags.
     */
    @Override
    public void setClosingTagRequired(boolean onOff) {
    }

    /**
     * Gives the value of closingTag
     */
    @Override
    public Boolean getClosingTagRequired() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTagsAggregationEnabled(boolean onOff) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getTagsAggregationEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getForceSpacePreserving() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setForceSpacePreserving(boolean onOff) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String constructShortcuts(List<Element> elements, List<ProtectedPart> protectedParts) {
        return null;
    }
}
