package MyHashMap;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K,V> implements MyMap<K,V>  {
	// 定义默认数组大小 16 defaulAddSizeFactor=useSize/defaulLenth 4/16 =0.25
		private static int defaulLenth = 1 << 4;//aka =16
		// 扩容标准 所使用的 useSize /数组长度 > 0.75 扩容
		// defaulAddSizeFactor过大 造成扩容概率变低 存储小 但是就是存 和 取效率降低
		// 0.9 有限的数组长度空间位置内会形成链表 在存或者取值都需要进行大量的遍历和判断（逻辑）
		//JDK通过大量的数据带入计算只会 得到比较完美的值
		private static double defaulAddSizeFactor = 0.75;
		// 使用数组位置的总是   可见性操作  将不会存在处理器特定内存当中  内存是其他线程不可见   JMM 当中有描述 
		private volatile int useSize;
		// 定义Map 骨架数据结构之一   数组
		private Entry<K, V>[] table = null;
		//新的数组  扩容后的数组
		private Entry<K, V>[] newtTable=null;
		// SPRING 门面模式运用
		public MyHashMap() {
			this(defaulLenth, defaulAddSizeFactor);
		}

		public MyHashMap(int length, double defaulAddSizeFactor) {
			if (length < 0) {
				throw new IllegalArgumentException("参数不能为负数" + length);

			}
			if (defaulAddSizeFactor <= 0 || Double.isNaN(defaulAddSizeFactor)) {
				throw new IllegalArgumentException("扩容标准必须是大于0的数字" + defaulAddSizeFactor);
			}
			this.defaulLenth = length;
			this.defaulAddSizeFactor = defaulAddSizeFactor;
			//内存当划分连续内存空间 数组初始化完成 16
			table = new Entry[defaulLenth];
		}
//key  sam1  123  
	// sam2  123
	// sam3  123
    //person =<id,name>
	@Override
	public V put(K k, V v) {
		//判断是否需要扩容
		if (useSize > defaulAddSizeFactor * defaulLenth) {
			up2Size();
		}
		//获取数组位置的方法
		int index = getIndex(k, table.length);
		Entry<K, V> entry = table[index];
		if (entry == null) {
			//Entry：存储在数组和链表当中的数据结果对象 
			table[index] = new Entry(k, v, null);
			useSize++;
			//有冲突
		} else if (entry != null) {
			//将当前放进来的Entry占用这个位置table[index]
			//原来在这个index位置的entry
			table[index] = new Entry(k, v, entry);
		}

		return table[index].getValue();
	}
   //定位方法
	private int getIndex(K k, int length) {
		int m=length-1;
		//使用 到
		int index =hash(k.hashCode()) & m;
		return index;
	}
  //要求一：具备自己的hash算法  jdk 1.7 版本
	//JDK大量数学运算确定 
	private int hash(int hashCode) {
		hashCode =hashCode^((hashCode>>>20)^(hashCode>>>12));
		return hashCode^((hashCode>>>7)^(hashCode>>>4));
	}
   //而且保证自我判断扩容（数组）
	private void up2Size() {
		//开辟一个空间
		newtTable =new Entry[2*defaulLenth];
		//将老数组的内容再次散列hash到新数组
		againHash(newtTable);
		
	}
    
	private void againHash(MyHashMap<K, V>.Entry<K, V>[] newtTable2) {
		List<Entry<K, V>> entryList = new ArrayList<MyHashMap<K, V>.Entry<K, V>>();
		// for出来就代表内容全部由遍历到entryList当中
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null) {
				continue;
			}
			// 说明数组上这个位置的Entry=table[i]!=null
			foundEntryByNext(table[i], entryList);

		}
		// 设置entryList
		if (entryList.size() > 0) {
			
			defaulLenth = 2 * defaulLenth;
			for (Entry<K, V> entry : entryList) {
				//将所有的链表打断
				if (entry.next != null) {
					entry.next = null;
				}
				useSize = 0;
				for(int i=0;i<table.length;i++){
					
				}
				table = null;
				table =newtTable2;
				put(entry.getKey(), entry.getValue());
			}
		}
		
	}
    //这个方法里面不存在我们的entry == null
	private void foundEntryByNext(MyHashMap<K, V>.Entry<K, V> entry, List<MyHashMap<K, V>.Entry<K, V>> entryList) {
		if (entry.next != null) {
			entryList.add(entry);
			// 递归 不断的一层一层取存entry
			foundEntryByNext(entry.next, entryList);
		} else {
			// 没有链表的情况entry.next == null
			entryList.add(entry);
		}
		
	}

	@Override
	public V get(K k) {
		// hashcode（new Person(1234,"Sammy")）-->hash-->getIndex-->最终索引位置
				int index = getIndex(k, table.length);
				if (table[index] == null) {
					throw new NullPointerException();
				}
				// key 存在情况
				return findValueByEqualKey(k, table[index]);
	}
	// 不同k 可能在同一个位置
	private V findValueByEqualKey(K k, MyHashMap<K, V>.Entry<K, V> entry) {
		if (k == entry.getKey() || k.equals(entry.getKey())) {
			return entry.getValue();
			//是否在下面链表位置
		} else if (entry.next != null) {
			// 循环一层一层递归 和传进来k相同的entry
			return findValueByEqualKey(k, entry.next);
		}
		return null;
	}

	// 创建一个内部存储的对象类型
	class Entry<K, V> implements MyMap.Entry<K, V> {
		//外界传进封装双列数据 key  value
		K k;
		V v;
		// next:压下去的Entry对象
		Entry<K, V> next;

		public Entry(K k, V v, Entry<K, V> next) {
			this.k = k;
			this.v = v;
			this.next = next;
		}

		public K getKey() {
			// TODO Auto-generated method stub
			return k;
		}

		public V getValue() {
			// TODO Auto-generated method stub
			return v;
		}

	}
}
