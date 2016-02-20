package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;




public class Socket {
    private SocketChannel  socketChannel = null;
    private StringBuilder output = null;
    
    
    public Socket(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        output = new StringBuilder();
    }
    
    public SocketChannel get_socket_channel(){
    	return socketChannel;
    }
    
    public String getOut(){
    	return output.toString();
    }

    public int read(ByteBuffer byteBuffer) throws IOException {
        int bytesRead = this.socketChannel.read(byteBuffer);
        int totalBytesRead = bytesRead;
        
        while(bytesRead != -1){
        	byteBuffer.flip();
            output.append(Charset.forName("ISO-8859-1").decode(byteBuffer));
            byteBuffer.compact();
            bytesRead = this.socketChannel.read(byteBuffer);
            totalBytesRead += bytesRead;
        }
        return totalBytesRead;
    }


}
