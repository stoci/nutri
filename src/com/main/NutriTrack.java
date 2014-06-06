package src.com.main;


import src.com.day.*;
import src.com.food.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.SessionStatistics;
import java.util.Date;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class NutriTrack extends Application
{
	// key = date "xx/xx/xxxx" while value is day object
	static LinkedHashMap<String,Day> calendar_db = new LinkedHashMap<String,Day>();
	// key = name while value is food object
	static HashMap<String,Food> food_db = new HashMap<String,Food>();
	SessionFactory factory;
	/*database interaction objects*/
	Connection conn = null; Statement s = null; ResultSet rs=null; 
	ArrayList<String> result=new ArrayList<String>();
	
	static public void main(String [] args) throws Exception
	{
		launch(args);
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
		/*get today's date in US format*/
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");


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
				// Get properly formatted date string for mySQL YYYY-MM-DD
				String d = dateField.getText();
				if(d.length()!=10)statusLabel.setText("Date format MM/DD/YYYY");
				else if(servField.getText().length()==0)statusLabel.setText("Serving size empty");
				else	
				{
					// extract proper date String
					d = d.substring(6, d.length())+"-"+d.substring(0, 2)+"-"+d.substring(3, 5);
					// Make call to db
					String s = modifyFoodDB("add meal",new Meal(d,typeCB.getValue(),
							Integer.parseInt(servField.getText()),nameCB.getValue()));
					statusLabel.setText(s);
				}

				
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
	
	private String modifyFoodDB(String action, Object o)
	{
		String m="";
		/*try to connect to mysql DB*/
		try
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/nutri","root","password");
			s = conn.createStatement();
			switch(action.toLowerCase())
			{
				/*add new food to food table*/
				case "add":
					s.executeUpdate("INSERT INTO food(name,serv_size,fat,cholesterol,sodium,carbs,protein) "
							+ "VALUES("+"'"+((Food)o).getName()+"'"+","+((Food) o).getServ_size()+","
							+((Food) o).getFat()+","+((Food) o).getCholesterol()
							+","+((Food) o).getSodium()+","+((Food) o).getCarbs()+","+((Food) o).getProtein()+");");
					m="Success";modifyFoodDB("select name",null);break;
				case "update":
					break;
				case "delete":
					break;
				/*retrieves name column from food table*/
				case "select name":
					rs = s.executeQuery("SELECT name FROM food;");
					while(rs.next()!=false)
						result.add(rs.getString("name"));
					m="Success";break;
				/*add new meal*/
				case "add meal":
					rs = s.executeQuery("SELECT food_id FROM food WHERE name='"+((Meal)o).getName()+"';");
					rs.next();
					s.executeUpdate("INSERT INTO meal(meal_date,food_id,meal_type,num_serv) "
							+ "VALUES("+"'"+((Meal)o).getDate()+"',"+rs.getString(1)+","
							+"'"+((Meal)o).getType()+"',"+((Meal)o).getServ_size()+");");
					m="Success";break;
			}
			/*rs = s.executeQuery("SELECT * FROM food");
			while(rs.next()!=false)
				System.out.println(rs.getString("name"));*/
			
			conn.close();s.close(); if(rs!=null)rs.close();
		}
		catch(Exception e)
		{
			m="Failed";
			e.printStackTrace();
		}
		finally{return m;}
	}
	
	private void hibernateModify()
	{
		Session session = factory.openSession();
		
	}
}
