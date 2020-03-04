package chat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;
import chat.network.User;

public class Chat extends JFrame implements TCPConnectionListener {

	private TCPConnection connection;
	
	DefaultListModel<String> listOfChat = new DefaultListModel<>();
	JList<String> list = new JList<>(listOfChat);

	ArrayList<Messages> history = new ArrayList<Messages>();
	int lineNumber = 0;
	Date date = new Date();
	
	MainWindow main;
	

	public Chat(MainWindow mainWindow,String nickName, String ip, int port) {
		this.main = mainWindow;

		setSize(600, 600);
		setLocationRelativeTo(null);
		
		setTitle("Private Chat");
		setLayout(new BorderLayout());
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(600, 50));
		infoPanel.setBackground(Color.GRAY);
		infoPanel.setLayout(new GridLayout(1, 2));

		JLabel name_of_this_user = new JLabel(nickName);
		JLabel name_of_other_user = new JLabel();

		infoPanel.add(name_of_this_user);
		

		JPanel chatPanel = new JPanel();
		chatPanel.setPreferredSize(new Dimension(600, 300));
		chatPanel.setBackground(new Color(8, 107, 140));
		chatPanel.setLayout(new BorderLayout());

		chatPanel.add(new JScrollPane(list), BorderLayout.CENTER);
		
		
		 

		JPanel inputPanel = new JPanel();
		inputPanel.setPreferredSize(new Dimension(600, 70));
		inputPanel.setLayout(new BorderLayout());
		JTextField inputField = new JTextField();
		JTextArea inputArea =  new JTextArea();

		JButton send = new JButton("Send..");
		inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
		inputPanel.add(send, BorderLayout.EAST);

		add(infoPanel, BorderLayout.NORTH);
		add(new JScrollPane(chatPanel), BorderLayout.CENTER);

		add(inputPanel, BorderLayout.SOUTH);

		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Date date = new Date();
				String dateOfMessage = date.toString();
				Messages msg = new Messages(inputArea.getText(), dateOfMessage);
				history.add(msg);
				SimpleDateFormat ft = new SimpleDateFormat("hh:mm");
				String message = nickName + " [" + ft.format(date) + "]" + " : " + "\n \t \t \t" + msg.getValue();
				if (message.equals("")) {
					return;
				} else {
					connection.sendString(message);
				}
				inputField.requestFocus();
				int lastIndex = list.getModel().getSize() - 1;
				if (lastIndex >= 0) {
				   list.ensureIndexIsVisible(lastIndex);
				}
				
				
				inputArea.setText("");
				
				pack();
				Chat.this.repaint();

			}
		});

		pack();
		setVisible(true);
		try {
			connection = new TCPConnection(this, ip, port);
		} catch (IOException e) {
			printMsg("Connection exceprion: " + e);
		}
	
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String timeOfConnection = ft.format(date);
		
		try {
			writeClients(nickName, timeOfConnection);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent windowEvent) {
	            main.setVisible(true);
	        }
	    });

	}

	@Override
	public void onConnectionReady(TCPConnection tcpConnection) {
		printMsg("Connection ready...");
	}

	@Override
	public void onReceiveSrting(TCPConnection tcpConnection, String value) {
		printMsg(value);
	}

	@Override
	public void onDisconnect(TCPConnection tcpConnection) {
		printMsg("Connection close...");
		Chat.this.repaint();
	}

	@Override
	public void onExeption(TCPConnection tcpConnection, Exception e) {
		printMsg("Connection exceprion: " + e);
	}

	private synchronized void printMsg(String value) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				listOfChat.addElement(value);

			}
		});
	}
	
	void writeClients(String nickname, String time) throws Exception {
		String PATH = "D:/Work/Java/studyspace/ChatPrivate/res/Clients.txt";
		
		File file = new File(PATH);
		if (file.exists() && !file.isDirectory()) {
			try {

				File myFile = new File(PATH);
				FileReader fileReader = new FileReader(myFile);
				LineNumberReader lineNumberReader = new LineNumberReader(fileReader);

				while (lineNumberReader.readLine() != null) {
					lineNumber++;
				}

				lineNumberReader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(PATH, true));
				bw.write((lineNumber + 1)+ ") " + nickname + ". Connected [" + time + "]"+"\n");
				bw.flush();
				bw.close();

			} catch (IOException e) {
				JOptionPane.showInternalMessageDialog(this, e);
			}

		} else {
			File connectins = new File(PATH);
			connectins.createNewFile();
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(PATH));
				bw.write("1) " + nickname + ". Connected [" + time + "]" + "\n");
				bw.flush();
				bw.close();;
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	
	
	

}
