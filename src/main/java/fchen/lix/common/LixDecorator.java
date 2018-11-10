package fchen.lix.common;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


public class LixDecorator {
  private final static LixClient lixClient = new LixClient();

  public static <T> void decorateMethods(Class clazz, Consumer<T>... invokers) {
    for (Consumer<T> invoker : invokers) {
      decorateMethod(clazz, invoker);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> void decorateMethod(Class clazz, Consumer<T> invoker) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
      String lixKey = method.getAnnotation(LixAnnotation.class).lixKey();
      String treatment = method.getAnnotation(LixAnnotation.class).treatment();
      String treatmentValue = lixClient.getTreatment(lixKey);
      if (!treatment.equals(treatmentValue)) {
        return null;
      }
      try {
        proxy.invokeSuper(obj, args);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    });
   try {
      invoker.accept((T) enhancer.create());
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(String.format("Invalid method reference on class [%s]", clazz));
    }
  }

  public static void closeClient() {
    lixClient.close();
  }
}