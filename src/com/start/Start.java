package com.start;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.server.Main;
import com.util.ReadProperty;

public class Start {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JLabel portPanel;
	private JTextField Jport;
	/**
	 * 入口
	 * 
	 * @param args
	 */

	public Start() {
		prepareGUI();
	}

	public static void main(String[] args) {
		Start s = new Start();
		s.showButtonDemo();
	}

	private void prepareGUI() {
		mainFrame = new JFrame("socket服务端");
		mainFrame.setSize(300, 200);
		mainFrame.setLayout(new GridLayout(4, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		ReadProperty r = new ReadProperty();
		String port = r.propertyRead("Server","SERVER_PORT");
		JPanel jp = new JPanel();  
        jp.setLayout(new GridLayout(1,3));  //2行2列的面板jp（网格布局）   
		portPanel = new JLabel("端口号："); 
		Jport = new JTextField(5);
		JButton updatePortbtn = new JButton("修改");
		Jport.setText(port);
		jp.add(portPanel);
		jp.add(Jport);
		jp.add(updatePortbtn);
		jp.setLayout(new FlowLayout());
		
		updatePortbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 修改server.properties文件
				try {
					ReadProperty r = new ReadProperty();
					r.updateProperty("Server", "SERVER_PORT",Jport.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);
		statusLabel.setSize(350, 100);
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		mainFrame.add(jp,BorderLayout.CENTER);//将整块面板定义在中间
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);
	}

	private void showButtonDemo() {
		headerLabel.setText("Control in action: Button");
		JButton okButton = new JButton("启动");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread("启动socket线程") {
					@Override
					public void run() {
						try {
							Main m = new Main();
							m.runServer(Integer.parseInt(Jport.getText()));
						} catch (Exception e1) {
							statusLabel.setText("socket服务无法启动！！！");
						}
					}
				}.start();
				statusLabel.setText("已启动socket服务,端口号："+Jport.getText());
			}
		});
		controlPanel.add(okButton);
		mainFrame.setVisible(true);
	}
}
