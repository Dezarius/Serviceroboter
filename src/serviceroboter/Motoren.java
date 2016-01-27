package serviceroboter;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Motoren {
	
	public UnregulatedMotor motor_links;
	public UnregulatedMotor motor_rechts;
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
		this.motor_links = new UnregulatedMotor(MotorPort.A);
		this.motor_rechts = new UnregulatedMotor(MotorPort.D);
		this.motor_sensor = new EV3MediumRegulatedMotor(MotorPort.B);
		this.sensoren = sensoren;
	}
	/**
	 * Senkt den Farbsensor und Ultraschallsensor durch drehen des Motors ab
	 * @param
	 * @return 
	 */
	public void senkeSensoren(){
		this.motor_sensor.rotate(-95, true);
		//this.motor_sensor.stop();
	}
	/**
	 * Hebt den Farbsensor und Ultraschallsensor durch drehen des Motors ab
	 * @param
	 * @return 
	 */
	public void hebeSensoren(){
		this.motor_sensor.setSpeed(100);
		this.motor_sensor.rotate(95);
		this.motor_sensor.stop();
	}
	/**
	 * Nach erkennen der Tonne wird näher an die Tonne herangefahren
	 * @param
	 * @return Erkannte Farbe 
	 */
	public String ranfahren(){
		String farbe = null;
		this.motor_links.setPower(21);
		this.motor_rechts.setPower(21);
		farbe = this.sensoren.analyseRGB();
		float[] values = this.sensoren.getValues();
		float gyro = values[0];
		final long timeStart = System.currentTimeMillis();
		long timeEnd = timeStart;
		
		while(farbe == null && values[3] == 0 && timeEnd-timeStart <= 2500){
			this.motor_links.forward();
			this.motor_rechts.forward();
			if (values[0] > gyro) {
				this.motor_links.setPower(20);
				this.motor_rechts.setPower(16);
			}
			else if (values[0] < gyro) {
				this.motor_links.setPower(16);
				this.motor_rechts.setPower(20);
			}
			else if (values[0] == gyro) {
				this.motor_links.setPower(20);
				this.motor_rechts.setPower(20);
			}
			
			farbe = this.sensoren.analyseRGB();
			values = this.sensoren.getValues();
			
			timeEnd=System.currentTimeMillis();
		}
		System.out.println("Farbe: " + farbe);
		//LCD.drawString("Farbe: " + farbe, 1, 8);
		this.stop();
		return farbe;
	}
	/**
	 * Richtet den Roboter aus
	 * @param
	 * @return 
	 */
	public void ausrichten(){
		this.motor_links.setPower(40);
		this.motor_rechts.setPower(10);
		this.motor_links.forward();
		this.motor_rechts.forward();
		this.forward = false;
		
	}
	/**
	 * Setzt die Geschwindigkeit von beiden Motoren
	 * @param Die zu fahrende Geschwindigkeit
	 * @return 
	 */
	public void setPower(int speed){
		this.motor_links.setPower(speed);
		this.motor_rechts.setPower(speed);
	}
	/**
	 * Lässt Roboter geradeaus fahren
	 * @param
	 * @return 
	 */
	public void forward() {
		this.line = false;
		this.rightTurn = false;
		this.leftTurn = false;
		this.secondTurnFindLine = false;
		this.firstTurnFindLine = false;
		if (!this.forward) {
			this.motor_links.setPower(60);
			this.motor_rechts.setPower(61);
			this.motor_links.forward();
			this.motor_rechts.forward();
			this.forward = true;
		}
	}
	/**
	 * Stopt die Motoren
	 * @param
	 * @return 
	 */
	public void stop() {
		this.motor_links.stop();
		this.motor_rechts.stop();
	}
	/**
	 * Gleicht schräge Fahrweise auf der Linie aus
	 * @param
	 * @return 
	 */
	public void diviation() {
		float values[] = this.sensoren.getValues();
		//Abweichung links
		if(values[0] >= 3 && !this.abweichung) {
			this.motor_links.setPower(60);
			this.motor_rechts.setPower(59);
			this.abweichung = true;
		}
		//Abweichung rechts
		else if(values[0] <= -3 && !this.abweichung) {
			this.motor_links.setPower(58);
			this.motor_rechts.setPower(61);
			this.abweichung = true;
		}
		else if(values[0] <= 0.5f && values[0] >= -0.5f && this.abweichung){
			this.motor_links.setPower(60);
			this.motor_rechts.setPower(61);
			this.abweichung = false;
		}
	}
	/**
	 * Sucht die Linie nach einer Kurve
	 * @param
	 * @return 
	 */
	public void findLine(){
		this.forward = false;
		this.abweichung = false;
		
		float values[] = this.sensoren.getValues();
		if (values[0] >= 0 && !this.line && !this.firstTurnFindLine){
			this.rightfirst = true;
			this.stop();
			this.motor_links.setPower(30);
			this.motor_rechts.setPower(30);
			this.motor_links.forward();
			this.motor_rechts.backward();
			this.firstTurnFindLine = true;
		}
		else if (values[0] < 0 && !this.line && !this.firstTurnFindLine) {
			this.rightfirst = false;
			stop();
			this.motor_links.setPower(30);
			this.motor_rechts.setPower(30);
			this.motor_rechts.forward();
			this.motor_links.backward();
			this.firstTurnFindLine = true;
		}
		else if ((values[0] < -60 || values[0] > 60) && !this.line) {
			this.line = true;
		} 
		else if (this.line && !this.rightfirst && !this.secondTurnFindLine) {
			this.stop();
			this.motor_links.setPower(30);
			this.motor_rechts.setPower(30);
			this.motor_links.forward();
			this.motor_rechts.backward();
			this.secondTurnFindLine = true;
		} 
		else if (this.line && this.rightfirst && !this.secondTurnFindLine) {
			this.stop();
			this.motor_links.setPower(30);
			this.motor_rechts.setPower(30);
			this.motor_rechts.forward();
			this.motor_links.backward();
			this.secondTurnFindLine = true;
		} 
		else {
			if (this.rightfirst && values[0] > 60 && this.line) {
				//Sound.twoBeeps();
				this.stop();
				//System.out.println("ENDE");
			}
			if (!this.rightfirst && values[0] < -60 && this.line) {
				//Sound.twoBeeps();
				this.stop();
				//System.out.println("ENDE");
			}
		}
		
	}
	/**
	 * Macht eine Linksdrehung
	 * @param
	 * @return 
	 */
	public void turnLeft() {
		float values[] = this.sensoren.getValues();
		if (!this.leftTurn) {
			this.stop();
			
			this.motor_links.setPower(22);
			this.motor_rechts.setPower(48);
			this.motor_links.forward();
			this.motor_rechts.forward();
			this.leftTurn = true;
		}
		else if(values[0] >= 90) {
			this.stop();
			
			Main.setKurve(-1);
			Delay.msDelay(200);
			this.sensoren.resetGyro();
			this.motor_links.setPower(25);
			this.motor_rechts.setPower(25);
			this.motor_links.backward();
			this.motor_rechts.backward();
			Delay.msDelay(1000);
			this.stop();
			this.line = false;
			this.rightTurn = false;
			this.secondTurnFindLine = false;
			this.firstTurnFindLine = false;
		}
	}
	/**
	 * Macht eine Rechtsdrehung
	 * @param
	 * @return 
	 */
	public void turnRight() {
		float values[] = this.sensoren.getValues();
		if (!this.rightTurn) {
			this.stop();
			
			this.motor_links.setPower(50);
			this.motor_rechts.setPower(20);
			this.motor_links.forward();
			this.motor_rechts.forward();
			this.rightTurn = true;
		}
		else if(values[0] <= -90) {
			stop();
			
			Main.setKurve(-1);
			Delay.msDelay(200);
			this.sensoren.resetGyro();
			this.motor_links.setPower(25);
			this.motor_rechts.setPower(25);
			this.motor_links.backward();
			this.motor_rechts.backward();
			Delay.msDelay(1000);
			this.stop();
			this.line = false;
			this.rightTurn = false;
			this.secondTurnFindLine = false;
			this.firstTurnFindLine = false;
			
		}
	}

}
