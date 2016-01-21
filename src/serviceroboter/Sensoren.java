package serviceroboter;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Sensoren {
	
	EV3GyroSensor sensor1;
	EV3ColorSensor sensor2;
	SensorModes sensor3;
	SensorModes sensor4;
	
	SampleProvider gyro;
	SampleProvider color;
	SampleProvider colorRGB;
	SampleProvider distance;
	SampleProvider touch;
	
	float colorRGBValue[];
	
	float values[] = new float[4];
	float tempValues[] = new float[1];
	
	public Sensoren() {
		this.sensor1 = new EV3GyroSensor(SensorPort.S1);
		this.gyro = sensor1.getMode("Angle");
		sensor1.reset();
		
		this.sensor2 = new EV3ColorSensor(SensorPort.S2);
		this.color = sensor2.getColorIDMode();
		this.colorRGB = sensor2.getRGBMode();
		this.colorRGBValue = new float[colorRGB.sampleSize()];
		
		this.sensor3 = new EV3UltrasonicSensor(SensorPort.S3); 
		this.distance = sensor3.getMode("Distance");
		
		this.sensor4 = new EV3TouchSensor(SensorPort.S4);
		this.touch = sensor4.getMode("Touch");

	}
	
	public float[] getValues() {
		this.gyro.fetchSample(this.tempValues, 0);
		this.values[0] = this.tempValues[0];
		this.color.fetchSample(this.tempValues, 0);
		this.values[1] = this.tempValues[0];
		this.distance.fetchSample(this.tempValues, 0);
		this.values[2] = this.tempValues[0];
		this.touch.fetchSample(this.tempValues, 0);
		this.values[3] = this.tempValues[0];
		return values;
	}
	
	public void resetGyro() {
		sensor1.reset();
	}
	
	public String analyseRGB(){
		this.colorRGB.fetchSample(this.colorRGBValue, 0);
		
		//RED
		if(colorRGBValue[0] >= 70 && colorRGBValue[1] <= 15 && colorRGBValue[2] <= 15){
			return "Rot";
		}
		//GREEN
		else if(colorRGBValue[0] <= 15 && colorRGBValue[1] >= 60 && colorRGBValue[2] <= 15){
			return "Gruen";
		}
		//YELLOW
		else if(colorRGBValue[0] >= 35 && colorRGBValue[1] >= 35 && colorRGBValue[2] <= 10){
			return "Gelb";
		}
		//OTHER
		else 
			return null;
		
		
	}

}