package serviceroboter;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BasicMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTMotor;
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
	private boolean firstTurnFindLine = false;
	private boolean secondTurnFindLine = false;
	private boolean abweichung = false;
	
	private boolean line = false;
	private boolean rightfirst = false;
	
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
		secondTurnFindLine = false;
		firstTurnFindLine = false;
		if (!forward) {
			motor_links.setSpeed(350);
			motor_rechts.setSpeed(350);
			motor_links.forward();
			motor_rechts.forward();
			forward = true;
		}
	}
	
	public void stop() {
		motor_links.stop(true);
		motor_rechts.stop();
		//System.out.println("Stop");
	}
	
	public void diviation() {
		float values[] = sensoren.getValues();
		//Abweichung links
		if(values[0] >= 3 && !abweichung) {
			motor_links.setSpeed(350);
			motor_rechts.setSpeed(340);
			abweichung = true;
		}
		//Abweichung rechts
		else if(values[0] <= -3 && !abweichung) {
			motor_links.setSpeed(340);
			motor_rechts.setSpeed(350);
			abweichung = true;
		}
		else if(values[0] <= 0.5f && values[0] >= -0.5f && abweichung){
			motor_links.setSpeed(350);
			motor_rechts.setSpeed(350);
			abweichung = false;
		}
	}
	
	public void findLine(){
		forward = false;
		abweichung = false;
		float values[] = sensoren.getValues();
		
		if (values[0] >= 0 && !line && !firstTurnFindLine){
			rightfirst = true;
			stop();
			motor_links.setSpeed(120);
			motor_rechts.setSpeed(120);
			motor_links.forward();
			motor_rechts.backward();
			firstTurnFindLine = true;
		}
		else if (values[0] < 0 && !line && !firstTurnFindLine) {
			rightfirst = false;
			stop();
			motor_links.setSpeed(120);
			motor_rechts.setSpeed(120);
			motor_rechts.forward();
			motor_links.backward();
			firstTurnFindLine = true;
		}
		else if ((values[0] < -45 || values[0] > 45) && !line) {
			line = true;
		} 
		else if (line && !rightfirst && !secondTurnFindLine) {
			stop();
			motor_links.setSpeed(120);
			motor_rechts.setSpeed(120);
			motor_links.forward();
			motor_rechts.backward();
			secondTurnFindLine = true;
		} 
		else if (line && rightfirst && !secondTurnFindLine) {
			stop();
			motor_links.setSpeed(120);
			motor_rechts.setSpeed(120);
			motor_rechts.forward();
			motor_links.backward();
			secondTurnFindLine = true;
		} 
		else {
			if (rightfirst && values[0] > 45 && line) {
				stop();
				System.out.println("ENDE");
			}
			if (!rightfirst && values[0] < -45 && line) {
				stop();
				System.out.println("ENDE");
			}
		}
		
		
		/*
		if (values[0] <= 40 && line) {
			if (!leftTurnFindLine) {
				motor_links.setSpeed(100);
				motor_rechts.setSpeed(100);
				motor_rechts.forward();
				motor_links.backward();
				leftTurnFindLine = true;
			}
		}
		else if (values[0] > 40 && line) {
			stop();
		}
		if (values[0] >= -40 && !line) {
			if (!rightTurnFindLine) {
				stop();
				motor_links.setSpeed(100);
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
		*/
		
	}
	
	public void turnLeft() {
		float values[] = sensoren.getValues();
		if (!leftTurn) {
			stop();
			
			motor_links.setSpeed(70);
			motor_rechts.setSpeed(170);
			motor_links.forward();
			motor_rechts.forward();
			leftTurn = true;
		}
		else if(values[0] >= 90) {
			stop();
			
			Main.setKurve(-1);
			Delay.msDelay(200);
			sensoren.resetGyro();
			Delay.msDelay(200);
			line = false;
			rightTurn = false;
			secondTurnFindLine = false;
			firstTurnFindLine = false;
		}
	}
	
	public void turnRight() {
		float values[] = sensoren.getValues();
		if (!rightTurn) {
			stop();
			
			motor_links.setSpeed(170);
			motor_rechts.setSpeed(70);
			motor_links.forward();
			motor_rechts.forward();
			rightTurn = true;
		}
		else if(values[0] <= -90) {
			stop();
			
			Main.setKurve(-1);
			Delay.msDelay(200);
			sensoren.resetGyro();
			Delay.msDelay(200);
			line = false;
			rightTurn = false;
			secondTurnFindLine = false;
			firstTurnFindLine = false;
			
		}
	}

}
