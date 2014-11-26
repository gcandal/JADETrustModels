package projeto.main;

import java.util.*;
import java.util.stream.IntStream;

public class KnowledgeBase {
    private Random random = new Random();
    private static KnowledgeBase instance = null;

    private Map<Category, List<Question>> questions = new HashMap<>();
    private Vector<Question> unsorted_questions = new Vector<>();

    public void addQuestion(Question question) {
        if( questions.containsKey(question.category) )
            questions.get(question.category).add(question);
        else
            questions.put(question.category, new ArrayList<>());

        unsorted_questions.add(question);
    }

    public List<Question> getQuestionsFromCategory(Category category) {
        return questions.get(category);
    }

    public List<Question> getNRandomQuestions(Integer n) {
        List<Question> random_questions = new ArrayList<>();

        IntStream.range(0, n)
                .forEach(x -> random_questions.add(unsorted_questions.get(random.nextInt(unsorted_questions.size()))));

        return random_questions;
    }

    private KnowledgeBase() {
    }

    public static KnowledgeBase getInstance() {
        if(instance == null) {
            instance = new KnowledgeBase();
        }
        return instance;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        unsorted_questions.forEach( question -> sb.append(question.toString()).append('\n') );

        return sb.toString();
    }

    public void fillRandom(Integer nQuestions) {
        IntStream.range(0, nQuestions).forEach( i -> addQuestion(new Question()));
    }
}
