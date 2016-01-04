package de.geile.gruppe;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Pit {

	public static void main(String[] args) {
		
		boolean onLine = false;
		boolean lastLeft = false;
		
		RegulatedMotor motor_links = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor motor_rechts = new EV3LargeRegulatedMotor(MotorPort.D);
		RegulatedMotor motor_sensor = new EV3MediumRegulatedMotor(MotorPort.B);
		
		EV3GyroSensor sensor1 = new EV3GyroSensor(SensorPort.S1);
		SampleProvider gyro = sensor1.getMode("Angle");
		float gyroValue[] = new float[gyro.sampleSize()];
		
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

		/*touch.fetchSample(touchValue, 0);
		Sound.beep();
		while(touchValue[0] == 0){
			touch.fetchSample(touchValue, 0);
			Delay.msDelay(100);
		}
		LCD.clear();
		Delay.msDelay(500);
		touch.fetchSample(touchValue, 0);*/
		sensor1.reset();
		while(touchValue[0] == 0 ){
			gyro.fetchSample(gyroValue, 0);
			color.fetchSample(colorValue, 0);
			
			//Geradeaus auf Linien
			while(colorValue[0] == 7 && gyroValue[0] <= 3 && gyroValue[0] >= -3){
				if(!motor_links.isMoving() && !motor_rechts.isMoving()){
					motor_links.setSpeed(300);
					motor_rechts.setSpeed(300);
					motor_links.forward();
					motor_rechts.forward();
				}	
				color.fetchSample(colorValue, 0);
				gyro.fetchSample(gyroValue, 0);
				
				if(motor_links.getTachoCount() >= 700){
					lastLeft = false;
				}
			}
			
			// Abweichung nach links
			while(colorValue[0] == 7 && gyroValue[0] >= 3){
					motor_links.setSpeed(300);
					motor_rechts.setSpeed(270);
					color.fetchSample(colorValue, 0);
					gyro.fetchSample(gyroValue, 0);
					lastLeft=false;
			}
			
			// Abweichung nach rechts
			while(colorValue[0] == 7 && gyroValue[0] <= -3){
				motor_links.setSpeed(270);
				motor_rechts.setSpeed(300);
				color.fetchSample(colorValue, 0);
				gyro.fetchSample(gyroValue, 0);
				lastLeft=false;
			}
			
			// Kurven
			while(colorValue[0] != 7 && gyroValue[0] <= 4 && gyroValue[0] >= -4 && touchValue[0] == 0){
				onLine = false;
				Delay.msDelay(600);
				motor_links.stop(true);
				motor_rechts.stop();
				motor_links.setSpeed(250);
				motor_rechts.setSpeed(250);
				gyro.fetchSample(gyroValue, 0);
				
				// Rechtskurve
				while(gyroValue[0] > -90 && !onLine &&!lastLeft){	
					motor_rechts.backward();
					color.fetchSample(colorValue, 0);
					if (colorValue[0] == 7){
						onLine = true;
						motor_rechts.stop();
					}
					lastLeft = false;
					gyro.fetchSample(gyroValue, 0);
				}
				
				motor_rechts.stop();
				sensor1.reset();
				
				// Linkskurve
				if (!onLine && !lastLeft){
					gyro.fetchSample(gyroValue, 0);
					while(gyroValue[0] < 90){	
						motor_rechts.forward();	
						gyro.fetchSample(gyroValue, 0);
					}
					motor_rechts.stop();
					sensor1.reset();
				}

				if (!onLine){
					gyro.fetchSample(gyroValue, 0);
					while(gyroValue[0] < 90 && !onLine){
						motor_links.backward();
						color.fetchSample(colorValue, 0);
						if (colorValue[0] == 7){
							onLine = true;
							motor_links.stop();
							motor_links.resetTachoCount();
						}
						gyro.fetchSample(gyroValue, 0);
					}
					sensor1.reset();
					lastLeft=true;
				}
				
				LCD.drawString(Boolean.toString(lastLeft), 1, 1);
				
			}
			touch.fetchSample(touchValue, 0);			
		}
				
		motor_links.stop();
		motor_rechts.stop();
		LCD.clear();
		LCD.drawString("Ende erreicht.", 1, 1);
		Delay.msDelay(5000);
	}
}