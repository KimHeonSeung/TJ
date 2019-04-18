package tje.chat.server.net;

import java.net.*;

import javax.swing.JOptionPane;

import tje.chat.server.ServerFrame;

import java.io.*;

public class ClientSocket {
	// ������������ ��������
	private ServerFrame frame;
	// Ŭ���̾�Ʈ ������ ��������
	private Socket socket;
	// Ŭ���̾�Ʈ�� ip �ּ�
	private String ip;
	// Ŭ���̾�Ʈ ��Ī
	private String nickName;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public ClientSocket(ServerFrame frame, Socket client) {
		this.frame = frame;
		this.socket = client;
		this.ip = this.socket.getInetAddress().getHostAddress();
		
		//this.frame.writeLog(ip + " ����");
	}
	
	// �Ȱ��� ip�� �ι� ������ ��Ʈ���� �ѹ��� ��������� ���� ������.
	// �׷��� Ŭ���̾�Ʈ�ʰ��� �� �ٸ��� ��Ʈ������
	public void initStream() {
		try {
			// ������Ʈ �ƿ�ǲ ��Ʈ�� ���� �ƿ�ǲ��Ʈ�� �����ϰ� �ٷ� �÷��ø� �����. �����ʵ� ��������
			out = new ObjectOutputStream(
					new BufferedOutputStream(this.socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(
					new BufferedInputStream(this.socket.getInputStream()));
			
			nickName = (String)in.readObject();
			
			this.frame.writeLog(ip + " / " + nickName +  " �� ����");
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "������ ���� �������� ������ �߻��߽��ϴ�.");
			return;
		}
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public void close() {
		try {
			this.socket.close();
			this.frame.writeLog(ip + " ���� ����");
		} catch (IOException e) {
			this.frame.writeLog(ip + " ���� ���� �������� ������ �߻��Ͽ����ϴ�.");
		}
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof ClientSocket) )
			return false;
		
		ClientSocket target = (ClientSocket) obj;
		boolean flag = this.ip.equals(target.ip);
		
		return flag;
	}
	
}
