package it.eng.pkgdiff.batch;

import java.io.IOException;
import java.util.Scanner;

public class ExecPkgDiff {

	public static void execute(String pathFirstJar, String pathSecondJar,
			String pathTempDir, String pathReportDir) {
		try {
			String command = "pkgdiff " + pathFirstJar + " " + pathSecondJar
					+ " -report-path " + pathReportDir + " -tmp-dir "
					+ pathTempDir + " -details";

			System.out.println("Executing command: " + command);
			Process p = Runtime.getRuntime().exec(command);
			Scanner sc = new Scanner(p.getInputStream());

			while (sc.hasNext())
				System.out.println(sc.nextLine());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
