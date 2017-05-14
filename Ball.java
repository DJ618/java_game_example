import java.applet.*;
import java.awt.*;
import java.util.Random;

public class Ball
{
  /*Properties of the basic ball. These are initialized in the constructor using the values read from the config.xml file*/
	protected  int pos_x;
	protected int pos_y;
	protected int radius;
	protected int first_x;
	protected int first_y;
	protected int x_speed;
	protected int y_speed;
	protected int maxspeed;
	Color color;
	int hpCheck = 0;
	int hitCount = 0;
  GameWindow gameW;
	Player player;
	public int id;

	/*constructor*/
	public Ball (int newid, int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, Player player,  GameWindow gameW)
	{
		this.radius = radius;
		pos_x = initXpos;
		pos_y = initYpos;
		first_x = initXpos;
		first_y = initYpos;
		x_speed = speedX;
		y_speed = speedY;
		maxspeed = maxBallSpeed;
		this.color = color;
		this.player = player;
		this.gameW = gameW;
		this.id = newid;
	}

	/*update ball's location based on it's speed*/
	public void move ()
	{
		pos_x += x_speed;
		pos_y += y_speed;
		isOut();
	}

	/*when the ball is hit, reset the ball location to its initial starting location*/
	public void ballWasHit ()
	{
		this.hitCount++;
		resetBallPosition();
	}

	/*check whether the player hit the ball. If so, update the player score based on the current ball speed. */
	public boolean userHit (int maus_x, int maus_y)
	{
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;
		double distance = Math.sqrt ((x*x) + (y*y));

		if (Double.compare(distance-this.radius , player.scoreConstant + Math.abs(x_speed)) <= 0.5)  {
			int additionalPoints = (int)(player.scoreConstant * Math.abs(x_speed) + player.scoreConstant);
			player.addScore(additionalPoints);

			//check if we need to give a life
			hpCheck += additionalPoints;
			if(hpCheck >= player.score2EarnLife){
				player.numLives++;
				hpCheck = 0;
			}
			player.hits++;
			return true;
		}
		else{
			player.misses++;
			return false;
		}
	}

  /*reset the ball position to its initial starting location*/
	protected void resetBallPosition()
	{
		Random rando = new Random();
		int newXSpeed = rando.nextInt((this.maxspeed + 1 + this.maxspeed)) - this.maxspeed;
		int newYSpeed = rando.nextInt((this.maxspeed + 1 + this.maxspeed)) - this.maxspeed;

		//protect against standing still
		if(newXSpeed == 0 && newYSpeed == 0){newXSpeed = 1;}
		this.x_speed = newXSpeed;
		this.y_speed = newYSpeed;
		pos_x = first_x;
		pos_y = first_y;
	}

	/*check if the ball is out of the game borders. if so, game is over!*/
	protected boolean isOut ()
	{
		if ((pos_x < gameW.x_leftout) || (pos_x > gameW.x_rightout) || (pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)) {
			resetBallPosition();

			if(player.numLives <= 0){
				player.gameIsOver();
			}else{
				player.numLives--;
			}
			return true;
		}
		else return false;
	}

	/*draw ball*/
	public void DrawBall (Graphics g)
	{
		g.setColor (color);
		g.fillOval (pos_x - radius, pos_y - radius, 2 * radius, 2 * radius);
	}
}
