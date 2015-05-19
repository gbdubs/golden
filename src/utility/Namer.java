package utility;

public class Namer {

	public static String intToString(int i){
		int order = 0;
		int powerToSub = 1;
		int temp = i;
		do{
			order++;
			i -= powerToSub;
			powerToSub *= 26;
			temp -= powerToSub;
		} while (temp > 0);
		String result = "";
		while (result.length() < order){
			result = (char) (i % 26 + 65) + result;
			i /= 26;
		}
		return result;
	}

}
