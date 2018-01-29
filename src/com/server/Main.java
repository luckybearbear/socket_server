package com.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.util.ReadDirector;
import com.util.ReadProperty;
import com.util.ServerThread;

public class Main {
	private ReadProperty r =new ReadProperty();
	private FileInputStream fis;
	private DataOutputStream dos;
	 private Socket server;

	
	public void runServer(int port) throws IOException {
		ServerSocket ss=new ServerSocket(port);//服务端监听8899这个端口
		try {
			while(true){ 
				server =ss.accept();//服务端接受客户端的连接
				ServerThread serverThread=new ServerThread(server);
				serverThread.start();
				sendFile();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 /**
     * 向服务端传输文件
     * @throws Exception
     */
    public String sendFile() throws Exception {
        try {
            String dir = r.propertyRead("Server","dir");//读取配置文件key = dir
            String fileName= ReadDirector.getFiles(dir);//需要上传的文件的路径目录下的修改日期最新的文件名
            File file = new File(dir+"/"+fileName);
            if(file.exists()) {
                fis = new FileInputStream(file);
                dos = new DataOutputStream(server.getOutputStream());
                // 文件名和长度
                dos.writeUTF(file.getName());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();
                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
//                long progress = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
//                    progress += length;
                    /*System.out.print("| " + (100*progress/file.length()) + "% |");*/
                }
                return "======== 文件传输成功 ========";
            }
            else{
                return "文件不存在";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
                fis.close();
            if(dos != null)
                dos.close();
            server.close();
        }
		return null;
    }
}
