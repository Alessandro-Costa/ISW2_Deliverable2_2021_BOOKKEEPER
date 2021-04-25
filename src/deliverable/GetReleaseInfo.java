package deliverable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.json.JSONException;
import org.json.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONArray;


public class GetReleaseInfo {
	
	   public static HashMap<LocalDateTime, String> releaseNames;
	   public static HashMap<LocalDateTime, String> releaseID;
	   public static ArrayList<LocalDateTime> releases;
	   public static Integer numVersions;

	public static ArrayList <VersionObject>listVersion(){
		   ArrayList <VersionObject> listVersion = new ArrayList <VersionObject>();
		   ArrayList<RevCommit> commitList = new ArrayList<RevCommit>();
		   ArrayList<String> classes = new ArrayList<String>();
		try {
			commitList = GetCommitInfo.commitList();
		} catch (IOException | GitAPIException e2) {
			e2.printStackTrace();
		}
		   String projName ="BOOKKEEPER";
		 //Fills the arraylist with releases dates and orders them
		   //Ignores releases with missing dates
		   releases = new ArrayList<LocalDateTime>();
		   		 Integer i;
		         String url = "https://issues.apache.org/jira/rest/api/2/project/" + projName;
		         JSONObject json;
				try {
					json = readJsonFromUrl(url);
				
		         JSONArray versions = json.getJSONArray("versions");
		         releaseNames = new HashMap<LocalDateTime, String>();
		         releaseID = new HashMap<LocalDateTime, String> ();
		         for (i = 0; i < versions.length(); i++ ) {
		            String name = "";
		            String id = "";
		            if(versions.getJSONObject(i).has("releaseDate")) {
		               if (versions.getJSONObject(i).has("name"))
		                  name = versions.getJSONObject(i).get("name").toString();
		               if (versions.getJSONObject(i).has("id"))
		                  id = versions.getJSONObject(i).get("id").toString();
		               addRelease(versions.getJSONObject(i).get("releaseDate").toString(),
		                          name,id);
		            }
		         }
				} catch (IOException | JSONException e1) {
					e1.printStackTrace();
				}
		         // order releases by date
		        Collections.sort(releases,(o1,o2)-> o1.compareTo(o2));
		         if (releases.size() < 6)
		            return listVersion;
		         FileWriter fileWriter = null;
			 try {
		            fileWriter = null;
		            String outname = projName + "VersionInfo.csv";
						    //Name of CSV for output
						    fileWriter = new FileWriter(outname);
		            fileWriter.append("Index,Version ID,Version Name,Date");
		            fileWriter.append("\n");
		            numVersions = releases.size();
		            for ( i = 0; i < releases.size(); i++) {
		               VersionObject versionInfo = new VersionObject();
		               HashMap <String, LocalDateTime>versionDate = new HashMap <String, LocalDateTime>();
		               Integer index = i + 1;
		               fileWriter.append(index.toString());
		               fileWriter.append(",");
		               fileWriter.append(releaseID.get(releases.get(i)));
		               fileWriter.append(",");
		               fileWriter.append(releaseNames.get(releases.get(i)));
		               fileWriter.append(",");
		               fileWriter.append(releases.get(i).toString());
		               fileWriter.append("\n");
		               versionDate.put(releaseNames.get(releases.get(i)),releases.get(i));
		               versionInfo.add(index, releaseNames.get(releases.get(i)), releases.get(i));
		               listVersion.add(versionInfo);
		               Release release = new Release();
		               for(int m = 0; m<commitList.size();m++) {
		            	   if(commitList.get(m).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(releases.get(i))) {
			            	   release.setCommitList(commitList);
			            	   release.setDate(releases.get(i));
			            	   release.setClassification(i);
			            	   release.setRelease(releases.get(i).toString());
			               }
		               }  
		         } 
		         } catch (Exception e) {
		            System.out.println("Error in csv writer");
		            e.printStackTrace();
		         } finally {
		            try {
		               fileWriter.flush();
		               fileWriter.close();
		            } catch (IOException e) {
		               System.out.println("Error while flushing/closing fileWriter !!!");
		               e.printStackTrace();
		            }
		         }
			 try {
				GetJavaFile.commitHistory(commitList, classes);
			} catch (GitAPIException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		         return listVersion;
		   }
 
	
	   public static void addRelease(String strDate, String name, String id) {
		      LocalDate date = LocalDate.parse(strDate);
		      LocalDateTime dateTime = date.atStartOfDay();
		      if (!releases.contains(dateTime))
		         releases.add(dateTime);
		      releaseNames.put(dateTime, name);
		      releaseID.put(dateTime, id);
		      return;
		   }


	   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	      InputStream is = new URL(url).openStream();
	      try {
	         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	         String jsonText = readAll(rd);
	         JSONObject json = new JSONObject(jsonText);
	         return json;
	       } finally {
	         is.close();
	       }
	   }
	   
	   private static String readAll(Reader rd) throws IOException {
		      StringBuilder sb = new StringBuilder();
		      int cp;
		      while ((cp = rd.read()) != -1) {
		         sb.append((char) cp);
		      }
		      return sb.toString();
		   }
}