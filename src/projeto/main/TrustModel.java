package projeto.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TrustModel {

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
	}

    protected List<String> sourceIds = new ArrayList<String>();
	protected List<Record> records = new ArrayList<Record>();

	public void addRecord(Integer round, Boolean correctAnswer, String sourceId, Category category) {
		records.add(new Record(round, correctAnswer, sourceId, category));
	}

    public void addSourceId(String sourceId){
    	sourceIds.add(sourceId);
    }
	
	public abstract String chooseSource(Category category, Integer round);
	
	public List<Record> getRecords() {
		return records;
	}
	
	protected List<Record> getRecordsFromCategory(Category category) {
        return records.stream().filter(r -> r.category.equals(category)).collect(Collectors.toList());
	}
}
