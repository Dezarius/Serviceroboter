package serviceroboter;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Motoren {
	
	private RegulatedMotor motor_links;
	private RegulatedMotor motor_rechts;
	private RegulatedMotor motor_sensor;
	private Sensoren sensoren;
	
	private boolean forward = false;
	private boolean leftTurn = false;
	private boolean rightTurn = false;
	private boolean leftTurnFindLine = false;
	private boolean rightTurnFindLine = false;
	
	private boolean line = false;
	
	public Motoren(Sensoren sensoren) {
		this.motor_links = new EV3LargeRegulatedMotor(MotorPort.A);
		this.motor_rechts = new EV3LargeRegulatedMotor(MotorPort.D);
		this.motor_sensor = new EV3MediumRegulatedMotor(MotorPort.B);
		this.sensoren = sensoren;
	}
	
	public void forward() {
		line = false;
		rightTurn = false;
		leftTurn = false;
		rightTurnFindLine = false;
		leftTurnFindLine = false;
		if (!forward) {
			motor_links.setSpeed(300);
			motor_rechts.setSpeed(300);
			motor_links.forward();
			motor_rechts.forward();
			forward = true;
		}
	}
	
	public void stop() {
		motor_links.stop(true);
		motor_rechts.stop();
	}
	
	public void diviation() {
		float values[] = sensoren.getValues();
		//Abweichung links
		if(values[0] >= 3) {
			motor_links.setSpeed(300);
			motor_rechts.setSpeed(270);
			motor_links.forward();
			motor_rechts.forward();
		}
		//Abweichung rechts
		else if(values[0] <= -3) {
			motor_links.setSpeed(270);
			motor_rechts.setSpeed(300);
			motor_links.forward();
			motor_rechts.forward();
		}
		else if(values[0] <= 0.5f && values[0] >= -0.5f){
			motor_links.setSpeed(300);
			motor_rechts.setSpeed(300);
			motor_links.forward();
			motor_rechts.forward();
		}
	}
	
	public void findLine(){
		forward = false;
		float values[] = sensoren.getValues();
		LCD.drawString(String.valueOf(values[0]), 1, 3);
		if (values[0] <= 40 && line) {
			if (!leftTurnFindLine) {
				motor_links.stop(true);
				motor_rechts.stop();
				//Delay.msDelay(1000);
				motor_links.setSpeed(100);		//Geschwindigkeit muss noch eingestellt werden!
				motor_rechts.setSpeed(100);
				motor_links.backward();
				motor_rechts.forward();
				leftTurnFindLine = true;
			}
		}
		else if (values[0] > 40 && line) {
			stop();
		}
		if (values[0] >= -40 && !line) {
			if (!rightTurnFindLine) {
				motor_links.stop(true);
				motor_rechts.stop();
				//Delay.msDelay(1000);
				motor_links.setSpeed(100);		//Geschwindigkeit muss noch eingestellt werden!
				motor_rechts.setSpeed(100);
				motor_links.forward();
				motor_rechts.backward();
				rightTurnFindLine = true;
			}
		}
		else if (values[0] < -40 && !line) {
			line = true;
			stop();
		}
		
	}
	
	public void turnLeft() {
		float values[] = sensoren.getValues();
		if (!leftTurn) {
			motor_links.stop(true);
			motor_rechts.stop();
			
			motor_links.setSpeed(75);
			motor_rechts.setSpeed(170);
			motor_links.forward();
			motor_rechts.forward();
			leftTurn = true;
		}
		else if(values[0] >= 90) {
			motor_links.stop(true);
			motor_rechts.stop();
			
			Delay.msDelay(200);
			sensoren.resetGyro();
			Delay.msDelay(200);
			line = false;
			rightTurn = false;
			rightTurnFindLine = false;
			leftTurnFindLine = false;
		}
	}
	
	public void turnRight() {
		float values[] = sensoren.getValues();
		if (!rightTurn) {
			motor_links.stop(true);
			motor_rechts.stop();
			
			motor_links.setSpeed(170);
			motor_rechts.setSpeed(75);
			motor_links.forward();
			motor_rechts.forward();
			rightTurn = true;
		}
		else if(values[0] <= -90) {
			motor_links.stop(true);
			motor_rechts.stop();
			
			Delay.msDelay(200);
			sensoren.resetGyro();
			Delay.msDelay(200);
			line = false;
			rightTurn = false;
			rightTurnFindLine = false;
			leftTurnFindLine = false;
			
		}
	}

}
