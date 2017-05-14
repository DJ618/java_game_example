import java.awt.*;
import java.util.*;
import java.applet.*;
import java.awt.event.MouseEvent;
import javax.swing.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main extends Applet implements Runnable
{
  private int numBalls;
  private int refreshrate = 15;
	private boolean isStoped = true;
  Font f = new Font ("Arial", Font.BOLD, 18);
  int mouseClicks = 0;
	private Player player;
	Thread th;
  Cursor c;
  private GameWindow gwindow;
	private Image dbImage;
	private Graphics dbg;
	private Ball[] ballsArray;

	class HandleMouse extends MouseInputAdapter
	{
  	public HandleMouse()
  	{
      addMouseListener(this);
    }

    	public void mouseClicked(MouseEvent e)
    	{
    		mouseClicks++;
      	if (!isStoped) {
      		for(int i = 0; i < numBalls; i++){
      			if(ballsArray[i].userHit(e.getX(), e.getY())){
      				ballsArray[i].ballWasHit();
      			}
      		}
			  } else if (isStoped && e.getClickCount() == 2) {
  				isStoped = false;
  				init ();
			  }
    	}

    	public void mouseReleased(MouseEvent e)
    	{
    	}

    	public void RegisterHandler()
    	{
    	}
    }
	HandleMouse hm = new HandleMouse();

	//JSON reader
	public void JSONReader() throws IOException, ParseException
	{
		String filePath = "config.JSON";
		FileReader reader = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		//numballs from jsonObject
		this.numBalls = Integer.parseInt((String) jsonObject.get("numBalls"));
		//gameWindow
		JSONObject innerObject = (JSONObject)jsonObject.get("GameWindow");
		this.gwindow.x_leftout = Integer.parseInt((String)innerObject.get("x_leftout"));
		this.gwindow.x_rightout = Integer.parseInt((String)innerObject.get("x_rightout"));
		this.gwindow.y_upout = Integer.parseInt((String)innerObject.get("y_upout"));
		this.gwindow.y_downout = Integer.parseInt((String)innerObject.get("y_downout"));
		//player
		JSONObject innerPObject = (JSONObject)jsonObject.get("Player");
		this.player.numLives = Integer.parseInt((String)innerPObject.get("numLives"));
		this.player.score2EarnLife = Integer.parseInt((String)innerPObject.get("score2EarnLife"));
		//retreive ball info
		this.ballsArray = new Ball[this.numBalls];
		JSONArray innerBObjectArray = (JSONArray)jsonObject.get("Ball");
		for(int i = 0; i < this.numBalls; i++){
			//grab inner ball item
			JSONObject newBallJsonObject = (JSONObject)innerBObjectArray.get(i);
			//get all attributes that are shared among all ball types:
			//id
			int newBallid = Integer.parseInt((String)newBallJsonObject.get("id"));
			//type
			String newBallType = (String)newBallJsonObject.get("type");
			//radius
			int newBallRadius = Integer.parseInt((String)newBallJsonObject.get("radius"));
			//initXpos
			int newBallinitXpos = Integer.parseInt((String)newBallJsonObject.get("initXpos"));
			//initYpos
			int newBallinitYpos = Integer.parseInt((String)newBallJsonObject.get("initYpos"));
			//speedX
			int newBallspeedX = Integer.parseInt((String)newBallJsonObject.get("speedX"));
			//speedY
			int newBallspeedY = Integer.parseInt((String)newBallJsonObject.get("speedY"));
			//maxBallSpeed
			int newBallmaxBallSpeed = Integer.parseInt((String)newBallJsonObject.get("maxBallSpeed"));
			//color {array}
			JSONArray colorArray = (JSONArray) newBallJsonObject.get("color");
			Color newBallColor = new Color(Integer.parseInt((String)colorArray.get(0)),Integer.parseInt((String)colorArray.get(1)),Integer.parseInt((String)colorArray.get(2)));

			switch(newBallType){
			case "basicball": this.ballsArray[i] = new basicBall(newBallid,newBallRadius,newBallinitXpos,newBallinitYpos,newBallspeedX,newBallspeedY,newBallmaxBallSpeed,newBallColor,this.player,this.gwindow);break;
			case "bounceball": this.ballsArray[i] = new BounceBall(newBallid,newBallRadius,newBallinitXpos,newBallinitYpos,newBallspeedX,newBallspeedY,newBallmaxBallSpeed,newBallColor,this.player,this.gwindow,Integer.parseInt((String)newBallJsonObject.get("bounceCount")));break;
			case "shrinkball": this.ballsArray[i] = new ShrinkBall(newBallid,newBallRadius,newBallinitXpos,newBallinitYpos,newBallspeedX,newBallspeedY,newBallmaxBallSpeed,newBallColor,this.player,this.gwindow,Integer.parseInt((String)newBallJsonObject.get("shrinkRate")));break;
			}
		}//end of for loop
	}//end of json parser

	/*initialize the game*/
	public void init ()
	{
		player = new Player ();
		gwindow = new GameWindow(0,800,0,1000);

		//reads info from JSON doc
		try {
			this.JSONReader();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c = new Cursor (Cursor.CROSSHAIR_CURSOR);
		this.setCursor (c);

		setBackground (Color.black);
		setFont (f);

		if (getParameter ("refreshrate") != null) {
			refreshrate = Integer.parseInt(getParameter("refreshrate"));
		}
		else refreshrate = 15;

		this.setSize(gwindow.x_rightout, gwindow.y_downout); //set the size of the applet window.
	}

	/*start the applet thread and start animating*/
	public void start ()
	{
		if (th==null){
			th = new Thread (this);
		}
		th.start ();
	}

	/*stop the thread*/
	public void stop ()
	{
		th=null;
	}

	public void run ()
	{
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while (true) {
			if (!isStoped) {
				for(int i = 0; i < this.ballsArray.length; i++){
					this.ballsArray[i].move();
				}
			}
			repaint();
			try {
				Thread.sleep (refreshrate);
			}
			catch (InterruptedException ex) {
			}
		}
	}

	public void paint (Graphics g)
	{
		if (!player.isGameOver()) {
			g.setColor (Color.yellow);
			g.drawString ("Score: " + player.getScore(), 10, 40);
			g.drawString("Lives: " + this.player.numLives , 10, 70);

			for(int i = 0; i < this.ballsArray.length; i++){
				this.ballsArray[i].DrawBall(g);
			}

			if (isStoped) {
				g.setColor (Color.yellow);
				g.drawString ("Doubleclick on Applet to start Game!", 40, 200);
			}
		}
		/*if the game is over (i.e., the ball is out) display player's score*/
		else {
			int hits = 0;
			String mostHitId = "";
			double accuracy = (player.hits / (player.hits + player.misses)*100);
			for(int i = 0; i < numBalls; i++){
				if(ballsArray[i].hitCount > hits){
					mostHitId = ballsArray[i].getClass().toString();
					hits = ballsArray[i].hitCount;
				}
			}
			//clean the string up
			mostHitId = mostHitId.substring(5);
			g.setColor (Color.yellow);
			g.drawString ("Game over!", 130, 100);
			g.drawString ("You scored " + player.getScore() + " Points!", 90, 140);
			g.drawString("Statistics: ", 400, 160);
			g.drawString("Number of Clicks: " + mouseClicks, 400, 180); // The number of clicks need to be displayed
			g.drawString("% of Successful Clicks: " + accuracy + "%",400,200); // The % of successful clicks need to be displayed
			g.drawString("Ball most hit: " + mostHitId, 400, 240); // The nball that was hit the most need to be displayed
			g.drawString ("Doubleclick on the Applet, to play again!", 20, 220);

			isStoped = true;
		}
	}

	public void update (Graphics g)
	{
		if (dbImage == null)
		{
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);
		dbg.setColor (getForeground());
		paint (dbg);
		g.drawImage (dbImage, 0, 0, this);
	}
}
