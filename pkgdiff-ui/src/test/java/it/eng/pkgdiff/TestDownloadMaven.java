package it.eng.pkgdiff;

import java.io.File;

import it.eng.pkgdiff.batch.DownloadMavenDependency;

import org.junit.Assert;
import org.junit.Test;

public class TestDownloadMaven {

	@Test
	public void testForDepInXml(){
		String str="<dependency><groupId>javax.faces</groupId><artifactId>javax.faces-api</artifactId><version>2.1</version></dependency>";
		DownloadMavenDependency.download(str,"target");
		File f=new File("target"+File.separator+"javax.faces-api-2.1.jar");
		if(f.exists())
			Assert.assertTrue(true);
		else
			Assert.assertTrue(false);
	}
	@Test
	public void testForDep(){
		
		DownloadMavenDependency.download("javax.servlet","servlet-api","2.5","target");
		File f=new File("target"+File.separator+"servlet-api-2.5.jar");
		if(f.exists())
			Assert.assertTrue(true);
		else
			Assert.assertTrue(false);
	}
}
