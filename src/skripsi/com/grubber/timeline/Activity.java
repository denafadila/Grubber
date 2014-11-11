package skripsi.com.grubber.timeline;

public class Activity {
	
	private String username;
	private String restaurant;
	private int rating;
	private int price;
	private String review;
	private String date;
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getRestaurant(){
		return restaurant;
	}
	
	public void setRestaurant(String restaurant){
		this.restaurant = restaurant;
	}
	
	public int getRating(){
		return rating;
	}
	
	public void setRating(int rating){
		this.rating = rating;
	}
	
	public int getPrice(){
		return price;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public String getReview(){
		return review;
	}
	
	public void setReview(String review){
		this.review = review;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
}
