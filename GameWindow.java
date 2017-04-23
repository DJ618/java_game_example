public class GameWindow
{
	public int x_leftout;
	public int x_rightout;
	public int y_upout;
	public int y_downout;	
	
	/* x_leftout, x_rightout, y_upout, y_downout are config arguments. These should be initialized with the values read from the config.xml file*/		
	public GameWindow(int x_leftout, int x_rightout, int y_upout, int y_downout)
	{
		this.x_leftout = x_leftout;
		this.x_rightout = x_rightout;
		this.y_upout = y_upout;
		this.y_downout = y_downout;
	}
}