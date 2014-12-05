package projeto.main;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import java.util.Random;

public class SourceAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private String category;
    private Float randomnessPercentage;
    private Float randomnessForCategoryPercentage;
    private Random r;
    
    protected void setup() {
    	
    	r = new Random();
		String[] args = (String[]) getArguments();
		category = args[0];
		randomnessPercentage = Float.valueOf(args[1]);
		if(args.length==3)
			randomnessForCategoryPercentage =  Float.valueOf(args[2]);
		else randomnessForCategoryPercentage = 0.0f;
    	
        System.out.println("Agent "+getLocalName()+" waiting for questions...");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );

        addBehaviour(new AchieveREResponder(this, template) {

			private static final long serialVersionUID = 1L;

			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
                ACLMessage inform = request.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                String question = request.getContent();
                System.out.println("Agent "+getLocalName()+" got the question: "+question);
                
                String content = "dont know";
                
                	Question q = KnowledgeBase.getInstance().getQuestion(question);
                	
                	if(!category.equals(q.category.toString()))
                	{
                		if(r.nextFloat()>=randomnessPercentage)
                		{
                            System.out.println("Agent "+getLocalName()+" says: i'm not an expert but I'm answering right");
                    		content = q.answersText[q.rightAnswer];
                		}
                    	else{
                            System.out.println("Agent "+getLocalName()+" says: i'm not an expert and I'm answering randomly");
                            System.out.println("Agent "+getLocalName()+" got the question: "+question);
                    		content = q.answersText[r.nextInt(4)];
                    	}
                	}else               	
                    {
                		if(r.nextFloat()>=randomnessForCategoryPercentage)
                		{
                            System.out.println("Agent "+getLocalName()+" says: i'm an expert in this category and I'm answering right");
                    		content = q.answersText[q.rightAnswer];
                		}
                    	else
                    		{
                    		 content = q.answersText[r.nextInt(4)];
                             System.out.println("Agent "+getLocalName()+" says: i'm an expert in this category but I'm answering randomly");
                    		}
                    }
                
                	assert(!content.equals("dont know"));
                	
                inform.setContent(content);
                return inform;
            }
        } );
    }

}

