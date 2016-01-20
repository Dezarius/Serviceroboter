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
	private static boolean ranfahren = false;
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
			if(values[0] > 30){
				kurve = 1;
			}
			else if(values[0] < -30){
				kurve = 2;
			}
			search = false;
			motoren.stopNXT();
		}

		LCD.drawString(String.valueOf(values[2]), 5, 1);
		if(((values[2] <= 0.09f && values[2] != 0.0f) || tonne ) && kurve == -1){
			
			if (!tonne){
				motoren.stopNXT();
				tonne = true;
			}
			else if (test || hindernis.findTonne(values[2])) {
				test = true;
				
				if (!ranfahren) {
					motoren.hebeSensoren();
					motoren.ranfahren();
					motoren.stopNXT();
					Delay.msDelay(200);
					values = sensoren.getValues();
					motoren.stopNXT();
					values = sensoren.getValues();
					System.out.println("Farbe: " + values[1]);
					color.addColor(values[1]);
					ranfahren = true;
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
					ranfahren = false;
					test = false;
				}
			}
		}
		else if(kurve == 1){
			motoren.turnLeft();
		}
		else if(kurve == 2){
			motoren.turnRight();
		}
		else if(values[1] == farbe && !search && kurve == -1){
			motoren.forward();
			motoren.diviation();
		}
		else if(values[1] != farbe){
			motoren.findLine();
			search = true;
		}
		
	}
	
	public static void setKurve(int i) {
		kurve = i;
	}
	
	public static void ausgleich(float[] values) {
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
	}
}
