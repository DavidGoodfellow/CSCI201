
public class Player {
	private int points;
	private String name;
	public int finalBet;
	
	public Player(String n){
		name = n;
		points = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public int getPoints(){
		return points;
	}
	
	public void setPoints(int p){
		points = p;
	}
	
	public void addPoints(int val){
		points+=val;
	}
	
	public void minusPoints(int val){
		points-=val;
	}

}
