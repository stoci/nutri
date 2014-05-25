package src.com.main;


import src.com.day.*;
import src.com.food.*;

import java.sql.*;
import java.util.*;
import java.io.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class NutriTrack extends Application
{
	// key = date "xx/xx/xxxx" while value is day object
	static LinkedHashMap<String,Day> calendar_db = new LinkedHashMap<String,Day>();
	// key = name while value is food object
	static HashMap<String,Food> food_db = new HashMap<String,Food>();
	
	static public void main(String [] args) throws Exception
	{
		launch(args);
		System.out.println("NutriTrack v0.1 Developed by stoci");
		//load serialized DBs from files into program -- eventually use MySQL
		NutriTrack.initializeFoodDB();
		NutriTrack.initializeCalendarDB();
		Connection conn = null; Statement s = null; ResultSet rs = null;
		
		/*try to connect to DB -- future use*/
		try
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/crashcourse","root","password");
			s = conn.createStatement();
			rs = s.executeQuery("SELECT prod_name,prod_price FROM crashcourse.products");
			//while(rs.next()!=false)
				//System.out.println(rs.getString("prod_name"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{conn.close();s.close();rs.close();}
		
		/*processes commands in infinite loop*/
		while(true)
		{
			System.out.print(">>> ");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String input = bufferedReader.readLine();
			//String input = System.console().readLine();
			String [] commands = input.trim().split(" ");
			
			/*exit command breaks while loop*/
			if(input.trim().equalsIgnoreCase("exit")) break;
			/*prints out all food currently in database file*/
			else if(commands[0].equalsIgnoreCase("print") && commands[1].equalsIgnoreCase("food"))
			{
				printFoodDB();
			}
			/*print out all meals in a specific day -- input format: xx/xx/xxxx */
			else if(commands[0].equalsIgnoreCase("print") && commands[1].equalsIgnoreCase("day") 
			&& commands[2].length()==10)
			{
				Day d = calendar_db.get(commands[2]);
				if(d!=null)
					printDay(d);
				else
					System.out.println("That date doesn't exist.");
			}
			/*print out every day including meals in database*/
			else if(commands[0].equalsIgnoreCase("print") && commands[1].equalsIgnoreCase("day") 
			&& commands[2].equalsIgnoreCase("all"))
			{
				printDayAll();
			}
			/*add new food to database*/
			else if(commands[0].equalsIgnoreCase("addf")&& commands[1]!=null
			&&commands[2]!=null && commands[3]!=null && commands[4]!=null)
			{
				food_db.put(commands[1],new Food(commands[1],Double.parseDouble(commands[2]),
				Double.parseDouble(commands[3]),Double.parseDouble(commands[4]),Double.parseDouble(commands[5])));
			}
			/*add specific food[3] to specific date(xx/xx/xxxx)[1] and specific meal(lunch)[2]*/
			else if(commands[0].equalsIgnoreCase("addd")&& commands[1].length()==10
			&&commands[2]!=null && commands[3]!=null)
			{
				//check to see if name of food in db
				Food f = food_db.get(commands[3]);
				if(f!=null)
				{
					//check to see if day already in calendar_db
					Day d = calendar_db.get(commands[1]);
					if(d!=null)
					{
						System.out.println("Adding to calendar_db");
						addFood2Day(f,d,commands[2]);
					}
					//date not in calendar_db
					else
					{
						Day d1 = new Day();
						d1.setDate(commands[1]);
						//add date to LinkedHashMap calendar_db
						calendar_db.put(commands[1],d1);
						addFood2Day(f,calendar_db.get(commands[1]),commands[2]);
					}
				}
				//food not in database
				else System.out.println("Please use addf command to add this food!");
			}
			/*print out list of valid commands*/
			else if(commands[0].equalsIgnoreCase("help"))
			{
				System.out.println(" print food\n addf [name_of_food carbs protein fat grams/serving]\n addd [date meal name_of_food]\n print day [date]\n print day all\n exit");
			}
			/*command doesn't exist*/
			else
				System.out.println("Unrecognized command. Type help to see valid commands.");
		}
		/*update database files with any changes*/
		updateCalendarDB();
		updateFoodDB();
		
	}
	
	/*create serialized input stream and read the HashMap containing all stored Foods*/
	private static void initializeFoodDB()
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("food_db.txt"));
			food_db = (HashMap<String,Food>) ois.readObject();
			
			ois.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/*create serialized input stream and read the LinkedHashMap containing all stored Days*/
	private static void initializeCalendarDB()
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("calendar_db.txt"));
			calendar_db = (LinkedHashMap<String,Day>) ois.readObject();
			
			//System.out.println(calendar_db.get("11/12/1111").getLunch().get(0).getName());
			ois.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	/*print to console ALL Food objects*/
	private static void printFoodDB()
	{
		System.out.printf("%-25s %10s %10s %10s %n", "Name", "C", "P", "F");
		for ( Map.Entry<String,Food> entry : food_db.entrySet() )
		{
			System.out.printf("%-25s %10.2f %10.2f %10.2f %n", entry.getKey(), 
			entry.getValue().getCarbs(), entry.getValue().getProtein(),
			entry.getValue().getFat());
		}
	}
	/*print the specified Day object including meals to console*/
	private static void printDay(Day day)
	{
		ArrayList<Food> breakfast = day.getBreakfast();
		ArrayList<Food> lunch = day.getLunch();
		ArrayList<Food> snack0 = day.getSnack0();
		ArrayList<Food> dinner = day.getDinner();
		ArrayList<Food> snack1 = day.getSnack1();
		
		ListIterator<Food> litr = breakfast.listIterator();

		System.out.println(day.getDate());
		System.out.printf("%-15s","Breakfast: ");
		while(litr.hasNext())
		{
			System.out.print(litr.next().getName()+" ");
		}

		litr = lunch.listIterator();
		System.out.printf("%-15s","\nLunch: ");
		while(litr.hasNext())
		{
			System.out.print(litr.next().getName()+" ");
		}

		litr = snack0.listIterator();
		System.out.printf("%-15s","\nSnack0: ");
		while(litr.hasNext())
		{
			System.out.print(litr.next().getName()+" ");
		}
		
		litr = dinner.listIterator();
		System.out.printf("%-15s","\nDinner: ");
		while(litr.hasNext())
		{
			System.out.print(litr.next().getName()+" ");
		}

		litr = snack1.listIterator();
		System.out.printf("%-15s","\nSnack 1: ");
		while(litr.hasNext())
		{
			System.out.print(litr.next().getName()+" ");
		}
		System.out.println();		
	}
	/*print ALL Days in database including meals to console*/
	private static void printDayAll()
	{
		for(Map.Entry<String,Day> entry : calendar_db.entrySet())
			printDay(entry.getValue());
	}
	/*create output stream and serialize the food_db HashMap for storage*/
	private static void updateFoodDB()
	{		
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("food_db.txt"));
			oos.writeObject(NutriTrack.food_db);
			
			oos.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	/*create output stream and serialize the calendar_db LinkedHashMap for storage*/
	private static void updateCalendarDB()
	{		
		try
		{
			//add Day to calendar_db.txt
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("calendar_db.txt"));
			oos.writeObject(NutriTrack.calendar_db);
			oos.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	/*add specific Food to specific meal in a Day object*/
	private static void addFood2Day(Food f, Day d, String meal)
	{
		if(meal.equalsIgnoreCase("breakfast"))
			calendar_db.get(d.getDate()).setBreakfast(f);
		if(meal.equalsIgnoreCase("lunch"))
			calendar_db.get(d.getDate()).setLunch(f);
		if(meal.equalsIgnoreCase("snack0"))
			calendar_db.get(d.getDate()).setSnack0(f);
		if(meal.equalsIgnoreCase("dinner"))
			calendar_db.get(d.getDate()).setDinner(f);
		if(meal.equalsIgnoreCase("snack1"))
			calendar_db.get(d.getDate()).setSnack1(f);
	}

	/*start JavaFX GUI*/
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		/*overall window layout: top,left,center,right,bottom*/
		BorderPane borderPane = new BorderPane();
		/*layout of left which is for adding food eaten to specific day->meal*/
		VBox leftVBox= new VBox();
		/*layout of right which is for RUD'ing food in the database*/
		FlowPane rightFPane = new FlowPane(); rightFPane.setPrefWrapLength(190); rightFPane.setVgap(2);rightFPane.setHgap(2);
		/*element included in rightFpane*/
		ObservableList<String> actions = FXCollections.observableArrayList("Add","Update","Delete");
		ComboBox<String> selectActionCB = new ComboBox<String>(); selectActionCB.setItems(actions);
		Label nameLabel = new Label("Name");Label actionLabel = new Label("Select action: ");
		TextField nameField = new TextField();
		rightFPane.getChildren().addAll(actionLabel, selectActionCB,nameLabel,nameField);
		
		
		borderPane.setLeft(leftVBox); borderPane.setRight(rightFPane);
		Scene scene = new Scene(borderPane, 1280, 720);
		primaryStage.setScene(scene); primaryStage.setTitle("NutriTrack v0.1");
		primaryStage.show();
	}
}
