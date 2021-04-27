package oggetti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.revwalk.RevCommit;

public class Release {
	private List<RevCommit> commit;
	private LocalDateTime date;
	private Integer classification;
	private String release;
	private List<String> javaFileList;
	public Release() {
		
	}
	public Release(LocalDateTime date,Integer classification,String release, ArrayList<String> javaFileList) {
		this.commit = new ArrayList<>();
		this.date = date;
		this.classification = classification;
		this.release = release;
		this.javaFileList = javaFileList;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public Integer getClassification() {
		return classification;
	}
	public String getRelease() {
		return release;
	}
	public List<RevCommit> getCommitList(){
		return commit;
	}
	public List<String> getFileList(){
		return javaFileList;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public void setClassification(Integer classification) {
		this.classification = classification;
	}
	public void setRelease(String release) {
		this.release = release;
	}
	public void setCommitList(List<RevCommit> commitList) {
		this.commit = commitList;
	}
	public void setFileList(List<String> javaFileList) {
		this.javaFileList = javaFileList;
	}
}
