package deliverable;

import java.util.ArrayList;

import oggetti.VersionObject;

public class Proportion {
	public static boolean proportion(VersionObject FV, VersionObject OV, ArrayList<VersionObject> AV,VersionObject IV, ArrayList<VersionObject> listVersion,Integer count) {
		ArrayList <Integer> P = new ArrayList<Integer>();
		Integer scartati = 0;
		Integer indexFV = FV.getId();
       	Integer indexOV = OV.getId();
       	Integer indexIV = IV.getId();
          if(IV.getId()==null) {
           	System.out.println("Entro nel for maledetto dove IV non esiste");
           	if(P.size()<=4) {
           		P.add(0);
           		return true;
           	}
           	else {
       			Integer predictedIV = indexFV-(indexFV-indexOV)*((P.get(count-1)+P.get(count-2)+P.get(count-3)+P.get(count-4))/4);
       			if(predictedIV > OV.getId()) {
       				scartati++;
       				return false;
       			}
       			for(int n = 0;n<listVersion.size();n++) {
       				if(predictedIV == listVersion.get(n).getId()) {
       					AV.add(listVersion.get(n));
       					IV.addIdVersion(listVersion.get(n).getId(), listVersion.get(n).getVersion());
       					return true;
       				}
       			}
           		System.out.println(predictedIV);
           		if(indexFV-indexOV !=0) {
           			P.add((indexFV-predictedIV)/(indexFV-indexOV));
           			return true;
           		}
           		else {
           			System.out.println("Sono entrato nel caso divisiore = 0");
           			P.add(0);
           			return true;
           		}
       		}
           }
           else {
           		if(indexFV-indexOV !=0) {
           			P.add((indexFV-indexIV)/(indexFV-indexOV));
           			return true;
           		}
           		else {
           			System.out.println("Sono entrato nel caso divisiore = 0");
           			P.add(0);
           			return true;
           		}
           	 }
	}
}
