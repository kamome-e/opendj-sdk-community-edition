/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2009 Sun Microsystems, Inc.
 *      Portions copyright 2011 ForgeRock AS
 */

package org.forgerock.opendj.ldap.schema;

import static org.forgerock.opendj.ldap.CoreMessages.*;
import static org.forgerock.opendj.ldap.schema.SchemaConstants.EMR_OID_FIRST_COMPONENT_OID;
import static org.forgerock.opendj.ldap.schema.SchemaConstants.SYNTAX_MATCHING_RULE_NAME;

import org.forgerock.i18n.LocalizableMessage;
import org.forgerock.i18n.LocalizableMessageBuilder;
import org.forgerock.opendj.ldap.ByteSequence;
import org.forgerock.opendj.ldap.DecodeException;

import com.forgerock.opendj.util.StaticUtils;
import com.forgerock.opendj.util.SubstringReader;

/**
 * This class implements the matching rule description syntax, which is used to
 * hold matching rule definitions in the server schema. The format of this
 * syntax is defined in RFC 2252.
 */
final class MatchingRuleSyntaxImpl extends AbstractSyntaxImpl {

    @Override
    public String getEqualityMatchingRule() {
        return EMR_OID_FIRST_COMPONENT_OID;
    }

    public String getName() {
        return SYNTAX_MATCHING_RULE_NAME;
    }

    public boolean isHumanReadable() {
        return true;
    }

    /**
     * Indicates whether the provided value is acceptable for use in an
     * attribute with this syntax. If it is not, then the reason may be appended
     * to the provided buffer.
     *
     * @param schema
     *            The schema in which this syntax is defined.
     * @param value
     *            The value for which to make the determination.
     * @param invalidReason
     *            The buffer to which the invalid reason should be appended.
     * @return <CODE>true</CODE> if the provided value is acceptable for use
     *         with this syntax, or <CODE>false</CODE> if not.
     */
    public boolean valueIsAcceptable(final Schema schema, final ByteSequence value,
            final LocalizableMessageBuilder invalidReason) {
        // We'll use the decodeMatchingRule method to determine if the value
        // is acceptable.
        final String definition = value.toString();
        try {
            final SubstringReader reader = new SubstringReader(definition);

            // We'll do this a character at a time. First, skip over any
            // leading whitespace.
            reader.skipWhitespaces();

            if (reader.remaining() <= 0) {
                // This means that the value was empty or contained only
                // whitespace. That is illegal.
                final LocalizableMessage message = ERR_ATTR_SYNTAX_MR_EMPTY_VALUE1.get(definition);
                final DecodeException e = DecodeException.error(message);
                StaticUtils.DEBUG_LOG.throwing("MatchingRuleSyntax", "valueIsAcceptable", e);
                throw e;
            }

            // The next character must be an open parenthesis. If it is not,
            // then that is an error.
            final char c = reader.read();
            if (c != '(') {
                final LocalizableMessage message =
                        ERR_ATTR_SYNTAX_MR_EXPECTED_OPEN_PARENTHESIS.get(definition,
                                (reader.pos() - 1), String.valueOf(c));
                final DecodeException e = DecodeException.error(message);
                StaticUtils.DEBUG_LOG.throwing("MatchingRuleSyntax", "valueIsAcceptable", e);
                throw e;
            }

            // Skip over any spaces immediately following the opening
            // parenthesis.
            reader.skipWhitespaces();

            // The next set of characters must be the OID.
            SchemaUtils.readOID(reader, schema.allowMalformedNamesAndOptions());
            String syntax = null;

            // At this point, we should have a pretty specific syntax that
            // describes what may come next, but some of the components are
            // optional and it would be pretty easy to put something in the
            // wrong order, so we will be very flexible about what we can
            // accept. Just look at the next token, figure out what it is and
            // how to treat what comes after it, then repeat until we get to
            // the end of the value. But before we start, set default values
            // for everything else we might need to know.
            while (true) {
                final String tokenName = SchemaUtils.readTokenName(reader);

                if (tokenName == null) {
                    // No more tokens.
                    break;
                } else if (tokenName.equalsIgnoreCase("name")) {
                    SchemaUtils.readNameDescriptors(reader, schema.allowMalformedNamesAndOptions());
                } else if (tokenName.equalsIgnoreCase("desc")) {
                    // This specifies the description for the matching rule. It
                    // is
                    // an arbitrary string of characters enclosed in single
                    // quotes.
                    SchemaUtils.readQuotedString(reader);
                } else if (tokenName.equalsIgnoreCase("obsolete")) {
                    // This indicates whether the matching rule should be
                    // considered obsolete. We do not need to do any more
                    // parsing
                    // for this token.
                } else if (tokenName.equalsIgnoreCase("syntax")) {
                    syntax = SchemaUtils.readOID(reader, schema.allowMalformedNamesAndOptions());
                } else if (tokenName.matches("^X-[A-Za-z_-]+$")) {
                    // This must be a non-standard property and it must be
                    // followed by either a single definition in single quotes
                    // or
                    // an open parenthesis followed by one or more values in
                    // single quotes separated by spaces followed by a close
                    // parenthesis.
                    SchemaUtils.readExtensions(reader);
                } else {
                    final LocalizableMessage message =
                            ERR_ATTR_SYNTAX_MR_ILLEGAL_TOKEN1.get(definition, tokenName);
                    final DecodeException e = DecodeException.error(message);
                    StaticUtils.DEBUG_LOG.throwing("MatchingRuleSyntax", "valueIsAcceptable", e);
                    throw e;
                }
            }

            // Make sure that a syntax was specified.
            if (syntax == null) {
                final LocalizableMessage message = ERR_ATTR_SYNTAX_MR_NO_SYNTAX.get(definition);
                final DecodeException e = DecodeException.error(message);
                StaticUtils.DEBUG_LOG.throwing("MatchingRuleSyntax", "valueIsAcceptable", e);
                throw e;
            }
            return true;
        } catch (final DecodeException de) {
            invalidReason
                    .append(ERR_ATTR_SYNTAX_MR_INVALID1.get(definition, de.getMessageObject()));
            return false;
        }
    }
}
