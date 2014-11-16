package projeto.main;

import jade.Boot;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.stream.IntStream;

public class GUI {

	public static void main(String[] args) {
        /*
		String []arguments = {"-gui", ""};
		
		Boot.main(arguments);
		AgentContainer container = jade.core.Runtime.instance().createMainContainer( new ProfileImpl() );
		
		try {
			arguments[0] = "BETA";
			AgentController ac = container.createNewAgent("responder0", "projeto.main.PlayerAgent", null);
			ac.start();
		
			arguments[0] = "Sinalpha";
			ac = container.createNewAgent("responder0", "projeto.main.PlayerAgent", null);
			ac.start();
			
			arguments[0] = "1";		// nrPlayers
			arguments[1] = "10";	// nrRounds
			ac = container.createNewAgent("initiator", "projeto.main.AskerAgent", arguments);
			ac.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}*/

        Integer nQuestions = 200;
        KnowledgeBase knowledgeBase = KnowledgeBase.getInstance();

        fillKnowledgeBase(knowledgeBase, nQuestions);

        System.out.println(knowledgeBase);
	}

    private static void fillKnowledgeBase(KnowledgeBase knowledgeBase, Integer nQuestions) {
        IntStream.range(0, nQuestions).forEach( i -> knowledgeBase.addQuestion(new Question()));
    }
}
