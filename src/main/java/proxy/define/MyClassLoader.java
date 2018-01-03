package proxy.define;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 自定义类加载器 主要是指定了我自己加载的类的位置
 * @author Sam
 *
 */
public class MyClassLoader extends ClassLoader {
	private File dir;

	public MyClassLoader(String path) {
		dir = new File(path);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		// 这里是执行父类的 jdk提供AppClassLoader 不执行这个findClass
		// return super.findClass(name);

		if (dir != null) {
			File clazzFile = new File(dir, name + ".class");
			if (clazzFile.exists()) {
				FileInputStream input = null;
				try {
					input = new FileInputStream(clazzFile);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = input.read(buffer)) != -1) {
						baos.write(buffer, 0, len);
					}

					return defineClass("proxy.define." + name,
							baos.toByteArray(), 0, baos.size());

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				

			}
		}
		return null;
	}
}
