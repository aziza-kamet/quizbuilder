package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    @FXML
    Pane startPane;
    @FXML
    Button uploadFileButton;
    @FXML
    Button startQuizButton;
    @FXML
    RadioButton oneLineAZ;
    @FXML
    RadioButton multiLineCircles;
    ToggleGroup toggleGroup;

    File file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleGroup = new ToggleGroup();
        oneLineAZ.setToggleGroup(toggleGroup);
        multiLineCircles.setToggleGroup(toggleGroup);
    }

    public void onUploadButtonClicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text", "*.txt"));
        fileChooser.setInitialDirectory(new File("/Users/mac/Downloads"));
        File file = fileChooser.showOpenDialog(Main.stage);
        if (file != null) {
            this.file = file;
//            System.out.println("File is not empty");
            Main.quiz = convertFromTxt(file);
        }
    }

    public void onStartButtonClicked() throws Exception {

        if (this.file != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("quiz.fxml"));
            fxmlLoader.setController(new QuestionController());
            Parent localRoot = (Parent) fxmlLoader.load();
            Scene scene = new Scene(localRoot, 600, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
    }

    public ArrayList<Question> convertFromTxt(File file) {

        ArrayList<Question> questions = new ArrayList<Question>();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));


            if (oneLineAZ.isSelected()) {
                oneLineAZConverting(reader, false, questions);
            } else if (multiLineCircles.isSelected()) {
                multiLineCirclesConverting(reader, questions);
            }

        } catch (Exception ex) {
            System.out.println("Exception");
            ex.printStackTrace();
        }

        if (questions.isEmpty())
            System.out.println("EMPTY");

        return questions;
    }


    public void oneLineAZConverting(BufferedReader reader, boolean end, ArrayList questions) {

        try {

            String text = "";
            ArrayList<String> answer = new ArrayList<String>();
            ArrayList<String> variants = new ArrayList<String>();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.length() > 1) {

                    if (line.split("\\s+")[0].matches("\\d+" + Pattern.quote("."))) {

                        text = line.split("\\.", 2)[1];
                        //                    System.out.println(text);

                    } else if (line.substring(0, 2).equals("a)")) {

                        String[] var = line.substring(2, line.length()).split("\\s+" + "[a-z]" + Pattern.quote(")"));
                        for (String s : var) {
                            if (s.contains("+")) {
                                s = s.replace("+", "");
                                answer.add(s);
                                //                            System.out.println(answer);
                            }
                            //                        System.out.println(s);
                            variants.add(s.trim());
                            end = true;
                        }
                    }

                    if (end) {
                        questions.add(new Question(text.trim(), variants, answer));
                        text = "";
                        answer = new ArrayList<String>();
                        variants = new ArrayList<String>();
                        end = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void multiLineCirclesConverting(BufferedReader reader, ArrayList questions) {

        try {

            String text = "";
            ArrayList<String> answer = new ArrayList<String>();
            ArrayList<String> variants = new ArrayList<String>();
            String line;

            while ((line = reader.readLine())!= null) {

                if (line.length() > 1) {

                    if (line.split("\\s+")[0].matches("\\d+" + Pattern.quote("."))) {
                        if (!variants.isEmpty()) {
                            questions.add(new Question(text.trim(), variants, answer));
                            answer = new ArrayList<String>();
                            variants = new ArrayList<String>();
                        }

                        text = line.split("\\.", 2)[1];
                        //                    System.out.println(text);

                    } else if (line.substring(0, 1).equals("#")) {

                        variants.add(line.replace("#", "").trim());

                    } else if (line.substring(0, 1).equals("•")) {

                        String currentAnswer = line.replace("•", "").trim();
                        answer.add(currentAnswer);
                        variants.add(currentAnswer);
                    }
                }
            }

            if (!variants.isEmpty()) {
                questions.add(new Question(text.trim(), variants, answer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
