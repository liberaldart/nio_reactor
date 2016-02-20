package client;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import java.nio.channels.SocketChannel;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

 
public class MessagingClientStreaming{
    
	private String serverHostName;
	private int serverPort;
	private SocketChannel socketchannel = null;
	InfiniteStreamer inf_streamer1;
	InfiniteStreamer inf_streamer2;
	
	class InfiniteStreamer{

		private Character channel = null;
		private Supplier<String> supplier = null;
		public InfiniteStreamer(Character channelId){
			channel = channelId;
			supplier = this::getRandomColorString;
		}
		
		private String getRandomColorString() {
			// TODO Auto-generated method stub
			char[] rgb = new char[] {'R', 'G', 'B'};
			StringBuilder sb = new StringBuilder();
			Random r = new Random();
			sb.append(rgb[r.nextInt(3)]);
			sb.append(this.channel);
			sb.append('_');
			sb.append(r.nextInt(255));
			sb.append('\n');
			return sb.toString();
		}
		
		public Stream<String> getStream(){
			return Stream.generate(supplier);
		}
		
		

		
	}

	
	
	
	public MessagingClientStreaming(String host, int port){
		serverHostName = host;
		serverPort = port;
		inf_streamer1 = new InfiniteStreamer('1');
		inf_streamer2 = new InfiniteStreamer('2');
	}
	
	
	public void write(SocketChannel sc, final ByteBuffer s){
		try{
			s.flip();

			while(s.hasRemaining()) {
			    sc.write(s);
			}
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	public static void main(String[] args){
		MessagingClientStreaming client = new MessagingClientStreaming("localhost", 8000);
		try {
			client.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() throws IOException{
		// TODO Auto-generated method stub
		try {
			socketchannel = SocketChannel.open();
			socketchannel.connect(new InetSocketAddress(serverHostName, serverPort));
			
			Runnable task1 = () -> {
				inf_streamer1.getStream()
				.map(String::getBytes)
				.map(ByteBuffer::wrap)
				.limit(6)
				.forEach((s) -> write(socketchannel, s));
			};
			
			Thread t1 = new Thread(task1);
			t1.start();
			
			Runnable task2 = () -> {
				inf_streamer2.getStream()
				.map(String::getBytes)
				.map(ByteBuffer::wrap)
				.limit(6)
				.forEach((s) -> write(socketchannel, s));
			};
				
			Thread t2 = new Thread(task1);
			t2.start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
