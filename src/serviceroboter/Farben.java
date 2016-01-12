package serviceroboter;

public class Farben {
	private float farben [] = new float [1]; 
	
	
	
	public void addColor(float color){
	    float farben_new [] = new float [this.farben.length +1 ];
		farben_new[this.farben.length -1] = color;
		this.farben = farben_new;
	}
	
	public void print(){
		System.out.println("Folgende Farben wurden erkannt " + "\n");
		for(float flt : this.farben){
			System.out.println("Farbecode:" + flt);
		}
	}
	public float [] getColors (){
		return this.farben;
	}

}
