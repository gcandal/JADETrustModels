package projeto.main;

import java.util.ArrayList;
import java.util.List;

public abstract class TrustModel {

	public TrustModel(String nSources){
		this.nSources = Integer.parseInt(nSources);
	}

	protected int nSources;

	private class Record {
		public Integer round;
		public Boolean correctAnswer;
		public String sourceId;
		public Category category;
		
		public Record(Integer _round, Boolean _correctAnswer, String _sourceId, Category _category) {
			round = _round;
			correctAnswer = _correctAnswer;
			sourceId = _sourceId;
			category = _category;
		}
	};

	private ArrayList<Record> records;

	public void addRecord(Integer round, Boolean correctAnswer, String sourceId, Category category) {
		records.add(new Record(round, correctAnswer, sourceId, category));
	}
	
	public abstract String chooseSource(String category, Integer round);
	
	public ArrayList<Record> getRecords() {
		return records;
	}
	
	protected ArrayList<Record> getRecordsFromCategory(Category category) {
		return records;
	}
}
