package projeto.main;

import java.util.ArrayList;

public interface TrustModel {
	public void addRecord(Boolean correctAnswer, String sourceId, Category category);

    public void addSourceId(String sourceId);
	
	public  String chooseSource(Category category);

    public ArrayList<Double> getTrust(String source, Category category);
}
