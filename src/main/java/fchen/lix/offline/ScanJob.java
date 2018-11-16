package fchen.lix.offline;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.commons.io.FileUtils;


public class ScanJob {

  public static final String GITHUB_LIX_CLEANUP_URL = "https://github.com/hzcgh/LixCleanup.git";
  public static final String COMMIT_MESSAGE = "lix cleanup";
  public static final String PROJECT_NAME = "LixCleanup";
  public static final String PATH_TO_FILE = "/src/main/java/fchen/lix/example/Work.java";

  public static void main(String[] args) throws Exception {
    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
    String path = "/Users/fchen1/tmp" + "/" + timestamp;
    File dir = new File(path);
    dir.mkdir();
    exec(dir,"git", "clone", GITHUB_LIX_CLEANUP_URL);
    File lixCleanupPath = new File(path+ "/" + PROJECT_NAME);
    exec(lixCleanupPath,"git", "checkout","-b",timestamp);
    Parser.inspectJavaFile(new File(lixCleanupPath + PATH_TO_FILE));
    exec(lixCleanupPath, "git", "commit","-am", COMMIT_MESSAGE);
    exec(lixCleanupPath, "git", "push", "-u", "origin", timestamp);
    exec(lixCleanupPath, "hub", "pull-request", "--no-edit");
    FileUtils.deleteDirectory(dir);
  }

  private static void exec(File directory, String... command) throws Exception{
    ProcessBuilder pb = new ProcessBuilder(command);
    pb.directory(directory);
    Process pr = pb.start();
    BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
    String line = null;
    while ((line = input.readLine()) != null) {
      System.out.println(line);
    }
    BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
    while ((line = error.readLine()) != null) {
      System.out.println(line);
    }
    pr.waitFor();
  }
}
