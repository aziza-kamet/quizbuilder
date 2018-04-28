package sample;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Aziza on 5/11/2016.
 */
public class Question {

    private String question;
    private ArrayList<String> variants;
    private ArrayList<String> answer;

    public Question(String question, ArrayList<String> variants, ArrayList<String> answer) {
        this.question = question;
        this.variants = variants;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<String> variants) {
        this.variants = variants;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }
}
