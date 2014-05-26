package src.com.main;


import src.com.day.*;
import src.com.food.*;

import java.sql.*;
import java.util.*;
import java.io.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
			//System.out.printf("%-25s %10.2f %10.2f %10.2f %n", entry.getKey(), 
			//entry.getValue().getCarbs(), entry.getValue().getProtein(),
			//entry.getValue().getFat());
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
		//	System.out.print(litr.next().getName()+" ");
		}

		litr = lunch.listIterator();
		System.out.printf("%-15s","\nLunch: ");
		while(litr.hasNext())
		{
		//	System.out.print(litr.next().getName()+" ");
		}

		litr = snack0.listIterator();
		System.out.printf("%-15s","\nSnack0: ");
		while(litr.hasNext())
		{
		//	System.out.print(litr.next().getName()+" ");
		}
		
		litr = dinner.listIterator();
		System.out.printf("%-15s","\nDinner: ");
		while(litr.hasNext())
		{
		//	System.out.print(litr.next().getName()+" ");
		}

		litr = snack1.listIterator();
		System.out.printf("%-15s","\nSnack 1: ");
		while(litr.hasNext())
		{
		//	System.out.print(litr.next().getName()+" ");
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
		VBox rightBox = configureRight();

		
		
		
		borderPane.setLeft(leftVBox); borderPane.setRight(rightBox);
		Scene scene = new Scene(borderPane, 1280, 720);
		primaryStage.setScene(scene); primaryStage.setTitle("NutriTrack v0.1");
		primaryStage.show();
	}
	
	private VBox configureRight()
	{
		/*set up two column layout*/
		VBox rightVBox = new VBox(); //rightFPane.setPrefWrapLength(190); rightFPane.setVgap(2);rightFPane.setHgap(2);
		/*database actions list*/
		ObservableList<String> actions = FXCollections.observableArrayList("Add","Update","Delete");
		/*combobox containing list of database actions -- need event listener?*/
		ComboBox<String> selectActionCB = new ComboBox<String>(); selectActionCB.setItems(actions);
		/*all necessary labels*/
		Label nameLabel = new Label("Name**: ");Label actionLabel = new Label("Select action**: ");
		Label servLabel = new Label("Serving Size: ");Label fatLabel = new Label("Fat: ");
		Label cholLabel = new Label("Cholesterol: ");Label sodiumLabel = new Label("Sodium: ");
		Label carbLabel = new Label("Carbohydrates: ");Label proteinLabel = new Label("Protein: ");
		Label statusLabel = new Label(); statusLabel.setStyle("-fx-text-fill: #ff6347");
		/*all necessary textfields*/
		TextField nameField = new TextField();TextField servField = new TextField();
		TextField fatField = new TextField();TextField cholField = new TextField();
		TextField sodiumField = new TextField();TextField carbField = new TextField();
		TextField proteinField = new TextField();
		/*right submit button*/
		Button rightBtn = new Button("Submit"); //rightBox.getChildren().add(rightBtn);
		/*event listener*/
		rightBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) 
			{
				/*make sure action selected in combobox and name filled*/
				if(selectActionCB.getValue()==null || nameField.getText().equals(""))statusLabel.setText("A required field is empty.");
				/*pass data to modifyFoodDB method*/
				else 
				{
					statusLabel.setText("");
					Food f = new Food(nameField.getText(),servField.getText(),fatField.getText(),cholField.getText(),sodiumField.getText(),
							carbField.getText(),proteinField.getText());
					String s = modifyFoodDB(selectActionCB.getValue(),nameField.getText(),servField.getText(),fatField.getText(),cholField.getText(),sodiumField.getText(),
							carbField.getText(),proteinField.getText());
					statusLabel.setText(s);
				}
			}
		});
		rightVBox.getChildren().addAll(actionLabel, selectActionCB,nameLabel,nameField,servLabel,servField,fatLabel,fatField,
				cholLabel,cholField,sodiumLabel,sodiumField,carbLabel,carbField,proteinLabel,proteinField,rightBtn,statusLabel);
		return rightVBox;
	}
	
	private String modifyFoodDB(String...arr)
	{
		Connection conn = null; Statement s = null; ResultSet rs; String m="";
		/*try to connect to mysql DB*/
		try
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/nutri","root","password");
			s = conn.createStatement();
			switch(arr[0].toLowerCase())
			{
				case "add":
					s.executeUpdate("INSERT INTO food(name,serv_size,fat,cholesterol,sodium,carbs,protein) "
							+ "VALUES("+"'"+arr[1]+"'"+","+arr[2]+","+arr[3]+","+arr[4]+","+arr[5]+","+arr[6]+","+arr[7]+");");
					m="Success";
					break;
				case "update":
					break;
				case "delete":
					break;				
			}
			//rs = s.executeQuery("SELECT * FROM food");
			//while(rs.next()!=false)
				//System.out.println(rs.getString("name"));
			conn.close();s.close();
		}
		catch(Exception e)
		{
			m="Failed";
			e.printStackTrace();
		}
		finally{return m;}
	}
}
