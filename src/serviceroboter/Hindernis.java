package serviceroboter;

import lejos.utility.Delay;

public class Hindernis{
	
	Motoren motoren;
	
	float[] values;
	
	boolean convert = false;
	
	public Hindernis(Motoren motoren, Sensoren sensoren){
		this.motoren = motoren;
	}
	
	public boolean circumvent(float value){
		if (!convert){
			//motoren.toEV3();
			motoren.motor_rechts.setPower(40);
			motoren.motor_links.setPower(5);
			this.convert = true;
			motoren.motor_rechts.backward();
			motoren.motor_links.forward();
			Delay.msDelay(1300);
			motoren.motor_rechts.stop();
			motoren.senkeSensoren();
			motoren.motor_links.setPower(25);
			motoren.motor_rechts.setPower(45);
			motoren.motor_links.forward();
			motoren.motor_rechts.forward();
			return false;
			
		} else if (value == 6){
			motoren.stopNXT();
			this.convert = false;
			return true;
			
		}
		return false;

	}
}
