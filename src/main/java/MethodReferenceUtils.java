import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


public class MethodReferenceUtils {
  @SuppressWarnings("unchecked")
  public static <T> Method findReferencedMethod(Class clazz, Consumer<T> invoker) {
    AtomicReference<Method> ref = new AtomicReference<>();
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
      ref.set(method);
      return null;
    });
    try {
      invoker.accept((T) enhancer.create());
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(String.format("Invalid method reference on class [%s]", clazz));
    }

    Method method = ref.get();
    if (method == null) {
      throw new IllegalArgumentException(String.format("Invalid method reference on class [%s]", clazz));
    }

    return method;
  }


}