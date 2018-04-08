package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JPasswordField;
import java.awt.Insets;
import java.awt.Point;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import main.java.memoranda.util.Util;

import com.jgoodies.forms.factories.FormFactory;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class SetCredDialog extends JDialog {
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SetCredDialog dialog = new SetCredDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
	        Point loc = App.getFrame().getLocation();
	        Dimension frmSize = App.getFrame().getSize();
	        Dimension dlgSize = dialog.getSize();
	        dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SetCredDialog() {
		setBounds(100, 100, 543, 222);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblUsername = new JLabel("Username:");
			getContentPane().add(lblUsername, "2, 2, 5, 1, left, default");
		}
		{
			textField = new JTextField();
			getContentPane().add(textField, "2, 4, 5, 1, fill, default");
			textField.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password");
			getContentPane().add(lblPassword, "2, 6, 5, 1, left, default");
		}
		{
			JButton btnNewButton = new JButton("Authenticate");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checkAuth();
				}
			});
			{
				passwordField = new JPasswordField();
				passwordField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						checkAuth();
					}
				});
				getContentPane().add(passwordField, "2, 8, 5, 1, fill, default");
			}
			getContentPane().add(btnNewButton, "1, 10, 6, 1");
		}
		{
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			getContentPane().add(btnCancel, "1, 12, 6, 1");
		}
	}
	public void checkAuth() {
		File file = new File(Util.getEnvDir()+"authencoded.txt");
		  
		//Create the file
		try {
			if (file.createNewFile()){
			System.out.println("File is created!");
			}else{
			System.out.println("File already exists.");
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		 
		//Write Content
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			@SuppressWarnings("deprecation")
			String userCredentials = textField.getText() + ":" + passwordField.getText(); //"username:password";
			String basicAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(userCredentials.getBytes());
			writer.write(basicAuth);
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			URL commitsURL = new URL("https://api.github.com/repos/ser316asu-2018/Ahrensburg/commits");
			HttpURLConnection con = (HttpURLConnection) commitsURL.openConnection();
			String content;
			content = new String(Files.readAllBytes(Paths.get(Util.getEnvDir()+"authencoded.txt")));
			con.setRequestProperty("Authorization", content);
			if(con.getHeaderField("x-ratelimit-limit").equals("5000")) {
				JOptionPane.showMessageDialog(null, "Authenciated successfull.");
				System.out.println(con.getHeaderField("x-ratelimit-limit"));
				dispose();
			}
			else {
				JOptionPane.showMessageDialog(null, "Authenciated failed.");
				System.out.println(con.getHeaderField("x-ratelimit-limit"));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
