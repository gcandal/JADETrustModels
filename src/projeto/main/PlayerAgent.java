/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package projeto.main;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

public class PlayerAgent extends Agent {
	private TrustModel trustModel;
	private String[] sources;
	private String answer;

	protected void setup() {
		
		System.out.println("Agent "+getLocalName()+" waiting for requests...");
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );

		String[] args = (String[]) getArguments();
		String trustModelstr = args[0];
		sources = new String[args.length-1];
		for(int i = 0; i < sources.length; i++)
			sources[i] = args[i+1];

		switch(trustModelstr) {
		case "BETA":
			this.trustModel = new BETA();
			break;
		case "Sinalpha":
			this.trustModel = new Sinalpha();
			break;
		default:
			assert(false);
		}
		
		for(int i = 0; i < sources.length; i++)
			this.trustModel.addSourceId(sources[i]);
		
		addBehaviour(new AchieveREResponder(this, template) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
				if(answer==null)
				{
					System.out.println("player received new question");
					String question = request.getContent();
					String[] strs = question.split(":-:");
					Integer round = Integer.valueOf(strs[0]);
					question = strs[1];
					Question q = KnowledgeBase.getInstance().getQuestion(question);
					String source = trustModel.chooseSource(q.category);
					addBehaviour(getAnswer(question,source, this));
					block();
					return null;
				}else
				{
					//ask
					System.out.println("player returned to new question");
					ACLMessage inform = request.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					inform.setContent(answer);
					answer = null;
					return inform;	
				}
			}
			
		} );
	}

	private AchieveREInitiator getAnswer(String question, String source, AchieveREResponder pending) {
		// Fill the REQUEST message
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(source, AID.ISLOCALNAME));
		
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
		msg.setContent(question);
		
		AchieveREResponder pendingResponder = pending;
		
		return new AchieveREInitiator(this, msg) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void handleInform(ACLMessage inform) {
				System.out.println("got the answer");
				answer = inform.getContent();
			}

			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent "+refuse.getSender().getName()+" refused to perform the requested action");
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					System.out.println("agent does not exist");
				}
				else {
					System.out.println("Agent "+failure.getSender().getName()+" failed to perform the requested action");
				}
			}
			
			@SuppressWarnings("rawtypes")
			protected void handleAllResultNotifications(Vector notifications) {
				System.out.println("sources returned");
				pendingResponder.restart();
			}
		};
	}
}

