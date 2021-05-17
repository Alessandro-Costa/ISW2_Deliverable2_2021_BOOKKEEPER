package deliverable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONObject;

import oggetti.TicketObjectVersionID;


 
public class GetCommitInfo{
	private GetCommitInfo() {
	    throw new IllegalStateException("Utility class");
	  }
	public static List<RevCommit> commitList() throws IOException, GitAPIException {
		List<RevCommit> ticketCommits= new ArrayList <>();
		var dir= new File("/home/alessandro/eclipse-workspace/bookkeeper/.git");
		var build = new RepositoryBuilder();
		Repository rep = build.setGitDir(dir).readEnvironment().findGitDir().build();
		var git = new Git(rep);
		Iterable<RevCommit> log = git.log().call();
		for(RevCommit r : log) {
			ticketCommits.add(r);	
		}
		git.close();
		return ticketCommits;
	}
	public static void commitListTicket(String ticketID, TicketObjectVersionID info ) throws IOException, GitAPIException {
		var dir= new File("/home/alessandro/eclipse-workspace/bookkeeper/.git");
		var build = new RepositoryBuilder();
		Repository rep = build.setGitDir(dir).readEnvironment().findGitDir().build();
		var git = new Git(rep);
		Iterable<RevCommit> log = git.log().call();
		for(RevCommit r : log) {
			if(r.getFullMessage().contains(ticketID.toString())) {
				info.addCommitList(r);	
			}	
		}
		git.close();
	}
}