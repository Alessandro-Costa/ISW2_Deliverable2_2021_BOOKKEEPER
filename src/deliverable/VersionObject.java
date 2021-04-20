package deliverable;

import java.time.LocalDateTime;

public class VersionObject {
	
	private Integer id;
	private String version;
	private LocalDateTime date;
	
	public VersionObject() {
		
	}
	
	public VersionObject(Integer id, String version) {
		this.id = id;
		this.version = version;
	}
	
	public void add(Integer id, String version, LocalDateTime date) {
		this.id = id;
		this.version = version;
		this.date = date;
		}
	public void addIdVersion(Integer id, String version) {
		this.id = id;
		this.version = version;
		}
	public Integer getId() {
		return this.id;
	}
	public String getVersion() {
		return this.version;
	}
	public LocalDateTime getDate() {
		return this.date;
	}
	public String getAll() {
		String all = "ID:"+ this.id +"\n"+"Version:"+ this.version +"\n"+"Date:"+ this.date;
		return all;
	}
	public String getIdVersion() {
		return "ID:"+this.id +"\n"+"Version:" + this.version;
	}
}
