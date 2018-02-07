

public class Tile {
	public String question;
	public String answers;
	public int value;
	public String category;
	
	private boolean played;
	
	public Tile(){
		played = false;
	}
	
	//GETTERS & SETTERS
	public boolean isPlayed(){
		return played;
	}
	
	public void played(){
		played = true;
	}
	
	public void restartTile(){
		played = false;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getAnswer(){
		return answers;
	}
	
	public int getValue(){
		return value;
	}
}
