package projeto.main;

import java.util.Random;
import java.util.stream.IntStream;

public class Question {
    static private Integer next_id = 0;
    static private Random random = new Random();

	public Integer id;
	public String questionText;
	public String[] answersText = new String[4];
	public Integer rightAnswer;
	public Category category;
	
	public Question(String _questionText, String[] _answersText, Integer _rightAnswer, Category _category) {
        assert(_answersText.length == 4);
        assert(_rightAnswer >= 0 && _rightAnswer < 4);

		id = next_id++;
		questionText = _questionText;
		answersText = _answersText;
		rightAnswer = _rightAnswer;
        category = _category;
	}

    public Question() {
        id = next_id++;
        questionText = getRandomText();

        IntStream.range(0, 4)
                .forEach(i -> answersText[i] = getRandomText());

        rightAnswer = random.nextInt(4);
        category = Category.randomCategory();
    }

    public Question(Category _cateogry) {
        id = next_id++;
        questionText = getRandomText();

        IntStream.range(0, 4)
                .forEach(i -> answersText[i] = getRandomText());

        rightAnswer = random.nextInt(4);
        category = _cateogry;
    }

    private String getRandomText() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    public String toString() {
        return "[" + category + "] " + questionText + "? " + answersText[0] + " | " + answersText[1] + " | " + answersText[2] + " | " + answersText[0] + " -> " + rightAnswer;
    }
}
