package projeto.main;

import javax.sound.midi.spi.SoundbankReader;
import javax.xml.transform.Source;

import java.lang.reflect.Array;
import java.util.*;


public class Sinalpha extends TrustModel {

	private final int STEPS = 12;//number of steps to reach trustworthiness
	private final double omega =  Math.PI/STEPS; //speed to reach trustwordiness (pi/12 = 12 steps)
	private final double delta = 0.5; // normalize for range [0,1]
	private final double alpha_min = 3*Math.PI/2;
	private final double alpha_max = 5*Math.PI/2;
	private final double lambda_positive = 1.0;
	private final double lambda_negative = -1.5;

	private class SourceTrust {
		public Double alpha;//x
		public Double trust;//y
	}

	public Map<Category, Map<String, ArrayList<SourceTrust>>> categoryTrust = new HashMap<>(Category.values().length);

	@Override
	public void addSourceId(String sourceId){
		for (Category category : Category.values()) {
			categoryTrust.putIfAbsent(category, new HashMap<>());
			categoryTrust.get(category).put(sourceId, new ArrayList<>());
		}
		super.addSourceId(sourceId);
	}

	public ArrayList<Double> getTrust(String source, Category category){
		ArrayList<Double> func = new ArrayList<>();
		for(Iterator i = categoryTrust.get(category).get(source).iterator(); i.hasNext(); )
			func.add(((SourceTrust)i.next()).trust);
		return func;
	}

	public void addRecord(Integer round, Boolean isCorrect, String source, Category category) {
		//calc of trustworthiness
		SourceTrust sourceTrust = new SourceTrust();
		double lambda = (isCorrect) ? lambda_positive : lambda_negative;
		//double alpha;
		int size = categoryTrust.get(category).get(source).size();
		if(size > 0){
			sourceTrust.alpha = categoryTrust.get(category).get(source).get(size-1).alpha + lambda * omega;
		} else {
			sourceTrust.alpha = alpha_min;
		}
		if(sourceTrust.alpha >= alpha_min) {
			if (sourceTrust.alpha > alpha_max)
				sourceTrust.alpha = alpha_max;
		} else {
			sourceTrust.alpha = alpha_min;
		}

		double sin = Math.sin(sourceTrust.alpha);

		sourceTrust.trust = delta * sin + delta;

		//add alpha and y(x) to func
		categoryTrust.get(category).get(source).add(sourceTrust);

		super.addRecord(round,isCorrect,source,category);
	}

	@Override
	public String chooseSource(Category category, Integer round) {

		String betterSource = "";
		Double betterTrust = 0.0;
		for(Iterator i = categoryTrust.get(category).entrySet().iterator(); i.hasNext(); ) {
			HashMap.Entry pairs = (HashMap.Entry)i.next();
			String source = (String) pairs.getKey();
			ArrayList<SourceTrust> trustFunc = ((ArrayList<SourceTrust>)pairs.getValue());
			Double trust = (trustFunc.size()>0) ? trustFunc.get(trustFunc.size()-1).trust : 0.0;
			if(trust > betterTrust){
				betterSource = source;
				betterTrust = trust;
			}
		}

		if(betterTrust != 0){
			return betterSource;
		}else{//no specialist => get random
			Random generator = new Random();
			int index = generator.nextInt(sourceIds.size()-1);
			String source = sourceIds.get(index);
			return source;
		}
	}


}
