package total_system;

import java.awt.*;

import org.apache.log4j.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import IOdata_file.*;

import java.io.*;
import java.net.*;
import java.util.Vector;
public class chatClient extends JFrame implements Runnable,ActionListener{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(chatClient.class); 
	private static final long serialVersionUID = -7103711638195532808L;
	//private static final int SERVICE_PORT=13;
	int SERVICE_PORT;
	DataInputStream in;
	DataOutputStream out;
	ServerSocket server;
	Socket socket;
	String hostname;
	JTextField textField,nameField,portField;
	JTextArea textArea,textArea2;
	JLabel l1;
	//如下定义按钮：消息发送、文件下载、文件上传、连接服务器、断开服务器、管理接收到的文件
	JButton button,fButton,dButton,conButton,disButton,fileButton;
	JFileChooser chooser;
    static Vector<String> receive=new Vector();

	ButtonGroup group;
	Font font = new Font("Adobe 黑体 Std",0,16);
	JLabel label;
	String path;
 
	void init(){
		Container con = this.getContentPane();
		
		label=new JLabel();
		label.setBounds(0, 0, 800, 500);
		//设置消息发送按钮
		button=new JButton("发送消息");
		button.setFont(font);
		button.setBounds(670,425,110,30);
		button.addActionListener(this);
		con.add(button);
		
		//设置选择文件按钮
		fButton=new JButton("上传文件");
		fButton.setFont(font);
		fButton.setBounds(550, 425, 110, 30);
		fButton.addActionListener(this);
		con.add(fButton);
		
		//设置下载文件按钮
		dButton=new JButton("下载文件");
		dButton.setFont(font);
		dButton.setBounds(450, 425, 110, 30);
		dButton.addActionListener(this);
		con.add(dButton);
		//设置文件操作按钮
		fileButton=new JButton("管理接受文件");
		fileButton.setFont(font);
		fileButton.setBounds(300, 425, 130, 30);
		fileButton.addActionListener(this);
		con.add(fileButton);
		
		//设置文本发送区域
		textField=new JTextField();
		textField.setBounds(10,375,780,40);
		textField.setFont(font);
		textField.setOpaque(true);
		textField.setBorder(null);
		
		//键盘监听
		textField.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent e) {
		    	 
			        if(e.getKeyCode()==KeyEvent.VK_ENTER){
			        	try {
							out.writeUTF(textField.getText());
							textField.setText("");
						} catch (IOException e2) {}
			        }		        
			      }
			    });
		con.add(textField);
		
	
		//显示文件名称的文本区域
		nameField=new JTextField();
		nameField.setBounds(225, 25, 120, 25);
		nameField.setText("已接受的文件");
		nameField.setFont(font);
		nameField.setOpaque(false);
		nameField.setBorder(null);
		con.add(nameField);
		//显示端口号的文本区域
		portField=new JTextField();
		portField.setBounds(480, 20, 80, 25);
		portField.setText("1000");
		portField.setFont(font);
		portField.setOpaque(false);
		portField.setBorder(null);
		
		l1=new JLabel("localhost:");
		l1.setBounds(400, 20, 120, 25);
		con.add(l1);
		con.add(portField);
		
		conButton=new JButton("连接");
		disButton=new JButton("断开");
		disButton.setSelected(true);//设置该按钮为可用
		conButton.setSelected(true);
		conButton.setBounds(610, 17, 85, 30);
		disButton.setBounds(700, 17, 85, 30);
		conButton.addActionListener(this);
		disButton.addActionListener(this);
		group = new ButtonGroup();
		group.add(conButton);
		group.add(disButton);
		con.add(conButton);
		con.add(disButton);
		
		//运行日志记录区域
		textArea=new JTextArea();
		textArea.setBounds(426, 85, 347,265);
		textArea.setFont(font);
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setBorder(null);
		con.add(textArea);		
		JScrollPane scrollPane = new JScrollPane(textArea);//添加滚动条
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);		
		scrollPane.setBounds(426, 85, 347,265);		
		con.add(scrollPane);
		
		textArea2=new JTextArea();
		textArea2.setBounds(36, 85, 347,265);
		textArea2.setFont(font);
		textArea2.setEditable(true);
		textArea2.setOpaque(false);
		textArea2.setBorder(null);
		con.add(textArea2);		
		JScrollPane scrollPane2 = new JScrollPane(textArea2);
		scrollPane2.setOpaque(false);
		scrollPane2.setBorder(null);		
		scrollPane2.setBounds(36, 85, 347,265);		
		con.add(scrollPane2);
		
		chooser=new JFileChooser();
		
		con.add(label);	
	}	
	
	public chatClient(){
		this.setTitle("Client");
		init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);      
        this.setBounds(0, 0, 800, 500);
        this.setResizable(false);
//        this.setLocationRelativeTo(null);
//        this.setUndecorated(true);
        this.setLocation(200, 200);
        this.setVisible(true);	
	}
//	@Override
	public void run() {
		while(true){
		try{
			Thread.sleep(500);
 
			if(!conButton.isSelected()) continue;
			SERVICE_PORT=Integer.valueOf(portField.getText()).intValue();
			//hostname=nameField.getText();
    		socket=new Socket(hostname,SERVICE_PORT);
    		in=new DataInputStream(socket.getInputStream());
    		out=new DataOutputStream(socket.getOutputStream());
    		textArea.append("连接服务器成功!\n");
    		logger.info("连接服务器成功!\\n");
    		
         while(true){
        	 String strUTF=in.readUTF();
        	 if(strUTF.equals("&&close&&"))  {disButton.setSelected(true);out.writeUTF("&&close&&");break;}
	        	if(strUTF.equals("&&file.start&&"))
	        	{    		
	        		String fileName =in.readUTF();		        		
	        		chooser.setSelectedFile(new File(fileName)); 	        		
	        		int returnVal=chooser.showSaveDialog(this.getContentPane());
	        		if(returnVal==JFileChooser.APPROVE_OPTION){
	        			FileOutputStream fos=new FileOutputStream(chooser.getSelectedFile().getAbsolutePath());
	        			path = chooser.getSelectedFile().getAbsolutePath();
	 
	        			int data;              
	        			while(-1!=(data= in.readInt()))         
	        			{
	                         fos.write(data);        
	        			}	 
	        			receive.add(path);
	        			System.out.println("文件接收完毕");
	        			logger.info("文件接收完毕");
	        			textArea.append(fileName+"接收完毕!\n");	
	        			textArea2.append(fileName+"\n");	        		
	        			fos.close(); 
	        			}


	        	}
	        	else
	        		textArea.append(strUTF+"\n");
	         }
         in.close();
         out.close();
         textArea.append("连接已断开!\n");
         logger.info("连接已断开!");
    	}catch(Exception e){}
	}
	}
	
	
	@Override
	//事件处理函数
	public void actionPerformed(ActionEvent e) {
		//处理发送消息事件
		if(e.getSource()==button)
			try {
				out.writeUTF(textField.getText());
				textArea2.append(textField.getText()+"\n");
				textField.setText("");
			} catch (IOException e2) {}
		//处理上传文件事件
		else if (e.getSource()==fButton) {
			try {
				int returnVal=chooser.showOpenDialog(this);
        		if(returnVal==JFileChooser.APPROVE_OPTION){
        			String filePath = chooser.getSelectedFile().getAbsolutePath();			
        			String fileName = chooser.getSelectedFile().getName();	
        			
        			out.writeUTF("&&file.start&&");			        	
        			out.writeUTF(fileName);	 	 
        				textArea.append("开始发送"+fileName+"...\n");        			
        				FileInputStream fileInputStream=new FileInputStream(filePath);			        			
        				int data;			        			
        				while(-1!=(data=fileInputStream.read())){				       				
        					out.writeInt(data);		        			
        				}        			
        				out.writeInt(data);        			
        				fileInputStream.close();       			
        				System.out.println("文件已发送完毕");
        				logger.info("文件已发送完毕");
        				textArea.append(fileName+"发送完毕!\n");
        		}
			}catch(IOException e3){}
		}
		//处理下载文件事件
		else if(e.getSource()==dButton) {
			try {
        			
        			popup p=new popup();
        			p.display();
        			
        		
			}catch(Exception e3){}
		}
		//管理接受文件按钮事件
		else if(e.getSource()==fileButton)
		{
			for(String i:receive)
			{
				System.out.println(i);
				FileRegulation popfile=new FileRegulation(receive,textArea);
			}
		
		}
		//处理断开服务器操作操作
		else if(e.getSource()==disButton)
		{
			try {
				out.writeUTF("&&close&&");
			} catch (IOException e2) {}
		}
	}
 
 
}
