package it.eng.pkgdiff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }	
    }
    
	public String getVersion() {
		return "1.0";
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
		System.out.println("sampleListener");
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
		System.out.println("Effettuo Download per groupId:"+groupId);
		System.out.println("artifactId:"+artifactId);
		System.out.println("versionId:"+versionId);
		try {
			// Sostituire la url corretta letta del properties
			DownloadMavenDependency.download(groupId, artifactId,versionId,"http://161.27.213.71:8081/nexus/content/groups/public");
		} catch (Exception e){ 
			e.printStackTrace();
		}
		System.out.println("fileName:"+fileName);
		File app = new File ("./TmpUpload/"+fileName);		
		System.out.println("Effettuo getAbsolutePath:"+app.exists());		
		// Sostituire i pkg Corretti e i path Corretti
			
		System.out.println("Effettuo ExecPkgDiff");
		
		//ExecPkgDiff.execute("PrestazioniEJB.jar",
		//		"PrestazioniEJB.jar", "target",
		//		"changes_report.html",System.out);		
        return null;
    }
}
