package it.eng.pkgdiff.batch;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author Pasquale Paola - Engineering Ingegneria Informatica
 * 
 */
public class DownloadMavenDependency {

	/**
	 * Permette il download della dipendenza maven a partire dall'xml.
	 * 
	 * @param dep
	 *            La dipendenza maven ad esempio:
	 *            <p>
	 *            <code>&lt;dependency&gt;<br>&lt;groupId&gt;javax.servlet&lt;/groupId&gt;<br>&lt;artifactId&gt;servlet-api&lt;/artifactId&gt;<br>&lt;version&gt;2.5&lt;/version&gt;<br>&lt;/dependency&gt;</code>
	 *            </p>
	 * @param downloadedPath
	 *            Directory dove scariacare l'artefatto.
	 * @return Il path al file scaricato.
	 */
	public static String download(String dep,String downloadedPath) {
		
		InputStream is = new ByteArrayInputStream( dep.getBytes());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		if(Util.isEmpty(downloadedPath)||Util.isEmpty(dep))
			return null;
		String groupId=null;
		String artifactId=null;
		String version=null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			groupId = doc.getElementsByTagName("groupId").item(0)
					.getTextContent();
			artifactId = doc.getElementsByTagName("artifactId").item(0)
					.getTextContent();
			version = doc.getElementsByTagName("version").item(0)
					.getTextContent();
			is.close();
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return download(groupId, artifactId, version, downloadedPath);	 
	}

	/**
	 * Permette il download della dipendenza maven dichiarandala esplicitamente.
	 * 
	 * @param groupId
	 *            groupId di maven.
	 * @param artifactId
	 *            artifactId di maven.
	 * @param version
	 *            version di maven.
	 * @param downloadedPath
	 *            Directory dove scariacare l'artefatto.
	 * @return Il path al file scaricato.
	 */
	public static String download(String groupId, String artifactId,
			String version,String downloadedPath) {
		String myFile = downloadedPath+File.separator+artifactId+"-"+version+".jar";
		String path = null;
		String[] repositories = null;

		path = groupId.replace(".", "/")+"/"+artifactId+"/"+version+"/"+ artifactId + "-" + version + ".jar";
		
		String value = Util.getResourceProperties().getProperty("maven.repos");
		repositories = value.split(",");
		int failed = 0;
		
		for (String repo : repositories) {
			URL website;
			try {
				website = new URL(repo + "/" + path);
				System.out.println("Try downloading " + repo + "/" + path);
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());				
				FileOutputStream fos = new FileOutputStream(myFile);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
				rbc.close();
				System.out.println("Downladed!");
				break;
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
				System.err.println("Downlad failed!");
				failed++;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.err.println("Downlad failed!");
				failed++;
			}
		}
		if (failed == repositories.length)
		  return null;
		return myFile;		
	}

}
