package deliverable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONArray;
import org.json.JSONException;

import oggetti.Release;
import oggetti.VersionObject;
import utility.FileLogger;

public class VersionGenerator {
	private  VersionGenerator(){
	    throw new IllegalStateException("Utility class");
	  }
	public static VersionObject gettingOV(LocalDateTime creationDate,List<VersionObject> listVersion){
		var oV = new VersionObject();
		Integer k = 0;
		Integer size = listVersion.size();
		LocalDateTime dataUltima = listVersion.get(size-1).getDate();
		if(creationDate.isAfter(dataUltima)) {
			 oV.addIdVersion(listVersion.get(size-1).getId(),listVersion.get(size-1).getVersion());
			return oV;
		}
		for(k = 0;k<size;k++ ) {
			if(creationDate.isBefore(listVersion.get(k).getDate())) {
				oV.addIdVersion(listVersion.get(k).getId(),listVersion.get(k).getVersion());
				break;
			}
		}
		return oV;
	}
	
	public static VersionObject gettingFV(String ticketID,List<VersionObject> listVersion) throws  IOException, GitAPIException {
		var fV = new VersionObject();
		Integer size = listVersion.size();
		LocalDateTime dataUltima = listVersion.get(size-1).getDate();
		List<RevCommit> commits = GetCommitInfo.commitList();
		var variable = LocalDateTime.now();
		var dataWinnerCommit = LocalDateTime.now();
		Integer k = 0;
		Integer g = 0;
		for(k = 0;k < commits.size();k++) {
    		String message = commits.get(k).getFullMessage();
    		if (message.contains(ticketID +",") || message.contains(ticketID +"\r") || message.contains(ticketID +"\n")|| message.contains(ticketID + " ") || message.contains(ticketID +":")
					 || message.contains(ticketID +".")|| message.contains(ticketID + "/") || message.endsWith(ticketID) ||
					 message.contains(ticketID + "]")|| message.contains(ticketID+"_") || message.contains(ticketID + "-") || message.contains(ticketID + ")") &&  variable.isAfter(commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) 
    		{
				 
					dataWinnerCommit = commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					variable = commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				 
    		}
		 }
		if(dataWinnerCommit.isAfter(dataUltima)) {
			fV.addIdVersion(listVersion.get(size-1).getId(), listVersion.get(size-1).getVersion());
			return fV;
		}
		for(g= 0;g<size;g++ ) {
			if(dataWinnerCommit.isBefore(listVersion.get(g).getDate())) {
				fV.addIdVersion(listVersion.get(g).getId(), listVersion.get(g).getVersion());
				break;
			}
		 }
		 return fV;
	}
	public static List<VersionObject> gettingAV(Integer dimension, JSONArray affectedVersion,List<VersionObject> listVersion) throws JSONException {
		ArrayList<VersionObject> aV = new ArrayList <>();
		Integer size = listVersion.size();
		Integer m = 0;
		Integer k = 0;
		if (dimension >=1) {
        	for(m = 0;m <dimension;m++ ) {
            	var s = affectedVersion.getJSONObject(m).getString("name");
            	for(k = 0;k<size;k++ ) {
        			String versionList = listVersion.get(k).getVersion();
            		if(s.contains(versionList)) {
        				aV.add(listVersion.get(k));
        			}
        		}
            }
        }
		return aV;
	}
	public static VersionObject gettingIV(List<VersionObject> aV){
		Integer k;
		var iV = new VersionObject();
		for( k = 0; k<aV.size();k++) {
			iV = aV.get(0);
		}
		return iV;
	}
	public static List<Release> releaseCreate(List<RevCommit> commitList, List<LocalDateTime> releases) {
		List<RevCommit> tempCommitList = new ArrayList<>();
		List<Release> releaseList = new ArrayList<>();
		Integer k = 0;
		Integer count = 0;
		for(k = 0; k < releases.size(); k++) {
			var release = new Release();
			for(RevCommit commit : commitList) {	
				if(commit.getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(releases.get(k))) {
     			   tempCommitList.add(commit);
            	   release.setDate(releases.get(k));
            	   release.setClassification(k+1);
            	   release.setRelease(releases.get(k).toString());
     		  }
            }
			release.setCommitList(tempCommitList);
			release.setFileList(new ArrayList <>());
			try {
   				GetJavaFile.commitHistory(release);
   			} catch (IOException e) {
   				e.printStackTrace();
   			}
			releaseList.add(release);
			count+=release.getFileList().size();
        }
		FileLogger.getLogger().info("|||||||||||||||||||||||||||||||||||||" +count);
		return releaseList;
	}
}
