package serviceroboter;

import lejos.utility.Delay;

public class Hindernis{
	
	Motoren motoren;
	
	float[] values;
	float oldDist = 0;
	float newDist = 0;
	
	boolean convert = false;
	boolean first = false;
	boolean rightDirection = false;
	
	public Hindernis(Motoren motoren, Sensoren sensoren){
		this.motoren = motoren;
	}
	
	public boolean circumvent(float value){
		if (!convert){
			motoren.motor_rechts.setPower(40);
			motoren.motor_links.setPower(5);
			this.convert = true;
			motoren.motor_rechts.backward();
			motoren.motor_links.forward();
			Delay.msDelay(1400);
			motoren.motor_rechts.stop();
			motoren.senkeSensoren();
			motoren.motor_links.setPower(25);
			motoren.motor_rechts.setPower(25);
			motoren.motor_links.forward();
			motoren.motor_rechts.forward();
			Delay.msDelay(600);
			motoren.motor_links.setPower(25);
			motoren.motor_rechts.setPower(45);
			return false;
			
		} else if (value == 6){
			motoren.stopNXT();
			this.convert = false;
			return true;
			
		}
		return false;

	}
	
	public boolean findTonne(float value) {
		newDist = value;
		
		if (!first) {
			motoren.motor_rechts.setPower(15);
			motoren.motor_links.setPower(15);
			motoren.motor_rechts.backward();
			motoren.motor_links.forward();
			first = true;
			oldDist = value;
		}
		else if (newDist < oldDist) {
			rightDirection = true;
			oldDist = newDist;
		}
		else  if (newDist > oldDist){
			if (rightDirection) {
				motoren.stopNXT();
				first = false;
				rightDirection = false;
				return true;
			}
			else {
				motoren.stopNXT();
				motoren.motor_rechts.setPower(15);
				motoren.motor_links.setPower(15);
				motoren.motor_rechts.forward();
				motoren.motor_links.backward();
				oldDist = newDist;
			}
		}
		
		return false;
	}
}
