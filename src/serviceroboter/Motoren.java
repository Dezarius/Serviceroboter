package serviceroboter;

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
	
	public void senkeSensoren(){
		this.motor_sensor.rotate(-95);
		this.motor_sensor.stop();
	}
	
	public void hebeSensoren(){
		this.motor_sensor.setSpeed(100);
		this.motor_sensor.rotate(95);
		this.motor_sensor.stop();
	}
	
	public String ranfahren(){
		String farbe = null;
		this.motor_links.setPower(20);
		this.motor_rechts.setPower(21);
		farbe = this.sensoren.analyseRGB();
		while( farbe == null && this.sensoren.getValues()[3] == 0){
			this.motor_links.forward();
			this.motor_rechts.forward();
			farbe = this.sensoren.analyseRGB();
		}
		this.stop();
		return farbe;
	}
	
	public void ausrichten(){
		this.motor_links.setPower(40);
		this.motor_rechts.setPower(13);
		this.motor_links.forward();
		this.motor_rechts.forward();
		this.forward = false;
		
	}
	
	public void forward() {
		this.line = false;
		this.rightTurn = false;
		this.leftTurn = false;
		this.secondTurnFindLine = false;
		this.firstTurnFindLine = false;
		if (!this.forward) {
			this.motor_links.setPower(65);
			this.motor_rechts.setPower(66);
			this.motor_links.forward();
			this.motor_rechts.forward();
			this.forward = true;
		}
	}
	
	public void stop() {
		this.motor_links.stop();
		this.motor_rechts.stop();
	}
	
	public void diviation() {
		float values[] = this.sensoren.getValues();
		//Abweichung links
		if(values[0] >= 3 && !this.abweichung) {
			this.motor_links.setPower(65);
			this.motor_rechts.setPower(64);
			this.abweichung = true;
		}
		//Abweichung rechts
		else if(values[0] <= -3 && !this.abweichung) {
			this.motor_links.setPower(63);
			this.motor_rechts.setPower(67);
			this.abweichung = true;
		}
		else if(values[0] <= 0.5f && values[0] >= -0.5f && this.abweichung){
			this.motor_links.setPower(65);
			this.motor_rechts.setPower(67);
			this.abweichung = false;
		}
	}
	
	public void findLine(){
		this.forward = false;
		this.abweichung = false;
		
		float values[] = this.sensoren.getValues();
		if (values[0] >= 0 && !this.line && !this.firstTurnFindLine){
			this.rightfirst = true;
			this.stop();
			this.motor_links.setPower(35);
			this.motor_rechts.setPower(35);
			this.motor_links.forward();
			this.motor_rechts.backward();
			this.firstTurnFindLine = true;
		}
		else if (values[0] < 0 && !this.line && !this.firstTurnFindLine) {
			this.rightfirst = false;
			stop();
			this.motor_links.setPower(35);
			this.motor_rechts.setPower(35);
			this.motor_rechts.forward();
			this.motor_links.backward();
			this.firstTurnFindLine = true;
		}
		else if ((values[0] < -60 || values[0] > 60) && !this.line) {
			this.line = true;
		} 
		else if (this.line && !this.rightfirst && !this.secondTurnFindLine) {
			this.stop();
			this.motor_links.setPower(35);
			this.motor_rechts.setPower(35);
			this.motor_links.forward();
			this.motor_rechts.backward();
			this.secondTurnFindLine = true;
		} 
		else if (this.line && this.rightfirst && !this.secondTurnFindLine) {
			this.stop();
			this.motor_links.setPower(35);
			this.motor_rechts.setPower(35);
			this.motor_rechts.forward();
			this.motor_links.backward();
			this.secondTurnFindLine = true;
		} 
		else {
			if (this.rightfirst && values[0] > 60 && this.line) {
				this.stop();
				System.out.println("ENDE");
			}
			if (!this.rightfirst && values[0] < -60 && this.line) {
				this.stop();
				System.out.println("ENDE");
			}
		}
		
	}
	
	public void turnLeft() {
		float values[] = this.sensoren.getValues();
		if (!this.leftTurn) {
			this.stop();
			
			this.motor_links.setPower(25);
			this.motor_rechts.setPower(45);
			this.motor_links.forward();
			this.motor_rechts.forward();
			this.leftTurn = true;
		}
		else if(values[0] >= 89) {
			this.stop();
			
			Main.setKurve(-1);
			Delay.msDelay(200);

			this.sensoren.resetGyro();
			Delay.msDelay(400);
			this.line = false;
			this.rightTurn = false;
			this.secondTurnFindLine = false;
			this.firstTurnFindLine = false;
		}
	}
	
	public void turnRight() {
		float values[] = this.sensoren.getValues();
		if (!this.rightTurn) {
			this.stop();
			
			this.motor_links.setPower(45);
			this.motor_rechts.setPower(25);
			this.motor_links.forward();
			this.motor_rechts.forward();
			this.rightTurn = true;
		}
		else if(values[0] <= -89) {
			stop();
			
			Main.setKurve(-1);
            System.out.println(values[0]);
			Delay.msDelay(200);
			this.sensoren.resetGyro();
			Delay.msDelay(500);
			this.line = false;
			this.rightTurn = false;
			this.secondTurnFindLine = false;
			this.firstTurnFindLine = false;
			
		}
	}

}
