package deliverable;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.json.JSONException;

public class Main {
	public static void main(String[] args) throws IOException, JSONException, NoHeadException, GitAPIException{
	RetrieveTicketsID.reportTicket();
 }
}
