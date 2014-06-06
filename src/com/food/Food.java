package src.com.food;

import javax.persistence.*;

@Entity
@Table(name="nutri.Food")
public class Food
{
	/**/
	private String name = "";	//name of food
	private double carbs = 0;		//grams of carbohydrates
	private double protein = 0;	//grams of protein
	private double fat = 0;		//grams of fat
	private int serv_size = 0;	//grams per serving or quantity of item (typically 1)
	private int cholesterol = 0;//milligrams cholesterol
	private int sodium = 0;		//milligrams sodium
	
	/*constructor for properly formatted fields*/
	public Food(String name, double carbs, double protein, double fat, int serv_size,
			int cholesterol, int sodium)
	{
		super();
		this.name = name;
		this.carbs = carbs;
		this.protein = protein;
		this.fat = fat;
		this.serv_size = serv_size;
		this.cholesterol = cholesterol;
		this.sodium = sodium;
	}
	
	/*constructor for String inputs*/
	public Food(String...item)
	{
		this.name=item[0];
		this.serv_size=Integer.parseInt(item[1]);
		this.fat=Double.parseDouble(item[2]);
		this.cholesterol=Integer.parseInt(item[3]);
		this.sodium=Integer.parseInt(item[4]);
		this.carbs=Double.parseDouble(item[5]);
		this.protein=Double.parseDouble(item[6]);
		
	}
	
	/*standard no arg constructor for Hibernate*/
	public Food()
	{
		
	}

	/*commence getter/setters*/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCarbs() {
		return carbs;
	}

	public void setCarbs(double carbs) {
		this.carbs = carbs;
	}

	public double getProtein() {
		return protein;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public int getServ_size() {
		return serv_size;
	}

	public void setServ_size(int serv_size) {
		this.serv_size = serv_size;
	}

	public int getCholesterol() {
		return cholesterol;
	}

	public void setCholesterol(int cholesterol) {
		this.cholesterol = cholesterol;
	}

	public int getSodium() {
		return sodium;
	}

	public void setSodium(int sodium) {
		this.sodium = sodium;
	}
	
}