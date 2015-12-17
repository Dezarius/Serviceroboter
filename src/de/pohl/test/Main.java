package de.pohl.test;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		/*
		LCD.drawString("Moin moin.", 1, 1);
		Delay.msDelay(3000);
		LCD.drawString("Starte Motoren!", 1, 1);
		Delay.msDelay(2000);
		Sound.twoBeeps();
		Delay.msDelay(500);
		
		Motor.A.setSpeed(200);
		Motor.D.setSpeed(250);
		Motor.A.forward();
		Motor.D.forward();
		LCD.clear();
		Delay.msDelay(5000);
		Motor.A.backward();
		Motor.D.backward();
		Delay.msDelay(4000);
		Motor.D.flt();
		Motor.A.flt();
		*/
		
		
		EV3ColorSensor color = new EV3ColorSensor(SensorPort.S2);
		LCD.drawString(String.valueOf(color.getColorID()), 1, 1);
		Delay.msDelay(4000);
	}

}
