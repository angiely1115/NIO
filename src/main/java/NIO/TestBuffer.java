package NIO;

import java.nio.ByteBuffer;

/**
 * @Author lvrongzhuan
 * @desc:
 * @createTime 2018/1/219:56
 * @Modified by:
 */
public class TestBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("初始容量："+buffer.capacity());
        System.out.println("初始当前位置："+buffer.position());
        System.out.println("初始limit:"+buffer.limit());
        System.out.println("------------------------------");
        buffer.put("2018".getBytes());
        System.out.println("写数据容量："+buffer.capacity());
        System.out.println("写数据始当前位置："+buffer.position());
        System.out.println("写数据limit:"+buffer.limit());
        System.out.println("读模式------------------------------");
        buffer.flip();
        System.out.println("读模式数据容量："+buffer.capacity());
        System.out.println("读模式数据始当前位置："+buffer.position());
        System.out.println("读模式数据limit:"+buffer.limit());
        while(buffer.hasRemaining())
        {
            System.out.print((char)buffer.get());
        }
        System.out.println("读完后------------------------------");
        System.out.println("读完后数据容量："+buffer.capacity());
        System.out.println("读完后数据始当前位置："+buffer.position());
        System.out.println("读完后数据limit:"+buffer.limit());
        buffer.rewind();//使得可重复读取
        System.out.println("重复读取------------------------------");
        System.out.println("重复读取数据容量："+buffer.capacity());
        System.out.println("重复读取数据始当前位置："+buffer.position());
        System.out.println("重复读取数据limit:"+buffer.limit());
        while(buffer.hasRemaining())
        {
            System.out.print((char)buffer.get());
        }
        buffer.clear();
        System.out.println("clean------------------------------");
        System.out.println("clean数据容量："+buffer.capacity());
        System.out.println("clean数据始当前位置："+buffer.position());
        System.out.println("clean数据limit:"+buffer.limit());
        //clean只是重置位置，buffer里面的数据仍然未清空 但是不能直接put写数据 如果想继续写数据需要带调用compact方法
        //原先的buffer的数据依然存在,clean方法和compact不能同时存在 clean后再写数据将会覆盖，compact则不会 因为他会先复制出来
        while(buffer.hasRemaining())
        {
            System.out.print((char)buffer.get());
        }
         buffer = buffer.compact();
        System.out.println("compact------------------------------");
        System.out.println("compact数据容量："+buffer.capacity());
        System.out.println("compact数据当前位置："+buffer.position());
        System.out.println("compact数据limit:"+buffer.limit());
        buffer.put("我们ll".getBytes());
        buffer.asCharBuffer();
        System.out.println("compact后写------------------------------");
        System.out.println("compact后写数据容量："+buffer.capacity());
        System.out.println("compact后写数据当前位置："+buffer.position());
        System.out.println("compact后写数据limit:"+buffer.limit());
        //原来buffer的数据依然存在
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
         buffer.get(bytes,0,bytes.length);
        System.out.println(new String(bytes));
        System.out.println("读后写------------------------------");
        System.out.println("读数据容量："+buffer.capacity());
        System.out.println("读数据当前位置："+buffer.position());
        System.out.println("读数据limit:"+buffer.limit());

    }
}
