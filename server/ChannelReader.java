package server;

import java.io.IOException;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ChannelReader {
	private SocketAccepter  socketAccepter  = null;
    private SocketProcessor socketProcessor = null;

    private int tcpPort = 0;
	
	public ChannelReader(int port){
		tcpPort = port;
	}
	
	public void start() throws IOException {

		//Maximum expected SocketChannel : 1024: Configure it through a property file
        Queue<Socket> socketQueue = new ArrayBlockingQueue<Socket>(1024);

        this.socketAccepter  = new SocketAccepter(tcpPort, socketQueue);


        
        this.socketProcessor = new SocketProcessor(socketQueue);

        Thread accepterThread  = new Thread(this.socketAccepter);
        Thread processorThread = new Thread(this.socketProcessor);

        accepterThread.start();
        processorThread.start();
    }
	
	public static void main(String[] args){
		ChannelReader server = new ChannelReader(8000);
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
