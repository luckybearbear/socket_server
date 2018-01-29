package com.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


/*
 * 服务器端线程处理
 */
public class ServerThread extends Thread{
	
	public Socket socket;
	public ServerThread(Socket socket){
		this.socket=socket;
	}
	
	public void run(){
		InputStream is=null;
		InputStreamReader isr=null;
		BufferedReader br=null;
		OutputStream os=null;
		PrintWriter pw=null;
		ObjectInputStream ois=null;
		try {
			//3.通过getInputStream方法,接收来自客户端的输入流
			/*
			 * 从客户端接受对象类型数据
			 */
			is=socket.getInputStream();
			ois=new ObjectInputStream(is);
			/*
			 * 从客户端接受字符串数据
			 */
//			isr=new InputStreamReader(is);
//			br=new BufferedReader(isr);
//			String info=null;
//			while((info=br.readLine())!=null){
//				System.out.println("我是服务器，客户端发来消息："+info);
//			}
			//4.创建输出流，准备输出信息给客户端
			os=socket.getOutputStream();
			pw=new PrintWriter(os);
			pw.write("欢迎您");
			pw.flush();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.shutdownInput();
				//关闭其他资源
				if(pw!=null){
					pw.close();
				}
				if(os!=null){
					os.close();
				}
//				br.close();
//				isr.close();
				if(ois!=null){
					ois.close();
				}
				if(is!=null){
					is.close();
				}
				if(socket!=null){
					socket.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
