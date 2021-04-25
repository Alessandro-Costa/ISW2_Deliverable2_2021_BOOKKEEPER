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
	   ArrayList <Integer> P = new ArrayList<Integer>();
	   Integer j = 0;  
	   Integer i = 0;
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
         JSONArray issues2 = json.getJSONArray("issues");
         total = json.getInt("total");
         Integer scartati = 0;
         JSONArray issues = new JSONArray();
         for(int m=issues2.length()-1;m>=0;m--) {
        	 issues.put(issues2.getJSONObject(m));
         }
         for (; i < total && i < j; i++) {
            //Iterate through each bug
        	String ticketID = issues.getJSONObject(i%1000).get("key").toString();
        	System.out.println(ticketID);
        	String creationDate = issues.getJSONObject(i%1000).getJSONObject("fields").getString("created").substring(0,16);
        	LocalDateTime data = LocalDateTime.parse(creationDate);
        	Integer dimension = issues.getJSONObject(i%1000).getJSONObject("fields").getJSONArray("versions").length();
            JSONArray versionAffected = issues.getJSONObject(i%1000).getJSONObject("fields").getJSONArray("versions");
        	VersionObject OV = VersionGenerator.gettingOV(data);
        	VersionObject FV = VersionGenerator.gettingFV(ticketID);
        	ArrayList<VersionObject> AV = VersionGenerator.gettingAV(ticketID, dimension,versionAffected);
        	VersionObject IV = VersionGenerator.gettingIV(AV);
            Integer indexFV = FV.getId();
        	Integer indexOV = OV.getId();
        	Integer indexIV = IV.getId();
            if(IV.getId()==null) {
            	System.out.println("Entro nel for maledetto dove IV non esiste");
            	if(P.size()<=4) {
            		P.add(0);
            	}
            	else {
        			Integer predictedIV = indexFV-(indexFV-indexOV)*((P.get(count-1)+P.get(count-2)+P.get(count-3)+P.get(count-4))/4);
        			if(predictedIV > OV.getId()) {
        				scartati++;
        				continue;
        			}
        			ArrayList <VersionObject>listVersion = GetReleaseInfo.listVersion();
        			for(int n = 0;n<listVersion.size();n++) {
        				if(predictedIV == listVersion.get(n).getId()) {
        					AV.add(listVersion.get(n));
        					IV.addIdVersion(listVersion.get(n).getId(), listVersion.get(n).getVersion());
        				}
        			}
            		
        			System.out.println(predictedIV);
            		if(indexFV-indexOV !=0) {
            			P.add((indexFV-predictedIV)/(indexFV-indexOV));
            		}
            		else {
            			System.out.println("Sono entrato nel caso divisiore = 0");
            			P.add(0);
            		}
        		}
            }
            else {
            		if(indexFV-indexOV !=0) {
            			P.add((indexFV-indexIV)/(indexFV-indexOV));
            		}
            		else {
            			System.out.println("Sono entrato nel caso divisiore = 0");
            			P.add(0);
            		}
            	 }
            System.out.println(P);
          
            System.out.println(count);
            count++;
            TicketObjectVersionID info =new TicketObjectVersionID(OV,FV,AV,IV);
            info.printInfo(ticketID);
         }
         System.out.println("Gli elementi scartati sono:"+scartati);
      } while (i < total);
   }

 
}
