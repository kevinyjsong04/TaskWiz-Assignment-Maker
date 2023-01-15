import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDateTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * ToDoList class opens an application where the user can input and finish tasks.
 * @author Kevin Song
 * @version 1.0
 */
public class ToDoList extends Application {
    /**list of tasks to be displayed. */
    private static ObservableList<String> tasks;
    /**list of task types. */
    private static ArrayList<String> taskType;
    /**list of task names. */
    private static ArrayList<String> taskName;
    /**list of lengths of tasks. */
    private static ArrayList<Integer> hours;
    /**list of finish times of each task in the list. */
    private static ArrayList<Date> finishTime;
    /**current Type of task. */
    private static String currentType;
    /**current Name of task. */
    private static String currentTask;
    /**current length of current task. */
    private static int currentHours;
    /**number of tasks remaining. */
    private static int numRemaining;
    /**number of tasks completed. */
    private static int numCompleted;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TaskWiz");
        HBox titleBar = new HBox(); //title-----------------
        titleBar.setPadding(new Insets(20, 20, 20, 20));
        titleBar.setAlignment(Pos.CENTER);
        Label title = new Label("TaskWiz Assignment Maker");
        Font largeFont = Font.font(25);
        title.setFont(largeFont);
        titleBar.getChildren().add(title);
        VBox showCompletedBar = new VBox(); //show tasks completed and # remaining------------
        showCompletedBar.setPadding(new Insets(20, 20, 20, 20));
        showCompletedBar.setSpacing(15);
        showCompletedBar.setAlignment(Pos.CENTER);
        Label remainingStr = new Label("Tasks Remaining:");
        Label remaining = new Label("" + numRemaining);
        remaining.setFont(largeFont);
        Label completedStr = new Label("Tasks Completed:");
        Label completed = new Label("" + numCompleted);
        completed.setFont(largeFont);
        showCompletedBar.getChildren().addAll(remainingStr, remaining, completedStr, completed);
        HBox taskInputBar = new HBox(); //input tasks/enqueue/dequeue tasks--------------------
        taskInputBar.setPadding(new Insets(20, 20, 20, 20));
        taskInputBar.setSpacing(15);
        taskInputBar.setAlignment(Pos.CENTER);
        ChoiceBox typeTask = new ChoiceBox();
        typeTask.getItems().addAll("Study", "Shop", "Cook", "Sleep");
        typeTask.setOnAction(e -> {
            Object typeObject = typeTask.getValue();
            if (typeObject != null) {
                currentType = typeObject.toString();
            }
        });
        TextField enterTask = new TextField("Enter task name. Press Enter to record task.");
        Button textPress = new Button("Save Task Name");
        textPress.setOnAction(e -> {
            CharSequence chars = enterTask.getCharacters();
            currentTask = String.valueOf(chars);
        });
        ChoiceBox numHours = new ChoiceBox();
        numHours.getItems().addAll("1", "2", "3", "4", "5");
        numHours.setOnAction(e -> {
            Object hourObject = numHours.getValue();
            if (hourObject != null) {
                currentHours = Integer.parseInt(hourObject.toString());
            }
        });
        Button enqueueButton = new Button("Enqueue");
        enqueueButton.setOnAction(e -> {
            if (enqueueVerifier(currentType, currentTask, currentHours)) {
                remaining.setText("" + numRemaining);
                typeTask.setValue(null);
                enterTask.clear();
                numHours.setValue(null);
                currentHours = 0;
            }
        });
        Button dequeueButton = new Button("Dequeue");
        dequeueButton.setOnAction(e -> {
            if (tasks.size() > 0) {
                tasks.remove(0, 1);
                numRemaining--;
                numCompleted++;
                remaining.setText("" + numRemaining);
                completed.setText("" + numCompleted);
            }
        });
        Button dequeueButtonAll = new Button("Dequeue All");
        dequeueButtonAll.setOnAction(e -> {
            if (tasks.size() > 0) {
                numRemaining = 0;
                remaining.setText("0");
                numCompleted += tasks.size();
                completed.setText("" + numCompleted);
                tasks.clear();
            }
        });
        taskInputBar.getChildren().addAll(typeTask, enterTask, textPress, numHours,
            enqueueButton, dequeueButton, dequeueButtonAll);
        VBox fancyBar = new VBox(); //for extra credit-------------------------------------
        fancyBar.setPadding(new Insets(20, 20, 20, 20));
        fancyBar.setSpacing(15);
        fancyBar.setAlignment(Pos.CENTER);
        Label colorTitle = new Label("Set Title Background Color With Options Below:");
        ToggleGroup colorGroup = new ToggleGroup();
        RadioButton normalColor = new RadioButton("Standard Color");
        BackgroundFill normal = new BackgroundFill(Color.ALICEBLUE, new CornerRadii(1), null);
        normalColor.setToggleGroup(colorGroup);
        normalColor.setSelected(true);
        normalColor.setOnAction(e -> {
            normalColor.setSelected(true);
            titleBar.setBackground(new Background(normal));
        });
        RadioButton redColor = new RadioButton("Red Color");
        BackgroundFill red = new BackgroundFill(Color.ORANGERED, new CornerRadii(1), null);
        redColor.setToggleGroup(colorGroup);
        redColor.setOnAction(e -> {
            redColor.setSelected(true);
            titleBar.setBackground(new Background(red));
        });
        RadioButton greenColor = new RadioButton("Green Color");
        BackgroundFill green = new BackgroundFill(Color.PALEGREEN, new CornerRadii(1), null);
        greenColor.setToggleGroup(colorGroup);
        greenColor.setOnAction(e -> {
            greenColor.setSelected(true);
            titleBar.setBackground(new Background(green));
        });
        Button resetText = new Button("Clear Task TextBox");
        resetText.setOnAction(e -> {
            enterTask.clear();
        });
        Button resetEverything = new Button();
        resetEverything.setText("Reset Everything");
        resetEverything.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                remaining.setText("0");
                completed.setText("0");
                enterTask.clear();
                typeTask.setValue(null);
                numHours.setValue(null);
                numRemaining = 0;
                numCompleted = 0;
                currentHours = 0;
                currentType = "";
                currentTask = "";
                tasks.clear();
                taskType.clear();
                taskName.clear();
                hours.clear();
                finishTime.clear();
            }
        });
        fancyBar.getChildren().addAll(colorTitle, normalColor, redColor,
            greenColor, resetText, resetEverything);
        VBox showList = new VBox(); //shows list of current tasks-----------
        showList.setAlignment(Pos.CENTER);
        ListView<String> assignments = new ListView<String>(tasks);
        showList.getChildren().add(assignments);
        BorderPane root = new BorderPane(); //----------------------
        root.setTop(titleBar);
        root.setBottom(taskInputBar);
        root.setRight(showCompletedBar);
        root.setLeft(fancyBar);
        root.setCenter(showList);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    /**
     * Verifies that the task can be put in the list.
     * @param inputtaskType type of task
     * @param inputtaskName name of task
     * @param inputHours length of task in hours
     * @return boolean of whether task can be added
     */
    private static boolean enqueueVerifier(String inputtaskType, String inputtaskName, int inputHours) {
        Alert a = new Alert(AlertType.ERROR);
        if (inputtaskType == null || inputtaskName == null || inputtaskName.equals("")
            || inputtaskName.equals("Enter the name of the task") || inputHours == 0) {
            a.setContentText("At least one of the task Type, task Text, or number of hours is blank.");
            a.show();
            return false;
        }
        int index = findIndex(inputtaskName);
        if (!(index < 0)) {
            if (taskType.get(index).equals(inputtaskType)) {
                if (hours.get(index).equals(inputHours)) {
                    a.setContentText("A task with the same name, type, and length was already added. Try Again.");
                    a.show();
                    return false;
                } else {
                    DateFormat dtf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                    hours.set(index, hours.get(index) + inputHours);
                    Date switchDate = finishTime.get(index);
                    switchDate.setHours(switchDate.getHours() + inputHours);
                    finishTime.set(index, switchDate);
                    tasks.set(index, "Type: " + inputtaskType + " Name: "
                        + inputtaskName + " Finish Time: " + dtf.format(finishTime.get(index)));
                    return true;
                }
            } else {
                a.setContentText("A task with the same name was already added. Try Again.");
                a.show();
                return false;
            }
        } else {
            taskType.add(inputtaskType);
            taskName.add(inputtaskName);
            hours.add(inputHours);
            DateFormat dtf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            LocalDateTime currTime = LocalDateTime.now();
            Date newDate = new Date(currTime.getYear() - 1900, currTime.getMonthValue() - 1,
                currTime.getDayOfMonth() - 1, currTime.getHour() + inputHours,
                currTime.getMinute(), currTime.getSecond());
            finishTime.add(newDate);
            tasks.add("Type: " + inputtaskType + " Name: " + inputtaskName + " Finish Time: " + dtf.format(newDate));
            numRemaining++;
        }
        return true;
    }
    /**
     * Helper method to find index of matching string taskName.
     * @param targetVal targetString to find a match
     * @return index of match, -1 if not found
     */
    private static int findIndex(String targetVal) {
        for (int i = 0; i < taskName.size(); i++) {
            if (taskName.get(i).equals(targetVal)) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Main method to run program.
     * @param args standard String[] argument
     */
    public static void main(String[] args) {
        tasks = FXCollections.observableArrayList();
        taskType = new ArrayList<String>();
        taskName = new ArrayList<String>();
        hours = new ArrayList<Integer>();
        finishTime = new ArrayList<Date>();
        currentType = null;
        currentTask = null;
        currentHours = 0;
        numRemaining = 0;
        numCompleted = 0;
        launch(args);
    }
}