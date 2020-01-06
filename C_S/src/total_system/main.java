package total_system;
import java.sql.*;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class main {
	 
	public static void  main(String[] args) {
		try {
			for(LookAndFeelInfo info:UIManager.getInstalledLookAndFeels()){
				if("Windows".equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		chatServer server=new chatServer();
		chatClient client=new chatClient();
		new Thread(server).start();
		new Thread(client).start();
	
	}
}
