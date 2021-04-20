package deliverable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONArray;
import org.json.JSONException;

public class VersionGenerator {
	
	private final static ArrayList <VersionObject> LISTVERSION = GetReleaseInfo.listVersion();
	
	public static VersionObject gettingOV(LocalDateTime creationDate) throws IOException, JSONException {
		VersionObject OV = new VersionObject();
		Integer size = LISTVERSION.size();
		LocalDateTime dataUltima = LISTVERSION.get(size-1).getDate();
		if(creationDate.isAfter(dataUltima)) {
			 OV.addIdVersion(LISTVERSION.get(size-1).getId(),LISTVERSION.get(size-1).getVersion());
			return OV;
		}
		for(int k = 0;k<size;k++ ) {
			if(creationDate.isBefore(LISTVERSION.get(k).getDate())) {
				OV.addIdVersion(LISTVERSION.get(k).getId(),LISTVERSION.get(k).getVersion());
				break;
			}
		}
		return OV;
	}
	
	public static VersionObject gettingFV(String ticketID) throws NoHeadException, IOException, GitAPIException, JSONException {
		VersionObject FV = new VersionObject();
		Integer size = LISTVERSION.size();
		LocalDateTime dataUltima = LISTVERSION.get(size-1).getDate();
		ArrayList<RevCommit> commits = GetCommitInfo.commitList();
		LocalDateTime var = LocalDateTime.now();
		LocalDateTime dataWinnerCommit = LocalDateTime.now();
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
			FV.addIdVersion(LISTVERSION.get(size-1).getId(), LISTVERSION.get(size-1).getVersion());
			return FV;
		}
		 for(int k = 0;k<size;k++ ) {
			if(dataWinnerCommit.isBefore(LISTVERSION.get(k).getDate())) {
				FV.addIdVersion(LISTVERSION.get(k).getId(), LISTVERSION.get(k).getVersion());
				break;
			}
		 }
		 return FV;
	}
	public static ArrayList<VersionObject> gettingAV(String TicketID, Integer dimension, JSONArray affectedVersion) throws JSONException, IOException {
		ArrayList<VersionObject> AV = new ArrayList <VersionObject>();
		Integer size = LISTVERSION.size();
		if (dimension >=1) {
        	for(int m = 0;m <dimension;m++ ) {
            	String s = affectedVersion.getJSONObject(m).getString("name");
            	for(int k = 0;k<size;k++ ) {
        			String versionList = LISTVERSION.get(k).getVersion();
            		if(s.contains(versionList)) {
        				AV.add(LISTVERSION.get(k));
        			}
        		}
            }
        }
		else {
			
		}
		return AV;
	}
	public static VersionObject gettingIV(String ticketID, Integer dimension, JSONArray affectedVersion) throws JSONException, IOException {
		ArrayList<VersionObject> AV = gettingAV(ticketID, dimension, affectedVersion);
		VersionObject IV = new VersionObject();
		for(int k = 0; k<AV.size();k++)
		IV = AV.get(0);
		return IV;
	}
}
