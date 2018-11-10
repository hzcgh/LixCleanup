package fchen.lix.example;

import fchen.lix.common.LixAnnotation;
import fchen.lix.common.LixDecorator;


public class Work {

  public void doSomething() {
    LixDecorator.decorateMethods(this.getClass(), Work::enabled, Work::control, Work::other);
  }

  @LixAnnotation(lixKey = "foo.bar", treatment = "enabled")
  public void enabled() {
    System.out.println("treatment enabled");
  }

  @LixAnnotation(lixKey = "foo.bar", treatment = "control")
  public void control() {
    System.out.println("treatment control");
  }

  @LixAnnotation(lixKey = "foo.bar", treatment = "other")
  public void other() {
    System.out.println("treatment other");
  }
}
