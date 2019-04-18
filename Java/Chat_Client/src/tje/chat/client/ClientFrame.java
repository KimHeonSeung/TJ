package tje.chat.client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import tje.chat.client.net.ClientSocket;
import tje.chat.client.panel.*;
import tje.chat.model.*;

public class ClientFrame extends JFrame {
	// �ܺο��� �� Ŭ������ ����ʵ忡 �����ϱ� ���� ��å
	private ClientFrame frame = this;
	
	// ���� ������ ���� ���� ��� �� File ��ü ����
	// ./�� �� Ŭ������ �������ִ� ��� ���ĸ� ����.
	private static final String CLIENT_DIR_PATH = "./client";
	private static final String SERVER_INFO_FILE_PATH = "server_info.dat";
	private static File CLIENT_DIR;
	private static File SERVER_INFO_SAVE_FILE;

	// �� ClientFrame ��ü�� �����ɶ� ���� �켱������ ����Ǵ� ���� ����.
	// ���������� �ʿ��� ��ΰ� �ִ��� Ȯ���ϰ� ������ �����ϸ� save.dat ���� ��ü ����
	static {
		CLIENT_DIR = new File(CLIENT_DIR_PATH);
		if (!CLIENT_DIR.exists())
			CLIENT_DIR.mkdirs();

		SERVER_INFO_SAVE_FILE = new File(CLIENT_DIR, SERVER_INFO_FILE_PATH);
	}

	private NorthPanel np = new NorthPanel();
	private CenterPanel cp = new CenterPanel();

	public ClientFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Chat Client - Ver 0.1");

		// ȭ�� ��ġ
		this.setLayout(new BorderLayout(0, 10));
		this.add(np, BorderLayout.NORTH);
		this.add(cp, BorderLayout.CENTER);

		// ����� ������ Ȯ���� ��, ȭ�� ������Ʈ�� ���̳� ���¸� ����
		if (SERVER_INFO_SAVE_FILE.exists())
			loadServerInfo();

		// ������Ʈ���� �̺�Ʈ ó��
		// �������� üũ�ڽ��� �̺�Ʈ ó��
		// �̳� Ŭ������ ���� ó���Ѵ�.
		np.getCbSaveInfo().addActionListener(new SaveClientInfoHanddler());
		
		// �����ư�� �̺�Ʈó��
		// ���� ���� ��ư�� Ŭ���Ǵ� ��� ����Ǵ� �ڵ�
		np.getBtConnect().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String strIp = np.getTfIP().getText().trim();
				String strPort = np.getTfPORT().getText().trim();
				String strNickName = np.getTfNickName().getText().trim();

				// �ƹ��͵� �Է��� �ȵǾ����� ��
				if (strIp.length() == 0 || strPort.length() == 0 || strNickName.length() == 0) {
					JOptionPane.showMessageDialog(null, "������ ��� �Է��ؾ߸� �մϴ�.");
					np.getCbSaveInfo().setSelected(false);
					return;
				}
				
				// port ��ȣ�� ���ڿ����� ������ ��ȯ
				int nPort = Integer.parseInt(strPort);
				// Ŭ���̾�Ʈ�� ������ �����ϴ� ��ü ����
				ServerInfo serverInfo = new ServerInfo(strIp, nPort, strNickName);
				
				ClientSocket clientSocket = new ClientSocket(frame, serverInfo);
			}
		});

		setSize(1000, 500);
		setVisible(true);
	}

	private void loadServerInfo() {
		try (ObjectInputStream in = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(SERVER_INFO_SAVE_FILE)))) {
			ServerInfo si = (ServerInfo) in.readObject();

			if (si == null)
				return;

			np.getTfIP().setText(si.getIp());
			np.getTfPORT().setText(si.getPort() + ""); // ���ڰ��̿��� �ٲ���
			np.getTfNickName().setText(si.getNickName());

			np.getCbSaveInfo().setSelected(true);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "������ �ε��ϴ� �������� ������ �߻��߽��ϴ�.");
		}
	}

	public static void main(String[] args) {
		new ClientFrame();
	}

	class SaveClientInfoHanddler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// üũ�ڽ��� ������ �����Ǹ� ������ �����ϴ� ���ǹ�
			if (!np.getCbSaveInfo().isSelected()) {
				if (SERVER_INFO_SAVE_FILE.exists())
					SERVER_INFO_SAVE_FILE.delete();
				JOptionPane.showMessageDialog(null, "����� ������ �����߽��ϴ�.");
				return;
			}

			// Ʈ���� ���� ������ �����.
			// ��� ������ �ٲ� �� �����Ƿ� ���� tje.chat.model ��Ű���� ��
			String strIp = np.getTfIP().getText().trim();
			String strPort = np.getTfPORT().getText().trim();
			String strNickName = np.getTfNickName().getText().trim();

			// �ƹ��͵� �Է��� �ȵǾ����� ��
			if (strIp.length() == 0 || strPort.length() == 0 || strNickName.length() == 0) {
				JOptionPane.showMessageDialog(null, "������ ��� �Է��ؾ߸� �մϴ�.");
				np.getCbSaveInfo().setSelected(false);
				return;
			}

			// port ��ȣ�� ���ڿ����� ������ ��ȯ
			int nPort = Integer.parseInt(strPort);
			// Ŭ���̾�Ʈ�� ������ �����ϴ� ��ü ����
			ServerInfo si = new ServerInfo(strIp, nPort, strNickName);

			// �Ʒ�ó�� try() ���� ��ȣ�ȿ� �ϸ� close�� �Ź� �����൵ ��
			try (ObjectOutputStream out = new ObjectOutputStream(
					new BufferedOutputStream(new FileOutputStream(SERVER_INFO_SAVE_FILE)));) {
				out.writeObject(si);
			} catch (Exception ex) { // �ͼ��� �ϳ��θ� ����
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "���� ���� �������� ������ �߻��Ͽ����ϴ�.");
				np.getCbSaveInfo().setSelected(false);
				return;
			}

			JOptionPane.showMessageDialog(null, "���� ������ �Ϸ��߽��ϴ�.");
		}
	}
}
