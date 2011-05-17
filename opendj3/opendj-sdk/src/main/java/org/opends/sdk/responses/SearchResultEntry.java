/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2009 Sun Microsystems, Inc.
 */

package org.opends.sdk.responses;



import java.util.Collection;
import java.util.List;

import org.forgerock.i18n.LocalizedIllegalArgumentException;
import org.opends.sdk.*;
import org.opends.sdk.controls.Control;
import org.opends.sdk.controls.ControlDecoder;



/**
 * A Search Result Entry represents an entry found during a Search operation.
 * <p>
 * Each entry returned in a Search Result Entry will contain all appropriate
 * attributes as specified in the Search request, subject to access control and
 * other administrative policy.
 * <p>
 * Note that a Search Result Entry may hold zero attributes. This may happen
 * when none of the attributes of an entry were requested or could be returned.
 * <p>
 * Note also that each returned attribute may hold zero attribute values. This
 * may happen when only attribute types are requested, access controls prevent
 * the return of values, or other reasons.
 */
public interface SearchResultEntry extends Response, Entry
{
  /**
   * {@inheritDoc}
   */
  boolean addAttribute(Attribute attribute)
      throws UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  boolean addAttribute(Attribute attribute,
      Collection<ByteString> duplicateValues)
      throws UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  SearchResultEntry addAttribute(String attributeDescription, Object... values)
      throws LocalizedIllegalArgumentException, UnsupportedOperationException,
      NullPointerException;



  /**
   * {@inheritDoc}
   */
  SearchResultEntry addControl(Control control)
      throws UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  SearchResultEntry clearAttributes() throws UnsupportedOperationException;



  /**
   * {@inheritDoc}
   */
  boolean containsAttribute(Attribute attribute,
      Collection<ByteString> missingValues) throws NullPointerException;



  /**
   * {@inheritDoc}
   */
  boolean containsAttribute(String attributeDescription, Object... values)
      throws LocalizedIllegalArgumentException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  Iterable<Attribute> getAllAttributes();



  /**
   * {@inheritDoc}
   */
  Iterable<Attribute> getAllAttributes(AttributeDescription attributeDescription)
      throws NullPointerException;



  /**
   * {@inheritDoc}
   */
  Iterable<Attribute> getAllAttributes(String attributeDescription)
      throws LocalizedIllegalArgumentException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  Attribute getAttribute(AttributeDescription attributeDescription)
      throws NullPointerException;



  /**
   * {@inheritDoc}
   */
  Attribute getAttribute(String attributeDescription)
      throws LocalizedIllegalArgumentException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  int getAttributeCount();



  /**
   * {@inheritDoc}
   */
  <C extends Control> C getControl(ControlDecoder<C> decoder,
      DecodeOptions options) throws NullPointerException, DecodeException;



  /**
   * {@inheritDoc}
   */
  List<Control> getControls();



  /**
   * {@inheritDoc}
   */
  DN getName();



  /**
   * {@inheritDoc}
   */
  boolean removeAttribute(Attribute attribute,
      Collection<ByteString> missingValues)
      throws UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  boolean removeAttribute(AttributeDescription attributeDescription)
      throws UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  SearchResultEntry removeAttribute(String attributeDescription,
      Object... values) throws LocalizedIllegalArgumentException,
      UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  boolean replaceAttribute(Attribute attribute)
      throws UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  SearchResultEntry replaceAttribute(String attributeDescription,
      Object... values) throws LocalizedIllegalArgumentException,
      UnsupportedOperationException, NullPointerException;



  /**
   * {@inheritDoc}
   */
  SearchResultEntry setName(DN dn) throws UnsupportedOperationException,
      NullPointerException;



  /**
   * {@inheritDoc}
   */
  SearchResultEntry setName(String dn)
      throws LocalizedIllegalArgumentException, UnsupportedOperationException,
      NullPointerException;

}