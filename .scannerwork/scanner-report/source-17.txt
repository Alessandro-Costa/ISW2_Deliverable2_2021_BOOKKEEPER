package oggetti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.revwalk.RevCommit;

public class Release {
	private List<RevCommit> commit;
	private LocalDateTime date;
	private Integer classification;
	private String releaseString;
	private List<JavaFile> javaFileList;
	public Release() {
		
	}
	public Release(LocalDateTime date,Integer classification,String release) {
		this.commit = new ArrayList<>();
		this.date = date;
		this.classification = classification;
		this.releaseString = release;
		this.javaFileList = new ArrayList<>();
	}
	public LocalDateTime getDate() {
		return date;
	}
	public Integer getClassification() {
		return classification;
	}
	public String getRelease() {
		return releaseString;
	}
	public List<RevCommit> getCommitList(){
		return commit;
	}
	public List<JavaFile> getFileList(){
		return javaFileList;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public void setClassification(Integer classification) {
		this.classification = classification;
	}
	public void setRelease(String release) {
		this.releaseString = release;
	}
	public void setCommitList(List<RevCommit> commitList) {
		this.commit = commitList;
	}
	public void setFileList(List<JavaFile> javaFileList) {
		this.javaFileList = javaFileList;
	}
}
