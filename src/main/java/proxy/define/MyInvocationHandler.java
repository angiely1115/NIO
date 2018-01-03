package proxy.define;

import java.lang.reflect.Method;

public interface MyInvocationHandler {
    public Object invoke(Object proxy,Method method,Object obj) throws Throwable;
}
