import java.util.*;

public class GameSystem {
	
	//------------------------------
	// 	      DATA
	//------------------------------
	
	//map for grid
	private Map<String, Integer> categoryCol;
	private Map<Integer, Integer> valueRow;
	
	//ultimate map to the tiles (rc,Tile)
	private Map<String, Tile> grid;
	
	private List<String> categories;
	private List<Integer> values;
	
	private boolean enteredFJ;
	//for streaming
	private int questionCount;
	//for playing
	private int questionsAsked;
	private Scanner s;
	
	//constructor
	public GameSystem(){
		categoryCol = new HashMap<String, Integer>();
		valueRow = new HashMap<Integer, Integer>();
		grid = new HashMap<String, Tile>();
		categories = new ArrayList<String>();
		values = new ArrayList<Integer>();
		enteredFJ = false;
		questionCount = 0;
		questionsAsked = 0;
		s = new Scanner(System.in);
	}
	
	//------------------------------
	// GETTERS & SETTERS
	//------------------------------
	
	public int getQCount(){
		return questionCount;
	}
	public boolean inlcudedFQ(){
		return enteredFJ;
	}
	
	public boolean isComplete(){
		if(questionsAsked == 25)
			return true;
		return false;
	}
	
	public boolean fiveAndFive(){
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				String rc = Integer.toString(i) + Integer.toString(j);
				if(grid.get(rc) == null){
					return false;
				}
			}
		}
		return true;
	}
	
	//-------------------------------
	//    METHODS FOR STREAMING
	//-------------------------------
	
	public boolean addCategory(String cat, int col){
		if(categories.size() != 0){
			for(int i = 0; i < categories.size(); i++){
				if((categories.get(i).toLowerCase()).equalsIgnoreCase(cat.toLowerCase())){
					return false;
				}
			}
		}
		categoryCol.put(cat.toLowerCase(), col);
		categories.add(cat);
		return true;
	}
	
	public boolean addValue(int val, int row){
		if(values.size() != 0){
			for(int i = 0; i < values.size(); i++){
				if(values.get(i) == val){
					return false;
				}
			}
		}
		valueRow.put(val, row);
		values.add(val);
		return true;
	}
	
	public boolean questionInput(String line){
		questionCount++;
		
		String input[] = line.split("::");
		
		//checking for Final Jeopardy Question first because different
		if(input[1].equalsIgnoreCase("FJ")){
			if(enteredFJ == true){
				System.out.println("Error: More than one Final Question.");
				return false;
			}
			else if(input.length != 4){
				System.out.println("Error: Wrong amount of parameters for Final Question.");
				return false;
			}
			else{
				Tile t = new Tile();
				t.question = input[2];
				t.answers = input[3];
				t.category = "FJ";
				
				grid.put("FJ", t);
				enteredFJ = true;
				return true;
			}
		}
		
		//check input for a regular question entry
		else if(input.length != 5){
			//System.out.println(input[4]);
			System.out.println("Error: Not the correct amount of parameters for a question.");
			return false;
		}
		
		Tile t = new Tile();
		int r = -1;//row for tile
		int c = -1;//col for title
		
		//checking category
		for(int i = 0; i < categories.size(); i++){
			//System.out.println(input[i] + " " + categories.get(i));
			if((categories.get(i).toLowerCase()).equalsIgnoreCase(input[1].toLowerCase())){
				c = categoryCol.get(input[1].toLowerCase());
				t.category = input[1];
				if(t.category.equalsIgnoreCase("FJ")){
					c = 6;
					r=6;//random value for map to bypass FJ on if below..
				}
			}
		}
		
		if(c == -1){
			System.out.println("Error: Question's category does not match");
			return false;
		}

		//checking value
		for(int i = 0; i < values.size(); i++){
			if(Integer.parseInt(input[2]) == values.get(i)){
				r = valueRow.get(Integer.parseInt(input[2]));
				t.value = Integer.parseInt(input[2]);
			}
		}
		if(r == -1){
			System.out.println("Error: Question's point value does not match");
			return false;
		}
		
		//adding questions and answers
		t.question = input[3];
		t.answers = input[4];
		
		//creating map spot for tile
		String r1 = Integer.toString(r);
		String c1 = Integer.toString(c);
		String rc = r1+c1;
		grid.put(rc, t);
		
		return true;
	}
	
	//-------------------------------
	//    METHODS FOR PLAYING
	//-------------------------------
	
	public int chooseCategory(String cat){
		for(int i = 0; i < categories.size(); i++){
			if((categories.get(i)).equalsIgnoreCase(cat)){
				return categoryCol.get(cat.toLowerCase());
			}
		}
		return -1;
	}
	
	public int chooseValue(int val){
		for(int i = 0; i < values.size(); i++){
			if(values.get(i) == val){
				return valueRow.get(val);
			}
		}
		return -1;
	}

	public boolean isFlipped(String rc){
		return grid.get(rc).isPlayed();
	}
	
	public void flip(String rc){
		grid.get(rc).played();
	}
	
	//called when a valid unflipped chip is called
	public void question(String rc){
		System.out.println(grid.get(rc).getQuestion());
		System.out.println("Please enter your answer:");
	}
	public boolean answer(String rc, String a){
		boolean prefix = false;
		boolean word = false;
		
		//who, what, when, where, why
		List<String> fiveW = new ArrayList<String>();
		fiveW.add("who");
		fiveW.add("what");
		fiveW.add("when");
		fiveW.add("where");
		fiveW.add("why");
		
		for(int i = 0; i < fiveW.size(); i++){
			if((a.toLowerCase()).startsWith((fiveW.get(i)).toLowerCase())){
				prefix = true;
			}
		}
		if((a.toLowerCase()).contains(grid.get(rc).getAnswer().toLowerCase())){
			word = true;
		}
		questionsAsked++;
		
		if(word == true && prefix == true){
			return true;
		}
		else if(word == true){
			word = false;
			System.out.println("Answers must be phrased as questions. Enter your one retry.");

			String str = s.nextLine();
			
			for(int i = 0; i < fiveW.size(); i++){
				if(str.startsWith(fiveW.get(i))){
					prefix = true;
				}
			}
			if(str.contains(grid.get(rc).getAnswer())){
				word = true;
			}
			if(word == true && prefix == true){
				return true;
			}
		}
		return false;
	}
	
	public void finalQ(){
		System.out.println(grid.get("FJ").getQuestion());
	}
	
	public void closeScanner(){
		s.close();
	}
	
	
	//------------------------
	//      RESTART METHOD
	//------------------------
	
	public void restart(){
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				String rc = Integer.toString(i) + Integer.toString(j);
				grid.get(rc).restartTile();
			}
		}
	}
	
	
}
