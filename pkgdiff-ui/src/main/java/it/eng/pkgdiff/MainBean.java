package it.eng.pkgdiff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

import it.eng.pkgdiff.batch.*;

/**
 * MasterBean del progetto
 * @author Russo Pasquale
 *
 */

@ManagedBean(name= MainBean.BEAN_NAME)
@ViewScoped
public class MainBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String BEAN_NAME = "mainBean";
	
	private List<String> fileData;
	private String fileName; 
	
	private String groupId;
    private String artifactId;
    private String versionId;
    
    private String absolutePath;
    private String uploadArtifactPath;
    private String tmpDirPath;
    private String reportDirPath;
    private String useSessionId = "true";
    private String responseConfronto;
    
    private Properties properties;
	
    public MainBean (){
    	System.out.println("MainBean");
    	try {
            properties = new Properties();
            InputStream resourceAsStream =  MainBean.class.getClassLoader().getResourceAsStream("./resources.properties");
            if (resourceAsStream != null) {
                properties.load(resourceAsStream);
                absolutePath = properties.getProperty("uploadPath");
                System.out.println("absolutePath:"+absolutePath);
                if (properties.getProperty("uploadArtifactPath") != null) {
                	uploadArtifactPath = properties.getProperty("uploadArtifactPath");
    				System.out.println("set Defautl downloadedPath = "+uploadArtifactPath);
    			}
                if (properties.getProperty("tmpDirPath") != null) {
                	tmpDirPath = properties.getProperty("tmpDirPath");
    				System.out.println("set Defautl tmpDirPath = "+tmpDirPath);
    			}
                if (properties.getProperty("reportDirPath") != null) {
                	reportDirPath = properties.getProperty("reportDirPath");
    				System.out.println("set Defautl reportDirPath = "+reportDirPath);
    			}
                if (properties.getProperty("userSessionId") != null) {
                	useSessionId = properties.getProperty("userSessionId");
    				System.out.println("set Defautl userSessionId = "+useSessionId);
    			}
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }	
    }
    
	public String getVersion() {
		return "1.0";
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
    
	// utilizzata dal fileEntry
	public void sampleListener(FileEntryEvent e)
    {		
        FileEntry fe = (FileEntry)e.getComponent();
        FileEntryResults results = fe.getResults();
        File parent = null;

        fileData = new ArrayList<String>();

    //get data About File
    
        for (FileEntryResults.FileInfo i : results.getFiles()) 
        {
        	fileName = i.getFileName();
        	System.out.println("fileName:"+fileName);
        	System.out.println("fe:"+fe.getRelativePath());        	
        	
            fileData.add("File Name: " + i.getFileName());

            if (i.isSaved()) {
                fileData.add("File Size: " + i.getSize() + " bytes");

                File file = i.getFile();
                if (file != null) {
                    parent = file.getParentFile();
                }
            } else {
                fileData.add("File was not saved because: " +
                    i.getStatus().getFacesMessage(
                        FacesContext.getCurrentInstance(),
                        fe, i).getSummary());
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
            fileData.add("Total Size of Files In Directory: " + dirSize + " bytes");
        }
    }

	// utilizzata dal fileEntry
    public List getFileData() {
        return fileData;
    }
    
	// utilizzata dal Bottone di Confronta Archive
	public String executeButtonConfronta() { 
		String myFileMaven = "";
		FacesContext fCtx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		String sessionId = session.getId();
		
		String appSessionIdSubdir = "";
		if (useSessionId.equalsIgnoreCase("true")){
			appSessionIdSubdir = "/"+sessionId;
		}
		
		try {
			// Sostituire la url corretta letta del properties
			if (!appSessionIdSubdir.isEmpty()) {
				File appDir = new File (uploadArtifactPath+appSessionIdSubdir);
				if (!appDir.exists()){
					appDir.mkdir();
				}
			}			
			myFileMaven = DownloadMavenDependency.download(groupId, artifactId,versionId,uploadArtifactPath+appSessionIdSubdir);
			System.out.println("Download Maven Dipendency Effettuato:"+myFileMaven);
		} catch (Exception e){ 
			e.printStackTrace();
		}
		
		System.out.println("fileName:"+fileName);
		System.out.println("pathArtifact:"+uploadArtifactPath+appSessionIdSubdir);		
		System.out.println("absolutePath:"+absolutePath+appSessionIdSubdir);
		
			
		System.out.println("Effettuo ExecPkgDiff");
		OutputStream appResultExecPkgDiff = new ByteArrayOutputStream ();
		
		 //ExecPkgDiff.execute(uploadArtifactPath+appSessionIdSubdir+"/"+fileName,
		ExecPkgDiff.execute(myFileMaven,
				absolutePath+appSessionIdSubdir+"/"+fileName, tmpDirPath+appSessionIdSubdir,
				reportDirPath+appSessionIdSubdir+"/changes_report.html",appResultExecPkgDiff);
		System.out.println("Effettuato ExecPkgDiff:"+appResultExecPkgDiff);
		responseConfronto = appResultExecPkgDiff.toString();
        return null;
    }
}
