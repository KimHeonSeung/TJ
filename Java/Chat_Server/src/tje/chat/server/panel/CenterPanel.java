package tje.chat.server.panel;

import java.awt.*;
import javax.swing.*;

public class CenterPanel extends JPanel {

	private JTextArea taLog = new JTextArea();
	// üũ�� �� ������ üũ�ڽ� ����
	private JCheckBox cbLog = new JCheckBox("�α׸޼��� ���", true);

	public CenterPanel() {
		this.setLayout(new BorderLayout());

		this.add(taLog, BorderLayout.CENTER);
		this.add(cbLog, BorderLayout.SOUTH);

	}

	// �ܺο��� �����ϱ� ���� ���͸� ����
	public JTextArea getTaLog() {
		return taLog;
	}

	public JCheckBox getCbLog() {
		return cbLog;
	}
}
