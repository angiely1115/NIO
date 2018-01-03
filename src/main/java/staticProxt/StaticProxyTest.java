package staticProxt;

public class StaticProxyTest {
     public static void main(String[] args){
    	 //调用处理器
    	 //Zhangsan ：委托类
    	 People people = new ZhangsanDad(new Zhangsan());
    	 people.zhaoduixiang();
     }
}
