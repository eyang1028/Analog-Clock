import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.lang.Math;
import javafx.animation.AnimationTimer;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage; //import necessary classes

public class AnalogClock extends Application { //class called AnalogClock
	Calendar cal;
	SimpleDateFormat sdf;
	String currentTime;
	String hms[];
	double rad, hour, minute, second;
	Group root;
	Canvas canvas;
	GraphicsContext gc; //initialize some variables

	public AnalogClock() { //constructor
		sdf = new SimpleDateFormat("HH:mm:ss");
	}

	private void refreshTime() { //recalculate current time
		cal = Calendar.getInstance();
		currentTime = sdf.format(cal.getTime());
		hms = currentTime.split(":");
		hour = Integer.parseInt(hms[0]);
		minute = Integer.parseInt(hms[1]);
		second = Integer.parseInt(hms[2]);
	}

	private double radianCalc(double h) { //return angle of inputed hour; parameter is type double for type convenience purposes
		h = h / 12.0;
		double a = Math.PI / 2;
		double b = 2 * Math.PI;
		return a - (b * h);
	}

	private double hourHand() { //return angle of hour hand
		hour = hour + (minute / 60);
		double a = Math.PI / 2;
		double b = 2 * Math.PI;
		double c = (hour % 12) / 12.0;
		return a - (b * c);
	}

	private double minuteHand() { //return angle of minute hand
		minute = minute + (second / 60);
		double a = Math.PI / 2;
		double b = 2 * Math.PI;
		double c = minute / 60.0;
		return a - (b * c);
	}

	private double secondHand() { //return angle of second hand
		double a = Math.PI / 2;
		double b = 2 * Math.PI;
		double c = second / 60.0;
		return a - (b * c);
	}

	private double xCalc(int handLength, double radian) { //return x axis value
		return (double) handLength * Math.cos(radian);
	}

	private double yCalc(int handLength, double radian) { //return y axis value
		return (double) handLength * Math.sin(radian);
	}

	public void start(Stage primaryStage) { //set up GUI
		primaryStage.setTitle("Analog Clock");
		root = new Group();
		canvas = new Canvas(800, 800);
		gc = canvas.getGraphicsContext2D();
		drawClock(gc);
		root.getChildren().add(canvas);
		primaryStage.setScene(new Scene(root, Color.GREY));
		primaryStage.show();
		new AnimationTimer() { //start doing clock animation every second
			long lastSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
			public void handle(long now) {
				long thisSecond = TimeUnit.NANOSECONDS.toSeconds(now);
				if (thisSecond != lastSecond)
					drawClock(gc);
				lastSecond = thisSecond;
			}
		}.start();
	}

	public void drawClock(GraphicsContext gc) { //draw the clock
		gc.clearRect(0, 0, 800, 800);
		gc.setStroke(Color.YELLOW);
		gc.setLineWidth(2);
		gc.strokeOval(10, 10, 780, 780); //radius of clock is 390
		gc.setFill(Color.YELLOW);
		gc.fillOval(390, 390, 20, 20);
		gc.setLineWidth(5);
		for(int i = 1; i < 12; i++) { //draw the tick lines on clock
			if(i != 3 && i != 6 && i != 9) {
				rad = radianCalc(i);
				gc.strokeLine(400 + xCalc(350, rad), 400 - yCalc(350, rad), 400 + xCalc(375, rad), 400 - yCalc(375, rad));
			}
		}
		gc.setFont(new Font(50));
		gc.strokeText("12", 365, 70);
		gc.strokeText("3", 730, 415);
		gc.strokeText("6", 385, 765);
		gc.strokeText("9", 40, 415);
		refreshTime();
		gc.setLineWidth(9);
		gc.strokeLine(400, 400, 400 + xCalc(250, hourHand()), 400 - yCalc(250, hourHand())); //hour hand is 250
		gc.setLineWidth(6);
		gc.strokeLine(400, 400, 400 + xCalc(300, minuteHand()), 400 - yCalc(300, minuteHand())); //minute hand is 300
		gc.setLineWidth(3);
		gc.strokeLine(400, 400, 400 + xCalc(350, secondHand()), 400 - yCalc(350, secondHand())); //second hand is 350
	}

	public static void main(String args[]) { //main method (javafx does not require a main method)
		launch(args);
	}
}
