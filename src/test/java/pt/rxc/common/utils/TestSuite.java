package pt.rxc.common.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * JUnit test suite for Java Utils project.
 * 
 * @author ruben.correia
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ BooleanUtilsTest.class, CollectionUtilsTest.class, DateUtilsTest.class, FileUtilsTest.class,
		MapUtilsTest.class, PropertiesUtilsTest.class, StringUtilsTest.class, OSUtilsTest.class })
public class TestSuite {

}
