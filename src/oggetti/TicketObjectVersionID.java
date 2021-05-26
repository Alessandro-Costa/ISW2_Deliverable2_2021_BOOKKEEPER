package oggetti;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jgit.revwalk.RevCommit;


public class TicketObjectVersionID {
	private static Logger logger = Logger.getLogger(TicketObjectVersionID.class.getName());
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
		this.ticketID = new String();
	}
	public void printInfo(String ticketID) {
		    //logger.log(Level.INFO,"Sto stampando l'OV del relativo: {}",ticketID);
		    System.out.println("Sto stampando l'OV del relativo:"+ticketID);
		    //logger.log(Level.INFO,"Sto stampando la versione dell'OV :{}", oV.getIdVersion());
		    System.out.println("Sto stampando la versione dell'OV:"+oV.getIdVersion());
		    //logger.log(Level.INFO,"Sto stampando l'FV del relativo:{}" , ticketID);
		    System.out.println("Sto stampando l'FV del relativo:"+ticketID);
		    //logger.log(Level.INFO,"Sto stampando la versione dell'FV: {}", fV.getIdVersion());
		    System.out.println("Sto stampando la versione dell'FV:"+fV.getIdVersion());
	        for(int r = 0;r < aV.size();r++) {
	        	System.out.println("Sto stampando l'AV del relativo:" + ticketID + aV.get(r).getIdVersion());
	        }
	        	System.out.println("Sto stampando l'IV del relativo:" + ticketID + iV.getIdVersion());
	        System.out.println(ticketID);
	        
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
