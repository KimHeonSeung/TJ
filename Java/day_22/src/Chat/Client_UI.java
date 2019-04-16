package Chat;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.Book;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Client_UI extends JFrame {
	
	private Container clientPane;
	
	private JTextField ipText;
	private JTextField portText;
	private JTextField nickText;
	private JCheckBox remember;
	private JButton conBtn;
	private String [] saved = new String[3];
	private JButton msgOut;
	private JTextArea cliListArea;
	private JTextArea notice;
	
	private int cliPort;
	private String cliIp;
	private String cliNick;
	private Socket socket;
	private InputStreamReader isr;
	
	private Client client;
	
	public Client_UI() {
		setTitle("Client");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		clientPane = getContentPane();
		clientPane.setLayout(new BorderLayout(15, 30));
		
		// ����� ���� (������ ����)
		JPanel upperPane = new JPanel(new GridLayout(1, 5, 2, 2));
		JLabel serverInfo = new JLabel("���� ����");
		serverInfo.setFont(new Font("����ü", Font.BOLD, 30));
		// IP ����
		JPanel ipPane = new JPanel(new FlowLayout());
		JLabel ip = new JLabel("IP");
		ip.setFont(new Font("����ü", Font.BOLD, 30));
		// saved �迭�� ����� ������ ���� �� �ڵ����� �ҷ����ֱ�.
		ipText = new JTextField("192.168.0.68", 20);
//		ipText = new JTextField(20);
		ipPane.add(ip);			ipPane.add(ipText);
		// PORT ����
		JPanel portPane = new JPanel(new FlowLayout());
		JLabel port = new JLabel("PORT");
		port.setFont(new Font("����ü", Font.BOLD, 30));
		portText = new JTextField("5050", 20);
		portPane.add(port);		portPane.add(portText);
		// �г��� ����
		JPanel nickPane = new JPanel(new FlowLayout());
		JLabel nickName = new JLabel("�г���");
		nickName.setFont(new Font("����ü", Font.BOLD, 30));
		nickText = new JTextField("aa", 20);
		nickPane.add(nickName);		nickPane.add(nickText);
		// ��ư�� ���� ���� ����
		JPanel btnPane = new JPanel(new FlowLayout());
		remember = new JCheckBox("���� ����       ", false);
		RememberListen rememLi = new RememberListen();
		remember.addItemListener(rememLi);
		remember.setFont(new Font("����ü", Font.BOLD, 30));
		conBtn = new JButton("����");
		conBtn.setFont(new Font("����ü", Font.BOLD, 30));
		ConnectListen connect = new ConnectListen();
		conBtn.addActionListener(connect);
		btnPane.add(remember);		btnPane.add(conBtn);		
		// ��� �ҿ� �߰�
		upperPane.add(serverInfo);
		upperPane.add(ipPane);
		upperPane.add(portPane);
		upperPane.add(nickPane);
		upperPane.add(btnPane);
		
		// �ϴ��� ���� (������ ����)
		JPanel underPane = new JPanel(new GridLayout(1, 4, 15, 15));
		// 1��° �� (�������� Ŭ���̾�Ʈ ���)
		JPanel firstPane = new JPanel(new BorderLayout());
		JLabel listClient = new JLabel("�������� Ŭ���̾�Ʈ ���");
		listClient.setFont(new Font("����ü", Font.BOLD, 30));
		cliListArea = new JTextArea();
//		ConList cl = new ConList(cliListArea);
		firstPane.add(listClient, BorderLayout.NORTH);
		firstPane.add(cliListArea, BorderLayout.CENTER);
		// 2��° �� (���� �޼��� ���)
		JPanel secondPane = new JPanel(new BorderLayout());
		JLabel noticeOut = new JLabel("�����޼��� ���");
		noticeOut.setFont(new Font("����ü", Font.BOLD, 30));
		notice = new JTextArea();
		secondPane.add(noticeOut, BorderLayout.NORTH);
		secondPane.add(notice, BorderLayout.CENTER);
		// 3��° �� (���� ��ȭ��)
		JPanel thirdPane = new JPanel(new BorderLayout());
		JLabel listChat = new JLabel("���� ��ȭ���� ���");
		listChat.setFont(new Font("����ü", Font.BOLD, 30));
		JList chatList = new JList();
		thirdPane.add(listChat);
		thirdPane.add(chatList);
		// 4��° �� ( ä�ù� )
		JPanel fourthPane = new JPanel(new BorderLayout());
		JTextArea enteredChat = new JTextArea();
		JPanel outPane = new JPanel(new FlowLayout());
		JTextField msgText = new JTextField(20);
		msgOut = new JButton("����");
//		ButtonListener send = new ButtonListener(msgText, enteredChat, nickText);
//		msgOut.addActionListener(send);
		msgOut.setFont(new Font("����ü", Font.BOLD, 30));
		JButton fileOut = new JButton("���� ����");
//		FileListener fileListen = new FileListener();
//		fileOut.addActionListener(fileListen);
		fileOut.setFont(new Font("����ü", Font.BOLD, 30));
		outPane.add(msgText);
		outPane.add(msgOut);
		outPane.add(fileOut);
		fourthPane.add(enteredChat, BorderLayout.CENTER);
		fourthPane.add(outPane, BorderLayout.SOUTH);
		// �ϴ� �ҿ� 1,2,3,4 ��° �� �߰�
		underPane.add(firstPane);
		underPane.add(secondPane);
		underPane.add(thirdPane);
		underPane.add(fourthPane);
		
		// Ŭ���̾�Ʈ �ҿ� �������� ���� �߰�
		clientPane.add(upperPane, BorderLayout.NORTH);
		clientPane.add(underPane, BorderLayout.CENTER);
		
		setSize(1950, 1300);
		setVisible(true);
	}
	
///////////////////////////////////////////////////////////////////////////////////////	
	
	// �������� ��ư�� �������� �̺�Ʈ ó��
	class RememberListen implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				// üũ�Ǿ��� �� ������ ����
				// 1. IP ����
				saved[0] = ipText.getText();
				// 2. PORT ����
				saved[1] = portText.getText();
				// 3. �г��� ����
				saved[2] = nickText.getText();
				// ������ ���� ������ ���Ͽ� ���
				String dirPath = "D:\\dev\\java_se\\Chatting";
				File dir = new File(dirPath);
				if ( !dir.exists() ) {
					dir.mkdirs();
				}
				File file = new File(dir, "savedInfo.txt");
				try {
					PrintWriter out = 
							new PrintWriter(
									new BufferedWriter(
											new FileWriter(file)), true);
					for(int i = 0 ; i < saved.length ; i++)
						out.printf("%s, ", saved[i]);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if (e.getStateChange() == ItemEvent.DESELECTED) {
				String dirPath = "D:\\dev\\java_se\\Chatting";
				File dir = new File(dirPath);
				if ( !dir.exists() ) {
					dir.mkdirs();
				}
				File file = new File(dir, "savedInfo.txt");
				file.delete();
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
	
	// ���� ��ư�� ������ �� �̺�Ʈ ó��
	class ConnectListen implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			if(b.getText().equals("����")) {
				b.setText("����");
				// Ŭ���̾�Ʈ ��ü�� �����Ͽ� ��Ʈ������ ����
				cliIp = ipText.getText();
				cliPort = Integer.parseInt(portText.getText());
				cliNick = nickText.getText();
				new Client(cliIp, cliPort, cliNick).start();
				
				// ���� �޼����� ��������
//				try {
//					notice.append(client.br.readLine() + "\n");
//				} catch (IOException e1) {
//					e1.printStackTrace();
//					}
				}
			else {
				b.setText("����");
			}
		}
	}
	
	// ���� ��ư�� ������ �� Ŭ���̾�Ʈ ��ü ó��
	class Client extends Thread {
		private String cliIp, cliNick;
		private int cliPort;
		private BufferedReader br;
		private PrintWriter pw;
		
		Client(String cliIp, int cliPort, String cliNick) {
			this.cliIp = cliIp;
			this.cliPort = cliPort;
			this.cliNick = cliNick;
		}

		public void exit() {
			try {
				br.close();
				pw.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public void run() {
			while(true) {
			try {
				socket = new Socket(cliIp, cliPort);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

				try {
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					notice.append(br.readLine()+"\n");
				} catch (IOException e) {
					notice.append("�������� ������ �����߽��ϴ�");
					break;
				}
			}
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		new Client_UI();
	}
}
