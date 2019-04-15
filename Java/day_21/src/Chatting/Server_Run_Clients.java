package Chatting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Server_Run_Clients extends Thread {
	ServerSocket server;
	Socket socket;
	String ip;
	int port;

	public Server_Run_Clients(String ip, int port) {
		this.ip = ip;
		this.port = port;
		try {
			// �������� ����
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "�ٸ� ���μ������� ������ PORT ��ȣ�� ��� ���Դϴ�.", "���", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			// �㰡�� ���ϵ��� ��̸���Ʈ�� �߰��ϴ� �ݺ��۾�
			socket = server.accept();
			ClientList.addClient(new ClientSocket(socket));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		while (true) {

		}
	}

}
