package fchen.lix.example;

import fchen.lix.common.LixAnnotation;
import fchen.lix.common.LixDecorator;

public class Work {

    public void doSomething() {
        enabled();
    }

    public void enabled() {
        System.out.println("treatment enabled");
    }
}
