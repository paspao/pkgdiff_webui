package it.eng.pkgdiff.batch;

import it.eng.pkgdiff.MainBean;

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

}
