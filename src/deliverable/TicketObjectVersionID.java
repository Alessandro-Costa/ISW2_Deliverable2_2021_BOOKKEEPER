package deliverable;

import java.util.ArrayList;

public class TicketObjectVersionID {
	private VersionObject OV;
	private VersionObject FV;
	private ArrayList<VersionObject> AV;
	private VersionObject IV;
	
	public TicketObjectVersionID(VersionObject OV, VersionObject FV,ArrayList<VersionObject> AV, VersionObject IV) {
		this.OV = OV;
		this.FV = FV;
		this.AV = AV;
		this.IV = IV;
	}
	public void printInfo(String ticketID) {
		System.out.println("Sto stampando l'OV del relativo:" + ticketID + OV.getIdVersion());
    	System.out.println("Sto stampando l'FV del relativo:" + ticketID + FV.getIdVersion());
        for(int r = 0;r < AV.size();r++) {
        	System.out.println("Sto stampando l'AV del relativo:" + ticketID + AV.get(r).getIdVersion());
        }
        System.out.println("Sto stampando l'IV del relativo:" + ticketID + IV.getIdVersion());
	}
}
