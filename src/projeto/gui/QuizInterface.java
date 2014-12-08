package projeto.gui;

import jade.core.ProfileImpl;
import jade.tools.rma.rma;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import projeto.main.AskerAgent;
import projeto.main.BETA;
import projeto.main.Category;
import projeto.main.KnowledgeBase;
import projeto.main.KnowledgeBaseParser;
import projeto.main.PlayerAgent;
import projeto.main.Question;
import projeto.main.Sinalpha;
import projeto.main.SourceAgent;

public class QuizInterface {

	
	private void lauchAgents()
	{
	        KnowledgeBase knowledgeBase = KnowledgeBase.getInstance();
            knowledgeBase.fillRandom(1000);
	        //KnowledgeBaseParser.parseFile(knowledgeBase, Category.GENERAL);
	        //KnowledgeBaseParser.parseFile(knowledgeBase, Category.PHYSICS);
	        //KnowledgeBaseParser.parseFile(knowledgeBase, Category.TECHNOLOGY);
	         
			AgentContainer container = jade.core.Runtime.instance().createMainContainer( new ProfileImpl() );
			try {
				AgentController rma = container.createNewAgent("rma", rma.class.getName(), null);
			    rma.start();

				String asource[]={"","0.9"};
                AgentController source1 = container.createNewAgent("source0", SourceAgent.class.getName(), asource);
                source1.start();

				String asource2[]={Category.GENERAL.toString(),"0.1","0.9"};
                //String asource2[]={"","0.3"};
				AgentController source2 = container.createNewAgent("source1", SourceAgent.class.getName(), asource2);
				source2.start();

                String asource3[]={Category.PHYSICS.toString(),"0.1","0.9"};
                //String asource3[]={"","0.7"};
                AgentController source3 = container.createNewAgent("source2", SourceAgent.class.getName(), asource3);
                source3.start();

                String asource4[]={"","0.9"};
                AgentController source4 = container.createNewAgent("source3", SourceAgent.class.getName(), asource4);
                source4.start();

                String asource5[]={"","0.9"};
                AgentController source5 = container.createNewAgent("source4", SourceAgent.class.getName(), asource5);
                source5.start();


				//String aPlayer[]={BETA.class.getSimpleName(),"true",source1.getName(),source2.getName()};
                String aPlayer[]={BETA.class.getSimpleName(),"true",source1.getName(),source2.getName(),source3.getName(),source4.getName(),source5.getName()};
                AgentController player1 = container.createNewAgent("player0", PlayerAgent.class.getName(), aPlayer);
				player1.start();

				//String aPlayer2[]={Sinalpha.class.getSimpleName(),"true",source1.getName(),source2.getName()};
                String aPlayer2[]={Sinalpha.class.getSimpleName(),"true",source1.getName(),source2.getName(),source3.getName(),source4.getName(),source5.getName()};
                AgentController player2 = container.createNewAgent("player1", PlayerAgent.class.getName(), aPlayer2);
				player2.start();

				String aAsker[] = {"1000","false",player1.getName(),player2.getName()};
				String playersNames[] = {player1.getName(),player2.getName()};
				addPlayersToGui(playersNames);
				AgentController asker = container.createNewAgent("asker", AskerAgent.class.getName(), aAsker);
				asker.start();
				AskerAgent.instance.setController(this);
			}
			catch (Exception e)
			{
				System.err.println("Error starting agents.");
			}
	}
	

	private JFrame frame;

	JTextPane info_console;
	JButton btnPlay;
	JPanel score_panel;
	JTextPane lblQuestion;
	JTextPane panel_3;
	JTextPane[] playersLabels;
	JTextPane[] scoreLabels;
	String[] playersNames;
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
		frame.setBounds(100, 100, 600, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		info_console = new JTextPane();
		JScrollPane jsp = new JScrollPane(info_console);
		info_console.setEditable(false);
		info_console.setText("                                                 \n");
		Printer.output = info_console;
		Printer.scroll = jsp;
		splitPane.setLeftComponent(jsp);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		btnPlay = new JButton("Play");
		panel.add(btnPlay, BorderLayout.SOUTH);
		
		score_panel = new JPanel();
		panel.add(score_panel, BorderLayout.EAST);
		score_panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		lblQuestion = new JTextPane();
		panel.add(lblQuestion, BorderLayout.NORTH);
				
		panel_3 = new JTextPane();
		panel.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		lauchAgents();
		
		btnPlay.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  	Printer.println("-------------------------");
					AskerAgent.instance.askQuestion();
			  }
			});
	}

	public void setQuestion(Question question) {
		StringBuilder ss = new StringBuilder();
		ss.append("-> ");
		ss.append(question.questionText);
		ss.append("\n A: ");
		ss.append(question.answersText[0]);
		ss.append("\n B: ");
		ss.append(question.answersText[1]);
		ss.append("\n C: ");
		ss.append(question.answersText[2]);
		ss.append("\n D: ");
		ss.append(question.answersText[3]);
		ss.append("\n\nCorrect Answer: ");
		ss.append(question.answersText[question.rightAnswer]);
		ss.append("\n");
		lblQuestion.setText(ss.toString());
	}
	
	public void setPlayerAnswer(String player, String answer)
	{
		for(int i = 0; i < playersNames.length; i++)
		{
			if(playersNames[i].equals(player))
				playersLabels[i].setText(playersNames[i] + ": " + answer);
		}
	}
	
	public void setPlayerScore(String player, String score)
	{
		for(int i = 0; i < playersNames.length; i++)
		{
			if(playersNames[i].equals(player))
				scoreLabels[i].setText(score);
		}
	}

	private void addPlayersToGui(String[] playersNames2) {
		playersNames = playersNames2;
		playersLabels = new JTextPane[playersNames2.length];
		scoreLabels = new JTextPane[playersNames2.length];
		for(int i = 0; i < playersNames.length; i++)
		{
			JTextPane lblPlayer = new JTextPane();
			lblPlayer.setText(playersNames[i]);
			playersLabels[i] = lblPlayer;
			panel_3.add(lblPlayer);
			
			JTextPane lblScore = new JTextPane();
			lblScore.setText("0");
			scoreLabels[i] = lblScore;
			score_panel.add(lblScore);
		}
		
	}
}
