package serviceroboter;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {
	
	static Motoren motoren;
	static Sensoren sensoren;	
	static Hindernis hindernis;
	static Farben color;
	
	private static int farbe = 6;
	private static boolean tonne = false;
	private static boolean ausrichtung = false;
	private static boolean test = false;
	
	private static boolean search = false;
	private static int kurve = -1;

	public static void main(String[] args) {
		
		sensoren = new Sensoren();
		motoren = new Motoren(sensoren);
		hindernis = new Hindernis(motoren,sensoren);
		color = new Farben();
		
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
			if(values[0] > 25){
				kurve = 1;
			}
			else if(values[0] < -25){
				kurve = 2;
			}
			search = false;
			motoren.stopNXT();
		}

		if(values[2] <= 0.07f || tonne ){
			
			if (!tonne){
				motoren.stopNXT();
				motoren.hebeSensoren();
				motoren.ranfahren();
				while ((values[0] >= 2 || values[0] <= -2) && values[3] == 0) {
					if (values[0] >= 2) {
						motoren.motor_links.setPower(20);
						motoren.motor_rechts.setPower(20);
						motoren.motor_links.forward();
						motoren.motor_rechts.backward();
					}
					else if (values[0] <= -2) {
						motoren.motor_links.setPower(20);
						motoren.motor_rechts.setPower(20);
						motoren.motor_links.backward();
						motoren.motor_rechts.forward();
					}
					values = sensoren.getValues();
				}
				motoren.stopNXT();
				Delay.msDelay(200);
				values = sensoren.getValues();
				motoren.stopNXT();
				values = sensoren.getValues();
				System.out.println("Farbe: " + values[1]);
				color.addColor(values[1]);
				tonne = true;
				
			}
			else if (!ausrichtung && hindernis.circumvent(values[1])){
				motoren.ausrichtenNXT();
				ausrichtung = true;
			}
			else if (ausrichtung && values[0] <= 0) {
				motoren.stopNXT();
				tonne = false;
				ausrichtung = false;
				search = false;
				kurve = -1;
			}
		}
		else if(kurve == 1){
			//System.out.println(values[0]);
			motoren.turnLeft();
		}
		else if(kurve == 2){
			//System.out.println(values[0]);
			motoren.turnRight();
		}
		else if(values[1] == farbe && !search && kurve == -1){
			//System.out.println(values[0]);
			motoren.forward();
			motoren.diviation();
		}
		else if(values[1] != farbe){
			//System.out.println(values[0]);
			motoren.findLine();
			search = true;
		}
		
	}
	
	public static void setKurve(int i) {
		kurve = i;
	}
}
