package ThreadDataShare.person;

import java.lang.ref.WeakReference;

public class TestWeakRefeses {

	public static void main(String[] args) {
		Person sam=new Person();
		WeakReference<Person> wr = new WeakReference<Person>(sam);
		while (true) {
			if(wr.get() !=null){
				//显示被调用
				System.out.println("对象依然存在"+wr);
			}else{
				System.out.println("弱引用对象已经GC");
			}
			
		}

	}

}
