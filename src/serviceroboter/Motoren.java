package serviceroboter;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BasicMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Motoren {
	
	private NXTMotor motor_links;
	private NXTMotor motor_rechts;
	private EV3MediumRegulatedMotor motor_sensor;
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
		this.motor_links = new NXTMotor(MotorPort.A);
		this.motor_rechts = new NXTMotor(MotorPort.D);
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
			motor_links.setPower(70);
			motor_rechts.setPower(70);
			motor_links.forward();
			motor_rechts.forward();
			forward = true;
		}
	}
	
	public void stop() {
		motor_links.stop();
		motor_rechts.stop();
		//System.out.println("Stop");
	}
	
	public void diviation() {
		float values[] = sensoren.getValues();
		//Abweichung links
		if(values[0] >= 3 && !abweichung) {
			motor_links.setPower(70);
			motor_rechts.setPower(66);
			abweichung = true;
		}
		//Abweichung rechts
		else if(values[0] <= -3 && !abweichung) {
			motor_links.setPower(66);
			motor_rechts.setPower(70);
			abweichung = true;
		}
		else if(values[0] <= 0.5f && values[0] >= -0.5f && abweichung){
			motor_links.setPower(70);
			motor_rechts.setPower(70);
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
			motor_links.setPower(30);
			motor_rechts.setPower(30);
			motor_links.forward();
			motor_rechts.backward();
			firstTurnFindLine = true;
		}
		else if (values[0] < 0 && !line && !firstTurnFindLine) {
			rightfirst = false;
			stop();
			motor_links.setPower(30);
			motor_rechts.setPower(30);
			motor_rechts.forward();
			motor_links.backward();
			firstTurnFindLine = true;
		}
		else if ((values[0] < -50 || values[0] > 50) && !line) {
			line = true;
		} 
		else if (line && !rightfirst && !secondTurnFindLine) {
			stop();
			motor_links.setPower(30);
			motor_rechts.setPower(30);
			motor_links.forward();
			motor_rechts.backward();
			secondTurnFindLine = true;
		} 
		else if (line && rightfirst && !secondTurnFindLine) {
			stop();
			motor_links.setPower(30);
			motor_rechts.setPower(30);
			motor_rechts.forward();
			motor_links.backward();
			secondTurnFindLine = true;
		} 
		else {
			if (rightfirst && values[0] > 50 && line) {
				stop();
				System.out.println("ENDE");
			}
			if (!rightfirst && values[0] < -50 && line) {
				stop();
				System.out.println("ENDE");
			}
		}
		
	}
	
	public void turnLeft() {
		float values[] = sensoren.getValues();
		if (!leftTurn) {
			stop();
			
			motor_links.setPower(25);
			motor_rechts.setPower(45);
			motor_links.forward();
			motor_rechts.forward();
			leftTurn = true;
		}
		else if(values[0] >= 90) {
			stop();
			
			Main.setKurve(-1);
			Delay.msDelay(50);
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
			
			motor_links.setPower(45);
			motor_rechts.setPower(25);
			motor_links.forward();
			motor_rechts.forward();
			rightTurn = true;
		}
		else if(values[0] <= -90) {
			stop();
			
			Main.setKurve(-1);
			Delay.msDelay(50);
			sensoren.resetGyro();
			Delay.msDelay(200);
			line = false;
			rightTurn = false;
			secondTurnFindLine = false;
			firstTurnFindLine = false;
			
		}
	}

}
