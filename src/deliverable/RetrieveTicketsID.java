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
	   Integer P = 0;
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
        	Integer dimension = issues.getJSONObject(i%1000).getJSONObject("fields").getJSONArray("versions").length();
            JSONArray versionAffected = issues.getJSONObject(i%1000).getJSONObject("fields").getJSONArray("versions");
        	HashMap <Integer, String> OV = VersionGenerator.gettingOV(data);
        	HashMap<Integer, String> FV = VersionGenerator.gettingFV(ticketID);
        	HashMap<Integer, String> AV = VersionGenerator.gettingAV(ticketID, dimension,versionAffected);
        	HashMap<Integer, String> IV = VersionGenerator.gettingIV(ticketID, dimension,versionAffected);
        	System.out.println("Sto stampando l'OV del relativo:" + ticketID + OV);
        	System.out.println("Sto stampando l'FV del relativo:" + ticketID + FV);
            System.out.println("Sto stampando l'AV del relativo:" + ticketID + AV);
            System.out.println("Sto stampando l'IV del relativo:" + ticketID + IV);
            String indexFV = FV.keySet().toString();
        	String indexOV = OV.keySet().toString();
        	String indexIV = IV.keySet().toString();
            if(IV.keySet().isEmpty()) {
            	System.out.println("Entro nel for maledetto dove IV non esiste");
            	if(indexFV.length()==4 && indexOV.length()==4 &&  indexIV.length()==4) {
            		Integer intFV = Integer.parseInt(indexFV.substring(1,3));
            		Integer intOV = Integer.parseInt(indexOV.substring(1,3));
            		Integer intIV = Integer.parseInt(indexIV.substring(1,3));
            		Integer predictedIV = intFV-(intFV-intOV)*P;
            		System.out.println(predictedIV);
            	}
            	if(indexFV.length()==3 && indexOV.length()==3 && indexIV.length()==3){
            		Integer intFV = Integer.parseInt(indexFV.substring(1,2));
            		Integer intOV = Integer.parseInt(indexOV.substring(1,2));
            		Integer intIV = Integer.parseInt(indexIV.substring(1,2));
            		Integer predictedIV = intFV-(intFV-intOV)*P;
            		System.out.println(predictedIV);
            	}
            	if(indexFV.length()==4 && indexOV.length()==3 &&  indexIV.length()==3){
            		Integer intFV = Integer.parseInt(indexFV.substring(1,3));
            		Integer intOV = Integer.parseInt(indexOV.substring(1,2));
            		Integer intIV = Integer.parseInt(indexIV.substring(1,2));
            		Integer predictedIV = intFV-(intFV-intOV)*P;
            		System.out.println(predictedIV);
                }
            	
            }
            else {
            	if(indexFV.length()==4 && indexOV.length()==4 &&  indexIV.length()==4) {
            		Integer intFV = Integer.parseInt(indexFV.substring(1,3));
            		Integer intOV = Integer.parseInt(indexOV.substring(1,3));
            		Integer intIV = Integer.parseInt(indexIV.substring(1,3));
            		if(intFV-intOV !=0) {
            			P = (intFV-intIV)/(intFV-intOV);
            		}
            		else {
            			System.out.println("Sono entrato nel caso divisiore = 0");
            			P = 0;
            		}
            	}
            	if(indexFV.length()==3 && indexOV.length()==3 && indexIV.length()==3){
            		Integer intFV = Integer.parseInt(indexFV.substring(1,2));
            		Integer intOV = Integer.parseInt(indexOV.substring(1,2));
            		Integer intIV = Integer.parseInt(indexIV.substring(1,2));
            		if(intFV-intOV !=0) {
            			P = (intFV-intIV)/(intFV-intOV);
            		}
            		else {
            			System.out.println("Sono entrato nel caso divisiore = 0");
            			P = 0;
            		}
            	}
            	if(indexFV.length()==4 && indexOV.length()==3 &&  indexIV.length()==3){
            		Integer intFV = Integer.parseInt(indexFV.substring(1,3));
            		Integer intOV = Integer.parseInt(indexOV.substring(1,2));
            		Integer intIV = Integer.parseInt(indexIV.substring(1,2));
            		if(intFV-intOV !=0) {
            			P = (intFV-intIV)/(intFV-intOV);
            		}
            		else {
            			System.out.println("Sono entrato nel caso divisiore = 0");
            			P = 0;
            		}
            	}
            	if(indexFV.length()==4 && indexOV.length()==4 &&  indexIV.length()==3){
            		Integer intFV = Integer.parseInt(indexFV.substring(1,3));
            		Integer intOV = Integer.parseInt(indexOV.substring(1,3));
            		Integer intIV = Integer.parseInt(indexIV.substring(1,2));
            		if(intFV-intOV !=0) {
            			P = (intFV-intIV)/(intFV-intOV);
            		}
            		else {
            			System.out.println("Sono entrato nel caso divisiore = 0");
            			P = 0;
            		}
            	}
            	System.out.println(P);
            }
         }
         System.out.println(commitTicketList);
         System.out.println(count);
         System.out.println(commitTicketList.size());
      } while (i < total);
   }

 
}
