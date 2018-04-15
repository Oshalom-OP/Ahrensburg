package main.java.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;

import main.java.memoranda.CurrentProject;
import main.java.memoranda.TeamMember;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * RepoSet Class
 * 
 * Developed to address US35. 
 * 
 * This class is used for setting the name of the GitHub owner/Repository
 * for a specific SchedStack project name.  This class is intended to be
 * called when the button "Set GitHub Repo" (JButton btnNewButtonRepo) is
 * pressed from the main Agenda view GUI.  
 * 
 * @author nick-okerberg
 *
 */
public class RepoSet extends JDialog {

	// Global Variables 
	private final JPanel _contentPanel = new JPanel();
	private JTextField _repoName;

	
	
	/**
	 * Main method.  This is the entry point for the class. It is called
	 * when the JButton is pressed, from AgendaPanel.java.  
	 * 
	 * @param args command line arguments. 
	 */
	public static void main(String[] args) {
		try {
			// Build new object. 
			RepoSet dialog = new RepoSet();
			// Close window when X is clicked. 
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			// Default visibility of dialog box is enabled. 
			dialog.setVisible(true);
			
			// Location and dimensions. 
	        Point loc = App.getFrame().getLocation();
	        Dimension frmSize = App.getFrame().getSize();
	        Dimension dlgSize = dialog.getSize();
	        dialog.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
	        
		} catch (Exception e) {
			System.out.println("Error in RepoSet.java, could not build dialog box");
			e.printStackTrace();
		}
	}


	
	/**
	 * Constructor for the dialog box. 
	 */
	public RepoSet() {
		// Set the title and layout. 
		setTitle("Set GitHub Repo");
		setBounds(100, 100, 413, 164);
		getContentPane().setLayout(new BorderLayout());
		_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(_contentPanel, BorderLayout.CENTER);
		_contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JLayeredPane layeredPane = new JLayeredPane();
			_contentPanel.add(layeredPane);
			
			JLabel lblGithubRepo = new JLabel("GitHub Owner/Repo:");
			lblGithubRepo.setBounds(10, 11, 135, 14);
			layeredPane.add(lblGithubRepo);
			
			_repoName = new JTextField();
			_repoName.setBounds(10, 36, 366, 20);
			layeredPane.add(_repoName);
			_repoName.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Get the String that the user enters. 
						String inputText = _repoName.getText();
						
						/* Moved error checking to ProjectImpl. It now throws RuntimeException
						// If the input text was null, then display an error. 
						if(repoName.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Enter GitHub Owner/Repo Name please");
						}
						
						// If the input text doesn't have one "/", display a format error. 
						else if((inputText.length() - inputText.replace("/", "").length()) != 1){
							JOptionPane.showMessageDialog(null, "Error...Enter in the format:  Owner/RepoName");
						}
						*/
						
						// If the above input validations pass, then do this next:
						//else {
						try {
							// Debugging lines. 
							System.out.print("Repo Name set to: \"" + _repoName.getText() + "\"");
							System.out.println(" for project: " + CurrentProject.get().getTitle());
							
							// Execute "addRepoName" from main.java.memoranda.CurrentProject. 
						  CurrentProject.get().addRepoName(_repoName.getText());
						  App.getFrame().refreshAgenda();
						  dispose();

						} catch (RuntimeException ex) {
							JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
