package projeto.main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextPane;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JPanel;

public class QuizInterface {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuizInterface window = new QuizInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public QuizInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JTextPane txtpnKlNoimnklnbikgbT = new JTextPane();
		txtpnKlNoimnklnbikgbT.setEditable(false);
		txtpnKlNoimnklnbikgbT.setText("info ffsivn dvonr\nvoindf vkjnsnfib s\nfnoibdfçbl-");
		splitPane.setLeftComponent(txtpnKlNoimnklnbikgbT);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblQuestionAndOptions = new JLabel("Question and options");
		panel.add(lblQuestionAndOptions, BorderLayout.NORTH);
		
		JButton btnPlay = new JButton("Play");
		panel.add(btnPlay, BorderLayout.SOUTH);
		
		JLabel lblAnswers = new JLabel("Answers\n\n\n\n\n\n\n\n\n\n\n");
		panel.add(lblAnswers, BorderLayout.CENTER);
		
		JLabel lblScore = new JLabel("Score");
		panel.add(lblScore, BorderLayout.EAST);
	}
}
