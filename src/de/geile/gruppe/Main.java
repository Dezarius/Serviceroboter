package de.geile.gruppe;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		RegulatedMotor motor_links = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor motor_rechts = new EV3LargeRegulatedMotor(MotorPort.D);
		RegulatedMotor motor_sensor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		EV3ColorSensor sensor2 = new EV3ColorSensor(SensorPort.S2);
		SampleProvider color = sensor2.getColorIDMode();
		float colorValue[] = new float[color.sampleSize()];
		
		SensorModes sensor3 = new EV3UltrasonicSensor(SensorPort.S3); 
		SampleProvider distance = sensor3.getMode("Distance");
		float distanceValue[] = new float[distance.sampleSize()];
		
		SensorModes sensor4 = new EV3TouchSensor(SensorPort.S4);
		SampleProvider touch = sensor4.getMode("Touch");
		float touchValue[] = new float[touch.sampleSize()];
		
		
		LCD.drawString("Moin moin.", 1, 1);

		touch.fetchSample(touchValue, 0);
		Sound.beep();
		while(touchValue[0] == 0){
			touch.fetchSample(touchValue, 0);
			Delay.msDelay(100);
		}
		Delay.msDelay(1000);
		
		touch.fetchSample(touchValue, 0);
		while (touchValue[0] == 0) {
			
			if(!motor_links.isMoving() && !motor_rechts.isMoving()){
				motor_links.setSpeed(250);
				motor_rechts.setSpeed(250);
				motor_links.forward();
				motor_rechts.forward();
			}
			color.fetchSample(colorValue, 0);
			if (colorValue[0] != 7) {
				
				motor_links.setSpeed(250);
				motor_rechts.setSpeed(100);
				
				
			} else {
				motor_links.setSpeed(250);
				motor_rechts.setSpeed(250);
				motor_links.stop();
				Delay.msDelay(150);
				motor_links.forward();
			}
			touch.fetchSample(touchValue, 0);
		}
		motor_links.stop();
		motor_rechts.stop();
		
		
		/*
		
		motor_links.setSpeed(250);
		motor_rechts.setSpeed(180);
		motor_links.forward();
		motor_rechts.forward();
		touch.fetchSample(touchValue, 0);
		while (touchValue[0] == 0) {
			touch.fetchSample(touchValue, 0);
		}
		
		
		
		
		
		
		LCD.drawString("Starte Motoren!", 1, 1);
		Delay.msDelay(2000);
		Sound.twoBeeps();
		Delay.msDelay(500);
		
		motor_links.setSpeed(250);
		motor_rechts.setSpeed(250);
		motor_links.forward();
		motor_rechts.forward();
		LCD.clear();
		
		touch.fetchSample(touchValue, 0);
		distance.fetchSample(distanceValue, 0);
		while (touchValue[0] == 0 && distanceValue[0] > 0.07f) {
			LCD.drawString("groesser 10 Zentimeter", 0, 0);
			LCD.drawString(String.valueOf(distanceValue[0]), 0, 1);
			Delay.msDelay(50);
			distance.fetchSample(distanceValue, 0);
			touch.fetchSample(touchValue, 0);
		}
		
		LCD.drawString("kleiner 10 Zentimeter", 0, 0);
		LCD.drawString(String.valueOf(distanceValue[0]), 0, 1);
		LCD.clear();
		motor_links.stop();
		motor_rechts.stop();
		motor_links.setSpeed(150);
		motor_links.rotate(45);
		Delay.msDelay(200);
		motor_sensor.rotateTo(90);
		Delay.msDelay(200);
		motor_links.setSpeed(50);
		motor_rechts.setSpeed(50);
		motor_links.rotate(20, true);
		motor_rechts.rotate(20);
		color.fetchSample(colorValue, 0);
		LCD.drawString(String.valueOf(colorValue[0]), 0, 0);
		Delay.msDelay(2000);
		motor_sensor.rotateTo(0);
		*/
		
	}

}
