package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import java.nio.channels.SocketChannel;

import java.util.Random;
 
public class MessagingClient2 implements Runnable{
    
	private String serverHostName;
	private int serverPort;
	private char channelId = '1';
	private SocketChannel socketchannel = null;
	
	public MessagingClient2(String host, int port, char channelid){
		serverHostName = host;
		serverPort = port;
		channelId = channelid;
	}
	
	
	private String getRandomColorString() {
		// TODO Auto-generated method stub
		char[] rgb = new char[] {'R', 'G', 'B'};
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		sb.append(rgb[r.nextInt(3)]);
		sb.append(channelId);
		sb.append('_');
		sb.append(r.nextInt(255));
		sb.append(' ');
		return sb.toString();
	}
	
	
	public static void main(String[] args){
		MessagingClient2 client1 = new MessagingClient2("localhost", 8000, '2');
		client1.run();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			socketchannel = SocketChannel.open();
			socketchannel.connect(new InetSocketAddress(serverHostName, serverPort));
			socketchannel.configureBlocking(false);
			
			
			while(true){
				String message = getRandomColorString();
				System.out.println(message);
				ByteBuffer bff = ByteBuffer.wrap(message.getBytes());
				while(bff.hasRemaining()){
					socketchannel.write(bff);
				}
			}
			//socketchannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
