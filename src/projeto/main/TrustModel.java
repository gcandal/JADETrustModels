package projeto.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface TrustModel {
	public void addRecord(Boolean correctAnswer, String sourceId, Category category);

    public void addSourceId(String sourceId);
	
	public abstract String chooseSource(Category category);

    public ArrayList<Double> getTrust(String source, Category category);
}
