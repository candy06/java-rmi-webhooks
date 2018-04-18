package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TextArea;

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
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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
	
	private NotificationClient client;
	private JPanel panel_2;
	private JTextArea textArea;
	private JTextArea articleTextArea;
	private JButton getButton;
	private JButton getArticleButton;
	private JScrollPane scrollPane_2;
	private JTextArea contentTextArea;
	

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
		contentTextArea.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmClientApplication = new JFrame();
		frmClientApplication.setResizable(false);
		frmClientApplication.setTitle("Client application ");
		frmClientApplication.setBounds(100, 100, 1083, 490);
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
				String articleContent = contentTextArea.getText();
				boolean titleError = (Objects.isNull(articleTitle) || articleTitle.trim().equals(""));
				boolean contentError = (Objects.isNull(articleContent) || articleContent.trim().equals(""));
				if (titleError || contentError) 
					JOptionPane.showMessageDialog(frmClientApplication,
						    "No title or content entered!",
						    "Oops...",
						    JOptionPane.ERROR_MESSAGE);
				else {
					try {
						int requestCode = client.getServerProxy().postArticle(client, new Article(articleTitle, client.getName(), articleContent));
						if (requestCode == -1) {
							JOptionPane.showMessageDialog(frmClientApplication,
								    "An article with the same title already exists!",
								    "Oops...",
								    JOptionPane.ERROR_MESSAGE);
						} else {
							articleTitleTextField.setEditable(false);
							contentTextArea.setEditable(false);
							postButton.setEnabled(false);
							cancelButton.setEnabled(false);
							getArticleButton.setEnabled(true);
							postArticleButton.setEnabled(true);
							unsubscribeButton.setEnabled(true);
							clearTextFields();
						}
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		postButton.setEnabled(false);
		
		getButton = new JButton("Read");
		getButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String articleTitle = articleTitleTextField.getText();
				if (Objects.isNull(articleTitle) || articleTitle.trim().equals("")) 
					JOptionPane.showMessageDialog(frmClientApplication,
						    "No title entered!",
						    "Oops...",
						    JOptionPane.ERROR_MESSAGE);
				
				else {
					Article foundArticle = null;
					try {
						foundArticle = client.getServerProxy().getArticle(client, articleTitle);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					if (Objects.isNull(foundArticle))
						articleTextArea.setText("Article not found!");
					else 
						articleTextArea.setText(foundArticle.toString());
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
				contentTextArea.setEnabled(false);
				postButton.setEnabled(false);
				getButton.setEnabled(false);
				cancelButton.setEnabled(false);
				articleTitleTextField.setEditable(false);
			}
		});
		cancelButton.setEnabled(false);
		
		scrollPane_2 = new JScrollPane();
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(articleTitleTextField, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
					.addGap(20))
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addContainerGap(155, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane_2))
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addGap(20)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(getButton, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
								.addComponent(cancelButton, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addGap(20)
							.addComponent(postButton, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel)
							.addGap(8)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(33)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(articleTitleTextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(postButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(getButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(cancelButton)
					.addGap(28))
		);
		
		contentTextArea = new JTextArea();
		contentTextArea.setLineWrap(true);
		contentTextArea.setEditable(false);
		scrollPane_2.setViewportView(contentTextArea);
		panel_1.setLayout(gl_panel_1);
		
		panel_2 = new JPanel();
		panel_2.setBackground(new Color(0, 51, 51));
		frmClientApplication.getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(null);
		
		subscribeButton = new JButton("Subscribe");
		subscribeButton.setForeground(new Color(230, 230, 250));
		subscribeButton.setBackground(new Color(0, 0, 128));
		subscribeButton.setBounds(10, 396, 113, 44);
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
		unsubscribeButton.setBounds(133, 396, 120, 44);
		panel_2.add(unsubscribeButton);
		unsubscribeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.getServerProxy().unsubscribe(client.getName(), client.getClientProxy());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				textArea.setText(textArea.getText() + "### Goodbye, see you soon!\n\n");
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
		postArticleButton.setBounds(263, 396, 120, 44);
		panel_2.add(postArticleButton);
		postArticleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				articleTitleTextField.setEditable(true);
				contentTextArea.setEditable(true);
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
		getArticleButton.setBounds(393, 396, 110, 44);
		panel_2.add(getArticleButton);
		getArticleButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		getArticleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				articleTitleTextField.setEditable(true);
				getButton.setEnabled(true);
				cancelButton.setEnabled(true);
				postButton.setEnabled(false);
				postArticleButton.setEnabled(false);
				getArticleButton.setEnabled(false);
				unsubscribeButton.setEnabled(false);
			}
		});
		getArticleButton.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 493, 374);
		panel_2.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setRows(2);
		textArea.setToolTipText("");
		textArea.setFont(new Font("DialogInput", Font.BOLD, 17));
		textArea.setForeground(Color.ORANGE);
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(513, 11, 315, 429);
		panel_2.add(scrollPane_1);
		
		articleTextArea = new JTextArea();
		articleTextArea.setLineWrap(true);
		articleTextArea.setWrapStyleWord(true);
		articleTextArea.setFont(new Font("Bookman Old Style", Font.PLAIN, 15));
		articleTextArea.setEditable(false);
		articleTextArea.setAlignmentX(TextArea.CENTER_ALIGNMENT);
		scrollPane_1.setViewportView(articleTextArea);
	}
}
