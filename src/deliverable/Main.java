package deliverable;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.json.JSONException;

import oggetti.VersionObject;

public class Main {
	public static void main(String[] args) throws IOException, JSONException, NoHeadException, GitAPIException{
	ArrayList<VersionObject>listVersion = GetReleaseInfo.listVersion();
	RetrieveTicketsID.reportTicket(listVersion);	
 }
}
