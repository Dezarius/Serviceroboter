package serviceroboter;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Motoren {
	
	RegulatedMotor motor_links;
	RegulatedMotor motor_rechts;
	RegulatedMotor motor_sensor;
	Sensoren sensoren;
	
	public Motoren(Sensoren sensoren) {
		this.motor_links = new EV3LargeRegulatedMotor(MotorPort.A);
		this.motor_rechts = new EV3LargeRegulatedMotor(MotorPort.D);
		this.motor_sensor = new EV3MediumRegulatedMotor(MotorPort.B);
		this.sensoren = sensoren;
	}
	
	public void forward() {
		if (!motor_links.isMoving() && !motor_rechts.isMoving()) {
			motor_links.setSpeed(300);
			motor_rechts.setSpeed(300);
			motor_links.forward();
			motor_rechts.forward();
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
		}
		//Abweichung rechts
		else if(values[0] <= -3) {
			motor_links.setSpeed(270);
			motor_rechts.setSpeed(300);
		}
		else if(values[0] <= 0.5f && values[0] >= -0.5f){
			motor_links.setSpeed(300);
			motor_rechts.setSpeed(300);
		}
	}

}
