package pt.rxc.common.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.MapUtils;

/**
 * Test class for {@link MapUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class MapUtilsTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testAddMapValues() {
    Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

    try {
      MapUtils.addValue(null, "elem1", 1);
    } catch (Exception e) {
      Assert.assertTrue(false);
    }

    MapUtils.addValue(map, "elem1", 1);
    MapUtils.addValue(map, "elem1", 2);
    MapUtils.addValue(map, "elem1", 3);
    Assert.assertEquals(3, map.get("elem1").size());

    MapUtils.addValue(map, "elem1", 1);
    MapUtils.addValue(map, "elem1", 2);
    MapUtils.addValue(map, "elem1", 3);
    Assert.assertEquals(6, map.get("elem1").size());

    MapUtils.addValue(map, "elem2", 1);
    MapUtils.addValue(map, "elem2", 2);
    MapUtils.addValue(map, "elem2", 3);
    Assert.assertEquals(3, map.get("elem2").size());

    MapUtils.addValue(map, "elem2", 1);
    MapUtils.addValue(map, "elem2", 2);
    MapUtils.addValue(map, "elem2", 3);
    Assert.assertEquals(6, map.get("elem2").size());
  }

  @Test
  public void testGetValue() {
    Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
    map.put(1, Arrays.asList(1, 2, 3));
    map.put(2, Arrays.asList(4, 5, 6));
    map.put(3, Arrays.asList(null, 5, 6));

    Assert.assertEquals((Integer) 1, MapUtils.getValue(1, map));
    Assert.assertEquals((Integer) 4, MapUtils.getValue(2, map));
    Assert.assertEquals(null, MapUtils.getValue(3, map));
    Assert.assertEquals(null, MapUtils.getValue(3, null));
  }

  @Test
  public void testGetKey() {
    Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
    map.put(1, Arrays.asList(1, 2, 3));
    map.put(2, Arrays.asList(3, 4, 5));
    map.put(2, Arrays.asList(3, 4, null));

    Map<Integer, List<Integer>> emptyMap = new HashMap<Integer, List<Integer>>();
    emptyMap.put(1, null);

    Assert.assertEquals((Integer) 1, MapUtils.getKey(map, 1));
    Assert.assertEquals((Integer) 1, MapUtils.getKey(map, 2));
    Assert.assertEquals((Integer) 2, MapUtils.getKey(map, 3));
    Assert.assertEquals((Integer) 2, MapUtils.getKey(map, 4));
    Assert.assertEquals((Integer) 2, MapUtils.getKey(map, null));

    Assert.assertEquals(null, MapUtils.getKey(null, null));
    Assert.assertEquals(null, MapUtils.getKey(emptyMap, 1));
  }
}
