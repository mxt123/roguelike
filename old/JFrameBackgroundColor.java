import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class JFrameBackgroundColor
{
	public static void main(String[] args){
		  // schedule this for the event dispatch thread (edt)
	    SwingUtilities.invokeLater(new Runnable()
	    {
	      public void run()
	      {
	        displayJFrame();
	      }
	    });
	}
	
	  static void displayJFrame()
	  {
	    // create our jframe as usual
	    JFrame jframe = new JFrame("JFrame Background Color");
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	    jframe.getContentPane().setBackground(Color.red);
	 
	    // set the jframe size and location, and make it visible
	    jframe.setPreferredSize(new Dimension(400, 300));
	    jframe.pack();
	    jframe.setLocationRelativeTo(null);
	    jframe.setVisible(true);
	  }
}