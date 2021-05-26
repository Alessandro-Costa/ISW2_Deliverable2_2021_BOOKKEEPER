package deliverable;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import oggetti.JavaFile;
import oggetti.Release;


public class Metrics {
	private static Repository repo;
	private static String REPO = "/home/alessandro/eclipse-workspace/bookkeeper/.git";
	private static Logger logger = Logger.getLogger(Metrics.class.getName());
	private static String URI = "/home/alessandro/eclipse-workspace/bookkeeper/";
	
	public static void nR(List<Release> releaseList) throws IOException {
		var repoBuilder = new FileRepositoryBuilder();
		repo = repoBuilder.setGitDir(new File(REPO)).readEnvironment().findGitDir().setMustExist(true).build();
		for(Release release : releaseList) {
			System.out.println("Release ==" + release.getClassification());
			List<JavaFile> fileList = new ArrayList<>(); 
			List<Integer> chgSetSizeList = new ArrayList<>();
			
			for(RevCommit commit : release.getCommitList()) {
				 var df = new DiffFormatter(DisabledOutputStream.INSTANCE); 
				 df.setRepository(repo);
				 df.setDiffComparator(RawTextComparator.DEFAULT);
				 df.setDetectRenames(true);
				 
				 
				 String authName = commit.getAuthorIdent().getName();
				 List<DiffEntry> diffs = GetReleaseInfo.getDiffs(commit);
				 if (diffs != null) {
					analyzeDiffEntryMetrics(diffs, fileList, authName, chgSetSizeList, df);
				 }
			}
			System.out.println("###\n\n");
			setFileRelease(fileList, release);
		}
	}
	public static void analyzeDiffEntryMetrics(List<DiffEntry> diffs, List<JavaFile> fileList, String authName, List<Integer> chgSetSizeList, DiffFormatter df) {
	 	var numDiff = 0 ; 
	 	for (DiffEntry diffEntry : diffs) {
			if (diffEntry.toString().contains(".java")) { 
				numDiff++;
			}
	 	}

		for (DiffEntry diff : diffs) {
			var type = diff.getChangeType().toString();
		
			if (diff.toString().contains(".java") && (type.equals("MODIFY") || type.equals("DELETE"))) { 
				String file;
				
				if (type.equals("DELETE")) {
					 file = diff.getOldPath();
				 }
				 else {
					 file = diff.getNewPath();
				 }
				System.out.println("FILE == " + file);
				
				addFileList(fileList, file, authName, numDiff, diff, df);
				System.out.println("######\n\n");

			}
		}
		
	 }

	 public static void addFileList(List<JavaFile> fileList, String fileName, String authName, int numDiff, DiffEntry diff, DiffFormatter df) {
		 var count = 0 ; 
		 var locAdded = 0;
		 var locDeleted = 0;
		 try {
			 for(Edit edit : df.toFileHeader(diff).toEditList()) {
				 locAdded += edit.getEndB()-edit.getBeginB();
				 locDeleted += edit.getEndA() - edit.getBeginA();
			 }
		 }catch(IOException e){
			 e.printStackTrace();
		 }
		 
		 int churn = locAdded - locDeleted;
		 System.out.println("LOC ADDED == " + locAdded);
		 System.out.println("LOC DELETED == " + locDeleted);
		 System.out.println("CHURN == " + churn);
		 
		 if (fileList.isEmpty()) {
			 System.out.println("LISTA VUOTA");
			 var javaFile = new JavaFile(fileName);
			 javaFile.setNr(1);
			 List<String> listAuth = new ArrayList<>();
			 listAuth.add(authName);
			 javaFile.setListNAuth(listAuth);
			 javaFile.setChgSetSize(numDiff);
			 List<Integer> chgSetSizeList = new ArrayList<>();
			 chgSetSizeList.add(numDiff);
			 javaFile.setChgSetSizeList(chgSetSizeList);
			 javaFile.setLOCadded(locAdded);
			 List<Integer> locAddedList = new ArrayList<>();
			 locAddedList.add(locAdded);
			 javaFile.setLOCaddedList(locAddedList);
			 javaFile.setChurn(churn);
			 List<Integer> churnList = new ArrayList<>();
			 churnList.add(churn);
			 javaFile.setChurnList(churnList);
			 fileList.add(javaFile);
			 count = 1;
		}
		 else {
			 for ( JavaFile file : fileList) {
				 if (file.getName().equals(fileName)) {
					 System.out.println("FILE PRESENTE NELLA LISTA ");

					 file.setNr(file.getNr()+1);
					 file.getnAuthList().add(authName);
					 file.setChgSetSize(file.getChgSetSize()+ numDiff);
					 file.getChgSetSizeList().add(numDiff);
					 file.setLOCadded(file.getLOCadded()+locAdded);
					 file.getLOCaddedList().add(locAdded);
					 file.setChurn(file.getChurn() + churn);
					 file.getChurnList().add(churn);
					 count =1;
				 }
			 }
		 }
		 
		 if (count == 0) { //vuol dire che il nome del file non e' presente in fileList, quindi lo aggiungo
			 System.out.println("FILE NON PRESENTE NELLA LISTA ");

			 var javaFile = new JavaFile(fileName);
			 javaFile.setNr(1);
			 List<String> listAuth = new ArrayList<>();
			 listAuth.add(authName);
			 javaFile.setListNAuth(listAuth);
			 javaFile.getnAuthList().add(authName);
			 javaFile.setChgSetSize(numDiff);
			 List<Integer> chgSetSizeList = new ArrayList<>();
			 chgSetSizeList.add(numDiff);
			 javaFile.setChgSetSizeList(chgSetSizeList);
			 javaFile.setLOCadded(locAdded);
			 List<Integer> locAddedList = new ArrayList<>();
			 locAddedList.add(locAdded);
			 javaFile.setLOCaddedList(locAddedList);
			 javaFile.setChurn(churn);
			 List<Integer> churnList = new ArrayList<>();
			 churnList.add(churn);
			 javaFile.setChurnList(churnList);
			 
			 fileList.add(javaFile);
		 }
	 }
	 public static void setFileRelease(List<JavaFile> fileList, Release release) {
		 for (JavaFile javaFile : fileList) {
			
			 List<String> nAuth = javaFile.getnAuthList();
			 List<Integer> chgSetSize = javaFile.getChgSetSizeList();
			 List<Integer> locAdded = javaFile.getLOCaddedList();
			 List<Integer> churn = javaFile.getChurnList();

			 

			 for (JavaFile fileRel : release.getFileList()) {
				 if (javaFile.getName().equals(fileRel.getName())) {
					 fileRel.setNr(fileRel.getNr() + javaFile.getNr());
					 List<String> listAuth = fileRel.getnAuthList();
					 listAuth.addAll(nAuth);
					 listAuth = listAuth.stream().distinct().collect(Collectors.toList());
					 fileRel.setListNAuth(listAuth);
					 fileRel.setChgSetSize(fileRel.getChgSetSize()+javaFile.getChgSetSize());
					 List<Integer> chgSetSizeList = fileRel.getChgSetSizeList();
					 chgSetSizeList.addAll(chgSetSize);
					 fileRel.setChgSetSizeList(chgSetSizeList);
					 fileRel.setLOCadded(fileRel.getLOCadded()+javaFile.getLOCadded());
					 List<Integer> locAddedList = fileRel.getLOCaddedList();
					 locAddedList.addAll(locAdded);
					 fileRel.setLOCaddedList(locAddedList);
					 
					 fileRel.setChurn(fileRel.getChurn()+javaFile.getChurn());
					 List<Integer> churnList = fileRel.getChurnList();
					 churnList.addAll(churn);
					 fileRel.setChurnList(churnList);
				
					 
				 }
				 
				 if(fileRel.getoldPaths()!=null && fileRel.getoldPaths().contains(javaFile.getName())) {
					 
					 fileRel.setNr(fileRel.getNr() + javaFile.getNr());
					 List<String> listAuth = fileRel.getnAuthList();
					 listAuth.addAll(nAuth);
					 listAuth = listAuth.stream().distinct().collect(Collectors.toList());
					 fileRel.setListNAuth(listAuth);
					 fileRel.setChgSetSize(fileRel.getChgSetSize() + javaFile.getChgSetSize());
					 List<Integer> chgSetSizeList = fileRel.getChgSetSizeList();
					 chgSetSizeList.addAll(chgSetSize);
					 fileRel.setChgSetSizeList(chgSetSizeList);
					 
					 fileRel.setLOCadded(fileRel.getLOCadded() + javaFile.getLOCadded());
					 List<Integer> locAddedList = fileRel.getLOCaddedList();
					 locAddedList.addAll(locAdded);
					 fileRel.setLOCaddedList(locAddedList);
					 
					 fileRel.setChurn(fileRel.getChurn()+javaFile.getChurn());
					 List<Integer> churnList = fileRel.getChurnList();
					 churnList.addAll(churn);
					 fileRel.setChurnList(churnList);
					 
				 }
			 }
			 

		 }
	 }
	 
	 public static int loc(TreeWalk treewalk, Repository repository) throws IOException {
			
			ObjectLoader loader = repository.open(treewalk.getObjectId(0));
			var output = new ByteArrayOutputStream();
			
			loader.copyTo(output);
			
			var filecontent = output.toString();
			var token= new StringTokenizer(filecontent,"\n");
			
			var count=0;
			while(token.hasMoreTokens()) {
				count++;
				token.nextToken();
			}
			return count;
	}
}
