package it.eng.pkgdiff.batch;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

import javax.servlet.ServletContext;

public class TimerCleaner extends TimerTask {

	private ServletContext servCtx;
	public TimerCleaner(ServletContext serv) {
		this.servCtx=serv;
	}
	@Override
	public void run() {
		System.out.println(new Date() + ": TimerCleaner Started...");

		
		String realPath = servCtx.getRealPath(File.separator);
		String absolutePath = realPath + File.separator
				+ Util.getResourceProperties().getProperty("uploadPath");
		String uploadArtifactPath = realPath
				+ File.separator
				+ Util.getResourceProperties()
						.getProperty("uploadArtifactPath");
		String tmpDirPath = realPath + File.separator
				+ Util.getResourceProperties().getProperty("tmpDirPath");
		String reportDirPath = realPath + File.separator
				+ Util.getResourceProperties().getProperty("reportDirPath");
		try {
			Util.delete(new File(absolutePath));
			Util.delete(new File(uploadArtifactPath));
			Util.delete(new File(tmpDirPath));
			Util.delete(new File(reportDirPath));
		} catch (IOException e) {
			System.out.println(new Date() + ": TimerCleaner Error!");
			System.err.println(e.getMessage());
		}
		System.out.println(new Date() + ": TimerCleaner Ended...");

	}
}
