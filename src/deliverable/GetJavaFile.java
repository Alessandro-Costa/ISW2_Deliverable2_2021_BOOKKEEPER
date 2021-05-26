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
	public static void commitHistory(Release release) throws   IOException	{
		var repoPath = Paths.get("/home/alessandro/eclipse-workspace/bookkeeper");
		//var repoPath = Paths.get("/home/alessandro/eclipse-workspace/zookeeper");
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
	
