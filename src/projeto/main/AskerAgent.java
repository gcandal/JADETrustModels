package projeto.main;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Date;
import java.util.Vector;

public class AskerAgent extends Agent {
	private int nResponders = 0, nQuestions = 0;

	protected void setup() {
		String[] args = (String[]) getArguments();

		if(!validArguments(args))
			return;

		nResponders = Integer.parseInt(args[0]);
		nQuestions = Integer.parseInt(args[1]);

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
		addBehaviour(cena());
	}

	private AchieveREInitiator cena() {
		// Fill the REQUEST message
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for (int i = 0; i < nResponders; ++i) {
			msg.addReceiver(new AID("responder" + i, AID.ISLOCALNAME));
		}

		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
		msg.setContent("dummy-action");

		System.out.println("A perguntar a " + nResponders + " agentes...");
		
		return new AchieveREInitiator(this, msg) {
			protected void handleInform(ACLMessage inform) {
				System.out.println("Agent "+inform.getSender().getName()+" successfully performed the requested action");
			}

			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent "+refuse.getSender().getName()+" refused to perform the requested action");
				nResponders--;
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					// FAILURE notification from the JADE runtime: the receiver
					// does not exist
					System.out.println("Responder does not exist");
				}
				else {
					System.out.println("Agent "+failure.getSender().getName()+" failed to perform the requested action");
				}
			}
			
			@SuppressWarnings("rawtypes")
			protected void handleAllResultNotifications(Vector notifications) {
				if (notifications.size() < nResponders) {
					// Some responder didn't reply within the specified timeout
					System.out.println("Timeout expired: missing "+(nResponders - notifications.size())+" responses");
				}
				
				System.out.println("All responded!");
				
				if( nQuestions-- > 0 )
					askQuestion();
			}
		};
	}
}
