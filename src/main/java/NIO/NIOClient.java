package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClient {
	SocketChannel socketChannel;
	InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8080);
	Selector selector;
	//Selector selector1;
	ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
	ByteBuffer sendBuffer = ByteBuffer.allocate(1024);

	public NIOClient() throws IOException {
		// 构造client实例
		socketChannel = SocketChannel.open(serverAddress);
		// 设置为非阻塞式 NON-BLOCKING
		socketChannel.configureBlocking(false);
		//线程绑定就没有 线程池
//		socketChannel.connect(serverAddress);
//		// 构造selector实例
		selector = Selector.open();
//
//		// 向管家注册连接事件
//		client.register(selector, SelectionKey.OP_CONNECT);
		// Netty Reactor线程池组 Tomcat bootStap 
		//
		//selector1 = Selector.open();
		//线程池
	}

	public void session() throws IOException {

		if (socketChannel.isConnectionPending()) {
			socketChannel.finishConnect();
			socketChannel.register(selector, SelectionKey.OP_WRITE);
			System.out.println("【系统消息提示】 已经连接到服务器，请发送您的消息！");

		}

		Scanner scan = new Scanner(System.in);
		while (scan.hasNextLine()) {
			// 键盘输入的内容
			String msg = scan.nextLine();
			if ("".equals(msg)) {
				continue;
			}
			if ("exit".equalsIgnoreCase(msg)) {
				System.exit(0);
			}

			process(msg);
		}

	}

	private void process(String name) throws IOException {
		boolean waitHelp = true;
		Iterator<SelectionKey> iterator = null;
		Set<SelectionKey> keys = null;
		while (waitHelp) {
			try {
				int readys = selector.select();
				// 如果没有客人，继续轮询
				if (readys == 0) {
					continue;
				}

				keys = selector.selectedKeys();

				iterator = keys.iterator();

				// 一个一个key迭代检查
				while (iterator.hasNext()) {

					SelectionKey key = iterator.next();

					if (key.isValid() && key.isWritable()) {
						// 首先是判断是否可写 可写 就是代表客户端要对服务端发送信息
						sendBuffer.clear();
						sendBuffer.put(name.getBytes());
						sendBuffer.flip();

						socketChannel.write(sendBuffer);

						socketChannel.register(selector, SelectionKey.OP_READ);

						// 服务器发送信息回来给客户端 去读
					} else if (key.isValid() && key.isReadable()) {

						receiveBuffer.clear();
						int len = socketChannel.read(receiveBuffer);
						if (len > 0) {
							receiveBuffer.flip();
							System.out.println("服务端反馈的消息:" + new String(receiveBuffer.array(), 0, len));
							socketChannel.register(selector, SelectionKey.OP_WRITE);
							waitHelp = false;
						}
					}
					// 检查完之后，打发客户走
					iterator.remove();
				}
			} catch (IOException e) {
				((SelectionKey) keys).cancel();
				socketChannel.socket().close();
				socketChannel.close();

				return;
			}
		}

	}

	public static void main(String[] args) throws IOException {
		new NIOClient().session();
	}

}