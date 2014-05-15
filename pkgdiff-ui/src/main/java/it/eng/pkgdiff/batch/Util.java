package it.eng.pkgdiff.batch;

public class Util {

	public static boolean isEmpty(String str)
	{
		boolean result=true;
		if(str==null);
		else 
		{
			str=str.trim();
			if(str.length()>0)
				result=false;
		}
			
		return result;
	}
	
}
