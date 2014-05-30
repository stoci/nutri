package src.com.main;


import src.com.day.*;
import src.com.food.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.io.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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
	/*database interaction objects*/
	Connection conn = null; Statement s = null; ResultSet rs=null; 
	ArrayList<String> result=new ArrayList<String>();
	
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
		VBox leftVBox= configureLeft();
		/*layout of right which is for RUD'ing food in the database*/
		VBox rightBox = configureRight();
		/*top has menubar*/
		MenuBar menuBar = configureMenuBar();

		
		
		
		borderPane.setLeft(leftVBox); borderPane.setRight(rightBox);
		borderPane.setTop(menuBar);
		Scene scene = new Scene(borderPane, 1280, 720);
		primaryStage.setScene(scene); primaryStage.setTitle("NutriTrack v0.1");
		primaryStage.show();
	}
	
	/*standard menubar configuration*/
	private MenuBar configureMenuBar()
	{
		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("File");
		Menu menuAbout = new Menu("About");
		menuBar.getMenus().addAll(menuFile,menuAbout);
		return menuBar;
	}
	
	/*allows adding food to meal*/
	private VBox configureLeft()
	{
		/*set up one column layout*/
		VBox leftVBox = new VBox(5);
		modifyFoodDB("select name",null);
		/*foods in database list*/
		ObservableList<String> foods = FXCollections.observableArrayList(result);
		/*combobox containing all food objects*/
		ComboBox<String> nameCB = new ComboBox<String>(); nameCB.setItems(foods);
		/*get today's date*/
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");


		/*combobox containing types of meals -- breakfast, lunch, dinner, snack0, snack1*/
		ObservableList<String> types = FXCollections.observableArrayList("Breakfast","Lunch","Snack0","Dinner","Snack1");
		ComboBox<String> typeCB = new ComboBox<String>(); typeCB.setItems(types);
		/*all necessary labels*/
		Label titleLabel = new Label("Interact with meal table\n"); titleLabel.setStyle("-fx-font-size: 15px;");
		Label nameLabel = new Label("Name**: ");Label dateLabel = new Label("Date**: ");
		Label servLabel = new Label("Serving Size: ");Label typeLabel = new Label("Meal: ");
		Label statusLabel = new Label(); statusLabel.setStyle("-fx-text-fill: #ff6347");
		/*all necessary textfields*/
		TextField dateField = new TextField(sdf.format(date));TextField servField = new TextField();
		/*right submit button*/
		Button leftBtn = new Button("Submit"); 
		/*event listener*/
		leftBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) 
			{
				
			}
		});
		leftVBox.setPadding(new Insets(0,10,0,10));
		leftVBox.getChildren().addAll(titleLabel,dateLabel,dateField,typeLabel,typeCB,servLabel,
				servField,nameLabel,nameCB,leftBtn,statusLabel);
		return leftVBox;
	}
	
	/*allows addition/update/remove Food from DB*/
	private VBox configureRight()
	{
		/*set up one column layout*/
		VBox rightVBox = new VBox(5); //rightFPane.setPrefWrapLength(190); rightFPane.setVgap(2);rightFPane.setHgap(2);
		/*database actions list*/
		ObservableList<String> actions = FXCollections.observableArrayList("Add","Update","Delete");
		/*combobox containing list of database actions -- need event listener?*/
		ComboBox<String> selectActionCB = new ComboBox<String>(); selectActionCB.setItems(actions);
		/*all necessary labels*/
		Label titleLabel = new Label("Interact with food table\n"); titleLabel.setStyle("-fx-font-size: 15px;");
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
		Button rightBtn = new Button("Submit"); 
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
					String s = modifyFoodDB(selectActionCB.getValue(),f);
					statusLabel.setText(s);
				}
			}
		});
		rightVBox.setPadding(new Insets(0,10,0,10));
		rightVBox.getChildren().addAll(titleLabel,actionLabel, selectActionCB,nameLabel,nameField,servLabel,servField,fatLabel,fatField,
				cholLabel,cholField,sodiumLabel,sodiumField,carbLabel,carbField,proteinLabel,proteinField,rightBtn,statusLabel);
		return rightVBox;
	}
	
	private String modifyFoodDB(String action, Food f)
	{
		String m="";
		/*try to connect to mysql DB*/
		try
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/nutri","root","password");
			s = conn.createStatement();
			switch(action.toLowerCase())
			{
				case "add":
					s.executeUpdate("INSERT INTO food(name,serv_size,fat,cholesterol,sodium,carbs,protein) "
							+ "VALUES("+"'"+f.getName()+"'"+","+f.getServ_size()+","+f.getFat()+","+f.getCholesterol()
							+","+f.getSodium()+","+f.getCarbs()+","+f.getProtein()+");");
					m="Success";modifyFoodDB("select name",null);break;
				case "update":
					break;
				case "delete":
					break;
				case "select name":
					rs = s.executeQuery("SELECT name FROM food;");
					m="Success";break;
			}
			/*rs = s.executeQuery("SELECT * FROM food");
			while(rs.next()!=false)
				System.out.println(rs.getString("name"));*/
			while(rs.next()!=false)
					result.add(rs.getString("name"));
			
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
