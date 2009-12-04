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
package org.opends.sdk.schema;



import static com.sun.opends.sdk.util.Messages.WARN_ATTR_SYNTAX_ILLEGAL_INTEGER;

import com.sun.opends.sdk.util.Message;
import org.opends.sdk.DecodeException;
import org.opends.sdk.util.ByteSequence;
import org.opends.sdk.util.ByteString;
import org.opends.sdk.util.StaticUtils;



/**
 * This class defines the integerMatch matching rule defined in X.520
 * and referenced in RFC 2252.
 */
final class IntegerEqualityMatchingRuleImpl extends
    AbstractMatchingRuleImpl
{

  public ByteString normalizeAttributeValue(Schema schema,
      ByteSequence value) throws DecodeException
  {
    try
    {
      return ByteString.valueOf(Integer.parseInt(value.toString()));
    }
    catch (final Exception e)
    {
      StaticUtils.DEBUG_LOG.throwing("IntegerEqualityMatchingRule",
          "normalizeAttributeValue", e);

      final Message message =
          WARN_ATTR_SYNTAX_ILLEGAL_INTEGER.get(value.toString());
      throw DecodeException.error(message);
    }
  }
}