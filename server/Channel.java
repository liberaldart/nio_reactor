package server;

import java.util.ArrayList;
import java.util.List;


public class Channel{
	private List<String> Rs;
	private List<String> Gs;
	private List<String> Bs;
	private List<String> my_list;
	
	public Channel(List<String> channel_list){
		Rs = new ArrayList<String>();
		Gs = new ArrayList<String>();
		Bs = new ArrayList<String>();
		my_list = channel_list;
		for(String str: channel_list){
			if(str.charAt(0) == 'R'){
				insertR(str);
			}
			if(str.charAt(0) == 'G'){
				insertG(str);
			}
			if(str.charAt(0) == 'B'){
				insertB(str);
			}
		}
	}
	
	public List<String> getQ(){
		return my_list;
	}
	
	public void match(Channel ch, StringBuilder output){
		
		if(getQ().size() < 1 || ch.getQ().size() < 1){
			return;
		}
		
			String str = ch.getQ().get(0);
			if(str.charAt(0) == 'R' && hasR()){
				//self updated
				String my_val = this.getR();
				getQ().remove(my_val);
				
				//remote channel's data updated
				ch.getQ().remove(str);
				ch.getR();
				
				//need to do placing of string 1 before 2
				if(str.charAt(1) == '1'){
					output.append(str + "," + my_val + " ");
				}
				else{
					output.append(my_val + "," + str + " ");
				}
				
				
				
			}
			if(str.charAt(0) == 'G' && hasG()){
				//self updated
				String my_val = this.getG();
				getQ().remove(my_val);
				
				//remote channel's data updated
				ch.getQ().remove(str);
				ch.getG();
				
				//need to do placing of string 1 before 2
				if(str.charAt(1) == '1'){
					output.append(str + "," + my_val + " ");
				}
				else{
					output.append(my_val + "," + str + " ");
				}
				
				
				
			}
			if(str.charAt(0) == 'B' && hasB()){
				//self updated
				String my_val = this.getB();
				getQ().remove(my_val);
				
				//remote channel's data updated
				ch.getQ().remove(str);
				ch.getB();
				
				//need to do placing of string 1 before 2
				if(str.charAt(1) == '1'){
					output.append(str + "," + my_val + " ");
				}
				else{
					output.append(my_val + "," + str + " ");
				}
				
				
				
			}
		
	}
	
	private void insertR(String _string){
		Rs.add(_string);
	}
	private void insertG(String _string){
		Gs.add(_string);
	}
	private void insertB(String _string){
		Bs.add(_string);
	}
	
	public boolean hasR(){
		if(Rs.size() > 0){
			return true;
		}
		return false;
	}
	public boolean hasG(){
		if(Gs.size() > 0){
			return true;
		}
		return false;
	}
	public boolean hasB(){
		if(Bs.size() > 0){
			return true;
		}
		return false;
	}
	
	public String getR(){
		if(hasR()){
			return Rs.remove(0);
		}
		return null;
		
	}
	public String getG(){
		if(hasG()){
		return Gs.remove(0);
		}
		return null;
	}
	
	public String getB(){
		if(hasB()){
		return Bs.remove(0);
		}
		return null;
	}
	
	
}
