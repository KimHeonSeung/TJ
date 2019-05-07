package tje.chat.server.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.Connection;

import javax.swing.*;

import tje.chat.common.jdbc.UserDAO;
import tje.chat.common.jdbc.model.User;
import tje.chat.common.util.JDBCConnection;
import tje.chat.server.ServerFrame;

// ���۹�ư�� ������ �� ��Ʈ��ȣ�� �޾ƿ� �����Ǵ� ��ü.
// ���������� �ʵ�� �������ִ�.
public class ClientCollector extends Thread {
	// ���� �������� ��������
	private ServerFrame frame;
	// ���� ���� ����
	private ServerSocket server;
	private ObjectInputStream ois;
	private User user;
	private UserDAO dao = UserDAO.getInstance();

	public ClientCollector(ServerFrame frame, int port) {
		this.frame = frame;
		try {
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			this.server = null;
			this.frame.noticeServerError();
			return;
		}

		this.frame.writeLog("���� ������ �Ϸ�Ǿ����ϴ�");
	}
	
	public void run() {
		if( this.server == null ) {
			this.frame.writeLog("���� ���� - run ���ۺκ�");
		}
		
		while(true) {
			Socket client = null;
			try {
				client = this.server.accept();
				
				ois = new ObjectInputStream(
						new BufferedInputStream(
								client.getInputStream()));
				
				try {
					user = (User) ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				
				Connection conn = JDBCConnection.getConnection();
				int correctInfo ;
				correctInfo = dao.login(conn, user);
				
				if( correctInfo == 1 ) {
					// �α��� �㰡
					ClientSocket cs = new ClientSocket(this.frame, client);
					
					if( ClientSaver.exists(cs) )
						cs.close();
					else {
						// ���� ��Ʈ������ ����� ��̸���Ʈ�� �����ؾ� ����.
						// �ֳ��ϸ� Ŭ���̾�Ʈ ���̹��� ��ü�� �����¼��� ������ ���� ��Ȳ����
						// ����� ������ �ȵɼ��� �ִ�.(��Ʈ���� ���°�ü�� �����°� �����̹Ƿ�)
						cs.initStream();
						ClientSaver.insert(cs);
						cs.start();
					}
				} else {
					// �α��� ����
				}
				
				
				
			} catch (IOException e) {
				break;
			}
		}
		
		
		this.frame.writeLog("���� ���� - run ����κ�");
	}
	
	public void close() {
		try {
			this.server.close();
			this.frame.writeLog("���� ���ᰡ �Ϸ�Ǿ����ϴ�");
		} catch (IOException e) {
			this.frame.writeLog("���� ���ῡ�� ������ �߻��Ͽ����ϴ�");
		}
	}

}
