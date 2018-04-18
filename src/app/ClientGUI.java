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
	private JButton cancelButton;
	
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
		
		postButton = new JButton("Create");
		postButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String articleTitle = articleTitleTextField.getText();
				String articleContent = articleContentTextField.getText();
				boolean titleError = (Objects.isNull(articleTitle) || articleTitle.trim().equals(""));
				boolean contentError = (Objects.isNull(articleContent) || articleContent.trim().equals(""));
				if (titleError || contentError) {
					textArea.setText("Error: no title or content typed for the article.");
					return;
				} else {
					try {
						int requestCode = client.getServerProxy().postArticle(client, new Article(articleTitle, client.getName(), articleContent));
						if (requestCode == -1)
							textArea.setText("Oops: an article with the same title already exist.");
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					articleTitleTextField.setEditable(false);
					articleContentTextField.setEditable(false);
					postButton.setEnabled(false);
					cancelButton.setEnabled(false);
					getArticleButton.setEnabled(true);
					postArticleButton.setEnabled(true);
					unsubscribeButton.setEnabled(true);
					clearTextFields();
				}
			}
		});
		postButton.setEnabled(false);
		
		articleContentTextField = new JTextField();
		articleContentTextField.setEditable(false);
		articleContentTextField.setColumns(10);
		
		getButton = new JButton("Read");
		getButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String articleTitle = articleTitleTextField.getText();
				if (Objects.isNull(articleTitle) || articleTitle.trim().equals("")) textArea.setText("Error: no title typed.");
				else {
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
					cancelButton.setEnabled(false);
					getArticleButton.setEnabled(true);
					postArticleButton.setEnabled(true);
					unsubscribeButton.setEnabled(true);
					clearTextFields();
				}
			}
		});
		getButton.setEnabled(false);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				unsubscribeButton.setEnabled(true);
				postArticleButton.setEnabled(true);
				getArticleButton.setEnabled(true);
				postButton.setEnabled(false);
				getButton.setEnabled(false);
				cancelButton.setEnabled(false);
				articleTitleTextField.setEditable(false);
				articleContentTextField.setEditable(false);
			}
		});
		cancelButton.setEnabled(false);
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(10)
							.addComponent(articleTitleTextField, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(10)
							.addComponent(articleContentTextField, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))
					.addContainerGap())
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(73)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(cancelButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
						.addComponent(getButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
						.addComponent(postButton, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
					.addGap(75))
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
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cancelButton)
					.addContainerGap(62, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 51, 51));
		frmClientApplication.getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setRows(2);
		textArea.setToolTipText("");
		textArea.setFont(new Font("DialogInput", Font.BOLD, 15));
		textArea.setForeground(Color.ORANGE);
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 493, 270);
		panel_2.add(textArea);
		
		subscribeButton = new JButton("Subscribe");
		subscribeButton.setForeground(new Color(230, 230, 250));
		subscribeButton.setBackground(new Color(0, 0, 128));
		subscribeButton.setBounds(10, 292, 113, 44);
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
		unsubscribeButton.setBackground(new Color(128, 0, 0));
		unsubscribeButton.setBounds(133, 292, 120, 44);
		panel_2.add(unsubscribeButton);
		unsubscribeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.getServerProxy().unsubscribe(client.getName(), client.getClientProxy());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				textArea.setText("Goodbye, see you soon!");
				subscribeButton.setEnabled(true);
				unsubscribeButton.setEnabled(false);
				postArticleButton.setEnabled(false);
				getArticleButton.setEnabled(false);
			}
		});
		unsubscribeButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		unsubscribeButton.setEnabled(false);
		
		postArticleButton = new JButton("Create article");
		postArticleButton.setForeground(new Color(230, 230, 250));
		postArticleButton.setBackground(new Color(0, 128, 0));
		postArticleButton.setBounds(263, 292, 120, 44);
		panel_2.add(postArticleButton);
		postArticleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				articleTitleTextField.setEditable(true);
				articleContentTextField.setEditable(true);
				postButton.setEnabled(true);
				cancelButton.setEnabled(true);
				getButton.setEnabled(false);
				postArticleButton.setEnabled(false);
				getArticleButton.setEnabled(false);
				unsubscribeButton.setEnabled(false);
			}
		});
		postArticleButton.setEnabled(false);
		postArticleButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		
		getArticleButton = new JButton("Read article");
		getArticleButton.setForeground(new Color(230, 230, 250));
		getArticleButton.setBackground(new Color(0, 128, 0));
		getArticleButton.setBounds(393, 292, 110, 44);
		panel_2.add(getArticleButton);
		getArticleButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		getArticleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				articleTitleTextField.setEditable(true);
				getButton.setEnabled(true);
				cancelButton.setEnabled(true);
				articleContentTextField.setEditable(false);
				postButton.setEnabled(false);
				postArticleButton.setEnabled(false);
				getArticleButton.setEnabled(false);
				unsubscribeButton.setEnabled(false);
			}
		});
		getArticleButton.setEnabled(false);
	}
}
