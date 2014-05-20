package it.eng.pkgdiff;

import it.eng.pkgdiff.batch.DownloadMavenDependency;
import it.eng.pkgdiff.batch.ExecPkgDiff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

/**
 * MasterBean del progetto
 * 
 * @author Russo Pasquale
 * 
 */

@ManagedBean(name = MainBean.BEAN_NAME)
@ViewScoped
public class MainBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String BEAN_NAME = "mainBean";

	private List<String> fileData;
	private String fileName = "";
	private String myFileMaven = "";

	private String groupId;
	private String artifactId;
	private String versionId;

	private String absolutePath;
	private String uploadArtifactPath;
	private String tmpDirPath;
	private String reportDirPath;
	private String ulrReportDir;
	private String useSessionId = "true";
	private String responseConfronto;
	private String urlDettaglio = "";
	private String tipoFile = "byArtifact";
	String sessionId = "";
	String appSessionIdSubdir = "";

	public void setTipoFile(String tipoFile) {
		this.tipoFile = tipoFile;
	}

	public String getTipoFile() {
		return tipoFile;
	}

	public String getMyFileMaven() {
		String ret = (myFileMaven!=null)?(myFileMaven.substring(myFileMaven.lastIndexOf("/") + 1)):"";
		return ret;
	}

	private Properties properties;

	public MainBean() {
		System.out.println("MainBean");
		
		FacesContext fCtx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fCtx.getExternalContext()
				.getSession(false);
		sessionId = session.getId();
		String realPath =((ServletContext)fCtx.getExternalContext().getContext()).getRealPath(File.separator);
		
		if (useSessionId.equalsIgnoreCase("true")) {
			appSessionIdSubdir = "/" + sessionId;
		}
		
		fileData = new ArrayList<String>();
		try {
			properties = new Properties();
			InputStream resourceAsStream = MainBean.class.getClassLoader()
					.getResourceAsStream("resources.properties");
			if (resourceAsStream != null) {
				properties.load(resourceAsStream);
				absolutePath =realPath+File.separator + properties.getProperty("uploadPath");
				System.out.println("absolutePath:" + absolutePath);
				if (properties.getProperty("uploadArtifactPath") != null) {
					uploadArtifactPath = realPath+File.separator +properties
							.getProperty("uploadArtifactPath");
					System.out.println("set Defautl downloadedPath = "
							+ uploadArtifactPath);
				}
				if (properties.getProperty("tmpDirPath") != null) {
					tmpDirPath = realPath+File.separator +properties.getProperty("tmpDirPath");
					File ftmpDirPath = new File(tmpDirPath);
					ftmpDirPath.mkdir();
					ftmpDirPath = new File(tmpDirPath +  appSessionIdSubdir);
					ftmpDirPath.mkdir();
					System.out
							.println("set Defautl tmpDirPath = " + tmpDirPath + appSessionIdSubdir);
				}
				if (properties.getProperty("reportDirPath") != null) {
					reportDirPath =realPath+File.separator + properties.getProperty("reportDirPath");
					System.out.println("set Defautl reportDirPath = " + reportDirPath + appSessionIdSubdir);
					File freportDirPath = new File(reportDirPath);
					freportDirPath.mkdir();
					freportDirPath = new File(reportDirPath +  appSessionIdSubdir);
					freportDirPath.mkdir();
				}
				if (properties.getProperty("ulrReportDir") != null) {
					ulrReportDir = properties.getProperty("ulrReportDir");
					System.out.println("set Defautl ulrReportDir = "
							+ ulrReportDir);
				}
				if (properties.getProperty("userSessionId") != null) {
					useSessionId = properties.getProperty("userSessionId");
					System.out.println("set Defautl userSessionId = "
							+ useSessionId);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getVersion() {
		return "1.0";
	}

	public String getUrlDettaglio() {
		return urlDettaglio;
	}

	public void setUrlDettaglio(String urlDettaglio) {
		this.urlDettaglio = urlDettaglio;
	}

	public String getResponseConfronto() {
		return responseConfronto;
	}

	public void setResponseConfronto(String aResponseConfronto) {
		this.responseConfronto = aResponseConfronto;
	}

	public String getUseSessionId() {
		return useSessionId;
	}

	public void setUseSessionId(String anUseSessionId) {
		this.useSessionId = anUseSessionId;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String anAbsolutePath) {
		this.absolutePath = anAbsolutePath;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String aGroupId) {
		this.groupId = aGroupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String anArtifactId) {
		this.artifactId = anArtifactId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String aVersionId) {
		this.versionId = aVersionId;
	}

	public String getFileName() {
		return fileName;
	}
	
	// utilizzata dal fileEntry
	public void sampleListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry) e.getComponent();
		FileEntryResults results = fe.getResults();
		File parent = null;

		// get data About File

		for (FileEntryResults.FileInfo i : results.getFiles()) {
			fileName = i.getFileName();
			System.out.println("fileName:" + fileName);
			System.out.println("fe:" + fe.getRelativePath());

			fileData.add("File Name: " + i.getFileName());

			if (i.isSaved()) {
				fileData.add("File Size: " + i.getSize() + " bytes");

				File file = i.getFile();
				if (file != null) {
					parent = file.getParentFile();
				}
			} else {
				fileData.add("File was not saved because: "
						+ i.getStatus()
								.getFacesMessage(
										FacesContext.getCurrentInstance(), fe,
										i).getSummary());
			}
		}

		if (parent != null) {
			long dirSize = 0;
			int fileCount = 0;
			for (File file : parent.listFiles()) {
				fileCount++;
				dirSize += file.length();
			}
			fileData.add("Total Files In Upload Directory: " + fileCount);
			fileData.add("Total Size of Files In Directory: " + dirSize
					+ " bytes");
		}
	}

	// utilizzata dal fileEntry
	public List getFileData() {
		return fileData;
	}

	public void downloadArtifact(ActionEvent event) {
		try {

			String appSessionIdSubdir = "";
			if (useSessionId.equalsIgnoreCase("true")) {
				appSessionIdSubdir = "/" + sessionId;
			}

			// Sostituire la url corretta letta del properties
			if (!appSessionIdSubdir.isEmpty()) {
				File appDir = new File(uploadArtifactPath);
				appDir.mkdir();
				appDir = new File( uploadArtifactPath + appSessionIdSubdir);
				appDir.mkdir();
			}
			myFileMaven = DownloadMavenDependency.download(groupId, artifactId,
					versionId, uploadArtifactPath + appSessionIdSubdir);
			if (myFileMaven == null) {
		      FacesMessage msg = new  FacesMessage(FacesMessage.SEVERITY_ERROR, "Artifact non trovato!","Artifact non trovato!");
			  FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (Exception e) {
	      FacesMessage msg = new  FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore download artifact [" + e + "]","Errore download artifact [" + e + "]");
		  FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	// utilizzata dal Bottone di Confronta Archive
	public void executeButtonConfronta(ActionEvent event) {

		System.out.println("fileName:" + fileName);
		System.out.println("pathArtifact:" + uploadArtifactPath
				+ appSessionIdSubdir);
		System.out.println("absolutePath:" + absolutePath + appSessionIdSubdir);

		System.out.println("Effettuo ExecPkgDiff");
		OutputStream appResultExecPkgDiff = new ByteArrayOutputStream();

		// ExecPkgDiff.execute(uploadArtifactPath+appSessionIdSubdir+"/"+fileName,
		ExecPkgDiff.execute(myFileMaven, absolutePath + appSessionIdSubdir
				+ "/" + fileName, tmpDirPath + appSessionIdSubdir,
				reportDirPath + appSessionIdSubdir + "/changes_report.html",
				appResultExecPkgDiff);

		responseConfronto = appResultExecPkgDiff.toString();
		urlDettaglio = ulrReportDir + appSessionIdSubdir + "/changes_report.html";
	}
	
	public void reset(ActionEvent event) {
		urlDettaglio ="";
		fileName = "";
		myFileMaven = "";
		groupId = "";
		artifactId = "";
		versionId = "";
	}
}
