package projeto.main;

import jade.Boot;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class GUI {

    public static void main(String[] args) {
        
        Integer nQuestions = 200;
        KnowledgeBase knowledgeBase = KnowledgeBase.getInstance();
        knowledgeBase.fillRandom(nQuestions);

		MainFrame ui = new MainFrame();

		System.out.println(knowledgeBase);
    	
        String []arguments = {"-gui", ""};
		
		Boot.main(arguments);
		AgentContainer container = jade.core.Runtime.instance().createMainContainer( new ProfileImpl() );
		
		try {
			String asource[]={"","0.2"};
			AgentController source1 = container.createNewAgent("source0", "projeto.main.SourceAgent", asource);
			source1.start();
			
			String asource2[]={"GERAL","0.0"};
			AgentController source2 = container.createNewAgent("source1", "projeto.main.SourceAgent", asource2);
			source2.start();
			
			String aPlayer[]={"BETA","source0","source1"};
			AgentController player1 = container.createNewAgent("responder0", "projeto.main.PlayerAgent", aPlayer);
			player1.start();
			
			String aPlayer2[]={"BETA","source0","source1"};
			AgentController player2 = container.createNewAgent("responder1", "projeto.main.PlayerAgent", aPlayer2);
			player2.start();
			
			String aAsker[] = {"2","10"}; //TODO send sources
			AgentController asker = container.createNewAgent("initiator", "projeto.main.AskerAgent", aAsker);
			asker.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
    }
}
