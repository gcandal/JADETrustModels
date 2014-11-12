package projeto.main;

public class Question {
	public Integer id;
	public String questionText;
	public String[] answersText;
	public Integer rightAnswer;
	public Category category;
	
	public Question(Integer _id, String _questionText, String[] _answersText, Integer _rightAnswer) {
		id = _id;
		questionText = _questionText;
		
		assert(_answersText.length == 4);
		answersText = _answersText;
		
		rightAnswer = _rightAnswer;
	}
}
