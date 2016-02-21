package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class SocketProcessor implements Runnable {

	private Queue<Socket>  inboundSocketQueue   = null;
	
	List<String> list_channel1 = null;
	List<String> list_channel2 = null;
	
	private Selector   readSelector    = null;
    StringBuilder output_string_builder = null;
    String output = new String();
      
	public SocketProcessor(Queue<Socket> socketQueue) {
		// TODO Auto-generated constructor stub
		this.inboundSocketQueue = socketQueue;
		output_string_builder = new StringBuilder();
		list_channel1 = new ArrayList<String>();
		list_channel2 = new ArrayList<String>();
			    
		try {
			this.readSelector         = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
            try{
                executeCycle();
            } catch(IOException e){
                e.printStackTrace();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
	
	private void executeCycle() throws IOException {
        takeNewSockets();
        readFromSockets();
        int new_len = output_string_builder.length();
        if(new_len > 0){
        	//Runtime.getRuntime().exec("clear");
        	System.out.println(output_string_builder.toString());
        	output_string_builder = new StringBuilder();
        }
    }
	
	private void takeNewSockets() throws IOException {
        Socket newSocket = this.inboundSocketQueue.poll();

        while(newSocket != null){
            SelectionKey key = newSocket.get_socket_channel().register(this.readSelector, SelectionKey.OP_READ);
            key.attach(newSocket);
            newSocket = this.inboundSocketQueue.poll();
        }
    }
	
	public void readFromSockets() throws IOException {
        int readReady = this.readSelector.selectNow();
        
        if(readReady > 0){
            Set<SelectionKey> selectedKeys = this.readSelector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                readFromSocket(key);
                keyIterator.remove();
            }
            selectedKeys.clear();
        }
        
        
    }
	
	private void readFromSocket(SelectionKey key) throws IOException {
        Socket socket = (Socket) key.attachment();
        
        
        String string_read = socket.read();
        
        List<String> color_values = Arrays.asList(string_read.split(" "));
        
        if(color_values.get(0).charAt(1) == '1'){
        	list_channel1.addAll(color_values);
        }
        if(color_values.get(1).charAt(1) == '2'){
        	list_channel2.addAll(color_values);
        }
        if(list_channel1.size() > 0 && list_channel2.size() > 0){
        	
        	generate_output();
        }
    }

	private void generate_output() {
		
		
		Channel ch1 = new Channel(list_channel1);
		Channel ch2 = new Channel(list_channel2);
		Integer count1 = new Integer(ch1.getQ().size());
		Integer count2 = new Integer(ch2.getQ().size());
		while((ch1.getQ().size()>0 && count1>0) && (ch2.getQ().size()>0 && count2>0)){
			ch1.match(ch2, output_string_builder);
			ch2.match(ch1, output_string_builder);
			count1--;
			count2--;
			
		}
		
	}


}
