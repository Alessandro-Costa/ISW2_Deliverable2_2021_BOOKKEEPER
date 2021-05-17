package deliverable;

import java.util.ArrayList;
import java.util.List;

import oggetti.VersionObject;

public class Proportion {
	private Proportion() {
	    throw new IllegalStateException("Utility class");
	  }
	public static boolean proportion(VersionObject fV, VersionObject oV, List<VersionObject> aV,VersionObject iV, List<VersionObject> listVersion,Integer count) {
		ArrayList <Integer> p = new ArrayList<>();
		Integer indexFV = fV.getId();
       	Integer indexOV = oV.getId();
       	Integer indexIV = iV.getId();
       	Integer n = 0;
          if(iV.getId()==null) {
        	System.out.println("Entro nel for maledetto dove IV non esiste");
           	if(p.size()<=4) {
           		p.add(0);
           		return true;
           	}
           	else {
       			Integer predictedIV = indexFV-(indexFV-indexOV)*((p.get(count-1)+p.get(count-2)+p.get(count-3)+p.get(count-4))/4);
       			if(predictedIV > oV.getId()) {
       				return false;
       			}
       			for(n = 0;n<listVersion.size();n++) {
       				if(predictedIV.equals(listVersion.get(n).getId() )) {
       					aV.add(listVersion.get(n));
       					iV.addIdVersion(listVersion.get(n).getId(), listVersion.get(n).getVersion());
       					return true;
       				}
       			}
           		System.out.println(predictedIV);
           		if(indexFV-indexOV !=0) {
           			p.add((indexFV-predictedIV)/(indexFV-indexOV));
           			return true;
           		}
           		else {
           			System.out.println("Sono entrato nel caso divisiore = 0");
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
           			System.out.println("Sono entrato nel caso divisiore = 0");
           			p.add(0);
           			return true;
           		}
           	 }
	}
}
