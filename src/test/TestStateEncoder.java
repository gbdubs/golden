package test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;

import utility.StateEncoder;

public class TestStateEncoder {

	@Test
	public void simpleDecimalEncodingTest(){
		
		int var1 = 9;
		int var2 = 9;
		int var3 = 9;
		int var4 = 9;
		int var5 = 9;
		int var6 = 9;
		int var7 = 9;
		
		StateEncoder encoder = new StateEncoder(var1, var2, var3, var4, var5, var6, var7);
		
		BigInteger result = encoder.encode(1, 5, 0, 3, 2, 7, 8);
		BigInteger expected = BigInteger.valueOf(1503278L);
		assertEquals(expected, result);
	}
	
	@Test
	public void simpleBinaryEncodingTest(){
		
		int var1 = 1;
		int var2 = 1;
		int var3 = 1;
		int var4 = 1;
		int var5 = 1;
		int var6 = 1;
		int var7 = 1;
		
		StateEncoder encoder = new StateEncoder(var1, var2, var3, var4, var5, var6, var7);
		
		BigInteger result =       encoder.encode(1, 0, 0, 1, 1, 1, 0);
		BigInteger expected = BigInteger.valueOf(64+0 +0 +8 +4 +2 +0);
		assertEquals(expected, result);
	}
	
	
	@Test
	public void testStateEncoderIsOneToOne(){
		
		int var1 = 2;
		int var2 = 3;
		int var3 = 2;
		int var4 = 3;
		int var5 = 2;
		int var6 = 5;
		int var7 = 2;
		
		StateEncoder encoder = new StateEncoder(var1, var2, var3, var4, var5, var6, var7);
		
		List<BigInteger> results = new ArrayList<BigInteger>();
		for(int a = 0; a <= var1; a++){
			for(int b = 0; b <= var2; b++){
				for(int c = 0; c <= var3; c++){
					for(int d = 0; d <= var4; d++){
						for(int e = 0; e <= var5; e++){
							for(int f = 0; f <= var6; f++){
								for(int g = 0; g <= var7; g++){
									results.add(encoder.encode(a, b, c, d, e, f, g));
								}
							}
						}
					}
				}
			}
		}
		
		Collections.sort(results);
		int expected = (var1+1)*(var2+1)*(var3+1)*(var4+1)*(var5+1)*(var6+1)*(var7+1);
		assertEquals(expected, results.size());
		Set<BigInteger> testNonDupes = new HashSet<BigInteger>(results);
		assertEquals(expected, testNonDupes.size());
	}
	
	@Test
	public void testInvertable(){
		
		int var1 = 3;
		int var2 = 2;
		int var3 = 7;
		int var4 = 4;
		int var5 = 5;
		int var6 = 2;
		int var7 = 1;
		
		StateEncoder se = new StateEncoder(var1, var2, var3, var4, var5, var6, var7);
		
		int totalPossible = (var1+1)*(var2+1)*(var3+1)*(var4+1)*(var5+1)*(var6+1)*(var7+1);
		int toTest = Math.min(10000, totalPossible);
		
		for (int i = 0; i < toTest; i++){
			int v1 = (int) Math.floor(Math.random() * (var1+1));
			int v2 = (int) Math.floor(Math.random() * (var2+1));
			int v3 = (int) Math.floor(Math.random() * (var3+1));
			int v4 = (int) Math.floor(Math.random() * (var4+1));
			int v5 = (int) Math.floor(Math.random() * (var5+1));
			int v6 = (int) Math.floor(Math.random() * (var6+1));
			int v7 = (int) Math.floor(Math.random() * (var7+1));
			
			BigInteger result = se.encode(v1, v2, v3, v4, v5, v6, v7);
			
			int r1 = se.decode(result, "depth");
			int r2 = se.decode(result, "inDegree");
			int r3 = se.decode(result, "sideDegree");
			int r4 = se.decode(result, "outDegree");
			int r5 = se.decode(result, "height");
			int r6 = se.decode(result, "downWeight");
			int r7 = se.decode(result, "upWeight");
			
			assertEquals(v1, r1);
			assertEquals(v2, r2);
			assertEquals(v3, r3);
			assertEquals(v4, r4);
			assertEquals(v5, r5);
			assertEquals(v6, r6);
			assertEquals(v7, r7);
		}
		
	}
}
