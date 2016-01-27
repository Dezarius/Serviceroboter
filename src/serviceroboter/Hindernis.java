package serviceroboter;

import lejos.utility.Delay;

public class Hindernis{
	
	Motoren motoren;
	
	float[] values;
	float dist = 0;
	float smallGyro = 0;
	
	boolean convert = false;
	boolean first = false;
	boolean rightDirection = false;
	boolean finish = false;
	float gyro = 0;
	
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
			Delay.msDelay(800);
			motoren.motor_links.setPower(28);
			motoren.motor_rechts.setPower(55);
			return false;
			
		} else if (value == 6){
			motoren.stop();
			this.convert = false;
			return true;
			
		}
		return false;

	}
	
	public boolean findTonne(float[] value) {
		if (!first){
			gyro = value[0];
			smallGyro = value[0];
			dist = value[2];
			motoren.motor_rechts.setPower(20);
			motoren.motor_links.setPower(20);
			motoren.motor_rechts.backward();
			motoren.motor_links.forward();
			first = true;
		}
		else if(value[2] < dist){
			//System.out.println(dist + " -> " + value[2]);
			dist = value[2];
			smallGyro = value[0];
		}
		else if (finish){
			if (smallGyro < value[0]){
				motoren.motor_rechts.backward();
				motoren.motor_links.forward();
			}
			else if (smallGyro > value[0]){
				motoren.motor_links.backward();
				motoren.motor_rechts.forward();
			}
			else if (smallGyro == value[0]){
				motoren.stop();
				finish = false;
				first = false;
				return true;
			}
		}
		else if (value[0] <= gyro - 15){
			motoren.stop();
			motoren.motor_links.backward();
			motoren.motor_rechts.forward();
			
		}
		else if (value[0] >= gyro + 15){
			motoren.stop();
			finish = true;
		}

		
		
		
		
		/*if (!first) {
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
				motoren.stop();
				first = false;
				rightDirection = false;
				return true;
			}
			else {
				motoren.stop();
				motoren.motor_rechts.setPower(15);
				motoren.motor_links.setPower(15);
				motoren.motor_rechts.forward();
				motoren.motor_links.backward();
				oldDist = newDist;
			}
		}
		
		return false;*/
		return false;
	}
}
