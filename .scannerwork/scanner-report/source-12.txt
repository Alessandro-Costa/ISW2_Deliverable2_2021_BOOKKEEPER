package deliverable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import oggetti.JavaFile;
import oggetti.Release;
import utility.FileLogger;

public class GetJavaFile {
	private GetJavaFile() {
	    throw new IllegalStateException("Utility class");
	  }
	private static Path repoPathB = Paths.get("/home/alessandro/eclipse-workspace/bookkeeper");
	private static Path repoPathZ = Paths.get("/home/alessandro/eclipse-workspace/zookeeper");
	private static Integer prescelto = 0;
	public static void commitHistory(Release release) throws   IOException	{
    InitCommand init = Git.init();
    
	if(prescelto == 0) {
 		init.setDirectory(repoPathB.toFile());
 		
 		    }
     else {
 		init.setDirectory(repoPathZ.toFile());
 		
     }
     try (var git = Git.open(repoPathB.toFile())) {
			List <JavaFile> tempList = new ArrayList<> ();
			List <String> tempString = new ArrayList<>();
			for (RevCommit commit :release.getCommitList()) {
			        try(var tw = new TreeWalk(git.getRepository())){
			        	tw.reset(commit.getTree());
			        	tw.setRecursive(true);
			            while (tw.next()){
			            var file = new JavaFile();
			            if (tw.getPathString().endsWith(".java") && !tempString.contains(tw.getPathString())) {
			                		file.setName(tw.getPathString());    
			                		tempList.add(file);
			                		tempString.add(tw.getPathString());
			                		FileLogger.getLogger().info(tw.getPathString());
			                		FileLogger.getLogger().info(String.valueOf(tempList.size()));
			                        release.setFileList(tempList);
			                	
			                }
			            }
			           }
			        }
		}
	
	}
	
}
	
