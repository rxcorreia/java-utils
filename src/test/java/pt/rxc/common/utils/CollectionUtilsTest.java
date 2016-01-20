package pt.rxc.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.CollectionUtils;

/**
 * Test class for {@link CollectionUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class CollectionUtilsTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testToString() {
    Assert.assertEquals("1, 2, 3, 4, 5, 6, 7, 8", CollectionUtils.toString(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8)));
    Assert.assertEquals("", CollectionUtils.toString(null));
    Assert.assertEquals("1", CollectionUtils.toString(Arrays.asList(1)));
    Assert.assertEquals("1", CollectionUtils.toString(Arrays.asList("1")));
    Assert.assertEquals("1, 2, 3, 4, 5, 6", CollectionUtils.toString(Arrays.asList("1", "2", "3", "4", "5", "6")));
    Assert.assertEquals("true, false", CollectionUtils.toString(Arrays.asList(true, false)));
    Assert.assertEquals("1.0, 2.0", CollectionUtils.toString(Arrays.asList(1.0, 2.0)));
  }

  @Test
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void testSet() {
    Set nullSet = null;
    Assert.assertEquals(true, CollectionUtils.isEmpty(nullSet));
    Assert.assertEquals(false, CollectionUtils.isNotEmpty(nullSet));

    Set emptySet = new HashSet<>();
    Assert.assertEquals(true, CollectionUtils.isEmpty(emptySet));
    Assert.assertEquals(false, CollectionUtils.isNotEmpty(emptySet));

    Set fullSet = new HashSet<>();
    fullSet.add(new Object());
    Assert.assertEquals(false, CollectionUtils.isEmpty(fullSet));
    Assert.assertEquals(true, CollectionUtils.isNotEmpty(fullSet));
  }

  @Test
  public void testCleanListDuplicates() {
    List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    List<Integer> list2 = Arrays.asList(7, 8, 9, 10, 11, 12, 13);
    List<Integer> list3 = Arrays.asList(1, 2, 3, 4, 5, 6);

    Assert.assertEquals(list3, CollectionUtils.cleanListDuplicates(list1, list2));
    Assert.assertEquals(null, CollectionUtils.cleanListDuplicates(null, list2));
    Assert.assertEquals(list1, CollectionUtils.cleanListDuplicates(list1, null));
    Assert.assertEquals(null, CollectionUtils.cleanListDuplicates(null, null));
    Assert.assertEquals(new ArrayList<>(), CollectionUtils.cleanListDuplicates(list1, list1));
  }

  @Test
  public void testPartition() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    Assert.assertEquals(Arrays.asList(), CollectionUtils.partition(null, 1));
    Assert.assertEquals(0, CollectionUtils.partition(null, 1).size());

    Assert.assertEquals(10, CollectionUtils.partition(list, 1).size());
    Assert.assertEquals(5, CollectionUtils.partition(list, 2).size());
    Assert.assertEquals(4, CollectionUtils.partition(list, 3).size());
    Assert.assertEquals(2, CollectionUtils.partition(list, 5).size());
    Assert.assertEquals(2, CollectionUtils.partition(list, 7).size());

    Assert.assertEquals(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9), Arrays.asList(10)),
        CollectionUtils.partition(list, 3));
  }

  @Test
  public void testContains() {
    List<String> list = Arrays.asList("ABC", "DEF", "GHI", null);

    Assert.assertTrue(CollectionUtils.contains(list, "ABC"));
    Assert.assertTrue(CollectionUtils.contains(list, "DEF"));
    Assert.assertFalse(CollectionUtils.contains(list, "abc"));
    Assert.assertFalse(CollectionUtils.contains(list, ""));
    Assert.assertTrue(CollectionUtils.contains(list, null));
    Assert.assertFalse(CollectionUtils.contains(null, null));
    Assert.assertFalse(CollectionUtils.contains(null, "ABC"));

    Assert.assertTrue(CollectionUtils.containsIgnoreCase(list, "ABC"));
    Assert.assertTrue(CollectionUtils.containsIgnoreCase(list, "DEF"));
    Assert.assertTrue(CollectionUtils.containsIgnoreCase(list, "abc"));
    Assert.assertFalse(CollectionUtils.containsIgnoreCase(list, ""));
    Assert.assertTrue(CollectionUtils.containsIgnoreCase(list, null));
    Assert.assertFalse(CollectionUtils.containsIgnoreCase(null, null));
    Assert.assertFalse(CollectionUtils.containsIgnoreCase(null, "ABC"));
  }

  @Test
  public void testIsEmpty() {
    List<String> list = Arrays.asList("ABC", "DEF", "GHI", null);
    List<String> emptyList = new ArrayList<String>();
    List<String> nullList = null;
    String[] array = new String[] { "ABC", "DEF" };
    String[] emptyArray = new String[] {};
    String[] nullArray = null;

    // LISTS
    Assert.assertFalse(CollectionUtils.isEmpty(list));
    Assert.assertTrue(CollectionUtils.isNotEmpty(list));
    Assert.assertTrue(CollectionUtils.isEmpty(nullList));
    Assert.assertTrue(CollectionUtils.isEmpty(emptyList));
    Assert.assertFalse(CollectionUtils.isNotEmpty(nullList));
    Assert.assertFalse(CollectionUtils.isNotEmpty(emptyList));

    // ARRAYS
    Assert.assertFalse(CollectionUtils.isNotEmpty(nullArray));
    Assert.assertFalse(CollectionUtils.isNotEmpty(emptyArray));
    Assert.assertTrue(CollectionUtils.isEmpty(nullArray));
    Assert.assertTrue(CollectionUtils.isEmpty(emptyArray));

    Assert.assertTrue(CollectionUtils.isNotEmpty(array));
    Assert.assertFalse(CollectionUtils.isEmpty(array));
  }
}
