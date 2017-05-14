import java.awt.Color;
import java.util.Random;

public class ShrinkBall extends Ball {

	private double shrinkRate;

	public ShrinkBall(int newid, int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color,
			Player player, GameWindow gameW, int newshrinkRate) {
		super(newid, radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, player, gameW);
		this.shrinkRate = newshrinkRate;
	}

	/*check whether the player hit the ball. If so, update the player score based on the current ball speed. */
	public boolean userHit (int maus_x, int maus_y)
	{
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;
		double distance = Math.sqrt ((x*x) + (y*y));

		if (Double.compare(distance-this.radius , player.scoreConstant + Math.abs(x_speed)) <= 0.5)  {
			player.addScore ((int)(player.scoreConstant * Math.abs(x_speed) + player.scoreConstant));

			//check if we need to give a life - only the shrinkball will give a life
			if(player.getScore() % player.score2EarnLife == 0){
				player.numLives++;
			}
			player.hits++;
			this.radius = (int) (this.radius - (this.radius * shrinkRate) / 100);
			return true;
		}
		else{
			player.misses++;
			return false;
		}
	}
}
