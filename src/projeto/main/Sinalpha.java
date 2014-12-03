package projeto.main;

import javax.sound.midi.spi.SoundbankReader;
import javax.xml.transform.Source;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


public class Sinalpha extends TrustModel {

	private final int STEPS = 12;//number of steps to reach trustworthiness

	private double omega =  Math.PI/STEPS; //speed to reach trustwordiness (pi/12 = 12 steps)
	private double delta = 0.5; // normalize for range [0,1]
	private double alpha_min = 3*Math.PI/2;
	private double alpha_max = 5*Math.PI/2;
	private double lambda_positive = 1.0;
	private double lambda_negative = -1.5;

	private HashMap<String, ArrayList<Double>> funcS = new HashMap<>(); //Sinalpha trust function <sourceID, alpha>
	private HashMap<String, Category> specialists = new HashMap<>(); //<Source,_>
	private HashMap<String, Category> possible_specialists = new HashMap<>(); //<Source,_>
	private HashMap<String, Integer> steps_log = new HashMap<>(); //<Source,Steps>


	private void setTrust(boolean isCorrect, String source, Category category){

		//initialize maps
		if ( !funcS.containsKey(source) )
			funcS.put(source, new ArrayList<>());
		if ( !steps_log.containsKey(source) )
			steps_log.put(source,0);

		//calc of trustworthiness
		double lambda = (isCorrect) ? lambda_positive : lambda_negative;
		double alpha;
		if(funcS.get(source).size()!=0)
			alpha = funcS.get(source).get(funcS.get(source).size()-1) + lambda * omega;//value of last element;
		else
			alpha = alpha_min;
		if(alpha > alpha_min)
			if(alpha >= alpha_max)
				alpha = alpha_max;
			else
				alpha = alpha_min;
		double trustworthiness = delta * Math.sin(alpha) + delta;

		//add y(x) to func
		funcS.get(source).add(trustworthiness);

		if(isCorrect) {
			steps_log.put(source, steps_log.get(source) + 1);
			possible_specialists.put(source,category);
		}else{
			steps_log.put(source, 0);
			if(possible_specialists.containsKey(source))
				possible_specialists.remove(source);
		}

		//if trustworthy set as specialist
		if (steps_log.get(source) >= STEPS)
			specialists.put(source,category);
	}

	public void addRecord(Integer round, Boolean correctAnswer, String sourceId, Category category) {
		setTrust(correctAnswer,sourceId,category);
		super.addRecord(round,correctAnswer,sourceId,category);
	}

	@Override
	public String chooseSource(Category category, Integer round) {
		//specialist already detected
		for(Iterator i = specialists.entrySet().iterator(); i.hasNext(); ) {
			HashMap.Entry pairs = (HashMap.Entry)i.next();
			if((Category)pairs.getValue() == category){
				String source = (String) pairs.getKey();
				System.out.println("specialist->"+source);
				return source;
			}
			i.remove(); // avoids a ConcurrentModificationException
		}

		//possible specialist
		for(Iterator i = possible_specialists.entrySet().iterator(); i.hasNext(); ) {
			HashMap.Entry pairs = (HashMap.Entry)i.next();
			if((Category)pairs.getValue() == category){
				String source = (String) pairs.getKey();
				System.out.println("possible specialist->"+source);
				return source;
			}
			i.remove();
		}

		//no specialist => get random
		Random generator = new Random();
		int index = generator.nextInt(sourceIds.size()-1);
		String source = sourceIds.get(index);
		System.out.println("no specialist->"+source);
		return source;
	}

	@Override
	public void addSourceId(String sourceId) {
		// TODO Auto-generated method stub
		
	}

}
