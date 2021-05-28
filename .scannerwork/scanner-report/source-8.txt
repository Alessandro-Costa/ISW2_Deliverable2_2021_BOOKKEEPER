package deliverable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

import oggetti.TicketObjectVersionID;


 
public class GetCommitInfo{
	private GetCommitInfo() {
	    throw new IllegalStateException("Utility class");
	  }
	private static Integer prescelto = 0;
	private static String uriB = "/home/alessandro/eclipse-workspace/bookkeeper/.git"; //se prescelto = 0
	private static String uriZ = "/home/alessandro/eclipse-workspace/zookeeper/.git";	//se prescelto != 0
	public static List<RevCommit> commitList() throws IOException, GitAPIException {
		List<RevCommit> ticketCommits= new ArrayList <>();
		var build = new RepositoryBuilder();
		if(prescelto == 0) {
			Repository rep = build.setGitDir(new File(uriB)).readEnvironment().findGitDir().build();
			var git = new Git(rep);
			Iterable<RevCommit> log = git.log().call();
			for(RevCommit r : log) {
				ticketCommits.add(r);	
			}
			git.close();
			return ticketCommits;
		}
		else {
			Repository rep = build.setGitDir(new File(uriZ)).readEnvironment().findGitDir().build();
			var git = new Git(rep);
			Iterable<RevCommit> log = git.log().call();
			for(RevCommit r : log) {
				ticketCommits.add(r);	
			}
			git.close();
			return ticketCommits;
		}
		
		
	}
	public static void commitListTicket(String ticketID, TicketObjectVersionID info ) throws IOException, GitAPIException {
		if(prescelto == 0) {
			var dir= new File(uriB);
			var build = new RepositoryBuilder();
			Repository rep = build.setGitDir(dir).readEnvironment().findGitDir().build();
			var git = new Git(rep);
			Iterable<RevCommit> log = git.log().call();
			for(RevCommit r : log) {
				if(r.getFullMessage().contains(ticketID)) {
					info.addCommitList(r);	
				}	
			}
			git.close();
		}
		else {
			var dir= new File(uriZ);
			var build = new RepositoryBuilder();
			Repository rep = build.setGitDir(dir).readEnvironment().findGitDir().build();
			var git = new Git(rep);
			Iterable<RevCommit> log = git.log().call();
			for(RevCommit r : log) {
				if(r.getFullMessage().contains(ticketID)) {
					info.addCommitList(r);	
				}	
			}
			git.close();
		}
	}
}