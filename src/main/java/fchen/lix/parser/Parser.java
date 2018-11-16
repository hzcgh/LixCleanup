package fchen.lix.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import fchen.lix.common.LixDecorator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;


public class Parser {

  public static void main(String[] args) throws IOException {
    File file = new File("/Users/fchen1/IdeaProjects/TestTmp/src/main/java/fchen/lix/example/Work.java");
    inspectJavaFile(file);
  }

  public static void inspectJavaFile(File file) throws IOException {
    CompilationUnit cu;
    FileInputStream in = new FileInputStream(file);
    try {
      cu = JavaParser.parse(in);
    } finally {
      in.close();
    }
    new VoidVisitorAdapter<Object>() {
      @Override
      public void visit(MethodDeclaration n, Object arg) {
        super.visit(n, arg);
        //System.out.println(n.getName());
        n.getAnnotations().forEach(annotation -> {
          if (annotation.getClass().equals(NormalAnnotationExpr.class)) {
            for (MemberValuePair pair : ((NormalAnnotationExpr) annotation).getPairs()) {
              //System.out.println(pair.getName() + " : " + pair.getValue());
            }
          }
        });
      }

      @Override
      public void visit(MethodCallExpr n, Object arg) {
        //System.out.println("Method: " + n.getScope().get() + " " + n.getNameAsString());
        //System.out.println(n.getParentNode().get());
        if (isMatch(n)) {
          n.removeScope().setName("enabled").setArguments(new NodeList<>());
          //System.out.println(n.remove());
        }
      }
    }.visit(cu, null);

    System.out.println(cu);
  }

  private static boolean isMatch(MethodCallExpr n) {
    boolean isScopeMatch = n.getScope()
        .map(expression -> expression instanceof NameExpr && ((NameExpr) expression).getNameAsString().equals(LixDecorator.class.getSimpleName()))
        .orElse(false);
    boolean isNameMatch = "decorateMethods".equals(n.getNameAsString());
    return isScopeMatch && isNameMatch;
  }
}