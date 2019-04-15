package Chatting;

import javax.swing.*;

import java.io.IOException;
import java.net.*;

public class Client {
	String ip, nick, host;
	int port;
	Socket socket;

	Client(String ip, int port, String nick) {
		this.ip = ip;
		this.port = port;
		this.nick = nick;
		this.host = this.socket.getInetAddress().getHostAddress();

		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "������ ã�� �� �����ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "������ ���ῡ �����߽��ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
			return;
		}

	}

}
