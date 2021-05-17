package deliverable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

import oggetti.JavaFile;
import oggetti.Release;
import oggetti.VersionObject;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.io.DisabledOutputStream;


public class GetReleaseInfo {
	private static Logger logger = Logger.getLogger(GetReleaseInfo.class.getName());
	private GetReleaseInfo() {
	    throw new IllegalStateException("Utility class");
	  }
	   private static Map<LocalDateTime, String> releaseNames;
	   private static Map<LocalDateTime, String> releaseID;
	   private static List<LocalDateTime> releases;
	   private static Integer numVersions;
	   private static Repository repository;

	public static List <VersionObject>listVersion(List <RevCommit> commitList) throws IOException, JSONException, GitAPIException{
		   List <VersionObject> listVersion = new ArrayList <>();
		   List<Release> releaseList = new ArrayList<>();
		   var projName ="BOOKKEEPER";
		 //Fills the arraylist with releases dates and orders them
		   //Ignores releases with missing dates
		   releases = new ArrayList<>();
		   		 Integer i;
		         String url = "https://issues.apache.org/jira/rest/api/2/project/" + projName;
		         JSONObject json;
				try {
					json = readJsonFromUrl(url);
				
		         var versions = json.getJSONArray("versions");
		         releaseNames = new HashMap<>();
		         releaseID = new HashMap<>();
		         for (i = 0; i < versions.length()/2; i++ ) {
		        	var name = "";
		            var id = "";
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
		               var versionInfo = new VersionObject();
		               HashMap <String, LocalDateTime>versionDate = new HashMap <>();
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
		            } 
		            releaseList = VersionGenerator.releaseCreate(commitList, releases); 
		         } catch (Exception e) {
		            logger.log(Level.INFO,"Error in csv writer");
		            e.printStackTrace();
		         } finally {
		            try {
		               fileWriter.flush();
		               fileWriter.close();
		            } catch (IOException e) {
		               logger.log(Level.INFO,"Error while flushing/closing fileWriter !!!");
		               e.printStackTrace();
		            }
		         }
			 RetrieveTicketsID.reportTicket(listVersion,releaseList,commitList);
		         return listVersion;
		   }
 
	
	   public static void addRelease(String strDate, String name, String id) {
		      var date = LocalDate.parse(strDate);
		      LocalDateTime dateTime = date.atStartOfDay();
		      if (!releases.contains(dateTime))
		         releases.add(dateTime);
		      releaseNames.put(dateTime, name);
		      releaseID.put(dateTime, id);
		   }

	   public static Integer compareDateVersion(LocalDateTime date) {
			 
			 Integer releaseIndex =0;
			 for (int k = 0; k<releases.size(); k++) {
				 if (date.isBefore(releases.get(k))) {
					 releaseIndex = k;
					 break;
				 }
				 
				 if(date.isAfter(releases.get(releases.size()-1))) {
					 releaseIndex = k-1;
				 }
			 }
			 return releaseIndex;
		 }
	   public static List<DiffEntry> getDiffs(RevCommit commit) throws IOException {
		   var dir= new File("/home/alessandro/eclipse-workspace/bookkeeper/.git");
		   var build = new RepositoryBuilder();
		   Repository rep = build.setGitDir(dir).readEnvironment().findGitDir().build();
		   List<DiffEntry> diffs;
			DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
			df.setRepository(rep);
			df.setDiffComparator(RawTextComparator.DEFAULT);
			df.setContext(0);
			df.setDetectRenames(true);
			if (commit.getParentCount() != 0) {
				RevCommit parent = (RevCommit) commit.getParent(0).getId();
				diffs = df.scan(parent.getTree(), commit.getTree());
			} else {
				RevWalk rw = new RevWalk(repository);
				ObjectReader reader = rw.getObjectReader();
				diffs = df.scan(new EmptyTreeIterator(), new CanonicalTreeParser(null, reader, commit.getTree()));
			}
			return diffs;

	 }
	   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	      InputStream is = new URL(url).openStream();
	      try{
	         var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	         String jsonText = readAll(rd);
	         return new JSONObject(jsonText);
	       } finally {
	         is.close();
	       }
	   }
	   
	   private static String readAll(Reader rd) throws IOException {
		      var sb = new StringBuilder();
		      int cp;
		      while ((cp = rd.read()) != -1) {
		         sb.append((char) cp);
		      }
		      return sb.toString();
		   }
}