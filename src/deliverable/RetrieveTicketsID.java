package deliverable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import oggetti.JavaFile;
import oggetti.Release;
import oggetti.TicketObjectVersionID;
import oggetti.VersionObject;
import utility.FileLogger;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONArray;

public class RetrieveTicketsID {
	
	private static List<String> projName = new ArrayList<>(List.of("BOOKKEEPER","ZOOKEEPER"));
	private static Integer prescelto = 0;
	private static Logger logger = Logger.getLogger(RetrieveTicketsID.class.getName());
	private RetrieveTicketsID() {
	    throw new IllegalStateException("Utility class");
	  }




   private static String readAll(Reader rd) throws IOException {
	      var sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      
      try(InputStream is = new URL(url).openStream()) {
    	 var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         return new JSONArray(jsonText);
       } 
       
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      try(InputStream is = new URL(url).openStream()) {
    	 var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         return new JSONObject(jsonText);
         
       } 
   }


  
  	   public static void reportTicket(List <VersionObject> listVersion, List<Release> releaseList, List<RevCommit> commitList) throws IOException, JSONException, GitAPIException {
	   List<TicketObjectVersionID> ticketList = new ArrayList<>();
	   List<JavaFile> buggyFileList = new ArrayList<>();
	   Integer count = 0;
	   Integer j = 0;  
	   Integer i = 0;
	   Integer total = 1;
      //Get JSON API for closed bugs w/ AV in the project 
      do {
         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
         j = i + 1000;
         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                     + projName.get(prescelto) + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                     + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,creationdate,versions,created&startAt="
                     + i.toString() + "&maxResults=" + j.toString();
         JSONObject json = readJsonFromUrl(url);
         var issues2 = json.getJSONArray("issues");
         total = json.getInt("total");
         var issues = new JSONArray();
         for(int m=issues2.length()-1;m>=0;m--) {
        	 issues.put(issues2.getJSONObject(m));
         }
         for (; i < total && i < j; i++) {
            //Iterate through each bug
        	var ticketID = issues.getJSONObject(i%1000).get("key").toString();
            logger.log(Level.INFO,ticketID);
        	var fields = issues.getJSONObject(i%1000).getJSONObject("fields");
        	var creationDate = fields.getString("created").substring(0,16);
        	var data = LocalDateTime.parse(creationDate);
        	Integer dimension = fields.getJSONArray("versions").length();
            var versionAffected = fields.getJSONArray("versions");
        	VersionObject oV = VersionGenerator.gettingOV(data,listVersion);
        	VersionObject fV = VersionGenerator.gettingFV(ticketID,listVersion);
        	List<VersionObject> aV = VersionGenerator.gettingAV(dimension,versionAffected,listVersion);
        	VersionObject iV = VersionGenerator.gettingIV(aV);
            FileLogger.getLogger().info(String.valueOf(count));
            count++;
            var info = new TicketObjectVersionID();
            if(Proportion.proportion(fV, oV, aV, iV, listVersion, count)) {
            	info.addFV(fV);
            	info.addOV(oV);
            	info.addAV(aV);
            	info.addIV(iV);
            	info.addTicketId(ticketID);
            	GetCommitInfo.commitListTicket(ticketID,info);
            	info.printInfo(ticketID);
            	ticketList.add(info);
            }
            else {
            	    logger.log(Level.INFO, "Un parametro è nullo-Ticket scartato");
            }   
         }
         FileLogger.getLogger().info(String.valueOf(ticketList.size()));
         preCheckFileBug(ticketList, commitList, releaseList,  buggyFileList);
         } while (i < total);
      Metrics.nR(releaseList);
      CsvWriter.write(releaseList);
      FileLogger.getLogger().info(String.valueOf(buggyFileList.size()));
   }
  	   public static void checkFileBug(List<Release> releaseList, DiffEntry entry, TicketObjectVersionID ticket, List<JavaFile> buggyFileList) {
  		   String file;
  		   if(entry.getChangeType()== DiffEntry.ChangeType.DELETE) {
  			 file =entry.getOldPath();
  		   }
  		   else {
  			   file=entry.getNewPath();
  		   }
  		   for (Release release : releaseList) {
  			   for (JavaFile javaFile : release.getFileList()){ 
				 //NON ENTRA IN QUESTO IF 
  				   if(javaFile.getName().equals(file) ) {
  					  compareAv(javaFile, ticket.getAV(), release,buggyFileList);
				 }
			 }
		 }
  	   }
  	   public static void compareAv(JavaFile javaFile, List<VersionObject> av, Release release,List<JavaFile> buggyFileList) {
  		   for(VersionObject version : av) {
  			if (version.getId().equals(release.getClassification())) {
  				 FileLogger.getLogger().info("LA CLASSE E' BUGGY\n###\n"); 
  				 javaFile.setBugg("YES");
  				 buggyFileList.add(javaFile);
  			 }
  			else {
  				javaFile.setBugg("NO");
  			}
  		 }
  		   
  	   }
  	   public static void preCheckFileBug(List<TicketObjectVersionID> ticketList, List<RevCommit> commitList, List<Release> releaseList, List<JavaFile> buggyFileList) throws IOException {
  		 for(TicketObjectVersionID ticket: ticketList) {
        	 for(RevCommit commit : commitList) {
        		 if(commit.getFullMessage().contains(ticket.getTicketID())) {
        			 List<DiffEntry> diffs = GetReleaseInfo.getDiffs(commit);
        			 entryFor(diffs,releaseList,ticket,buggyFileList);
        		 }
        	 }
         }
  	   }
  	   public static void entryFor(List<DiffEntry> diffs, List<Release> releaseList, TicketObjectVersionID ticket, List<JavaFile> buggyFileList) {
  		 for(DiffEntry entry : diffs) {
			 var file = new JavaFile(entry.toString());
			 if (file.getName().contains(".java") && (entry.getChangeType().toString().equals("MODIFY")
						|| entry.getChangeType().toString().equals("DELETE") )) { 
				 checkFileBug(releaseList,entry,ticket,buggyFileList);
			 }	
		 }
  	   }
}
