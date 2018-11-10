public class Work {
  Decorator _decorator = new Decorator();

  public void doSomething() {
    _decorator.decorateMethod(this, Work::noArg);
  }

  @LixAnnotation(lixKey = "foo.bar", treatment = "enabled")
  public void noArg() {
    System.out.println("it's working!");
  }
}
