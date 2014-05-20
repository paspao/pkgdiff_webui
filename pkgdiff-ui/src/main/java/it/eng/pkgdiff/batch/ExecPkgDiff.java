package it.eng.pkgdiff.batch;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 * 
 * @author Pasquale Paola - Engineering Ingegneria Informatica
 *
 */
public class ExecPkgDiff {

	/**
	 * Permette l'esecuzione del comando pkgdiff {@link https://github.com/lvc/pkgdiff}
	 * @param pathFirstJar Percorso del primo jar.
	 * @param pathSecondJar Percorso del secondo jar.
	 * @param pathTempDir Percorso alla directory temporanea.
	 * @param pathReportFileHtml Percorso al file html che conterrà il report.
	 * @param ou Stream di output del comando.
	 */
	public static void execute(String pathFirstJar, String pathSecondJar,
			String pathTempDir, String pathReportFileHtml, OutputStream ou) {

		PrintWriter printer = new PrintWriter(ou);

		try {
			String pkgDiffCommand=Util.getResourceProperties().getProperty("pkgdiff.path");
			String command = pkgDiffCommand+" " + pathFirstJar + " " + pathSecondJar
					+ " -report-path " + pathReportFileHtml + " -tmp-dir "
					+ pathTempDir + " -details";

			System.out.println("Executing command: " + command);
			Process p = Runtime.getRuntime().exec(command);
			Scanner sc = new Scanner(p.getInputStream());

			printer.write("<div>");
			printer.write("<h2>Result</h2>");
			printer.write("<ul>");
			while (sc.hasNext())
			{
				printer.write("<li>" + sc.nextLine() + "</li>");
				printer.flush();
			}
			printer.write("</ul>");
			printer.write("</div>");
		} catch (IOException e) {
			printer.write(e.getMessage());
		}
		printer.close();
	}
}
