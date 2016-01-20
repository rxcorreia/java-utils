package pt.rxc.common.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.BooleanUtils;

/**
 * Test class for {@link BooleanUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class BooleanUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEquals() {
		Assert.assertTrue(BooleanUtils.equals(null, null));
		Assert.assertTrue(BooleanUtils.equals(true, true));
		Assert.assertTrue(BooleanUtils.equals(false, false));

		Assert.assertFalse(BooleanUtils.equals(null, false));
		Assert.assertFalse(BooleanUtils.equals(null, true));
		Assert.assertFalse(BooleanUtils.equals(null, ""));
		Assert.assertFalse(BooleanUtils.equals(null, new Object()));
		Assert.assertFalse(BooleanUtils.equals(null, 1));
		Assert.assertFalse(BooleanUtils.equals(null, 0.0));

		Assert.assertFalse(BooleanUtils.equals(true, false));
		Assert.assertFalse(BooleanUtils.equals(true, null));
		Assert.assertFalse(BooleanUtils.equals(true, ""));
		Assert.assertFalse(BooleanUtils.equals(true, new Object()));
		Assert.assertFalse(BooleanUtils.equals(true, 1));
		Assert.assertFalse(BooleanUtils.equals(true, 0.0));

		Assert.assertFalse(BooleanUtils.equals(false, true));
		Assert.assertFalse(BooleanUtils.equals(false, null));
		Assert.assertFalse(BooleanUtils.equals(false, ""));
		Assert.assertFalse(BooleanUtils.equals(false, new Object()));
		Assert.assertFalse(BooleanUtils.equals(false, 1));
		Assert.assertFalse(BooleanUtils.equals(false, 0.0));
	}

	@Test
	public void testParse() {
		Assert.assertTrue(BooleanUtils.parse("yes"));
		Assert.assertTrue(BooleanUtils.parse("true"));
		Assert.assertTrue(BooleanUtils.parse("1"));
		Assert.assertTrue(BooleanUtils.parse("sim"));
		Assert.assertTrue(BooleanUtils.parse("s"));
		Assert.assertTrue(BooleanUtils.parse("y"));

		Assert.assertTrue(BooleanUtils.parse(1));

		Assert.assertFalse(BooleanUtils.parse("nao"));
		Assert.assertFalse(BooleanUtils.parse("no"));
		Assert.assertFalse(BooleanUtils.parse("false"));
		Assert.assertFalse(BooleanUtils.parse("xyz"));
		Assert.assertFalse(BooleanUtils.parse(""));

		Assert.assertFalse(BooleanUtils.parse(null));		
	}

	@Test
	public void testIntValue() {
		Assert.assertEquals((Integer) 1, BooleanUtils.intValue(true));
		Assert.assertEquals((Integer) 0, BooleanUtils.intValue(false));

		Assert.assertEquals(null, BooleanUtils.intValue(null));
	}
}
