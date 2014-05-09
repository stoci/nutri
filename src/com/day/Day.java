package src.com.day;
import java.util.ArrayList;
import src.com.food.*;

public class Day implements java.io.Serializable
{
	//key for LinkedHashMap of days
	private String date = "";
	//all possible meals in a day as ArrayLists of Food objects
	private ArrayList<Food> breakfast = new ArrayList<Food>();
	private ArrayList<Food> lunch = new ArrayList<Food>();
	private ArrayList<Food> snack0 = new ArrayList<Food>();
	private ArrayList<Food> dinner = new ArrayList<Food>();
	private ArrayList<Food> snack1 = new ArrayList<Food>();
	
	public ArrayList<Food> getBreakfast()
	{
		return breakfast;
	}
	public void setBreakfast(Food x)
	{
		this.breakfast.add(x);
	}
	public ArrayList<Food> getSnack0()
	{
		return snack0;
	}
	public void setSnack0(Food x)
	{
		this.snack0.add(x);
	}	
	public ArrayList<Food> getLunch()
	{
		return lunch;
	}
	public void setLunch(Food x)
	{
		this.lunch.add(x);
	}	
	public ArrayList<Food> getDinner()
	{
		return dinner;
	}
	public void setDinner(Food x)
	{
		this.dinner.add(x);
	}	
	public ArrayList<Food> getSnack1()
	{
		return snack1;
	}
	public void setSnack1(Food x)
	{
		this.snack1.add(x);
	}	
	public String getDate()
	{
		return date;
	}
	public void setDate(String x)
	{
		this.date = x;
	}
}