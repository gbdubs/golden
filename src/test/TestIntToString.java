package test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import utility.Namer;

public class TestIntToString {

	@Test
	public void testSingleVar(){
		for(int i = 1; i <= 26; i++){
			assertEquals("" + (char) (i + 64), Namer.intToString(i));
		}
	}
	
	@Test
	public void testBasicDouble(){
		for(int i = 27; i <= 52; i++){
			String expectation = "A" + (char) (i + 65 - 27);
			assertEquals(expectation, Namer.intToString(i));
		}
	}
	
	@Test
	public void testBasicTriple(){
		for(int i = 26 * 28 + 1; i <= 26 * 29; i++){	
			String expectation = "AB" + (char) (i + 64 - 26 * 28);
			assertEquals(expectation, Namer.intToString(i));
		}
	}
	
	@Test
	public void testExcusivity(){
		Set<String> result = new HashSet<String>();
		for(int i = 1; i < 100000; i++){
			result.add(Namer.intToString(i));
			assertEquals(i, result.size());
		}
		
	}
}
