package finalProject;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DigitalClock extends Application{
	public BorderPane containerPane = new BorderPane();
	private Timeline secondsTimeline;
	public LocalDateTime now;
	public VBox period;
	public ArrayList<Polygon[]> allDigits = new ArrayList<>();
	public ArrayList<Polygon[]> allDigitsDate = new ArrayList<>();
	public Scene scene;
	public boolean formatTime = false;
	public int combinations[][] = {
			new int[] { 1, 1, 1, 1, 1, 1, 0 },
			new int[] { 0, 0, 0, 0, 1, 1, 0 },
			new int[] { 1, 0, 1, 1, 0, 1, 1 },
			new int[] { 1, 0, 0, 1, 1, 1, 1 },
			new int[] { 0, 1, 0, 0, 1, 1, 1 },
			new int[] { 1, 1, 0, 1, 1, 0, 1 },
			new int[] { 0, 1, 1, 1, 1, 0, 1 },
			new int[] { 1, 0, 0, 0, 1, 1, 0 },
			new int[] { 1, 1, 1, 1, 1, 1, 1 },
			new int[] { 1, 1, 0, 1, 1, 1, 1 }
	};
        private int alarmHours;
        private int alarmMins;
        private   HBox blank;
        private HBox blank2;
        private Button snooze;
        private Button alarm;
        private Button off = new Button("Turn Off");
        private HBox topBox = new HBox(10);
        private CheckBox format;
        private Stage alarmStage = new Stage();
        private BorderPane alarmPane = new BorderPane();
        private Scene alarmScene;
        private int counter = 0;
        private Timeline timer;
   
        private File f = new File("src/finalproject/poop.mp3");
   
         Media media = new Media(f.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        
	@Override
	public void start(Stage primaryStage)
	{
		scene = new Scene(containerPane,1000,350);
		setUpMainPain();
		startTimeLine();
		setupBottom();
                
		primaryStage.setTitle("Digital Clock");
		primaryStage.setMinWidth(1000);
		primaryStage.setMinHeight(350);
		primaryStage.setScene(scene);
		primaryStage.show();
                
                alarmScene = new Scene(alarmPane, 350, 250);
                setupAlarmPane();
                alarmStage.setTitle("Set Alarm");
                alarmStage.setScene(alarmScene);
	}

	private void startTimeLine() {
		secondsTimeline = new Timeline(new KeyFrame(
				Duration.seconds(1), e -> {
					LocalDateTime digitTime = LocalDateTime.now();
					int seconds = digitTime.getSecond();
					int minutes = digitTime.getMinute();
					int hours = digitTime.getHour();
					int date = digitTime.getDayOfMonth();
					int month = digitTime.getMonthValue();
					String year = digitTime.getYear() + "";
					updatePeriod(hours);
					if(formatTime == false && hours > 12) {
						hours -= 12;
					}
					updateDigit(allDigits.get(0),hours / 10);
					updateDigit(allDigits.get(1),hours % 10);
					updateDigit(allDigits.get(2),minutes / 10);
					updateDigit(allDigits.get(3),minutes % 10);
					updateDigit(allDigits.get(4),seconds / 10);
					updateDigit(allDigits.get(5),seconds % 10);

					updateDigit(allDigitsDate.get(0),month / 10);
					updateDigit(allDigitsDate.get(1),month % 10);
					updateDigit(allDigitsDate.get(2),date / 10);
					updateDigit(allDigitsDate.get(3),date % 10);
					String[] yearSplit = year.split("");
					updateDigit(allDigitsDate.get(4),Integer.valueOf(yearSplit[0]));
					updateDigit(allDigitsDate.get(5),Integer.valueOf(yearSplit[1]));
					updateDigit(allDigitsDate.get(6),Integer.valueOf(yearSplit[2]));
					updateDigit(allDigitsDate.get(7),Integer.valueOf(yearSplit[3]));
                                        checkAlarm();
				}));
	}

	private void updateDigit(Polygon[] polygon, int num) {
		int value = num;
		for(int i = 0; i < combinations[value].length; i++)
		{
			if(combinations[value][i] == 1)
			{
				polygon[i].setStyle("-fx-fill: #010083;");
			}
			else if (combinations[value][i] == 0)
			{
				polygon[i].setStyle("-fx-fill: #EEE6E7;");
			}
		}
	}

	public void setUpMainPain() {
		BorderPane borderPane = new BorderPane();
                blank = new HBox();
                blank2 = new HBox();
		format = new CheckBox("24-Hour Format");
                alarm = new Button("Set Alarm");
                snooze = new Button("Snooze");
                
                snooze.setOnAction(e -> {
                    
                   alarmMins--;
                      if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
                       {
                            mediaPlayer.stop();
                       }
                    addOffButton();
                    snoozeTimer();
                });
                
                off.setOnAction(e ->{
                   removeOff(); 
                });
                snooze.setPrefWidth(400);
                
		format.setOnAction(e ->{
			formatTime = !formatTime;
			LocalDateTime digitTime = LocalDateTime.now();
			int hours = digitTime.getHour();
			updatePeriod(hours);
			if(formatTime == false && hours > 12) {
				hours -= 12;
			} 
			updateDigit(allDigits.get(0),hours / 10);
			updateDigit(allDigits.get(1),hours % 10);

		});
                HBox.setHgrow(format, Priority.ALWAYS);
                HBox.setHgrow(blank, Priority.ALWAYS);
                HBox.setHgrow(snooze, Priority.ALWAYS);
                HBox.setHgrow(blank2, Priority.ALWAYS);
                HBox.setHgrow(alarm, Priority.ALWAYS);
		topBox.getChildren().addAll(format, blank, snooze, blank2, alarm);
                alarm.setOnAction( e -> {                             
                        alarmStage.show();
                        
                });
                
                
		topBox.setPadding(new Insets(5));

		VBox mainVbox = new VBox();
		HBox middleHbox = new HBox(8);
		period = new VBox(15);
		Text am = new Text("AM");
		am.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		am.setStyle("-fx-fill: #EEE6E7;");
		Text pm = new Text("PM");
		pm.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
		pm.setStyle("-fx-fill: #EEE6E7;");
		period.getChildren().addAll(am, pm);
		period.setPadding(new Insets(20));

		HBox digitsHbox = new HBox(60);
		int check = 0;
		for(int i = 0; i < 8; i++) {
			Pane oneDigit = new Pane();
			if(check == 2) {
				oneDigit = createCicle();
				check = 0;
			}else {
				Polygon[] polygon = createDigit();
				allDigits.add(polygon);
				oneDigit.getChildren().addAll(polygon);
				check++;
			}
			digitsHbox.getChildren().addAll(oneDigit);
		}
		digitsHbox.setAlignment(Pos.CENTER);

		mainVbox.scaleXProperty().bind(containerPane.widthProperty().divide(600));
		mainVbox.scaleYProperty().bind(containerPane.heightProperty().divide(200));

		middleHbox.getChildren().addAll(period,digitsHbox);
		middleHbox.setPadding(new Insets(10, 70 , 5 , 5)); //top right bottom left


		HBox daysHbox = new HBox(60);
		check = 0;
		for(int i = 0; i < 10; i++) {
			Pane oneDigit = new Pane();
			if(check == 2 && i < 6) {
				oneDigit = createDash();
				check = 0;
			}else {
				Polygon[] polygon = createDigit();
				allDigitsDate.add(polygon);
				oneDigit.getChildren().addAll(polygon);
				check++;
			}
			daysHbox.getChildren().addAll(oneDigit);
		}
		daysHbox.setPadding(new Insets(20,0,0,50));
		daysHbox.scaleXProperty().bind(containerPane.widthProperty().divide(3500));
		daysHbox.scaleYProperty().bind(containerPane.heightProperty().divide(1400));

		mainVbox.getChildren().addAll(middleHbox,daysHbox);
		borderPane.getChildren().addAll(mainVbox);

		containerPane.setTop(topBox);
                containerPane.setCenter(borderPane);


	}
        

	private void updatePeriod(int hours) {
		Text[] periodArr = new Text[2];
		int n = 0;
		for (Node node : period.getChildren()){
			Text txt = (Text) node;
			periodArr[n] = txt;
			n++;
		}
		if(!formatTime) {
			if(hours >= 12) {
				periodArr[1].setStyle("-fx-fill: #EEE6E7;");
				periodArr[1].setStyle("-fx-fill: #010083;");
			}else {
				periodArr[0].setStyle("-fx-fill: #EEE6E7;");
				periodArr[0].setStyle("-fx-fill: #010083;");
			}
		}else {
			periodArr[0].setStyle("-fx-fill: #EEE6E7;");
			periodArr[1].setStyle("-fx-fill: #EEE6E7;");
		}
	}
        
     

	public void setupBottom()
	{

		HBox hbox = new HBox(10);

		for (int i = 0; i < 60; i++)
		{
			Circle circle = new Circle(3);
			circle.setStyle("-fx-fill: #EEE6E7;");
			hbox.getChildren().add(circle);
		}
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(-50,0,0,0));

		secondsTimeline.getKeyFrames().add(new KeyFrame(
				Duration.seconds(1), e ->{
					LocalTime current = LocalTime.now();

					int seconds = current.getSecond();
					if (seconds == 0)
					{
						clearCircle(hbox);
					}
					int counter = 0;

					List<Node> list = hbox.getChildren();
					Iterator<Node> iterator = list.iterator();

					while (counter <= seconds)
					{
						Circle circle = (Circle)iterator. next();
						circle.setStyle("-fx-fill: #010083;");
						counter++;
					}
				}));
		hbox.scaleXProperty().bind(containerPane.widthProperty().divide(970));
		hbox.scaleYProperty().bind(containerPane.heightProperty().divide(400));
		containerPane.setBottom(hbox);
		secondsTimeline.setCycleCount(Timeline.INDEFINITE);
		secondsTimeline.play();
	}

	public void clearCircle(HBox hbox)
	{
		List<Node> list = hbox.getChildren();
		Iterator<Node> iterator = list.iterator();

		while(iterator.hasNext())
		{
			Circle circle = (Circle) iterator.next();
			circle.setStyle("-fx-fill: #EEE6E7;");
		}
	}
	public Pane createDash() {
		Polygon dash = new Polygon(12, 49, 42, 49, 52, 54, 42, 59, 12, 59, 2, 54);
		dash.setStyle("-fx-fill: #010083;");

		VBox vbox = new VBox(0);
		vbox.setPadding(new Insets(60,0,0,0));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(dash);

		Pane pane = new Pane();
		pane.getChildren().addAll(vbox);
		return pane;
	}

	public Pane createCicle() {
		Circle cir1 = new Circle(13);
		Circle cir2 = new Circle(13);
		cir1.setStyle("-fx-fill: #010083;");
		cir2.setStyle("-fx-fill: #010083;");

		VBox vbox = new VBox(20);
		vbox.setPadding(new Insets(15));
		vbox.getChildren().addAll(cir1,cir2);

		Pane pane = new Pane();
		pane.getChildren().addAll(vbox);
		return pane;
	}

	public Polygon[] createDigit() {
	
		//		----0----
		//		1	    5
		//		----6----
		//		2	    4
		//		----3----	
		double[][] size = {	{2,  0, 	52, 0,	 	42, 10, 	12, 10},  				//0
				{0,  2, 	10, 12,		10, 47, 	0,  52},							//1	
				{0,  56, 	10, 61, 	10, 96, 	0,  106},							//2	
				{12, 98, 	42, 98, 	52, 108, 	2,  108},							//3	
				{44, 61, 	54, 56, 	54, 106, 	44, 96},							//4	
				{44, 12, 	54, 2, 		54, 52, 	44, 47},							//5	
				{12, 49, 	42, 49, 	52, 54, 	42, 59, 	12, 59, 	2, 54}};	//6	


		Polygon[] polygons = new Polygon[]{
				new Polygon(size[0]),
				new Polygon(size[1]),
				new Polygon(size[2]),
				new Polygon(size[3]),
				new Polygon(size[4]),
				new Polygon(size[5]),
				new Polygon(size[6])};

		for(int i = 0; i < polygons.length; i++) {
			polygons[i].setStyle("-fx-fill: #EEE6E7;");
		}

		
		return polygons;
	}
        public void setupAlarmPane()
        {
              VBox middle = new VBox(22);


              HBox timeBox = new HBox (20);

              Text time = new Text("Time:");
              time.setFont(Font.font(17));

              ComboBox<String> hours = new ComboBox<>(); 

              for (int i = 0; i <= 23; i++)
              {
                  String hour = String.format("%02d", i);
                  hours.getItems().add(hour);
              }
              hours.setValue(String.format("%02d", now.now().getHour()));

              ComboBox<String> minutes = new ComboBox<>(); 

              for (int i = 0; i <= 59; i++)
              {
                  String minute = String.format("%02d", i);
                  minutes.getItems().add(minute);
              }
              minutes.setValue(String.format("%02d", now.now().getMinute()));

              timeBox.getChildren().addAll(time, hours, minutes);
              timeBox.setAlignment(Pos.CENTER);
              middle.setMargin(timeBox, new Insets(0, 0, 0, -20));

              HBox buttons = new HBox (30);

              Button clear = new Button("Clear");
              Button submit = new Button("Submit");
              clear.setOnAction(e ->{
                    hours.setValue(String.format("%02d", now.now().getHour()));
                    minutes.setValue(String.format("%02d", now.now().getMinute()));
              });
              submit.setOnAction(e -> {

                  storeAlarm(hours, minutes);
                 alarmStage.close();

              });
              buttons.getChildren().addAll(clear, submit);
              buttons.setAlignment(Pos.CENTER);
              middle.setMargin(buttons, new Insets(20, 0, 0, 65));

              middle.getChildren().addAll(timeBox, buttons);
              middle.setAlignment(Pos.CENTER);

               alarmPane.setCenter(middle);
        }
        
        public void storeAlarm(ComboBox hour, ComboBox min)
        {
            alarmHours = Integer.parseInt(hour.getValue() + "");
            alarmMins = Integer.parseInt(min.getValue() + "");
           
        }
        
        public void checkAlarm()
        {
            int hour = now.now().getHour();
            int mins = now.now().getMinute();
            if (hour == alarmHours)
            {
                if (mins == alarmMins)
                {
                    mediaPlayer.play();
                    
                }
            }
        }
        private void snoozeTimer()
        {
            
             timer = new Timeline(new KeyFrame(
            Duration.seconds(1), e -> {
                counter++;
                if (counter == 5)
                {
                   if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED )
                   {
                       mediaPlayer.play();
                       counter = 0;
                   }
                }
            }));
            timer.setCycleCount(5);
            timer.play();
        }

        private void removeOff()
        {
            mediaPlayer.stop();
            
            alarmHours = 99;
            alarmMins = 99;
            timer.stop();
            topBox.getChildren().clear();
            topBox.getChildren().addAll(format, blank, snooze, blank2, alarm);
        }
        private void addOffButton()
        {
            topBox.getChildren().clear();
            topBox.getChildren().addAll(format, blank, snooze, off, blank2, alarm);
           
          
        }
        public static void main(String[] args)
	{
		launch(args);
	}
}




