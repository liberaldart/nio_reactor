package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

public class SocketAccepter implements Runnable {

	private int tcpPort = 0;
    private ServerSocketChannel serverSocket = null;

    private Queue<Socket> socketQueue = null;
    
	public SocketAccepter(int tcpPort, Queue<Socket> socketQueue) {
		// TODO Auto-generated constructor stub
		this.tcpPort     = tcpPort;
        this.socketQueue = socketQueue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
            this.serverSocket = ServerSocketChannel.open();
            this.serverSocket.bind(new InetSocketAddress(tcpPort));
        } catch(IOException e){
            e.printStackTrace();
            return;
        }


        while(true){ //Accept connections to the ServerSocketChannel
        	//and add the SocketChannel into a queue
            try{
                SocketChannel socketChannel = this.serverSocket.accept();

                //todo check if the queue can even accept more sockets.
                this.socketQueue.add(new Socket(socketChannel));

            } catch(IOException e){
                e.printStackTrace();
            }

        }

	}

}
