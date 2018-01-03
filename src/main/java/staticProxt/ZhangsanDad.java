package staticProxt;

public class ZhangsanDad implements People {
    //照片  鬼谷子引用
	Zhangsan zs;

	public ZhangsanDad(Zhangsan zs){
		this.zs = zs;
	}
   //Jvm 隐式实现
	public void zhaoduixiang() {
		before();
		zs.zhaoduixiang();
		after();
	}
  private void before(){
	  System.out.println("我是张三的爸爸，搞对象之前我先检查你的学历，身高，家境！");
  }
  private void after(){
	  System.out.println("通过所有审查，说明是一个好姑娘！");
  }
}