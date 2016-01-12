package serviceroboter;


public class Hindernis{
	
	Motoren motoren;
	
	float[] values;
	
	boolean convert = false;
	
	public Hindernis(Motoren motoren, Sensoren sensoren){
		motoren = motoren;
	}
	
	public boolean circumvent(float value){
		if (!convert){
			motoren.motor_links_ev3.setSpeed(200);
			motoren.toEV3();
			this.convert = true;
			motoren.motor_links_ev3.rotate(60);
			motoren.motor_links_ev3.setSpeed(100);
			motoren.motor_rechts_ev3.setSpeed(200);
			motoren.motor_links_ev3.forward();
			motoren.motor_rechts_ev3.forward();
			return false;
			
		} else if (value == 6){
			motoren.motor_links_ev3.stop();
			motoren.motor_rechts_ev3.stop();
			motoren.toNXT();
			return true;
			
		}
		return false;

	}
}
