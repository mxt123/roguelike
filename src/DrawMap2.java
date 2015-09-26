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


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Direction;
import model.Message;
import model.Point;
import model.world.Map;
import model.world.Thing;
import model.world.Tile;
import util.Fov;
import worldgen.MapGenDungeon;
import worldgen.MapGenWorld;

public class DrawMap2 extends JPanel  implements KeyListener{
		static JFrame f;
		static int fontSize = 14;
		int mapHeight;
		int mapWidth;
        float mapX;
        float mapY;
        int displayWidth =1026; //Screen size width.
        int displayHeight = 768; //Screen size height.
        private Map yourMap;
        private boolean FOLLOW = false;
        
        private int getSpacing(){
        	return  fontSize;
        }
                
        public DrawMap2(int height, int width) {
        	mapHeight = height;
        	mapWidth = width;
        	this.yourMap = MapGenDungeon.newFullMap(Map.newFilledMap("Dungeon!",Tile.SPACE,mapHeight,mapWidth));
        	mapY = 0 + getSpacing();
        	mapX = 0;
        }
       
        public void paint(Graphics g) {
        	
            int displayWidth = f.getWidth();
            int displayHeight = f.getHeight();
        	
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                Font font = new Font(Font.MONOSPACED,Font.PLAIN,fontSize);
				g2.setFont(font);            
	                				
                final int spacing = getSpacing();
                
                // get lightmap this should be stored in map and persisted
                int[][] lightMap = Fov.getFov(yourMap, yourMap.getPlayer().getLocation(), 5 );    
                
                // draw the map tiles
                for (int x = 0; x < mapWidth; x++) {
                        for (int y = 0; y < mapHeight; y++) {
                                if ((x*spacing+mapX > -10) && (x*spacing+mapX < displayWidth+10) && (y*getSpacing()+mapY > -10) && (y*spacing+mapY < displayHeight+10)){
                            		final Tile t = yourMap.getLevel()[y][x]; 
                            		if (t!=null) {
                            			boolean visited = yourMap.getVisited()[x][y];
                            			boolean isLit = lightMap[x][y] > 0;
                            			if (visited || isLit) {
	                            			if (yourMap.getVisited()[x][y]){
	                            				g2.setColor(t.getColorLight().darker().darker());                      
	                            			}
	                            			if (lightMap[x][y] > 0) {
	                            				g2.setColor(t.getColorLight());
	                            				yourMap.getVisited()[x][y] = true;
	                            			} 
                            			}else {
                            				g2.setColor(t.getColorDark());
                            			}
                            			g2.drawString(String.valueOf(t.getCharacter()), (x*spacing)+mapX, (y*spacing)+mapY); 
                            		} else {
                            			System.out.println("wtf");
                            		}
                                }
                        }
                }             
                
                // add the things
                for (Thing thing : yourMap.getThings()) {
                	// only draw if lit or player
                	if (thing.getName().equals("Player") || lightMap[thing.getLocation().getX()][thing.getLocation().getY()] > 0) {
                		g2.setColor(thing.getColor());
                		g2.drawString(String.valueOf(thing.getTile().getCharacter()), (thing.getLocation().getX()*spacing)+mapX, (thing.getLocation().getY()*spacing)+mapY);
                	}
                }
                                
                g2.setColor(Color.yellow);
                for (Message m: yourMap.getMessages()) {          
            		g2.drawString(m.getMessage(), (m.getP().getY() * fontSize)+mapY,(m.getP().getX()*fontSize)+mapX); 
                }
        }

        public static void main(String[] args) throws IOException {
                f = new JFrame("xzz");
                f.getContentPane().setBackground(Color.BLACK);
                f.pack();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                DrawMap2 map = new DrawMap2(50,50);
                f.getContentPane().add(map);
                JTextArea displayArea = new JTextArea();
                displayArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(displayArea);
                scrollPane.setPreferredSize(new Dimension(40, 40));
                f.getContentPane().add(scrollPane, BorderLayout.SOUTH);
                displayArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN, 20));
                displayArea.setText("[5] generate [1,2] zoom, [arrows] move [shift + arrows] [6] toggle follow scroll [space] center view, shift space to reset view");
                f.setSize(1024, 768);
                f.setPreferredSize(new Dimension(1026, 768));                           
                f.setVisible(true);
                displayArea.addKeyListener(map);
        }

		@Override
		public void keyPressed(KeyEvent key) {	
			
			// run all actors, timers and things here
			
			int spacing = getSpacing();
			
			if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_RIGHT ) {
				mapX -= spacing;
			} else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
				yourMap.getPlayer().move(Direction.EAST, 1);				
			}
			
			if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_LEFT ) {
				mapX += spacing;
			} else if (key.getKeyCode() == KeyEvent.VK_LEFT) {
				yourMap.getPlayer().move(Direction.WEST, 1);
			}
			
			if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_DOWN ) {
				mapY -= spacing;
			} else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
				yourMap.getPlayer().move(Direction.SOUTH, 1);
			}
			
			if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_UP ) {
				mapY += spacing;
			} else if (key.getKeyCode() == KeyEvent.VK_UP) {
				yourMap.getPlayer().move(Direction.NORTH, 1);
			}
			
			if (FOLLOW) {centreView(spacing);}
			
			if (key.getKeyCode() == KeyEvent.VK_1) {
				fontSize += 1;	
				centreView(spacing);
			}
			if (key.getKeyCode() == KeyEvent.VK_2) {
				fontSize -= 1;
				centreView(spacing);
			}
			
			if (key.getKeyCode() == KeyEvent.VK_6) {
				FOLLOW = !FOLLOW;
			}

			if (key.getKeyCode() == KeyEvent.VK_5) {
				this.yourMap = MapGenDungeon.newFullMap(Map.newFilledMap("Dungeon!",Tile.SPACE,mapHeight,mapWidth));
	        }
			
			if (key.getKeyCode() == KeyEvent.VK_7) {
				this.yourMap = MapGenWorld.newWorld("world",50,50,3);
	        }
			
			if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_SPACE ) {
				mapY = 0;
				mapX = 0;
				fontSize = 14;
			} else if (key.getKeyCode() == KeyEvent.VK_SPACE) {
				centreView(spacing);
	        }
			
			f.repaint();
			
		}

		private void centreView(int spacing) {
			mapX = displayWidth/ 2 - yourMap.getPlayer().getLocation().getX() * spacing;
			mapY = displayHeight/2 - yourMap.getPlayer().getLocation().getY() * spacing;
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
