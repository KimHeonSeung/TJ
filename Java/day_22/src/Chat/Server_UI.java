package Chat;

import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server_UI extends JFrame {
	public int port;
	
	Container contentPane;
	JTextField portText;
	JButton startEnd;
	JTextArea noticeArea;
	JCheckBox logWrite;
	JButton noticeBtn;
	JButton fileBtn;
	JTextField noticeText;
	ServerSocket ss;
	Socket client;
	BufferedReader br;
	PrintWriter pw;
	ArrayList<String> selectedFile = new ArrayList<String>();
	ArrayList<ClientSocketStr> ClientSockets = new ArrayList<ClientSocketStr>();
//	StartNotice sn;
	
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy�� MM�� dd�� HH�� mm�п� ��ϵ� ����");
	Date now = cal.getTime();
	
	public Server_UI() {
		setTitle("Server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(10, 50));
		
		// ����� ����
		JPanel upperPane = new JPanel(new FlowLayout());
		// ��ܿ� �󺧰� �ؽ�Ʈ�ʵ�, ��ư ��ġ
		JLabel portNum = new JLabel("��Ʈ ��ȣ");
		portNum.setFont(new Font("����ü", Font.BOLD, 30));
		portText = new JTextField("5050", 30);
		// ���� �ʿ���
		//Server_Run_Clients sr = new Server_Run_Clients("ip�ּ�", Integer.parseInt(portText.getText()));
		startEnd = new JButton("����");
		startEnd.setFont(new Font("����ü", Font.BOLD, 30));
		StartListen change = new StartListen();
		startEnd.addActionListener(change);
		upperPane.add(portNum);
		upperPane.add(portText);
		upperPane.add(startEnd);
		
		// �ߴ��� ����
		JPanel midPane = new JPanel(new BorderLayout());
		noticeArea = new JTextArea();
		noticeArea.setEditable(false);
		midPane.add(noticeArea);
		
		// �ϴ��� ����
		JPanel underPane = new JPanel(new GridLayout(2, 1, 1, 1));
		// üũ�ڽ��� �޼��� ��� �г� ����
		JPanel checkPane = new JPanel(new FlowLayout());
		logWrite = new JCheckBox("�α� �޼��� ���");
		logWrite.setFont(new Font("����ü", Font.BOLD, 30));
		CheckListen logListen = new CheckListen();
		logWrite.addItemListener(logListen);
		checkPane.add(logWrite);
		// ���� �޼��� ����
		JPanel noticePane = new JPanel(new BorderLayout());
		JLabel noticeLabel = new JLabel("�����޼���");
		noticeLabel.setFont(new Font("����ü", Font.BOLD, 30));
		noticeText = new JTextField();
		JPanel btnPane = new JPanel(new FlowLayout());
		noticeBtn = new JButton("����");
		noticeBtn.setFont(new Font("����ü", Font.BOLD, 30));
		ButtonListener send = new ButtonListener(noticeText, noticeArea, "����");
		noticeBtn.addActionListener(send);
		fileBtn = new JButton("���� ����");
//		FileListener fileListen = new FileListener();
//		fileBtn.addActionListener(fileListen);
		fileBtn.setFont(new Font("����ü", Font.BOLD, 30));
		btnPane.add(noticeBtn);
		btnPane.add(fileBtn);
		noticePane.add(noticeLabel, BorderLayout.WEST);
		noticePane.add(noticeText);
		noticePane.add(btnPane, BorderLayout.EAST);
		underPane.add(checkPane);
		underPane.add(noticePane);
		
		// �����̳ʿ� ��� �ߴ� �ϴ� ���� ����
		contentPane.add(upperPane, BorderLayout.NORTH);
		contentPane.add(midPane, BorderLayout.CENTER);
		contentPane.add(underPane, BorderLayout.SOUTH);
		
		setSize(1300, 800);
		setVisible(true);
	}
	
	
	class CheckListen implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			int sel = -1;
			if(e.getStateChange() == ItemEvent.SELECTED) {
				sel = 1;
				// �α� �޼��� ����� üũ�Ǿ��� �� ���� ��θ� ����� txt�� �����.
				String dirPath = "D:\\dev\\java_se\\Chatting";
				File dir = new File(dirPath);
				if ( !dir.exists() ) {
					dir.mkdirs();
				}
				File file = new File(dir, "log.txt");
				// ����� ���� out��Ʈ��
				try {
					PrintWriter out = 
							new PrintWriter(
									new BufferedWriter(
											new FileWriter(file, true)), true);
					out.println("------------����----------------");
					out.println(noticeArea.getText());
					out.println(sdf.format(now));
					out.println("------------����----------------");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else 
				sel = -1;
			}
		}
	
//////////////////////////////////////////////////////////////////////////////////////////
	
	// ������ ������ �Է��� ��Ʈ��ȣ�� ���� ���������� �����ϰ� ������ Ŭ���̾�Ʈ���� ��̸���Ʈ�� �����Ѵ�.
	class StartListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			if(b.getText().equals("����")) {
				b.setText("����");
				port = Integer.parseInt(portText.getText());
				// ������ Ŭ���̾�Ʈ���� ������ ��̸���Ʈ�� �����ϴ� ��ü ����
				new RunServer(port).start();
			}
			else {
				b.setText("����");
				noticeArea.setText("������ �����߽��ϴ�."); 
				try {
					ss.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	// ��Ʈ�� �Ű������� �޾ƿ� ������� Ŭ���̾�Ʈ ��ü���� �����Ѵ�.
	class RunServer extends Thread {
		private int port;
		
		RunServer(int port) {
			this.port = port;
		}
		
		public void run() {
			try {
				ss = new ServerSocket(this.port);
				client = ss.accept();
				noticeArea.append("����ڰ� ������\n");
				// Ŭ���̾�Ʈ ������ �����ɶ����� ��ü�� ���� ��̸���Ʈ�� ����
				ClientSockets.add(new ClientSocketStr(client));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ClientSocketStr {
		private Socket socket;
		private PrintWriter pw;
		ClientSocketStr(Socket socket) {
			this.socket = socket;
			try {
				this.pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// ���� ��ư�� ������ �� �̺�Ʈ ó��
	class ButtonListener implements ActionListener {
		
		private JTextField Field;
		private JTextArea Area;
		private String nick;

		ButtonListener(JTextField Field, JTextArea Area, JTextField nick) {
			this.Field = Field;
			this.Area = Area;
			this.nick = nick.getText();
		}
		
		ButtonListener(JTextField Field, JTextArea Area, String nick) {
			this.Field = Field;
			this.Area = Area;
			this.nick = nick;
		}
		
		public String toString() {
			return String.format("%s   :  %s\n", this.nick, Field.getText());
		}
		

		public void actionPerformed(ActionEvent e) {
			
			// ���� ��ư�� ���� �� ���� �ؽ�Ʈ ���� ���� ���� �߰�
			JButton b = (JButton) e.getSource();
			if (b.getText().equals("����")) {
				// ���� ��ư�� ���� �� ���� �ؽ�Ʈ ���� ���� ���� �߰�
				Area.append(toString());
				// �ؽ�Ʈ �ʵ忡 ���� ���� Ŭ���̾�Ʈ�鿡�� ����
				SendToClients stc = new SendToClients(noticeText);
				stc.start();
				// �ؽ�Ʈ �ʵ带 �ʱ�ȭ
				Field.setText("");
				}
			}
		}
	
	class SendToClients extends Thread {
		private JTextField Field;
		private PrintWriter pw;
		
		SendToClients(JTextField Field) {
			this.Field = Field;
		}
		
		public void run() {
			while(true) {

				for(int i = 0 ; i < ClientSockets.size() ; i++) {
					ClientSockets.get(i).pw.println(noticeText.getText());
					}
			}
			
		}
	}

	
		
	
	
/////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		new Server_UI();
	}
}
