import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class Decorator {

  public <T> void decorateMethod(T object, Consumer<T> invoker) {
    Method method = MethodReferenceUtils.findReferencedMethod(object.getClass(), invoker);
    System.out.println(method.getAnnotation(LixAnnotation.class).lixKey());
    System.out.println(method.getAnnotation(LixAnnotation.class).treatment());
    try {
      method.invoke(object);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
