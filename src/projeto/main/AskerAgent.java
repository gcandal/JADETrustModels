package projeto.main;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import projeto.gui.Printer;
import projeto.gui.QuizInterface;

public class AskerAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	public static AskerAgent instance;
	private QuizInterface controller;
	private int nQuestions = 0;
	private String[] players;
	private HashMap<String, Integer> score;
	private int current_round = 0;
	private ArrayList<Question> questions;
	private boolean ready;
	private boolean iscontrolled;
	
	public AskerAgent()
	{
		super();
		instance = this;
	}
	
	protected void setup() {
		String[] args = (String[]) getArguments();

		nQuestions = Integer.parseInt(args[0]);
		iscontrolled = Boolean.valueOf(args[1]);
		players = new String[args.length-2];
		for(int i = 0; i < players.length; i++)
			players[i] = args[i+2];		
		score = new HashMap<String, Integer>();
		questions = KnowledgeBase.getInstance().getNRandomQuestions(nQuestions);
		ready = true;
		Printer.println("Asker is ready.");
		if(!iscontrolled)
			askQuestion();
	}

	
	public void askQuestion() {
		if(ready)
		{
			ready = false;
			addBehaviour(sendQuestion());
		}
	}

	private AchieveREInitiator sendQuestion() {
		// Fill the REQUEST message
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for (int i = 0; i < players.length; ++i) {
			msg.addReceiver(new AID(players[i], AID.ISGUID));
		}

		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
		msg.setContent(current_round + ":-:" + questions.get(current_round).questionText);
		if(iscontrolled)
			controller.setQuestion(questions.get(current_round));

		Printer.println("Asking " + players.length + " players...");
		
		return new AchieveREInitiator(this, msg) {
			private static final long serialVersionUID = 1L;

			protected void handleInform(ACLMessage inform) {
				String answer = inform.getContent();
				
				if(controller!=null)
					controller.setPlayerAnswer(inform.getSender().getName(), answer);
				
				int correct;
				Question currentq = questions.get(current_round);
				if(answer.equals(currentq.answersText[currentq.rightAnswer]))
					correct = 1;
				else correct = -1;
				
				updateScore(inform, correct);
				
				Printer.println("Agent "+inform.getSender().getLocalName()+" answered " + answer);
				
				ACLMessage tellscore = inform.createReply();
				tellscore.setPerformative(ACLMessage.INFORM);
				tellscore.setContent(String.valueOf(correct));
				myAgent.send(tellscore);
			}

			protected void handleRefuse(ACLMessage refuse) {
				Printer.println("Agent "+refuse.getSender().getLocalName()+" refused to perform the requested action");
				updateScore(refuse, -1);
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					Printer.println("Responder does not exist");
				}
				else {
					updateScore(failure, -1);
					Printer.println("Agent "+failure.getSender().getLocalName()+" failed to perform the requested action");
				}
			}
			
			protected void handleAllResultNotifications(@SuppressWarnings("rawtypes") Vector notifications) {
				if(notifications.size()!=players.length)
					System.err.println("Agent(s) missing. Where are them? :o");
				
				score.forEach((name,score) -> {
					Printer.println(name + " score " + Integer.toString(score));});
				
				if( current_round < nQuestions-1)
				{
					current_round++;
					Printer.println("Next Round!");
					ready = true;
					if(!iscontrolled)
						askQuestion();
				} else
				{
					Printer.println("Game is over");
				}
			}
		};
	}


	private void updateScore(ACLMessage inform, int correct) {
		Integer current_score = score.get(inform.getSender().getLocalName());
		if(current_score==null)
			current_score = 0;
		
		int newscore = current_score+correct;
		
		if(controller!=null)
			controller.setPlayerScore(inform.getSender().getName(), String.valueOf(newscore));

		score.put(inform.getSender().getLocalName(),newscore);
	}


	public QuizInterface getController() {
		return controller;
	}


	public void setController(QuizInterface controller) {
		this.controller = controller;
	}
}
