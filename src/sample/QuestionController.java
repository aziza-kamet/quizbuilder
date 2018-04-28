package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

/**
 * Created by Aziza on 5/11/2016.
 */
public class QuestionController implements Initializable{

    @FXML
    Text titleText;
    @FXML
    VBox variantsBox;
    @FXML
    Button checkButton;
    @FXML
    Text answerText;

    Question question;
    ArrayList<Question> questions = new ArrayList<Question>();
    ToggleGroup toggleGroup = new ToggleGroup();
    ArrayList<CheckBox> selectedChecks= new ArrayList<CheckBox>();
    int index;

    public QuestionController(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        questions = Main.quiz;
        Collections.shuffle(questions, new Random(System.nanoTime()));
        setQuestion(0);

    }

    public void setQuestion(int ind){

        question = questions.get(ind);
        titleText.setText(question.getQuestion());
        ArrayList<String> variants = question.getVariants();
        Collections.shuffle(variants, new Random(System.nanoTime()));
        ArrayList<String> answers = questions.get(ind).getAnswer();

        if(answers.size() > 1) {

            ArrayList<CheckBox> checks= new ArrayList<CheckBox>();

            for(String v: variants) {

                CheckBox checkBox = new CheckBox();
                checkBox.setWrapText(true);
                checkBox.setText(v);
                checkBox.setFont(Font.font(16));

                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {

                    if(newValue) {
                        selectedChecks.add(checkBox);
                    } else {
                        selectedChecks.remove(checkBox);
                    }

                });

                checks.add(checkBox);

            }

            variantsBox.getChildren().addAll(checks);

        } else {

            ArrayList<RadioButton> radios = new ArrayList<RadioButton>();

            for (String v: variants) {

                RadioButton radioButton = new RadioButton();
                radioButton.setWrapText(true);
                radioButton.setText(v);
                radioButton.setToggleGroup(toggleGroup);
                radioButton.setFont(Font.font(16));
                radios.add(radioButton);
            }

            variantsBox.getChildren().addAll(radios);

        }

    }

    public void onCheckButtonClicked(){


            if (checkButton.getText().equals("Check")) {

                if(question.getAnswer().size() > 1) {

                    ArrayList<String> answers = question.getAnswer();
                    String text = "";

                    for (CheckBox answer: selectedChecks) {
                        if(answers.contains(answer.getText())) {
                            answer.setTextFill(Color.LIGHTGREEN);
                        } else {
                            System.out.println(answer);
                            answer.setTextFill(Color.RED);
                            questions.add(question);
                            break;
                        }
                    }

                    if(index < questions.size()-1)
                        checkButton.setText("Next");
                    else {
                        checkButton.setDisable(true);
                        index = 0;
                    }

                    for (String answer: answers) {
                        text += answer + ", ";
                    }

                    answerText.setText(text.replaceAll(", $", ""));

                } else if(toggleGroup.getSelectedToggle() != null) {

                    String selectedAnswer = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

                    String answer = "";
                    try {
                        answer = question.getAnswer().get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (selectedAnswer.equals(answer)) {
                        ((RadioButton) toggleGroup.getSelectedToggle()).setTextFill(Color.LIGHTGREEN);
                    } else {
                        ((RadioButton) toggleGroup.getSelectedToggle()).setTextFill(Color.RED);
                        questions.add(question);
                    }
                    if(index < questions.size()-1)
                        checkButton.setText("Next");
                    else {
                        checkButton.setDisable(true);
                        index = 0;
                    }
                    answerText.setText(answer);

                }

            } else {

                selectedChecks = new ArrayList<CheckBox>();
                variantsBox.getChildren().clear();
                setQuestion(++index);
                answerText.setText("");
                checkButton.setText("Check");

            }
    }

}
