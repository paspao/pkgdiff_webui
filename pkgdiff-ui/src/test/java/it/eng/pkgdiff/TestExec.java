package it.eng.pkgdiff;

import java.io.File;

import it.eng.pkgdiff.batch.ExecPkgDiff;

import org.junit.Test;

public class TestExec {

	@Test
	public void simpleExec() {
		File f = new File("target/reportPkg");
		f.mkdir();
		ExecPkgDiff.execute("./src/test/resources/lib/PrestazioniEJB.jar",
				"./src/test/resources/maven/PrestazioniEJB.jar", "target",
				"target/reportPkg/changes_report.html");
	}
}
