package deliverable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import oggetti.JavaFile;
import oggetti.TicketObjectVersionID;

public class Metrics {
	private static Logger logger = Logger.getLogger(Metrics.class.getName());
	public static void sizeLOC(String file, JavaFile javaFile){
		    String lineToRead = "";                                                 //will be used to read the lines from .txt file                                           //will be used to count all lines
		    try {
		        var br = new BufferedReader(new FileReader(file));
		        try {
		            while ((lineToRead = br.readLine()) != null) {
		                javaFile.addSize();
		                if (lineToRead.trim().isEmpty()) {
		                    javaFile.addSize();
		                }
		                if (lineToRead.startsWith("//")) {                          //if the line starts with single line comment 
		                    javaFile.addSize();                            //if yes, count it
		                } else if (lineToRead.startsWith("/*")) {                   //if the line starts with multiple line comment
		                    javaFile.addSize();                           //if yes, look at the content of it
		                    do {
		                        if (lineToRead.trim().isEmpty()) {                  //if there is a blank line, count it as blank
		                            javaFile.addSize();
		                        }
		                        else
		                        {                     //else, count it as comment line
		                            javaFile.addSize();
		                        }
		                    } while (!(lineToRead = br.readLine()).endsWith("*/")); //count until see multiple comment line closure
		                }
		            }
		        } catch (IOException ex) {
		            Logger.getLogger(Metrics.class.getName()).log(Level.SEVERE, null, ex);
		        }
		        br.close();                                                         //close the buffer reader to empty ram area
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    System.out.println("Total Number of Physical Lines " + javaFile.getSize());
		}
}
