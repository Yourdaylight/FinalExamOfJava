package total_system;
import IOdata_file.*;


import for_database.DataBaseView;
import for_database.TableChose;
import org.apache.log4j.Logger;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Vector;
public class chatServer extends JFrame implements Runnable,ActionListener{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(chatServer.class);
	private static final long serialVersionUID = -4929211394401993088L;
	//private static final int SERVICE_PORT=13;
	int SERVICE_PORT;
	DataInputStream in;
	DataOutputStream out;
	ServerSocket server;
	Socket nextClient;
	JTextField textField,nameField,portField;
	static JTextArea textArea,textArea2;
	JButton button,fButton,dataButton,fileButton;
	JFileChooser chooser;
	JLabel l1;
	JButton lisButton,disButton;
	ButtonGroup group;
	Font font = new Font("Adobe 黑体 Std",0,16);
	JLabel label;
	String path;
	static Vector<String> receive=new Vector();//接受到的文件列表
 
		//初始化界面
		void init(){
			Container con = this.getContentPane();
			
			label=new JLabel();
			label.setBounds(0, 0, 800, 500);
			//设置发送消息按钮
			button=new JButton("发送消息");
			button.setFont(font);
			button.setBounds(670,425,110,30);
			button.addActionListener(this);
			con.add(button);
			//设置选择文件按钮
			fButton=new JButton("选择文件");
			fButton.setFont(font);
			fButton.setBounds(550, 425, 110, 30);
			fButton.addActionListener(this);
			con.add(fButton);
			//设置查看数据库按钮
			dataButton=new JButton("显示数据库");
			dataButton.setFont(font);
			dataButton.setBounds(425, 425, 130, 30);
			dataButton.addActionListener(this);
			con.add(dataButton);
			//设置文件操作按钮
			fileButton=new JButton("管理接受文件");
			fileButton.setFont(font);
			fileButton.setBounds(300, 425, 130, 30);
			fileButton.addActionListener(this);
			con.add(fileButton);
			//设置发送消息文本区域
			textField=new JTextField();
			textField.setBounds(10,375,780,40);
			textField.setFont(font);
			textField.setOpaque(true);
			textField.setBorder(null);
			//设置键盘监听
			textField.addKeyListener(new KeyAdapter() {
				//按下回车时调用此方法
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
			
		
			//设置窗口名的显示框
			nameField=new JTextField();
			nameField.setBounds(180, 25, 120, 25);
			nameField.setText("服务器文件");
			nameField.setFont(font);
			nameField.setOpaque(false);
			nameField.setBorder(null);
			con.add(nameField);
			//设置端口名的显示框
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
			//设置监听和断开按钮
			lisButton=new JButton("监听");
			disButton=new JButton("断开");
			disButton.setSelected(false);
			lisButton.setSelected(true);
			lisButton.setBounds(610, 17, 85, 30);
			disButton.setBounds(700, 17, 85, 30);
			lisButton.addActionListener(this);
			disButton.addActionListener(this);
			group = new ButtonGroup();
			group.add(lisButton);
			group.add(disButton);
			con.add(lisButton);
			con.add(disButton);
			
				
			textArea=new JTextArea();
			textArea.setBounds(426, 85, 347,265);
			textArea.setFont(font);
			textArea.setEditable(false);
			textArea.setOpaque(true);
			textArea.setBorder(null);
			con.add(textArea);
			
			//设置滚动条
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setOpaque(true);
			scrollPane.setBorder(null);		
			scrollPane.setBounds(426, 85, 347,265);		
			con.add(scrollPane);
			
			textArea2=new JTextArea();
			textArea2.setBounds(36, 85, 347,265);
			textArea2.setFont(font);
			textArea2.setEditable(false);
			textArea2.setOpaque(false);
			textArea2.setBorder(null);
			con.add(textArea2);		
			JScrollPane scrollPane2 = new JScrollPane(textArea2);
			scrollPane2.setOpaque(false);
			scrollPane2.setBorder(null);		
			scrollPane2.setBounds(36, 85, 347,265);		
			con.add(scrollPane2);
			
			//服务器包含文件
			GetTableName table_name=new GetTableName();
			List<String> strnames = table_name.getTableNames();
			for (int i = 1; i < strnames.size(); i++) 
			{
	            String name = (String) strnames.get(i);
	            textArea2.append(name+"\n");
	        }
			
			chooser=new JFileChooser();
			
			
			con.add(label);	
		}
	
	
	public chatServer() {
		
		this.setTitle("Server");
		init();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);      
        this.setBounds(0, 0, 800, 500);
        this.setResizable(false);//窗口大小是否可调
        //this.setLocationRelativeTo(null);
        //this.setUndecorated(true);
        this.setLocation(1000,200);//设置窗口位置
        this.setVisible(true);//是否可视化
	}
	
	@Override
	public void run(){
		
		try{
 
		while(true){
 
	        System.out.println("Server Start!");
	        //判断服务器是否被连接
			if(!lisButton.isSelected()) continue;	
			
			textArea.append("开始监听端口"+portField.getText()+"!\n");
			SERVICE_PORT=Integer.valueOf(portField.getText()).intValue();
			System.out.println("1");
			logger.info("1");
			server=new ServerSocket(SERVICE_PORT);
			System.out.println("2");
			logger.info("2");
			
			nextClient = server.accept();
			textArea.append("收到并接受客户端的连接请求!\n");
			logger.info("收到并接收客户端的连接请求!");
			in=new DataInputStream(nextClient.getInputStream());
			out=new DataOutputStream(nextClient.getOutputStream());
			
			while (true){
				String strUTF=in.readUTF();
				if(strUTF.equals("&&close&&")) {disButton.setSelected(true);out.writeUTF("&&close&&");break;}
	        	if(strUTF.equals("&&file.start&&"))
	        	{    		
	        		String fileName =in.readUTF();		        		
	        		chooser.setSelectedFile(new File(fileName)); 	        		
	        		int returnVal=chooser.showSaveDialog(this.getContentPane());
	        		if(returnVal==JFileChooser.APPROVE_OPTION) 
	        		//点击确认后生效
	        		{
	        			FileOutputStream fos=new FileOutputStream(chooser.getSelectedFile().getAbsolutePath());
	        			path = chooser.getSelectedFile().getAbsolutePath();
	        			int data;              
	        			while(-1!=(data= in.readInt()))         
	        			{
	                         fos.write(data);        
	        			}	        		     			        		
	        			receive.add(path);//将接受的到的文件添加到文件列表
	        			System.out.println("文件接收完毕");
	        			logger.info("文件接收完毕");
	        			textArea.append(fileName+"接收完毕!\n");
	        			textArea2.append(fileName);
	        			
	        			fos.close(); 
	        			}
	        		
		        		String ext = fileName.substring(fileName.lastIndexOf("."));//获取文件路径
		        		if(ext.equals(".txt"))
		        		{
		        			System.out.println(ext);
			        		System.out.println("开始写入数据库!");
			        		logger.info("开始写入数据库");
			        		textArea.append("开始写入数据库");
			        		ImportDataFromTxtToMysql filewrite = new ImportDataFromTxtToMysql();
//			        		file filewrite = new file(path);
		        			filewrite.inserttxt(path);
		        			System.out.println("\n写入数据库成功!");
		        			logger.info("写入数据库成功");
		        			textArea.append("\n写入数据库成功!");
		        		}
		        		else if(ext.equals(".xls") || ext.equals(".xlsx"))	
		        		{
		        		System.out.println(ext);
		        		System.out.println("开始写入数据库!");
		        		logger.info("开始写入数据库");
		        		textArea.append("开始写入数据库");
		        		ImportDataFromExcelToMysql filewrite = new ImportDataFromExcelToMysql(path);
	        			filewrite.insetmysql(path);
	        			System.out.println("\n写入数据库成功!");
	        			logger.info("写入数据库成功");
	        			textArea.append("\n写入数据库成功!");
		        		}
	        		}	
	        	else
	        		textArea.append(strUTF+"\n");
	         }
			in.close();
			out.close();
			server.close();
			textArea.append("连接已断开!\n");
			logger.info("连接已断开");
 
          }
	    	}catch(Exception e){}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==button)
			try {
				out.writeUTF(textField.getText());
				textArea2.append(textField.getText()+"\n");
				textField.setText("");
			} catch (IOException e2) {}
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
		//点击显示数据库 按钮
		else if(e.getSource()==dataButton)
		{
			//弹出数据库表选择
			TableChose tj=new TableChose();
		
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
		//断开按钮事件
		else if(e.getSource()==disButton)
		{
			try {
				out.writeUTF("&&close&&");
			} catch (IOException e2) {}
		}
	}



 
}
 

