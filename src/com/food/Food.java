package src.com.food;

public class Food implements java.io.Serializable
{
	private double carbs = 0.0;		//grams of carbohydrates
	private double protein = 0.0;	//grams of protein
	private double fat = 0.0;		//grams of fat
	private String name = "";		//name of food
	
	//constructor for initializing instance variables
	public Food(String n, double c, double p, double f)
	{
		this.name=n;
		this.carbs=c;
		this.protein=p;
		this.fat=f;
	}
	public Food(String [] item)
	{
		this.name=item[0];
		this.carbs=Double.parseDouble(item[1]);
		this.protein=Double.parseDouble(item[2]);
		this.fat=Double.parseDouble(item[3]);
	}
	
	public double getCarbs()
	{
		return this.carbs;
	}
	public double getProtein()
	{
		return this.protein;
	}
	public double getFat()
	{
		return this.fat;
	}
	public String getName()
	{
		return this.name;
	}
	public void setCarbs(double c)
	{
		this.carbs=c;
	}
	public void setProtein(double p)
	{
		this.protein=p;
	}
	public void setFat(double f)
	{
		this.fat=f;
	}
	public final void setName(String n)
	{
		this.name=n;
	}
}