package projeto.main;

import jade.core.ProfileImpl;
import jade.tools.rma.rma;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class GUI {

    public static void main(String[] args) {
    	
        Integer nQuestions = 200;
        KnowledgeBase knowledgeBase = KnowledgeBase.getInstance();
        knowledgeBase.fillRandom(nQuestions);
        
		AgentContainer container = jade.core.Runtime.instance().createMainContainer( new ProfileImpl() );
		try {
			AgentController rma = container.createNewAgent("rma", rma.class.getName(), null);
		    rma.start();
			
			String asource[]={"","0.7"};
			AgentController source1 = container.createNewAgent("source0", SourceAgent.class.getName(), asource);
			source1.start();
			
			String asource2[]={Category.GERAL.toString(),"0.7","0.5"};
			AgentController source2 = container.createNewAgent("source1", SourceAgent.class.getName(), asource2);
			source2.start();
			
			String aPlayer[]={BETA.class.getSimpleName(),source1.getName(),source2.getName()};
			AgentController player1 = container.createNewAgent("player0", PlayerAgent.class.getName(), aPlayer);
			player1.start();
			
			String aPlayer2[]={Sinalpha.class.getSimpleName(),source1.getName(),source2.getName()};
			AgentController player2 = container.createNewAgent("player1", PlayerAgent.class.getName(), aPlayer2);
			player2.start();
			
			String aAsker[] = {"50",player1.getName(),player2.getName()};
			AgentController asker = container.createNewAgent("asker", AskerAgent.class.getName(), aAsker);
			asker.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
