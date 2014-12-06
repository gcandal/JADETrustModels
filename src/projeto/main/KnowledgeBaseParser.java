package projeto.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class KnowledgeBaseParser {
    public static void parseFile(KnowledgeBase knowledgeBase, Category category) {
        List<Question> questions = new ArrayList<>();
        String questionText, answersText[] = new String[4];
        Integer rightAnswer;
        File file = new File(category + ".txt");

        try {
            Scanner input = new Scanner(file);

            while (input.hasNext()) {
                //Question Nr
                input.nextLine();

                //Question
                questionText = input.nextLine();

                //Blank space
                input.nextLine();

                //Answers
                IntStream.range(0, 4).forEach(i ->
                    answersText[i] = input.nextLine().substring(3).trim()
                );

                //Right answer
                rightAnswer = rightAnswerToInt(input.nextLine());

                questions.add(new Question(questionText, answersText.clone(), rightAnswer, category));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        questions.stream().forEach(knowledgeBase::addQuestion);
    }

    private static Integer rightAnswerToInt(String line) {
        return ((int) line.charAt(0)) - ((int) 'A');
    }
}
