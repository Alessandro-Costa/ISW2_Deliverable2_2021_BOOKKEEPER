package deliverable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import oggetti.JavaFile;
import oggetti.Release;

import java.util.logging.Level;
public class CsvWriter {
	private static Logger logger = Logger.getLogger(CsvWriter.class.getName());
	private CsvWriter() {
	    throw new IllegalStateException("Utility class");
	  }

	public static void write(List <Release> releaseList)  {
		try (
				   FileWriter fileWriter = new FileWriter("metriche.csv")) {
				   
				   fileWriter.append("RELEASE ; FILENAME ; NR ; NAUTH ; BUGGYNESS \n");
				   for (Release release : releaseList) {
					   System.out.println("RELEASE CSV == " + release.getClassification());
					   for (JavaFile file : release.getFileList()) {
						   fileWriter.append(release.getClassification().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getName());
						   fileWriter.append(";");
						   fileWriter.append(file.getNr().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getnAuth().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getBugg());
						   fileWriter.append("\n");
					   }
				   } 
				  } catch (Exception ex) {
					  logger.log(Level.SEVERE,"Error in csv writer");
					  ex.printStackTrace();
				  
				  }
				 }	
}
