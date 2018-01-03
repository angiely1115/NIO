package proxy.define;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import sun.reflect.generics.scope.MethodScope;



/**
 * 我的自己写的创建通过参数产生特有的代理的工厂
 * 替换jdk给我们配的代理对象
 * @author Sam
 *
 */
public class MyProxy {
	//
	//产生的代理类要执行那个方法
	 protected MyInvocationHandler h;
	 //定义回车键
	 static String rt="\r\t";
	
	 private MyProxy() {
	    }
        
	    protected MyProxy(MyInvocationHandler h) {
	        this.h = h;
	    }
	    //创造一个内存当中的$Proxy0实例
	    public static Object createProxyInstance(ClassLoader loader,Class interf,MyInvocationHandler h) throws IllegalArgumentException,IOException{

	        if (h == null) {
	            throw new NullPointerException();
	        }
	        //实际运行这个动态里类构造一个java对象
	        System.out.println("=====================自定义：类构造一个代理类的java对象===========");
	        Method[]methods =interf.getMethods();
	        //内部运行时期的通过三个参数确定的代理类 .java类
	        //直接在运行时期创造我们的字节码 给jvm
	        //
	        String proxyClassString ="package proxy.define;"+rt
	        		+"import java.lang.reflect.Method;"+rt
	        		+"public class $Proxy0 implements "+interf.getName()+"{"+rt
	        		+"MyInvocationHandler h;"+rt
	        		+"public $Proxy0(MyInvocationHandler h){"+rt
	        		+"this.h=h;"+rt+"}"+getMethodString(methods,interf)
	        		+rt+"}";
	        //System.out.println("proxyClassString:"+proxyClassString);
           //将我们构造的自定义代理类 转换成文件
	        String fileName="F:/EclipseWorkspace/SamJavaPro/src/main/java/proxy/define/$Proxy0.java";
	        File file=new File(fileName);
	        FileWriter fw= new FileWriter(file);
	        fw.write(proxyClassString);
	        fw.flush();
	        fw.close();
	        //编译这个文件
	        JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
	        //
	        StandardJavaFileManager fileMangaer=compiler.getStandardFileManager(null, null, null);
	        Iterable units= fileMangaer.getJavaFileObjects(fileName);
	        //编译这个任务
	        CompilationTask compTast=compiler.getTask(null, fileMangaer, null, null, null, units);
	        compTast.call();
	        fileMangaer.close();
	        //编译完成后 是不是.java
	        file.delete();
	        //编译后就是class 文件   那么接下来我们就把这个class 文件   内存
	        MyClassLoader loader2 =new MyClassLoader("F:/EclipseWorkspace/SamJavaPro/src/main/java/proxy/define");
	      
	        try{
	        Class proxy0Class= loader2.findClass("$Proxy0");
	        //等类加载后返回 之后删除
	        File clazzFile= new File("F:/EclipseWorkspace/SamJavaPro/src/main/java/proxy/define/$Proxy0.class");
	        if(clazzFile.exists()){
	        	//物理路=路径下面编译字节码删除
	        	clazzFile.delete();
	        }
	        Constructor m= proxy0Class.getConstructor(MyInvocationHandler.class);
	        Object object= m.newInstance(h);
	        return object;
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        return null;
	    }
         //实现的方法
		private static String getMethodString(Method[] methods, Class interf) {
			String  proxyMe="";
			for(Method method:methods){
				proxyMe +="public void "+method.getName()+"() throws Throwable {"+rt
						+"Method md="+interf.getName()+".class.getMethod(\""
						+method.getName()+"\",new Class[]{});"+rt
						+" this.h.invoke(this, md, null);"+rt+"}"+rt;
						
			}
			//System.out.println("proxyMe:"+proxyMe);
			return proxyMe;
		}
		
	    
	    
}