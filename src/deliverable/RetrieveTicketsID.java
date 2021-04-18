package deliverable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.json.JSONArray;

public class RetrieveTicketsID {
	private RetrieveTicketsID() {
	    throw new IllegalStateException("Utility class");
	  }




   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      
      try(InputStream is = new URL(url).openStream()) {
    	 BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         return new JSONArray(jsonText);
       } 
       
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      try(InputStream is = new URL(url).openStream()) {
    	 BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         return new JSONObject(jsonText);
         
       } 
   }


  
  	   public static void reportTicket() throws IOException, JSONException, NoHeadException, GitAPIException {
	   String projName ="BOOKKEEPER";
	   Integer count = 0;
	   HashMap <String, LocalDateTime> commitTicketList = new HashMap <String, LocalDateTime>();
	   Integer j = 0;  
	   Integer i = 0;
	   Integer k = 0;
	   Integer total = 1;
      //Get JSON API for closed bugs w/ AV in the project 
      do {
         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
         j = i + 1000;
         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                 + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                 + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,creationdate,versions,created&startAt="
                 + i.toString() + "&maxResults=" + j.toString();
         JSONObject json = readJsonFromUrl(url);
         JSONArray issues = json.getJSONArray("issues");
         total = json.getInt("total");
         for (; i < total && i < j; i++) {
            //Iterate through each bug
        	count++;
        	String ticketID = issues.getJSONObject(i%1000).get("key").toString();
        	System.out.println(ticketID);
        	String creationDate = issues.getJSONObject(i%1000).getJSONObject("fields").getString("created").substring(0,16);
        	LocalDateTime data = LocalDateTime.parse(creationDate);
        	System.out.println(ticketID);
        	Integer dimension = issues.getJSONObject(i%1000).getJSONObject("fields").getJSONArray("versions").length();
            JSONArray versionAffected = issues.getJSONObject(i%1000).getJSONObject("fields").getJSONArray("versions");
        	HashMap <Integer, String> OV = VersionGenerator.gettingOV(data);
        	HashMap<Integer, String> FV = VersionGenerator.gettingFV(ticketID);
        	HashMap<Integer, String> AV = VersionGenerator.gettingAV(ticketID, dimension,versionAffected);
        	System.out.println("Sto stampando l'OV del relativo" + ticketID + OV);
        	System.out.println("Sto stampando l'FV del relativo" + ticketID + FV);
            System.out.println("Sto stampando l'AV del relativo" + ticketID + AV);
            String versionInjected = issues.getJSONObject(i%1000).getJSONObject("fields").getString("created");
            //System.out.println(getReleaseInfo.hashMapCreation());
            //String hashStringData = getReleaseInfo.hashMapCreation().get(1).values().toString().substring(1,17);
            //LocalDateTime hashData = LocalDateTime.parse(hashStringData);
            //System.out.println(data);
            /*for(int o=1;o<getReleaseInfo.hashMapCreation().size();o++) {
            	if(data.isBefore(hashData)) {
            		//System.out.println(hashData);
            		//System.out.println("urca");
            	}
            	else {
            		//System.out.println(hashData);
            		//System.out.println("vacca");
            	}
            }*/
         }
         System.out.println(commitTicketList);
         System.out.println(count);
         System.out.println(commitTicketList.size());
      } while (i < total);
   }

 
}
