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



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



/**
 * Abstract schema element tests.
 */
public abstract class AbstractSchemaElementTestCase extends SchemaTestCase
{
  /**
   * Empty schema element properties.
   */
  protected static final Map<String, List<String>> EMPTY_PROPS = Collections
      .emptyMap();

  /**
   * Empty names.
   */
  protected static final List<String> EMPTY_NAMES = Collections.emptyList();



  /**
   * Creates test data.
   *
   * @return The test data.
   * @throws Exception
   *           If an unexpected exception occurred.
   */
  @DataProvider(name = "equalsTestData")
  public abstract Object[][] createEqualsTestData() throws Exception;



  /**
   * Check that the equals operator works as expected.
   *
   * @param e1
   *          The first element
   * @param e2
   *          The second element
   * @param result
   *          The expected result.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "equalsTestData")
  public final void testEquals(final SchemaElement e1, final SchemaElement e2,
      final boolean result) throws Exception
  {

    Assert.assertEquals(e1.equals(e2), result);
    Assert.assertEquals(e2.equals(e1), result);
  }



  /**
   * Check that the {@code SchemaElement#getDescription()} method returns a
   * description.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public final void testGetDescription() throws Exception
  {
    final SchemaElement e = getElement("hello", EMPTY_PROPS);
    Assert.assertEquals(e.getDescription(), "hello");
  }



  /**
   * Check that the {@code SchemaElement#getDescription()} method returns
   * <code>null</code> when there is no description.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public final void testGetDescriptionDefault() throws Exception
  {
    final SchemaElement e = getElement("", EMPTY_PROPS);
    Assert.assertEquals(e.getDescription(), "");
  }



  /**
   * Check that the {@code SchemaElement#getExtraProperty(String)} method
   * returns values.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public final void testGetExtraProperty() throws Exception
  {
    final List<String> values = new ArrayList<String>();
    values.add("one");
    values.add("two");
    final Map<String, List<String>> props = Collections.singletonMap("test",
        values);
    final SchemaElement e = getElement("", props);

    int i = 0;
    for (final String value : e.getExtraProperty("test"))
    {
      Assert.assertEquals(value, values.get(i));
      i++;
    }
  }



  /**
   * Check that the {@code SchemaElement#getExtraProperty(String)} method
   * returns <code>null</code> when there is no property.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public final void testGetExtraPropertyDefault() throws Exception
  {
    final SchemaElement e = getElement("", EMPTY_PROPS);
    Assert.assertTrue(e.getExtraProperty("test").isEmpty());
  }



  /**
   * Check that the {@code SchemaElement#getExtraPropertyNames()} method.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public final void testGetExtraPropertyNames() throws Exception
  {
    final SchemaElement e = getElement("", EMPTY_PROPS);
    Assert.assertTrue(e.getExtraProperty("test").isEmpty());
  }



  /**
   * Check that the hasCode method operator works as expected.
   *
   * @param e1
   *          The first element
   * @param e2
   *          The second element
   * @param result
   *          The expected result.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "equalsTestData")
  public final void testHashCode(final SchemaElement e1,
      final SchemaElement e2, final boolean result) throws Exception
  {

    Assert.assertEquals(e1.hashCode() == e2.hashCode(), result);
  }



  /**
   * Creates a schema element.
   *
   * @param description
   * @param extraProperties
   * @return
   * @throws SchemaException
   */
  protected abstract SchemaElement getElement(String description,
      Map<String, List<String>> extraProperties) throws SchemaException;
}