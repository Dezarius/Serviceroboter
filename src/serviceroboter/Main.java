package serviceroboter;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {
	
	static Motoren motoren;
	static Sensoren sensoren;
	
	private static int farbe = 6;
	
	private static boolean search = false;
	private static int kurve = -1;

	public static void main(String[] args) {
		
		sensoren = new Sensoren();
		motoren = new Motoren(sensoren);
		
		
		while (true) {
			float values[] = sensoren.getValues();
			loop(values);
			if(values[3] == 1){
				break;
			}
		}
		
	}

	private static void loop(float values[]) {
		
		if(values[1] == farbe && search){
			Delay.msDelay(50);
			if(values[0] > 15){
				kurve = 1;
			}
			else if(values[0] < -15){
				kurve = 2;
			}
			search = false;
			motoren.stop();
		}
		
		if(kurve == 1 && values[0] > 15){
			motoren.turnLeft();
		}
		else if(kurve == 2 && values[0] < -15){
			motoren.turnRight();
		}
		else if(values[1] == farbe && !search){
			motoren.forward();
			motoren.diviation();
			
			LCD.drawString("schwarz", 1, 1);
			LCD.clear(2);
		}
		else {
			motoren.findLine();
			search = true;
			LCD.drawString("Nicht schwarz", 1, 2);
			LCD.clear(1);
		}
		
		if (values[0] >= 90 || values[0] <= -90) {
			kurve = -1;
		}
		
	}
}
