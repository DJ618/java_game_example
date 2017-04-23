import java.awt.Color;

public class BounceBall extends Ball {
	
	private int bounceCount;

	public BounceBall(int newid, int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color,
			Player player, GameWindow gameW, int initbounceCount) {
		super(newid, radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, player, gameW);
		this.bounceCount = initbounceCount;
	}

	/*check if the ball is out of the game borders. if so, game is over!*/ 
	protected boolean isOut ()
	{
		if ((pos_x < gameW.x_leftout) || (pos_x > gameW.x_rightout) || (pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)) {	
			//decremnt bounceCount
			bounceCount--;
			//now check if we are out of bounces to perform
			if(bounceCount > 0){
				//invert direction based on which bounds was met (top/bot or sides)
				if((pos_x < gameW.x_leftout) || (pos_x > gameW.x_rightout)){this.x_speed = -this.x_speed;}
				if((pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)){this.y_speed = -this.y_speed;}
			}else{
				resetBallPosition();
				if(player.numLives <= 0){
					player.gameIsOver();
				}else{
					player.numLives--;
				}
			}
			return true;
		}	
		else return false;
	}
}
