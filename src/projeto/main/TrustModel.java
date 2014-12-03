package projeto.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TrustModel {

	private class Record {
		public Boolean correctAnswer;
		public String sourceId;
		public Category category;
		
		public Record(Boolean _correctAnswer, String _sourceId, Category _category) {
			correctAnswer = _correctAnswer;
			sourceId = _sourceId;
			category = _category;
		}
	}

    protected List<String> sourceIds = new ArrayList<>();
	protected List<Record> records = new ArrayList<>();

	public void addRecord(Boolean correctAnswer, String sourceId, Category category) {
		records.add(new Record(correctAnswer, sourceId, category));
	}

    public void addSourceId(String sourceId){
    	sourceIds.add(sourceId);
    }
	
	public abstract String chooseSource(Category category);
	
	public List<Record> getRecords() {
		return records;
	}
	
	protected List<Record> getRecordsFromCategory(Category category) {
        return records.stream().filter(r -> r.category.equals(category)).collect(Collectors.toList());
	}
}
