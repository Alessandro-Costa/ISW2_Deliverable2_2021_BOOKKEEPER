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

import oggetti.Release;
import oggetti.VersionObject;

public class VersionGenerator {
	
	//private final static ArrayList <VersionObject> LISTVERSION = GetReleaseInfo.listVersion();
	
	public static VersionObject gettingOV(LocalDateTime creationDate,ArrayList<VersionObject> listVersion) throws IOException, JSONException {
		VersionObject OV = new VersionObject();
		Integer size = listVersion.size();
		LocalDateTime dataUltima = listVersion.get(size-1).getDate();
		if(creationDate.isAfter(dataUltima)) {
			 OV.addIdVersion(listVersion.get(size-1).getId(),listVersion.get(size-1).getVersion());
			return OV;
		}
		for(int k = 0;k<size;k++ ) {
			if(creationDate.isBefore(listVersion.get(k).getDate())) {
				OV.addIdVersion(listVersion.get(k).getId(),listVersion.get(k).getVersion());
				break;
			}
		}
		return OV;
	}
	
	public static VersionObject gettingFV(String ticketID,ArrayList<VersionObject> listVersion) throws NoHeadException, IOException, GitAPIException, JSONException {
		VersionObject FV = new VersionObject();
		Integer size = listVersion.size();
		LocalDateTime dataUltima = listVersion.get(size-1).getDate();
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
			FV.addIdVersion(listVersion.get(size-1).getId(), listVersion.get(size-1).getVersion());
			return FV;
		}
		 for(int k = 0;k<size;k++ ) {
			if(dataWinnerCommit.isBefore(listVersion.get(k).getDate())) {
				FV.addIdVersion(listVersion.get(k).getId(), listVersion.get(k).getVersion());
				break;
			}
		 }
		 return FV;
	}
	public static ArrayList<VersionObject> gettingAV(String TicketID, Integer dimension, JSONArray affectedVersion,ArrayList<VersionObject> listVersion) throws JSONException, IOException {
		ArrayList<VersionObject> AV = new ArrayList <VersionObject>();
		Integer size = listVersion.size();
		if (dimension >=1) {
        	for(int m = 0;m <dimension;m++ ) {
            	String s = affectedVersion.getJSONObject(m).getString("name");
            	for(int k = 0;k<size;k++ ) {
        			String versionList = listVersion.get(k).getVersion();
            		if(s.contains(versionList)) {
        				AV.add(listVersion.get(k));
        			}
        		}
            }
        }
		else {
			
		}
		return AV;
	}
	public static VersionObject gettingIV(ArrayList<VersionObject> AV) throws JSONException, IOException {
		VersionObject IV = new VersionObject();
		for(int k = 0; k<AV.size();k++) {
			IV = AV.get(0);
		}
		return IV;
	}
	public static void releaseCreate(ArrayList<VersionObject> listVersion, ArrayList<RevCommit> commitList,ArrayList<LocalDateTime> releases,Integer count) {
		ArrayList<RevCommit> tempCommitList = new ArrayList<RevCommit>();
		ArrayList<String> tempStringList = new ArrayList<String>(); 
		ArrayList<Release> releaseList = new ArrayList<Release>();
		Release release = new Release();
		for(int m = 0; m<commitList.size();m++) {
     	   if(commitList.get(m).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(releases.get(count))) {
     		   tempCommitList.add(commitList.get(m));
     		   release.setCommitList(tempCommitList);
         	   release.setDate(releases.get(count));
         	   release.setClassification(count);
         	   release.setRelease(releases.get(count).toString());
         	   release.setFileList(tempStringList);
         	   releaseList.add(release);
            }	      
        }
		try {
				GetJavaFile.commitHistory(releaseList);
			} catch (GitAPIException | IOException e) {
				e.printStackTrace();
			}
	}
}
