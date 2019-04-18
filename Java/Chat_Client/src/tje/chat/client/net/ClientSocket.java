package tje.chat.client.net;

import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

import tje.chat.client.ClientFrame;
import tje.chat.model.*;

public class ClientSocket extends Thread {
	private ClientFrame frame;
	private ServerInfo serverInfo;
	private Socket socket;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	public ClientSocket(ClientFrame frame, ServerInfo serverInfo) {
		this.frame = frame;
		this.serverInfo = serverInfo;
		try {
			// Ŭ���̾�Ʈ���� ������Ʈ �ƿ�ǲ ��Ʈ�� ���� �ƿ�ǲ��Ʈ������ �����ϰ� �ٷ� �÷��ø� �����. �����ʵ� ��������
			this.socket = new Socket(serverInfo.getIp(), serverInfo.getPort());
			out = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));

			// ���ڿ��ε� ��Ī�� ���. ������ ������� ���� ���� ���� �۾�.
			out.writeObject(this.serverInfo.getNickName());
			out.flush();

			JOptionPane.showMessageDialog(null, "������ ����Ǿ����ϴ�");

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "������ ���� �������� ������ �߻��߽��ϴ�.");
			return;
		}
	}

	public void run() {

	}

	public void close() {
		try {
			this.socket.close();
			JOptionPane.showMessageDialog(null, "������ ����Ǿ����ϴ�");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "������ ���� �������� ������ �߻��߽��ϴ�.");
		}
	}

}
