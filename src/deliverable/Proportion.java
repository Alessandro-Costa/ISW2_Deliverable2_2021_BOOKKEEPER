package deliverable;

import java.util.ArrayList;
import java.util.List;

import oggetti.VersionObject;
import utility.FileLogger;

public class Proportion {
	private Proportion() {
	    throw new IllegalStateException("Utility class");
	  }
	public static boolean proportion(VersionObject fV, VersionObject oV, List<VersionObject> aV,VersionObject iV, List<VersionObject> listVersion,Integer count) {
		ArrayList <Integer> p = new ArrayList<>();
		Integer indexFV = fV.getId();
       	Integer indexOV = oV.getId();
       	Integer indexIV = iV.getId();
          if(iV.getId()==null) {
        	FileLogger.getLogger().info("Entro nel for maledetto dove IV non esiste");
           	if(p.size()<=4) {
           		p.add(0);
           		return true;
           	}
           	else {
       			Integer predictedIV = indexFV-(indexFV-indexOV)*((p.get(count-1)+p.get(count-2)+p.get(count-3)+p.get(count-4))/4);
       			if(predictedIV > oV.getId()) {
       				return false;
       			}
       			iterativeFunc(listVersion,predictedIV, aV, iV);
       			
       			FileLogger.getLogger().info(String.valueOf(predictedIV));
           		if(indexFV-indexOV !=0) {
           			p.add((indexFV-predictedIV)/(indexFV-indexOV));
           			return true;
           		}
           		else {
           			FileLogger.getLogger().info("Sono entrato nel caso divisiore = 0");           			
           			p.add(0);
           			return true;
           		}
       		}
           }
           else {
           		if(indexFV-indexOV !=0) {
           			p.add((indexFV-indexIV)/(indexFV-indexOV));
           			return true;
           		}
           		else {
           			FileLogger.getLogger().info("Sono entrato nel caso divisiore = 0");
           			p.add(0);
           			return true;
           		}
           	 }
	}
	public static void iterativeFunc(List<VersionObject> listVersion, Integer predictedIV, List<VersionObject> aV, VersionObject iV) {
		Integer n = 0;
		for(n = 0;n<listVersion.size();n++) {
				if(predictedIV.equals(listVersion.get(n).getId() )) {
					aV.add(listVersion.get(n));
					iV.addIdVersion(listVersion.get(n).getId(), listVersion.get(n).getVersion());
				}
			}
	}//se è cambiato qualcosa bisogna rimmetterlo nel proportion con il return TRUE all'interno del for
}
