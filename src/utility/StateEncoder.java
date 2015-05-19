package utility;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateEncoder {

	private Map<String, Integer> maxValues;
	private Map<String, BigInteger> multiplicationMasks;
	public Set<String> definedOn;
	
	private static Map<Integer, String> propertyOrdering;
	
	static {
		propertyOrdering = new HashMap<Integer, String>();
		propertyOrdering.put(1, "depth");
		propertyOrdering.put(2, "inDegree");
		propertyOrdering.put(3, "sideDegree");
		propertyOrdering.put(4, "outDegree");
		propertyOrdering.put(5, "height");
		propertyOrdering.put(6, "downWeight");
		propertyOrdering.put(7, "upWeight");
	}
	
	public StateEncoder(int maxDepth, int maxInDegree, int maxSideDegree, int maxOutDegree,  int maxHeight,  int maxDownWeight, int maxUpWeight){
		maxValues = new HashMap<String, Integer>();
		multiplicationMasks = new HashMap<String, BigInteger>();
		definedOn = new HashSet<String>();
		definedOn.addAll(propertyOrdering.values());
		
		// The +1 allows us to encode between [0, N], both sides inclusive!
		maxValues.put("depth", maxDepth + 1);
		maxValues.put("inDegree", maxInDegree + 1);
		maxValues.put("sideDegree", maxSideDegree + 1);
		maxValues.put("outDegree", maxOutDegree + 1);
		maxValues.put("height", maxHeight + 1);
		maxValues.put("downWeight", maxDownWeight + 1);
		maxValues.put("upWeight", maxUpWeight + 1);
		
		BigInteger multiplicationMask = BigInteger.ONE;
		for (int i = maxValues.size(); i > 0; i--){
			multiplicationMasks.put(propertyOrdering.get(i), multiplicationMask);
			Long nextMaxValue = new Long(maxValues.get(propertyOrdering.get(i)));
			multiplicationMask = multiplicationMask.multiply(BigInteger.valueOf(nextMaxValue));
		}		
	}
	
	public BigInteger encode(int depth, int iDeg, int sDeg, int oDeg, int height, int dWeight, int uWeight){
		BigInteger result = BigInteger.ZERO;
		result = result.add(multiplicationMasks.get("depth").multiply(BigInteger.valueOf(new Long(depth))));
		result = result.add(multiplicationMasks.get("inDegree").multiply(BigInteger.valueOf(new Long(iDeg))));
		result = result.add(multiplicationMasks.get("sideDegree").multiply(BigInteger.valueOf(new Long(sDeg))));
		result = result.add(multiplicationMasks.get("outDegree").multiply(BigInteger.valueOf(new Long(oDeg))));
		result = result.add(multiplicationMasks.get("height").multiply(BigInteger.valueOf(new Long(height))));
		result = result.add(multiplicationMasks.get("downWeight").multiply(BigInteger.valueOf(new Long(dWeight))));
		result = result.add(multiplicationMasks.get("upWeight").multiply(BigInteger.valueOf(new Long(uWeight))));
		return result;
	}
	
	public int decode(BigInteger b, String property){
		BigInteger toMod = BigInteger.valueOf(new Long(maxValues.get(property)));
		BigInteger toDivide = multiplicationMasks.get(property);
		return ((b.divide(toDivide)).mod(toMod)).intValue();
	}
}