package src.com.food;

import javax.persistence.*;

@Entity
@Table(name="nutri.Food")
public class Food
{
	/**/
	private String name = "";	//name of food
	private int carbs = 0;		//grams of carbohydrates
	private int protein = 0;	//grams of protein
	private int fat = 0;		//grams of fat
	private int serv_size = 0;	//grams per serving or quantity of item (typically 1)
	private int cholesterol = 0;//milligrams cholesterol
	private int sodium = 0;		//milligrams sodium
	
	/*constructor for properly formatted fields*/
	public Food(String name, int carbs, int protein, int fat, int serv_size,
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
		this.fat=Integer.parseInt(item[2]);
		this.cholesterol=Integer.parseInt(item[3]);
		this.sodium=Integer.parseInt(item[4]);
		this.carbs=Integer.parseInt(item[1]);
		this.protein=Integer.parseInt(item[2]);
		
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

	public int getCarbs() {
		return carbs;
	}

	public void setCarbs(int carbs) {
		this.carbs = carbs;
	}

	public int getProtein() {
		return protein;
	}

	public void setProtein(int protein) {
		this.protein = protein;
	}

	public int getFat() {
		return fat;
	}

	public void setFat(int fat) {
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