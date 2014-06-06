package src.com.food;

/*Meal bean corresponds to entities in meal table*/
public class Meal
{
	private String date,type,name;
	int serv_size;

	public Meal(String date, String type, int serv_size, String name)
	{
		super();
		this.date = date;
		this.type = type.toLowerCase();
		this.serv_size = serv_size;
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getServ_size() {
		return serv_size;
	}

	public void setServ_size(int serv_size) {
		this.serv_size = serv_size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
