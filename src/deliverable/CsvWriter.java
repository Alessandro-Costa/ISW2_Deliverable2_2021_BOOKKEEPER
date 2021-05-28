package deliverable;

import java.io.FileWriter;
import java.util.List;
import java.util.logging.Logger;

import oggetti.JavaFile;
import oggetti.Release;
import utility.FileLogger;

import java.util.logging.Level;
public class CsvWriter {
	private static Logger logger = Logger.getLogger(CsvWriter.class.getName());
	private CsvWriter() {
	    throw new IllegalStateException("Utility class");
	  }

	public static void write(List <Release> releaseList)  {
		try (
				   var fileWriter = new FileWriter("metriche.csv")) {
				   
				   fileWriter.append("RELEASE ; FILENAME ; NR ; NAUTH ; ChgSize ; LOCAdded ; Churn ; BUGGYNESS \n");
				   for (Release release : releaseList) {
					   FileLogger.getLogger().info("RELEASE CSV == " + release.getClassification());
					   for (JavaFile file : release.getFileList()) {
						   fileWriter.append(release.getClassification().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getName());
						   fileWriter.append(";");
						   fileWriter.append(file.getNr().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getnAuth().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getChgSetSize().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getLOCadded().toString());
						   fileWriter.append(";");
						   fileWriter.append(file.getChurn().toString());
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
