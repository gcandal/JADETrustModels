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
import java.util.function.BiConsumer;

public class AskerAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int nResponders = 0, nQuestions = 0;

	private HashMap<String, Integer> score;
	private int current_round = 0;
	private ArrayList<Question> questions;
	
	protected void setup() {
		String[] args = (String[]) getArguments();

		if(!validArguments(args))
			return;

		nResponders = Integer.parseInt(args[0]);
		nQuestions = Integer.parseInt(args[1]);

		questions = KnowledgeBase.getInstance().getNRandomQuestions(nQuestions);
		askQuestion();
	}

	private boolean validArguments(String[] args) {
		if( args == null || args.length != 2 ) {
			System.out.println("Invalid arguments for Asker specified, you must provide NrResponders and NrQuestions");

			return false;
		}

		return true;
	} 
	
	private void askQuestion() {
		addBehaviour(sendQuestion());
	}

	private AchieveREInitiator sendQuestion() {
		// Fill the REQUEST message
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for (int i = 0; i < nResponders; ++i) {
			msg.addReceiver(new AID("responder" + i, AID.ISLOCALNAME));
		}

		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
		msg.setContent(questions.get(current_round).questionText);

		System.out.println("A perguntar a " + nResponders + " agentes...");
		
		return new AchieveREInitiator(this, msg) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void handleInform(ACLMessage inform) {
				String answer = inform.getContent();
				int correct;
				if(Integer.valueOf(answer)==questions.get(current_round).rightAnswer)
				correct = 1;
				else correct = -1;
				
				Integer current_score = score.get(inform.getSender().getName());
				if(current_score!=null)
					score.put(inform.getSender().getName(), current_score+correct);
				else score.put(inform.getSender().getName(), correct);
				
				System.out.println("Agent "+inform.getSender().getName()+" answered.");
			}

			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent "+refuse.getSender().getName()+" refused to perform the requested action");
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					System.out.println("Responder does not exist");
				}
				else {
					System.out.println("Agent "+failure.getSender().getName()+" failed to perform the requested action");
				}
			}
			
			@SuppressWarnings("rawtypes")
			protected void handleAllResultNotifications(Vector notifications) {
				if (notifications.size() < nResponders) {
					System.out.println("Timeout expired: missing "+(nResponders - notifications.size())+" responses");
				}
				
				System.out.println("Moving on!");
				
				if( current_round <  nQuestions)
				{
					current_round++;
					askQuestion();
				} else
				{
					System.out.println("Game is over");
					score.forEach((name,score) -> {
						System.out.print(name + " ");
						System.out.println(score);});
				}
			}
		};
	}
}
