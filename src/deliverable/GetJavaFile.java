package deliverable;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import oggetti.JavaFile;
import oggetti.Release;

public class GetJavaFile {
	private GetJavaFile() {
	    throw new IllegalStateException("Utility class");
	  }
	/*public static void commitHistory2(List<Release> releaseList, Integer count) throws  GitAPIException, IOException	{
		Integer i = 1;
		var repoPath = Paths.get("/home/alessandro/eclipse-workspace/bookkeeper");
		try (var git = Git.init().setDirectory(repoPath.toFile()).call()){
		}
		InitCommand init = Git.init();
		init.setDirectory(repoPath.toFile());
		try (var git = init.call()) {
		    	
		    }
		try (var git = Git.open(repoPath.toFile())) {
			   for (RevCommit commit :releaseList.get(count).getCommitList()) {
				    String commitID = commit.getName();
			        if (commitID != null && !commitID.isEmpty())
			        {
			            LogCommand logs2 = git.log().all();
			            var repository = logs2.getRepository();
			            var tw = new TreeWalk(repository);
			            tw.setRecursive(true);
			            RevCommit commitToCheck = commit;
			            tw.addTree(commitToCheck.getTree());
			            for (RevCommit parent : commitToCheck.getParents())
			            {
			                tw.addTree(parent.getTree());
			            }
			            while (tw.next()){
			                var similarParents = 0;
			                for (i = 1; i < tw.getTreeCount(); i++) {
			                	if (tw.getFileMode(i) == tw.getFileMode(0) && tw.getObjectId(0).equals(tw.getObjectId(i))) {
			                		similarParents++;
			                	}  	
			                }   
			                if (similarParents == 0 && tw.getPathString().endsWith(".java")) {
			                		List <String> tempList = new ArrayList<> ();
			                		tempList.add(tw.getPathString());
			                        releaseList.get(count).setFileList(tempList);
			                	
			                }
			            }
			            tw.close();
			           }
			        }
		    }
		}*/
	public static void commitHistory(Release release) throws   IOException	{
		var repoPath = Paths.get("/home/alessandro/eclipse-workspace/bookkeeper");
		InitCommand init = Git.init();
		init.setDirectory(repoPath.toFile());
		try (var git = Git.open(repoPath.toFile())) {
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
			                		System.out.println(tw.getPathString());
			                		System.out.println(tempList.size());
			                        release.setFileList(tempList);
			                	
			                }
			            }
			           }
			        }
		    }
		}
	}
	
