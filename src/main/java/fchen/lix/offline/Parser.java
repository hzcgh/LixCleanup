package fchen.lix.offline;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import fchen.lix.common.LixDecorator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class Parser {
  private static final String ENABLED = "\"enabled\"";

  public static void main(String[] args) throws IOException {
    File file = new File("/Users/fchen1/IdeaProjects/TestTmp/src/main/java/fchen/lix/example/Work.java");
    inspectJavaFile(file);
  }

  public static void inspectJavaFile(File file) throws IOException {
    CompilationUnit cu;
    FileInputStream in = new FileInputStream(file);
    Set<Node> nodesToBeRemoved = new HashSet<>();
    try {
      cu = JavaParser.parse(in);
    } finally {
      in.close();
    }
    new VoidVisitorAdapter<Object>() {
      @Override
      public void visit(MethodDeclaration n, Object arg) {
        super.visit(n, arg);
        n.getAnnotations().forEach(annotation -> {
          if (annotation.getClass().equals(NormalAnnotationExpr.class)) {
            boolean shouldMethodBeRemoved = true;
            for (MemberValuePair pair : ((NormalAnnotationExpr) annotation).getPairs()) {
              // TODO here we can get lix key and treatment name, hence we can check if it's 100% ramped
              if (ENABLED.equals(pair.getValue().toString())) {
                shouldMethodBeRemoved = false;
              }
            }
            if (shouldMethodBeRemoved) {
              nodesToBeRemoved.add(n);
            } else {
              nodesToBeRemoved.add(annotation);
            }
          }
        });
      }

      @Override
      public void visit(MethodCallExpr n, Object arg) {
        if (isMatch(n)) {
          n.removeScope().setName("enabled").setArguments(new NodeList<>());
        }
      }
    }.visit(cu, null);
    nodesToBeRemoved.forEach(Node::remove);
    //System.out.println(cu);
    writeToFile(file, cu.toString());
  }

  private static boolean isMatch(MethodCallExpr n) {
    boolean isScopeMatch = n.getScope()
        .map(expression -> expression instanceof NameExpr && ((NameExpr) expression).getNameAsString().equals(LixDecorator.class.getSimpleName()))
        .orElse(false);
    boolean isNameMatch = "decorateMethods".equals(n.getNameAsString());
    return isScopeMatch && isNameMatch;
  }

  private static void writeToFile(File file, String content ) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write(content);
    writer.close();
  }
}