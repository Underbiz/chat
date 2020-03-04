package chat.client;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import chat.network.User;
import chat.server.ChatServer;

public class MainWindow extends JFrame {

	JButton create = new JButton("New Channel");
	JButton connect = new JButton("Connect");

	public MainWindow(String nickname) {
		setTitle("Privat Chat");
		setPreferredSize(new Dimension(350, 150));
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3, 1));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel create_panel = new JPanel(new GridLayout(1, 3));
		JPanel connect_panel = new JPanel(new GridLayout(2, 2));
		JLabel port_label = new JLabel("Port: ");

		JComboBox<Integer> portBox = new JComboBox<Integer>();
		for (int i = 1; i <= 9999; i++) {
			portBox.addItem(i);
		}

		JComboBox<Integer> port_connect_Box = new JComboBox<Integer>();
		for (int i = 1; i <= 9999; i++) {
			port_connect_Box.addItem(i);
		}

		JLabel ip_label = new JLabel("IP: ");
		JTextField ip_field = new JTextField();

		create_panel.add(port_label);
		create_panel.add(portBox);
		create_panel.add(create);

		connect_panel.add(ip_label);
		connect_panel.add(ip_field);
		connect_panel.add(port_label);
		connect_panel.add(port_connect_Box);

		add(create_panel);
		add(connect_panel);
		add(connect);

		// add(create);
		// add(connect);

		pack();
		setVisible(true);

		create.addActionListener(new ActionListener() {
			int port_for_create = (int) portBox.getSelectedItem();

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {

						ChatServer server = new ChatServer(port_for_create);

					}
				});
				thread.start();

			}
		});

		connect.addActionListener(new ActionListener() {
			int port_to_connect = (int) port_connect_Box.getSelectedItem();
			String ip = ip_field.getText().toString();

			@Override
			public void actionPerformed(ActionEvent e) {
				new Chat(MainWindow.this,nickname, ip, port_to_connect);
				MainWindow.this.setVisible(false);;
			}
		});

	}

}
