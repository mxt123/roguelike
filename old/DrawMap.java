import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Message;
import model.Point;

import util.ConnectedIslands;
import util.Constants;
import util.Read2D;
import worldgen.MapGenCaves;
import worldgen.MapGenDungeon;

import static util.Constants.*;


public class DrawMap extends JPanel  implements KeyListener{
		static JTextArea displayArea;
		static JFrame f;
		static int fontSize = 14;
		int mapHeight;
		int mapWidth;
        //Location of your map, you'll need other methods to change this value to move your map around on the screen. But this will start your generation at 0,0 NOTE: Since you are dealing with ASCII you will probably be fine just using ints instead of floats.
         float mapX;
         float mapY;
         int displayWidth =1024; //Screen size width.
        int displayHeight = 768; //Screen size height.
        private byte[][] yourMap = new byte[mapHeight][mapWidth]; //Create byte array matching map height/width. (May need to be an int[][] or whatever[][] depending on what you're doing)
        private List<Message> messages = new ArrayList<Message>();
        
        Color color1 = Constants.LAND_COL;
        Color color2 = Constants.SEA_COL;   
        
        private int getSpacing(){
        	return  fontSize;
        }
        
        public DrawMap(final String fileName) throws IOException{
        	mapHeight = Read2D.countLines(fileName) +1;
        	this.yourMap = Read2D.read2dArray(fileName,mapHeight);
        	mapWidth = this.yourMap[0].length;
        	mapY = 0 + getSpacing();
        	mapX = 0;
        }
        
        public DrawMap(int height, int width) {
        	mapHeight = height;
        	mapWidth = width;
        	this.yourMap =  MapGenCaves.newBlankMap(height,width);
        	mapY = 0 + getSpacing();
        	mapX = 0;
        }
       
        public void paint(Graphics g) {
        	
            int displayWidth = f.getWidth();
            int displayHeight = f.getHeight();
        	
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
               // Font font = new Font(Font.MONOSPACED,Font.PLAIN, fontSize);
                Font font = new Font(Font.MONOSPACED,Font.PLAIN,fontSize);
               // Font font = Font.createFont(Font.PLAIN, fontFile)
                g2.setFont(font);            
                final int spacing = getSpacing();

                //Start looping through the entire byte array.
                for (int x = 0; x < mapWidth; x++) {
                        for (int y = 0; y < mapHeight; y++) {
                        	//  System.out.println(x + "," + y + "," + (char)yourMap[y][x]);
                                // Check to see if the coordinates being looked for are visible on the screen. If not, don't bother rendering them.
                                if ((x*spacing+mapX > -10) && (x*spacing+mapX < displayWidth+10) && (y*getSpacing()+mapY > -10) && (y*spacing+mapY < displayHeight+10)){
                                   // if (!(yourMap[x][y] == 0)){ //Assuming 0 is a "blank space", but whatever == Blank space here, so you're not trying to output nothing.
                                    //However you output the text would go here. For example, if you were using the Font class in slick2d:
                            		final char ch = (char)yourMap[y][x];                               
                            		g2.setColor(ch == Constants.WALL?color2 :color1);
                            		g2.drawString(String.valueOf(ch), (x*spacing)+mapX, (y*spacing)+mapY); 
                                     
                                }
                        }
                }
                
                // draw message layer
                g2.setColor(Color.yellow);
                for (Message m: messages) {          
                	
            		g2.drawString(m.getMessage(), (m.getP().getY() * fontSize)+mapY,(m.getP().getX()*fontSize)+mapX); 

                }
                
        }


        public static void main(String[] args) throws IOException {
                f = new JFrame("xzz");
                f.getContentPane().setBackground(Color.black);
                f.pack();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               // DrawMap map = new DrawMap("/home/matt/workspace/drawstring/src/map.txt");
                // TODO change recursive graph functions to avoid stackoverflows :)
                DrawMap map = new DrawMap(50,50);
                map.init();
                f.getContentPane().add(map);
                
                displayArea = new JTextArea();
                displayArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(displayArea);
                scrollPane.setPreferredSize(new Dimension(40, 40));
                f.getContentPane().add(scrollPane, BorderLayout.SOUTH);
                displayArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN, 20));
                displayArea.setText("hello");
                
                //map.yourMap = MapGenCaves.generateGrid(map.yourMap);
                
                f.setSize(1024, 768);
                f.setPreferredSize(new Dimension(1024, 768));                           
                f.setVisible(true);
                displayArea.addKeyListener(map);
        }

		@Override
		public void keyPressed(KeyEvent key) {	
			int spacing = getSpacing();
			if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
					mapX -= spacing;
			}
			if (key.getKeyCode() == KeyEvent.VK_LEFT) {
				if (Math.round(mapX) <=-spacing ) {
					mapX += spacing;
				}
			}
			if (key.getKeyCode() == KeyEvent.VK_DOWN) {
				mapY -= spacing;
			}
			if (key.getKeyCode() == KeyEvent.VK_UP) {
				if (Math.round(mapY) <=0 ) {
					mapY += spacing;
				}
			}
			if (key.getKeyCode() == KeyEvent.VK_1) {
				fontSize += 1;	
				mapX = 0;
				mapY=0;
			}
			if (key.getKeyCode() == KeyEvent.VK_2) {
				fontSize -= 1;
				mapX = 0;
				mapY=0;
			}
			if (key.getKeyCode() == KeyEvent.VK_SPACE) {
				this.yourMap = MapGenCaves.evolveGrid(this.yourMap);
	        }
			if (key.getKeyCode() == KeyEvent.VK_3) {
				messages = new ArrayList<Message>();
				color1 = LAND_COL;
				color2 = SEA_COL;
				this.yourMap = MapGenCaves.generateGrid(this.yourMap);
	        }
			if (key.getKeyCode() == KeyEvent.VK_4) {
				setIslandLabels();
	        }
			if (key.getKeyCode() == KeyEvent.VK_5) {
				messages = new ArrayList<Message>();
				color1 = ROOM_COL;
				color2 = WALL_COL;
				this.yourMap = MapGenDungeon.newFullMap(this.yourMap);
	        }
			if (key.getKeyCode() == KeyEvent.VK_6) {
				init();
	        }
			
			f.repaint();
			displayArea.setText("X:" +String.valueOf(mapX/spacing) + "-Y:" + String.valueOf(mapY/spacing));
			
		}

		private void init() {
			messages = new ArrayList<Message>();
			color1 = LAND_COL;
			color2 = SEA_COL;
			this.yourMap = MapGenCaves.generateGrid(this.yourMap);
			for (int i = 0 ; i < 3; i++) {
				this.yourMap = MapGenCaves.evolveGrid(this.yourMap);
			}
			setIslandLabels();
		}

		private void setIslandLabels() {
			List<Point> islands = ConnectedIslands.getIslands(yourMap);
			System.out.println("Number of islands is: " + islands.size());
			this.messages = new ArrayList<Message>();
			for (Point p: islands) {
				messages.add(new Message(p, "Island"));
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent key) {
			//displayArea.setText(String.valueOf(key.getKeyChar()));
			
		}

}
