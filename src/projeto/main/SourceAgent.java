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

public class SourceAgent extends Agent {

    private String category;
    private int ID;

    public int getID(){ return ID; };
    public void setID(int i){ ID = i; };

    public String getCategory(){ return category; };
    public void setCategory(String c){ category = c; };


    protected void setup() {
        System.out.println("Agent "+getLocalName()+" waiting for requests...");
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST) );

        addBehaviour(new AchieveREResponder(this, template) {
            protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
                return null;
            }

            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
                System.out.println("Agent "+getLocalName()+": Action successfully performed");
                ACLMessage inform = request.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                try {
                    inform.setContentObject(new String("hey!"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return inform;
            }
        } );
    }

}

