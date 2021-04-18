package deliverable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONArray;
import org.json.JSONException;

public class VersionGenerator {
	public static HashMap <Integer, String > gettingOV(LocalDateTime creationDate) throws IOException, JSONException {
		HashMap<Integer, String> OV = new HashMap <Integer, String>();
		HashMap <Integer, HashMap> excellList = GetReleaseInfo.hashMapCreation();
		Integer size = excellList.size();
		LocalDateTime dataUltima = LocalDateTime.parse(excellList.get(size).values().toString().substring(1,17));
		Integer index = 0;
		if(creationDate.isAfter(dataUltima)) {
			index = Integer.parseInt(excellList.keySet().toArray()[size-1].toString());
			OV.put(index, excellList.get(size).keySet().toString());
			return OV;
		}
		for(int k = 1;k<=size;k++ ) {
			String stringDate = excellList.get(k).values().toString().substring(1,17);
			LocalDateTime date = LocalDateTime.parse(stringDate);
			if(creationDate.isBefore(date)) {
				index = Integer.parseInt(excellList.keySet().toArray()[k-1].toString());
				OV.put(index, excellList.get(k).keySet().toString());
				break;
			}
		}
		return OV;
	}
	
	public static HashMap<Integer, String> gettingFV(String ticketID) throws NoHeadException, IOException, GitAPIException, JSONException {
		HashMap <Integer, String> FV = new HashMap<Integer, String>();
		HashMap <Integer, HashMap> excellList = GetReleaseInfo.hashMapCreation();
		Integer size = excellList.size();
		LocalDateTime dataUltima = LocalDateTime.parse(excellList.get(size).values().toString().substring(1,17));
		ArrayList<RevCommit> commits = GetCommitInfo.commitList();
		LocalDateTime var = LocalDateTime.now();
		LocalDateTime dataWinnerCommit = LocalDateTime.now();
		Integer index = 0;
		for(int k = 0;k < commits.size();k++) {
    		String message = commits.get(k).getFullMessage();
    		if (message.contains(ticketID +",") || message.contains(ticketID +"\r") || message.contains(ticketID +"\n")|| message.contains(ticketID + " ") || message.contains(ticketID +":")
					 || message.contains(ticketID +".")|| message.contains(ticketID + "/") || message.endsWith(ticketID) ||
					 message.contains(ticketID + "]")|| message.contains(ticketID+"_") || message.contains(ticketID + "-") || message.contains(ticketID + ")") ) 
    		{
				 if(var.isAfter(commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
					dataWinnerCommit = commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					var = commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				 }
    		}
		 }
		if(dataWinnerCommit.isAfter(dataUltima)) {
			index = Integer.parseInt(excellList.keySet().toArray()[size-1].toString());
			FV.put(index, excellList.get(size).keySet().toString());
			return FV;
		}
		 for(int k = 1;k<=size;k++ ) {
			String stringDate = excellList.get(k).values().toString().substring(1,17);
			LocalDateTime date = LocalDateTime.parse(stringDate);
			if(dataWinnerCommit.isBefore(date)) {
				index = Integer.parseInt(excellList.keySet().toArray()[k-1].toString());
				FV.put(index, excellList.get(k).keySet().toString());
				break;
			}
		 }
		 return FV;
	}
	public static HashMap <Integer, String> gettingAV(String TicketID, Integer dimension, JSONArray affectedVersion) throws JSONException, IOException {
		HashMap<Integer, String> AV = new HashMap<Integer, String>();
		HashMap <Integer, HashMap> excellList = GetReleaseInfo.hashMapCreation();
		Integer size = excellList.size();
		Integer index = 0;
		if (dimension >=1) {
        	for(int m = 0;m <dimension;m++ ) {
            	String s = affectedVersion.getJSONObject(m).getString("name");
            	for(int k = 1;k<=size;k++ ) {
        			String versionList = excellList.get(k).keySet().toString().substring(1,6);
            		if(s.contains(versionList)) {
        				index = Integer.parseInt(excellList.keySet().toArray()[k-1].toString());
            			AV.put(index, excellList.get(k).keySet().toString());
        			}
        		}
            }
        }
		else {
			
		}
		return AV;
	}
	public static HashMap<Integer, String> gettingIV(String ticketID, Integer dimension, JSONArray affectedVersion) throws JSONException, IOException {
		HashMap <Integer, String> AV = gettingAV(ticketID, dimension, affectedVersion);
		HashMap <Integer, String> IV = new HashMap <Integer, String>();
		for(int k = 0; k<AV.size();k++)
		{
			IV.put(Integer.parseInt(AV.keySet().toString().substring(1,2)), AV.values().toString()) ;
			break;
		}
		return IV;
	}
}
