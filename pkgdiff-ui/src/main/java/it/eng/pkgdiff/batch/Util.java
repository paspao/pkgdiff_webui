package it.eng.pkgdiff.batch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {

	private static Properties prop;

	public static boolean isEmpty(String str) {
		boolean result = true;
		if (str == null)
			;
		else {
			str = str.trim();
			if (str.length() > 0)
				result = false;
		}

		return result;
	}

	public static final Properties getResourceProperties() {
		if (prop == null) {
			synchronized (Util.class) {
				try {
					prop = new Properties();
					InputStream is = Util.class.getClassLoader().getResourceAsStream("./resources.properties");
					prop.load(is);
					is.close();

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		}
		return prop;
	}
	
	public static void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	 
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " 
	                                                 + file.getAbsolutePath());
	 
	    		}else{
	 
	    		   //list all the directory contents
	        	   String files[] = file.list();
	 
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	 
	        	      //recursive delete
	        	     delete(fileDelete);
	        	   }
	 
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	        	     System.out.println("Directory is deleted : " 
	                                                  + file.getAbsolutePath());
	        	   }
	    		}
	 
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	    }

}
