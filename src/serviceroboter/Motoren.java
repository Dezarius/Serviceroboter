package serviceroboter;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Motoren {
	
	public UnregulatedMotor motor_links;
	public UnregulatedMotor motor_rechts;
	public EV3LargeRegulatedMotor motor_links_ev3;
	public EV3LargeRegulatedMotor motor_rechts_ev3;
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
	
	public void toEV3(){
		this.closeNXT();
		this.motor_links_ev3 = new EV3LargeRegulatedMotor(MotorPort.A);
		this.motor_rechts_ev3 = new EV3LargeRegulatedMotor(MotorPort.D);
	}
	
	public void toNXT(){
		this.closeEV3();
		this.motor_links = new UnregulatedMotor(MotorPort.A);
		this.motor_rechts = new UnregulatedMotor(MotorPort.D);
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
	
	public void ranfahren(){
		motor_links.setPower(30);
		motor_rechts.setPower(30);
		motor_links.forward();
		motor_rechts.forward();
		Delay.msDelay(1100);
		this.stopNXT();
	}
	
	public void ranfahrenEV3(){
		motor_links_ev3.stop();
		motor_rechts_ev3.stop();
		motor_links_ev3.setSpeed(100);
		motor_rechts_ev3.setSpeed(100);
		motor_links_ev3.rotate(200, true);
		motor_rechts_ev3.rotate(200);
		//Delay.msDelay(1000);
		
	}
	
	public void ausrichtenNXT(){
		motor_links.setPower(40);
		motor_rechts.setPower(13);
		motor_links.forward();
		motor_rechts.forward();
		forward = false;
		
	}
	
	public void closeNXT() {
		this.motor_links.close();
		this.motor_rechts.close();
	}
	
	public void closeEV3() {
		this.motor_links_ev3.close();
		this.motor_rechts_ev3.close();
	}
	
	public void forward() {
		line = false;
		rightTurn = false;
		leftTurn = false;
		secondTurnFindLine = false;
		firstTurnFindLine = false;
		if (!forward) {
			motor_links.setPower(65);
			motor_rechts.setPower(66);
			motor_links.forward();
			motor_rechts.forward();
			forward = true;
		}
	}
	
	public void stopNXT() {
		motor_links.stop();
		motor_rechts.stop();
	}
	
	public void stopEV3() {
		motor_links_ev3.stop();
		motor_rechts_ev3.stop();
	}
	
	public void diviation() {
		float values[] = sensoren.getValues();
		//Abweichung links
		if(values[0] >= 3 && !abweichung) {
			motor_links.setPower(65);
			motor_rechts.setPower(64);
			abweichung = true;
		}
		//Abweichung rechts
		else if(values[0] <= -3 && !abweichung) {
			motor_links.setPower(63);
			motor_rechts.setPower(67);
			abweichung = true;
		}
		else if(values[0] <= 0.5f && values[0] >= -0.5f && abweichung){
			motor_links.setPower(65);
			motor_rechts.setPower(67);
			abweichung = false;
		}
	}
	
	public void findLine(){
		forward = false;
		abweichung = false;
		
		float values[] = sensoren.getValues();
		if (values[0] >= 0 && !line && !firstTurnFindLine){
			rightfirst = true;
			stopNXT();
			motor_links.setPower(35);
			motor_rechts.setPower(35);
			motor_links.forward();
			motor_rechts.backward();
			firstTurnFindLine = true;
		}
		else if (values[0] < 0 && !line && !firstTurnFindLine) {
			rightfirst = false;
			stopNXT();
			motor_links.setPower(35);
			motor_rechts.setPower(35);
			motor_rechts.forward();
			motor_links.backward();
			firstTurnFindLine = true;
		}
		else if ((values[0] < -60 || values[0] > 60) && !line) {
			line = true;
		} 
		else if (line && !rightfirst && !secondTurnFindLine) {
			stopNXT();
			motor_links.setPower(35);
			motor_rechts.setPower(35);
			motor_links.forward();
			motor_rechts.backward();
			secondTurnFindLine = true;
		} 
		else if (line && rightfirst && !secondTurnFindLine) {
			stopNXT();
			motor_links.setPower(35);
			motor_rechts.setPower(35);
			motor_rechts.forward();
			motor_links.backward();
			secondTurnFindLine = true;
		} 
		else {
			if (rightfirst && values[0] > 60 && line) {
				stopNXT();
				System.out.println("ENDE");
			}
			if (!rightfirst && values[0] < -60 && line) {
				stopNXT();
				System.out.println("ENDE");
			}
		}
		
	}
	
	public void turnLeft() {
		float values[] = sensoren.getValues();
		if (!leftTurn) {
			stopNXT();
			
			motor_links.setPower(25);
			motor_rechts.setPower(45);
			motor_links.forward();
			motor_rechts.forward();
			leftTurn = true;
		}
		else if(values[0] >= 89) {
			stopNXT();
			
			Main.setKurve(-1);
			Delay.msDelay(200);

			sensoren.resetGyro();
			Delay.msDelay(400);
			line = false;
			rightTurn = false;
			secondTurnFindLine = false;
			firstTurnFindLine = false;
		}
	}
	
	public void turnRight() {
		float values[] = sensoren.getValues();
		if (!rightTurn) {
			stopNXT();
			
			motor_links.setPower(45);
			motor_rechts.setPower(25);
			motor_links.forward();
			motor_rechts.forward();
			rightTurn = true;
		}
		else if(values[0] <= -89) {
			stopNXT();
			
			Main.setKurve(-1);
            System.out.println(values[0]);
			Delay.msDelay(200);
			sensoren.resetGyro();
			Delay.msDelay(500);
			line = false;
			rightTurn = false;
			secondTurnFindLine = false;
			firstTurnFindLine = false;
			
		}
	}

}
