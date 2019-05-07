package tje.chat.common;

import java.io.*;
import java.util.*;

// Ŭ���̾�Ʈ�� ������� ����
// ������ Ŭ���̾�Ʈ�� �����ϴ� ����
// ip�� ��Ī, ���ӽð��� �ʵ�� ���´�.
public class ClientInfo implements Serializable {
	// ������ Ŭ���̾�Ʈ�� ���� UID 1L�� ����ϵ��� �Ѵ�
	private static final long serialVersionUID = 1L;
	private String ip;
	private String ID;
	private Date connectedTime;
	
	public ClientInfo(String ip, String ID, Date connectedTime) {
		super();
		this.ip = ip;
		this.ID = ID;
		this.connectedTime = connectedTime;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getID() {
		return ID;
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	public Date getConnectedTime() {
		return connectedTime;
	}
	public void setConnectedTime(Date connectedTime) {
		this.connectedTime = connectedTime;
	}
}
