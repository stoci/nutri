package src.com.main;


import src.com.day.*;
import src.com.food.*;

import java.sql.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.SessionStatistics;

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
	SessionFactory factory;
	
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
		rightVBox.getChildren().addAll(actionLabel, selectActionCB,nameLabel,nameField,servLabel,servField,fatLabel,fatField,
				cholLabel,cholField,sodiumLabel,sodiumField,carbLabel,carbField,proteinLabel,proteinField,rightBtn,statusLabel);
		return rightVBox;
	}
	
	private String modifyFoodDB(String action, Food f)
	{
		Connection conn = null; Statement s = null; ResultSet rs; String m="";
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
	
	private void hibernateModify()
	{
		Session session = factory.openSession();
		
	}
}
