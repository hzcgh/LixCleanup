package fchen.lix.example;

import fchen.lix.common.LixDecorator;


public class Main {
  public static void main(String[] args) {
    Work work = new Work();
    work.doSomething();
    LixDecorator.closeClient();

    // test pull request
  }
}
