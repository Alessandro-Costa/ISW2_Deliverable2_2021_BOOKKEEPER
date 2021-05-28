package oggetti;

import java.util.ArrayList;
import java.util.List;

public class JavaFile {

	private String name;
	private String oldPath;
	private String newPath;
    private List<String> oldPaths;
	private String buggyness;
	private Integer size; // LOC
	private Integer locTouched;
	private Integer nr;
	private Integer nFix;
	private Integer nAuth;
	private List<String> listNAuth;
	private Integer locAdded;
	private List<Integer> locAddedList;
	private Integer maxLocAdded;
	private Integer avgLocAdded;
	private Integer churn;
	private List<Integer> churnList;
	private Integer maxChurn;
	private Integer avgChurn;
	private Integer chgSetSize;
	List<Integer> chgSetSizeList;
	private Integer maxChgSetSize;
	private Integer avgChgSetSize;
	private Integer age;
	
	public JavaFile() {
		this.size = 0;
		this.locTouched = 0;
		this.nr = 0;
		this.nFix = 0;
		this.nAuth = 0;
		this.listNAuth = new ArrayList<>();
		this.locAdded = 0;
		this.locAddedList = new ArrayList<>();
		this.maxLocAdded = 0;
		this.avgLocAdded = 0;
		this.churn = 0;
		this.churnList = new ArrayList<>();
		this.maxChurn = 0;
		this.avgChurn = 0;
		this.chgSetSize = 0;
		this.chgSetSizeList = new ArrayList<>();
		this.maxChgSetSize = 0;
		this.avgChgSetSize = 0;
		this.age = 0;
	}
	public JavaFile(String name) {
		this.size = 0;
		this.name = name;
		this.oldPath = "";
		this.locTouched = 0;
		this.nr = 0;
		this.nFix = 0;
		this.nAuth = 0;
		this.listNAuth = new ArrayList<>();
		this.locAdded = 0;
		this.locAddedList = new ArrayList<>();
		this.maxLocAdded = 0;
		this.avgLocAdded = 0;
		this.churn = 0;
		this.churnList = new ArrayList<>();
		this.maxChurn = 0;
		this.avgChurn = 0;
		this.chgSetSize = 0;
		this.chgSetSizeList = new ArrayList<>();
		this.maxChgSetSize = 0;
		this.avgChgSetSize = 0;
		this.age = 0;
	}

	// get
	public Integer getAge() {
		return age;
	}
	public List<String> getoldPaths() {
		return oldPaths;
	}
	public String getName() {
		return name;
	}

	public String getBugg() {
		return buggyness;
	}

	public String getoldPath() {
		return oldPath;
	}
	public String getnewPath() {
		return newPath;
	}

	public Integer getSize() {
		return size;
	}

	public Integer getLOCtouched() {
		return locTouched;
	}

	public Integer getLOCadded() {
		return locAdded;
	}
	public List<Integer> getLOCaddedList() {
		return locAddedList;
	}
	public Integer getChurn() {
		return churn;
	}
	public List<Integer> getChurnList() {
		return churnList;
	}
	public Integer getChgSetSize() {
		return chgSetSize;
	}

	public Integer getMaxlocAdded() {
		return maxLocAdded;
	}

	public Integer getAvglocAdded() {
		return avgLocAdded;
	}

	public Integer getNr() {
		return nr;
	}

	public Integer getMaxChgSetSize() {
		return maxChgSetSize;
	}

	public Integer getAvgChgSetSize() {
		return avgChgSetSize;
	}

	// set
	public void setAge(Integer age) {
		this.age = age;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setBugg(String buggyness) {
		this.buggyness = buggyness;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setLOCadded(Integer locAdded) {
		this.locAdded = locAdded;
	}
	public void setLOCaddedList(List<Integer> locAddedList) {
		this.locAddedList = locAddedList;
	}
	public void setLOCtouched(Integer locTouched) {
		this.locTouched = locTouched;
	}

	public void setChurn(Integer churn) {
		this.churn = churn;
	}
	public void setChurnList(List<Integer> churnList) {
		this.churnList = churnList;
	}

	public void setChgSetSize(Integer chgSetSize) {
		this.chgSetSize = chgSetSize;
	}

	public void setNr(Integer nr) {
		this.nr = nr;
	}

	public void setMaxLocAdded(Integer maxLOCAdded) {
		this.maxLocAdded = maxLOCAdded;
	}

	public void setAvgLOCAdded(Integer avgLOCAdded) {
		this.avgLocAdded = avgLOCAdded;
	}

	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public void setMaxChgSetSize(Integer maxChgSetSize) {
		this.maxChgSetSize = maxChgSetSize;
	}

	public void setAvgChgSetSize(Integer avgChgSetSize) {
		this.avgChgSetSize = avgChgSetSize;
	}
	public void setChgSetSizeList(List<Integer> chgSetSizeList) {
		this.chgSetSizeList = chgSetSizeList;
	}
	public List<Integer> getChgSetSizeList(){
		return chgSetSizeList;
	}
	public Integer getnFix() {
		return nFix;
	}

	public void setnFix(Integer nFix) {
		this.nFix = nFix;
	}

	public Integer getnAuth() {
		return nAuth;
	}
	public List<String> getnAuthList(){
		return listNAuth;
	}
	public void setnAuth(Integer nAuth) {
		this.nAuth = nAuth;
	}
	public void setListNAuth(List<String> listNAuth) {
		this.listNAuth = listNAuth;
	}
	public Integer getMAXChurn() {
		return maxChurn;
	}

	public void setMAXChurn(Integer maxChurn) {
		this.maxChurn = maxChurn;
	}

	public Integer getAVGChurn() {
		return avgChurn;
	}

	public void setAVGChurn(Integer aVGChurn) {
		this.avgChurn = aVGChurn;
	}
	public void addSize() {
		this.size ++;
	}
}
