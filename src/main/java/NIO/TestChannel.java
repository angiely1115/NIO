package NIO;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author lvrongzhuan
 * @desc:
 * @createTime 2018/1/314:01
 * @Modified by:
 */
public class TestChannel {
    //分散和聚集
    @Test
    public void testSactter() throws IOException{
        RandomAccessFile raf1 = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\订单新接口.txt", "rw");
        //获取通道
        FileChannel channel = raf1.getChannel();

        //2. 分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(500);
        ByteBuffer buf2 = ByteBuffer.allocate(500);

        //3. 分散读取
        ByteBuffer[] bufs = {buf1,buf2};
        channel.read(bufs);
      /*  while (channel.read(buf1)!=-1){
            System.out.println("容量："+buf1.capacity());
            System.out.println("当前位置："+buf1.position());
            System.out.println("limit："+buf1.limit());
            buf1.flip();
            System.out.println(new String(buf1.array(), 0, buf1.limit()));
        }*/


        for (ByteBuffer byteBuffer : bufs) {
            System.out.println("容量："+byteBuffer.capacity());
            System.out.println("当前位置："+byteBuffer.position());
            System.out.println("limit："+byteBuffer.limit());
            byteBuffer.flip();
        }


        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("-----------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        channel.close();
    }
    @Test
    public void testGether() throws IOException {
        //4. 聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
        FileChannel channel2 = raf2.getChannel();
        ByteBuffer bufs = ByteBuffer.allocate(1024);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteBuffer[] byteBuffers = {bufs,buffer};
        channel2.write(byteBuffers);
        channel2.close();
    }

    /**
     * 通道间数据传输
     * @throws IOException
     */
    @Test
    public void test3() throws IOException{
        FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\订单新接口.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\Administrator\\Desktop\\订单新接口2.txt"), StandardOpenOption.WRITE);
		inChannel.transferTo(0, inChannel.size(), outChannel);
//        inChannel.transferTo(0,inChannel.size(),outChannel);
//        outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }
}
