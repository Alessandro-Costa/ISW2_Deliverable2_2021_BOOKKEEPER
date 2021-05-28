package deliverable;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONException;



public class Main {
	public static void main(String[] args) throws IOException, JSONException, GitAPIException{
		List<RevCommit> commitList = GetCommitInfo.commitList();
		GetReleaseInfo.listVersion(commitList);
 }
}
