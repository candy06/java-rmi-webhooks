package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;

import client.NotificationClient;
import common.Article;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.SystemColor;

public class ClientGUI {

	private JFrame frmClientApplication;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	
	/* Buttons */
	private JButton unsubscribeButton;
	private JButton subscribeButton;
	private JButton postArticleButton;
	private JButton postButton;
	
	/* text fields */
	private JTextField articleTitleTextField;
	private JTextField articleContentTextField;
	
	private NotificationClient client;
	private JPanel panel_2;
	private JTextArea textArea;
	private JButton getButton;
	private JButton getArticleButton;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String name = args[0];
					int port = Integer.parseInt(args[1]);
					ClientGUI window = new ClientGUI(name, port);
					window.frmClientApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 */
	public ClientGUI(String name, int port) throws RemoteException, MalformedURLException, NotBoundException {
		this.client = new NotificationClient(name, port, "rmi://localhost:8080/notification_server", this);
		initialize();
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	private void clearTextFields() {
		articleTitleTextField.setText("");
		articleContentTextField.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmClientApplication = new JFrame();
		frmClientApplication.setTitle("Client application ");
		frmClientApplication.setBounds(100, 100, 758, 386);
		frmClientApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClientApplication.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	try {
					client.getServerProxy().unsubscribe(client.getName(), client);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
		    }
		});
		
		panel_1 = new JPanel();
		frmClientApplication.getContentPane().add(panel_1, BorderLayout.WEST);
		
		lblNewLabel = new JLabel("Title: ");
		lblNewLabel.setFont(new Font("Courier New", Font.PLAIN, 14));
		
		articleTitleTextField = new JTextField();
		articleTitleTextField.setEditable(false);
		articleTitleTextField.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Content:");
		lblNewLabel_1.setFont(new Font("Courier New", Font.PLAIN, 14));
		
		postButton = new JButton("Post");
		postButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String articleTitle = articleTitleTextField.getText();
				String articleContent = articleContentTextField.getText();
				if (Objects.isNull(articleTitle) || Objects.isNull(articleContent) || articleTitle.trim().equals("") || articleContent.trim().equals("")) return;
				try {
					int requestCode = client.getServerProxy().postArticle(client, new Article(articleTitle, client.getName(), articleContent));
					if (requestCode == -1)
						textArea.setText("An article with the same title already exist...");
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				articleTitleTextField.setEditable(false);
				articleContentTextField.setEditable(false);
				postButton.setEnabled(false);
				getArticleButton.setEnabled(true);
				postArticleButton.setEnabled(true);
				unsubscribeButton.setEnabled(true);
				clearTextFields();
			}
		});
		postButton.setEnabled(false);
		
		articleContentTextField = new JTextField();
		articleContentTextField.setEditable(false);
		articleContentTextField.setColumns(10);
		
		getButton = new JButton("Get");
		getButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String articleTitle = articleTitleTextField.getText();
				if (Objects.isNull(articleTitle) || articleTitle.trim().equals("")) return;
				Article foundArticle = null;
				try {
					foundArticle = client.getServerProxy().getArticle(client, articleTitle);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if (Objects.isNull(foundArticle))
					textArea.setText("Article not found!");
				else 
					textArea.setText(foundArticle.toString());
				articleTitleTextField.setEditable(false);
				getButton.setEnabled(false);
				getArticleButton.setEnabled(true);
				postArticleButton.setEnabled(true);
				unsubscribeButton.setEnabled(true);
				clearTextFields();
			}
		});
		getButton.setEnabled(false);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGap(10)
									.addComponent(articleTitleTextField, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
								.addComponent(lblNewLabel)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGap(10)
									.addComponent(articleContentTextField, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
								.addComponent(lblNewLabel_1)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(83)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(getButton, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addComponent(postButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(36)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(33)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(articleTitleTextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(articleContentTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(51)
					.addComponent(postButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(getButton)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 51, 51));
		frmClientApplication.getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea.setForeground(SystemColor.infoText);
		textArea.setBackground(SystemColor.inactiveCaptionBorder);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 493, 270);
		panel_2.add(textArea);
		
		subscribeButton = new JButton("Subscribe");
		subscribeButton.setForeground(new Color(230, 230, 250));
		subscribeButton.setBackground(new Color(0, 128, 128));
		subscribeButton.setBounds(21, 292, 109, 44);
		panel_2.add(subscribeButton);
		subscribeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.getServerProxy().subscribe(client.getName(), client.getClientProxy());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				unsubscribeButton.setEnabled(true);
				postArticleButton.setEnabled(true);
				getArticleButton.setEnabled(true);
				subscribeButton.setEnabled(false);
			}
		});
		subscribeButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		
		unsubscribeButton = new JButton("Unsubscribe");
		unsubscribeButton.setForeground(new Color(230, 230, 250));
		unsubscribeButton.setBackground(new Color(0, 128, 128));
		unsubscribeButton.setBounds(140, 292, 109, 44);
		panel_2.add(unsubscribeButton);
		unsubscribeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.getServerProxy().unsubscribe(client.getName(), client.getClientProxy());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				textArea.setText("Goodbye!");
				subscribeButton.setEnabled(true);
				unsubscribeButton.setEnabled(false);
				postArticleButton.setEnabled(false);
				getArticleButton.setEnabled(false);
			}
		});
		unsubscribeButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		unsubscribeButton.setEnabled(false);
		
		postArticleButton = new JButton("Post article");
		postArticleButton.setForeground(new Color(230, 230, 250));
		postArticleButton.setBackground(new Color(0, 128, 128));
		postArticleButton.setBounds(258, 292, 110, 44);
		panel_2.add(postArticleButton);
		postArticleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				articleTitleTextField.setEditable(true);
				articleContentTextField.setEditable(true);
				postButton.setEnabled(true);
				getButton.setEnabled(false);
				postArticleButton.setEnabled(false);
				getArticleButton.setEnabled(false);
				unsubscribeButton.setEnabled(false);
			}
		});
		postArticleButton.setEnabled(false);
		postArticleButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		
		getArticleButton = new JButton("Get article");
		getArticleButton.setForeground(new Color(230, 230, 250));
		getArticleButton.setBackground(new Color(0, 128, 128));
		getArticleButton.setBounds(377, 292, 110, 44);
		panel_2.add(getArticleButton);
		getArticleButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		getArticleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				articleTitleTextField.setEditable(true);
				articleContentTextField.setEditable(false);
				postButton.setEnabled(false);
				getButton.setEnabled(true);
				postArticleButton.setEnabled(false);
				getArticleButton.setEnabled(false);
				unsubscribeButton.setEnabled(false);
			}
		});
		getArticleButton.setEnabled(false);
	}
}
