package deliverable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import oggetti.Release;

public class GetJavaFile {
	public static void commitHistory(ArrayList<Release> releaseList) throws  GitAPIException, IOException	{
		Path repoPath = Paths.get("/home/alessandro/eclipse-workspace/bookkeeper");
		try (Git git = Git.init().setDirectory(repoPath.toFile()).call()) {
		    }
		InitCommand init = Git.init();
		init.setDirectory(repoPath.toFile());
		try (Git git = init.call()) {
		    	//
		    }
		try (Git git = Git.open(repoPath.toFile())) {
			   for (RevCommit commit : releaseList.get(0).getCommitList()) {
			    	String commitID = commit.getName();
			        if (commitID != null && !commitID.isEmpty())
			        {
			            LogCommand logs2 = git.log().all();
			            Repository repository = logs2.getRepository();
			            TreeWalk tw = new TreeWalk(repository);
			            tw.setRecursive(true);
			            RevCommit commitToCheck = commit;
			            tw.addTree(commitToCheck.getTree());
			            for (RevCommit parent : commitToCheck.getParents())
			            {
			                tw.addTree(parent.getTree());
			            }
			            while (tw.next()){
			                int similarParents = 0;
			                for (int i = 1; i < tw.getTreeCount(); i++) {
			                	if (tw.getFileMode(i) == tw.getFileMode(0) && tw.getObjectId(0).equals(tw.getObjectId(i))) {
			                		similarParents++;
			                	}  	
			                }   
			                if (similarParents == 0) {
			                	if(tw.getPathString().endsWith(".java")) {
			                		//System.out.println("File names: " + tw.getPathString());
			                        releaseList.get(0).getFileList().add(tw.getPathString());
			                	}
			                }
			            }
			           }
			        }
		    }
		}
	}
	
