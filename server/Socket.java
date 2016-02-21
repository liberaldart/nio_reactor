package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;




public class Socket {
    private SocketChannel  socketChannel = null;
    private String output = null;
    private ByteBuffer readByteBuffer;
    
    public Socket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        readByteBuffer = ByteBuffer.allocate(1024);
        output = new String();
    }
    
    public SocketChannel get_socket_channel(){
    	return socketChannel;
    }
    

    public String read() throws IOException {
        
    	StringBuilder output_gen = new StringBuilder();
    	
    	//Read data into buffer: Buffer gets written to
    	int bytesRead = this.socketChannel.read(readByteBuffer);
    	int totalBytesRead = bytesRead;
        
    	//keep writing until you have written 64 bytes
        while(totalBytesRead < 10 && bytesRead > 0){
            bytesRead = this.socketChannel.read(readByteBuffer);
            totalBytesRead += bytesRead;
        }
        
        
        
        //Make buffer available for read
        readByteBuffer.flip();
        
        // read all the characters into output_gen
        output_gen.append(Charset.forName("ISO-8859-1").decode(readByteBuffer));
        
        //start reading output_gen from the end until you find a ' '
        int n = output_gen.length();
        int i = n - 1;
        for(;i>0; i--){
        	if(output_gen.charAt(i) == ' '){
        		break;
        	}
        }
        
        //Make buffer ready for another write
        readByteBuffer.clear();
        
        if(i==n-1){
        	//the last character in the output_gen is ' '
        	//we don't need to do anything with the readByteBuffer
        	//but we need to delete the last character from output_gen
        	output_gen.deleteCharAt(i);
        }
        
        //write partial string object back into the readByteBuffer
        if(i < n - 1){
        	// j = i+1 because the char at i was space, because i < n-1
        	for(int j = i+1; j<n; j++){
        		readByteBuffer.putChar(output_gen.charAt(j));
        	}
        }
        
        output_gen.delete(i, n);
        //output_gen = new StringBuilder(output_gen.substring(0, i));
        //output = Charset.forName("ISO-8859-1").decode(readByteBuffer).toString();
        //readByteBuffer.get(stored_partial);
        output = output_gen.toString();
        return output;
    }


}
