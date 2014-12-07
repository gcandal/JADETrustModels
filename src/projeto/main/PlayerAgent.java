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

import java.util.ArrayList;

import projeto.gui.Chart;
import projeto.gui.Printer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PlayerAgent extends Agent {
	private static final long serialVersionUID = 1L;
	private TrustModel trustModel;
	private String[] sources;
	private boolean iscontrolled;
	
	protected void setup() {
		
		Printer.println("Agent "+getLocalName()+" waiting for requests...");
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );
		
		MessageTemplate templateReply = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM) );

		String[] args = (String[]) getArguments();
		String trustModelstr = args[0];
		iscontrolled = Boolean.valueOf(args[1]);
		sources = new String[args.length-2];
		for(int i = 0; i < sources.length; i++)
			sources[i] = args[i+2];

		switch(trustModelstr) {
		case "BETA":
			this.trustModel = new BETA();
			break;
		case "Sinalpha":
			this.trustModel = new Sinalpha();
			break;
		default:
			assert(false);
			break;
		}
		
		for(int i = 0; i < sources.length; i++)
			this.trustModel.addSourceId(sources[i]);
		
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1L;

			public void action() {

		      ACLMessage request = myAgent.blockingReceive(template);
		      if(request==null)
		      {
		    	  System.err.println(myAgent.getLocalName() + " didn't receive any message. He feels lonely. :(");
		    	  return;
		      }
		    	  String question = request.getContent();
		    	  String[] strs = question.split(":-:");
		    	  @SuppressWarnings("unused")
		    	  Integer round = Integer.valueOf(strs[0]);
		    	  question = strs[1];
		    	  Printer.println(myAgent.getLocalName() + " received new question: "+question);
		    	  Question q = KnowledgeBase.getInstance().getQuestion(question);
		    	  String source = trustModel.chooseSource(q.category);
		    	  
		    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(source, AID.ISGUID));				
				msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
				msg.setContent(question);
				myAgent.send(msg);

				ACLMessage answer = myAgent.blockingReceive(templateReply,1000);
			      if(answer==null)
			      {
			    	  System.err.println(myAgent.getLocalName() + " says: my sources didn't reply.");
			    	  return;
			      }
				String answerstr = answer.getContent();
				Printer.println(myAgent.getLocalName() + " says: Got answer "+answerstr);
				ACLMessage reply = request.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(answerstr);
				myAgent.send(reply);
				
			    ACLMessage score = myAgent.blockingReceive(templateReply,1000);
			    if(score==null)
			      {
			    	  System.err.println(myAgent.getLocalName() + " says: Was it correct? Was it wrong? The asker didnt reply");
			    	  return;
			      }
			    int updown = Integer.valueOf(score.getContent());
				Printer.println("Got score info "+updown);
			    trustModel.addRecord(updown==1 ? true : false, source, q.category);
			    if(iscontrolled)
			    {
			    	//TODO Fix drawing
	                ArrayList<Double> chartValues = trustModel.getTrust(source, q.category);
	                if(chartValues.size()>1)
	                	Chart.drawSinalpha(chartValues);	
			    }
		    }
		  } );
		
	}
}