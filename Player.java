public class Player
{
	private int score;
	private boolean gameover=false;
	public int scoreConstant = 10;
	public int numLives;
	public int score2EarnLife;
	public double hits;
	public double misses;

	public Player()
	{
		score = 0;
		numLives = 1;
		hits = 0;
		misses = 0;
	}

	/* get player score*/
	public int getScore ()
	{
		return score;
	}

	/*check if the game is over*/
	public boolean isGameOver ()
	{
		return gameover;
	}

	/*update player score*/
	public void addScore (int plus)
	{
		score += plus;
	}

	/*update "game over" status*/
	public void gameIsOver ()
	{
		gameover = true;
	}
}
