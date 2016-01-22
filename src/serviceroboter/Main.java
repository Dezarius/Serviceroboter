package serviceroboter;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Main {
	
	static Motoren motoren;
	static Sensoren sensoren;	
	static Hindernis hindernis;
	static Farben color;
	
	private static int farbe = 13;
	private static boolean tonne = false;
	private static boolean ausrichtung = false;
	private static boolean ranfahren = false;
	private static boolean start = false;
	private static String tonnenFarbe = null;
	private static boolean foundTonne = false;
	
	private static boolean search = false;
	private static int kurve = -1;

	public static void main(String[] args) {
		
		sensoren = new Sensoren();
		motoren = new Motoren(sensoren);
		hindernis = new Hindernis(motoren,sensoren);
		color = new Farben();
		float values[];
		
		values = sensoren.getValues();
		
		if (values[1] == farbe){
			System.out.println("Bereit zum Starten :)");
			Sound.twoBeeps();
		}else {
			System.out.println("Keine weisse Linie erkannt, trotzdem starten?");
			Sound.beep();
		}
		
		while (!start){
			values = sensoren.getValues();
			if (values[3] == 1){
				LCD.clear();
				start = true;
				Delay.msDelay(1000);
			}
		}
		
		while (true) {
			values = sensoren.getValues();
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
			motoren.stop();
		}

		LCD.drawString(String.valueOf(values[2]), 5, 1);
		if(((values[2] <= 0.09f && values[2] != 0.0f) || tonne ) && kurve == -1){
			
			if (!tonne){
				motoren.stop();
				tonne = true;
			}
			else if (foundTonne || hindernis.findTonne(values[2])) {
				foundTonne = true;
				
				if (!ranfahren) {
					motoren.hebeSensoren();
					tonnenFarbe = motoren.ranfahren();
					//System.out.println("Farbe: " + tonnenFarbe);
					color.addColor(values[1]);
					ranfahren = true;
				}
				else if (!ausrichtung && hindernis.circumvent(values[1])){
					motoren.ausrichten();
					ausrichtung = true;
				}
				else if (ausrichtung && values[0] <= 0) {
					motoren.stop();
					tonne = false;
					ausrichtung = false;
					search = false;
					kurve = -1;
					ranfahren = false;
					foundTonne = false;
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
