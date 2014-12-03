package projeto.main;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.io.IOException;
import java.util.Random;

public class SourceAgent extends Agent {

    private String category;
    private Float randomnessPercentage;
    private Random r;
    
    protected void setup() {
    	
    	r = new Random();
		String[] args = (String[]) getArguments();
		category = args[0];
		randomnessPercentage = Float.valueOf(args[1]);
    	
        System.out.println("Agent "+getLocalName()+" waiting for requests...");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );

        addBehaviour(new AchieveREResponder(this, template) {

			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
                System.out.println("Agent "+getLocalName()+": Action successfully performed");
                ACLMessage inform = request.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                String question = request.getContent();
                String content = "";
                if(category!="")
                {
                	Question q = KnowledgeBase.getInstance().getQuestion(question);
                	content = q.answersText[q.rightAnswer];
                    if(q.category.toString()==category)
                    {
                    	content = q.answersText[q.rightAnswer];
                    } else 
                    {
                    	if(r.nextFloat()>=randomnessPercentage)
                    		content = q.answersText[q.rightAnswer];
                    	else content = q.answersText[r.nextInt(4)];
                    }
                }
                inform.setContent(content);
                return inform;
            }
        } );
    }

}

