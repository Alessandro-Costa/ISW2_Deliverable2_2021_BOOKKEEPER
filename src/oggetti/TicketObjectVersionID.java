package oggetti;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jgit.revwalk.RevCommit;

import utility.FileLogger;


public class TicketObjectVersionID {
	private String ticketID;
	private VersionObject oV;
	private VersionObject fV;
	private List<VersionObject> aV;
	private VersionObject iV;
	private List<RevCommit> commitList;
	
	public TicketObjectVersionID(VersionObject oV, VersionObject fV,List<VersionObject> aV, VersionObject iV, String ticketID) {
		this.oV = oV;
		this.fV = fV;
		this.aV = aV;
		this.iV = iV;
		this.commitList = new ArrayList<>();
		this.ticketID = ticketID;
	}
	public TicketObjectVersionID() {
		this.oV = new VersionObject();
		this.fV = new VersionObject();
		this.aV = new ArrayList<>();
		this.iV = new VersionObject();
		this.commitList = new ArrayList<>();
	}
	public void printInfo(String ticketID) {
		FileLogger.getLogger().info("Sto stampando l'OV del relativo:"+ticketID);    
		FileLogger.getLogger().info("Sto stampando la versione dell'OV:"+oV.getIdVersion());    
		FileLogger.getLogger().info("Sto stampando l'FV del relativo:"+ticketID);
		FileLogger.getLogger().info("Sto stampando la versione dell'FV:"+fV.getIdVersion());
	        for(var r = 0;r < aV.size();r++) {
	        	FileLogger.getLogger().info("Sto stampando l'AV del relativo:" + ticketID + aV.get(r).getIdVersion());
	        }
	        FileLogger.getLogger().info("Sto stampando l'IV del relativo:" + ticketID + iV.getIdVersion());
	        FileLogger.getLogger().info(ticketID);
	      
	        
	}
	public String getTicketID() {
		return ticketID;
	}
	public VersionObject getIV() {
		return iV;
	}
	public List<VersionObject> getAV() {
		return aV;
	}
	public List<RevCommit> getCommitList(){
		return commitList;
	}
	public List<RevCommit> addCommitList(RevCommit commit) {
		this.commitList.add(commit);
		return commitList;
	}
	public void addFV(VersionObject fV) {
		this.fV = fV;
	}
	public void addOV(VersionObject oV) {
		this.oV = oV;
	}
	public void addAV(List<VersionObject> aV) {
		this.aV = aV;
	}
	public void addIV(VersionObject iV) {
		this.iV = iV;
	}
	public void addTicketId(String ticketID) {
		this.ticketID = ticketID;
	}
}
