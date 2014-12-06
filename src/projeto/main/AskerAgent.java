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

public class AskerAgent extends Agent {
	
	private static final long serialVersionUID = 1L;

	private int nQuestions = 0;
	private String[] players;
	private HashMap<String, Integer> score;
	private int current_round = 0;
	private ArrayList<Question> questions;
	
	protected void setup() {
		String[] args = (String[]) getArguments();

		nQuestions = Integer.parseInt(args[0]);
		players = new String[args.length-1];
		for(int i = 0; i < players.length; i++)
			players[i] = args[i+1];		
		score = new HashMap<String, Integer>();
		questions = KnowledgeBase.getInstance().getNRandomQuestions(nQuestions);
		askQuestion();
	}

	
	private void askQuestion() {
		addBehaviour(sendQuestion());
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

		System.out.println("Asking " + players.length + " players...");
		
		return new AchieveREInitiator(this, msg) {
			private static final long serialVersionUID = 1L;

			protected void handleInform(ACLMessage inform) {
				String answer = inform.getContent();
				int correct;
				Question currentq = questions.get(current_round);
				if(answer.equals(currentq.answersText[currentq.rightAnswer]))
					correct = 1;
				else correct = -1;
				
				updateScore(inform, correct);
				
				System.out.println("Agent "+inform.getSender().getLocalName()+" answered " + answer);
				
				ACLMessage tellscore = inform.createReply();
				tellscore.setPerformative(ACLMessage.INFORM);
				tellscore.setContent(String.valueOf(correct));
				myAgent.send(tellscore);
			}

			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent "+refuse.getSender().getLocalName()+" refused to perform the requested action");
				updateScore(refuse, -1);
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					System.out.println("Responder does not exist");
				}
				else {
					updateScore(failure, -1);
					System.out.println("Agent "+failure.getSender().getLocalName()+" failed to perform the requested action");
				}
			}
			
			protected void handleAllResultNotifications(@SuppressWarnings("rawtypes") Vector notifications) {
				if(notifications.size()!=players.length)
					System.err.println("Agent(s) missing. Where are them? :o");
				
				score.forEach((name,score) -> {
					System.out.print(name + " score ");
					System.out.println(score);});
				
				if( current_round < nQuestions-1)
				{
					current_round++;
					System.out.println("Next Round!");
					askQuestion();
				} else
				{
					System.out.println("Game is over");
				}
			}
		};
	}


	private void updateScore(ACLMessage inform, int correct) {
		Integer current_score = score.get(inform.getSender().getLocalName());
		if(current_score!=null)
			score.put(inform.getSender().getLocalName(), current_score+correct);
		else score.put(inform.getSender().getLocalName(), correct);
	}
}
