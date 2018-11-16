package fchen.lix.common;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class LixDecoratorTest {

  @Test
  public void happyPath() {
    assertEquals(1,1);
  }

  @Test
  public void failedOnPurpose() {
    assertEquals(1,2);
  }
}