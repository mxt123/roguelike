import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Direction;
import model.Message;
import model.world.Actor;
import model.world.Map;
import model.world.Player;
import model.world.Stats;
import model.world.Thing;
import model.world.Tile;
import util.Fov;
import worldgen.MapGenCaves;
import worldgen.MapGenDungeon;
import worldgen.MapGenWorld;
import worldgen.TestRoom;

public class DrawMap2 extends JPanel  implements KeyListener{
	private static final int LIGHT_RADIUS = 5;
	private static final long serialVersionUID = 1L;
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
    private Image ghost = getImage("Blinky8bit.png");
        
    private BufferedImage getImage(String filename) {
    	try {
    		InputStream in = getClass().getResourceAsStream(filename);
    	    return ImageIO.read(in);
    	} catch (IOException e) {
    	    System.out.println("The image was not loaded.");
    	}
    	    return null;
    	}
    
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
    	
    	// for the moment things become inactive if out of light
    	for (Thing t : yourMap.getThings()) {
    		t.setActive(false);
    	}
		
        int displayWidth = f.getWidth();
        int displayHeight = f.getHeight();
    	
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font(Font.MONOSPACED,Font.PLAIN,fontSize);
		g2.setFont(font);            
            				
        final int spacing = getSpacing();
        
        // get lightmap this should be stored in map and persisted
        int[][] lightMap = Fov.getFov(yourMap, yourMap.getPlayer().getLocation(), LIGHT_RADIUS );    
        
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
                g2.setColor(Color.YELLOW);
                Stats stats = yourMap.getPlayer().getStats();
                g2.drawString(stats.getMaxHp() +"\\" + stats.getHp(),10,10);
        }             
            
        // add the things
        for (Thing thing : yourMap.getThings()) {
        	int x = thing.getLocation().getX();
			int y = thing.getLocation().getY();
			if (!thing.getName().equals("Player") && lightMap[x][y] > 0)  {
    			thing.setActive(true);
    			g2.setColor(thing.getColor());
    			g2.drawString(String.valueOf(thing.getTile().getCharacter()), (x*spacing)+mapX, (y*spacing)+mapY);
        }

		// draw player last so appears on top of any passable things
		Player p = yourMap.getPlayer();
		g2.drawImage(
				ghost,
				(int) ((p.getLocation().getX()*spacing)+mapX),
				(int) ( (p.getLocation().getY()*spacing)+mapY) -spacing,
				spacing,
				spacing,
				null);
        }
                        
        for (Message m: yourMap.getMessages()) { 
        	Font fontSmall = new Font(Font.MONOSPACED,Font.PLAIN,fontSize/2);
     		g2.setFont(fontSmall);  
        	g2.setColor(Color.yellow); // messages should store a color
    		g2.drawString(m.getMessage(), ((m.getP().getX() ) * spacing )+mapX,((m.getP().getY() -1)   *spacing)+mapY); 
        }
        yourMap.setMessages(new ArrayList<Message>()); // TODO add a clearMessages
        
        for (Message m: yourMap.getPermanentMessages()) { 
        	// TODO permanent messages should be toggled
        	g2.setColor(Color.yellow); // messages should store a color
    		g2.drawString(m.getMessage(), ((m.getP().getX() +1) * spacing )+mapX,((m.getP().getY() -1)   *spacing)+mapY); 
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
		
		boolean tookTurn = false;
		
		int spacing = getSpacing();
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_RIGHT ) {
			mapX -= spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			yourMap.getPlayer().move(Direction.EAST, 1);
			tookTurn = true;
		}
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_LEFT ) {
			mapX += spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			yourMap.getPlayer().move(Direction.WEST, 1);
			tookTurn = true;
		}
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_DOWN ) {
			mapY -= spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			yourMap.getPlayer().move(Direction.SOUTH, 1);
			tookTurn = true;
		}
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_UP ) {
			mapY += spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_UP) {
			yourMap.getPlayer().move(Direction.NORTH, 1);
			tookTurn = true;
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

		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_5 ) {
			this.yourMap = TestRoom.newFullMap(Map.newFilledMap("Dungeon!",Tile.SPACE,mapHeight,mapWidth));
		} else if (key.getKeyCode() == KeyEvent.VK_5) {
			this.yourMap = MapGenDungeon.newFullMap(Map.newFilledMap("Dungeon!",Tile.SPACE,mapHeight,mapWidth));
        }
		
		if (key.getKeyCode() == KeyEvent.VK_7) {
			this.yourMap = MapGenWorld.newWorld("world",50,50,3);
        }
		
		if (key.getKeyCode() == KeyEvent.VK_8) {
			this.yourMap = MapGenCaves.newWorld("world",50,50,2);
        }
		
		if (key.getKeyCode() == KeyEvent.VK_9) {
			for (int i = 0 ; i < yourMap.getVisited().length; i ++ ) {
				for (int j = 0; j <  yourMap.getVisited()[0].length; j++) {
					yourMap.getVisited()[i][j] = true;
				}
			}
        }
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_SPACE ) {
			mapY = 0;
			mapX = 0;
			fontSize = 14;
		} else if (key.getKeyCode() == KeyEvent.VK_SPACE) {
			centreView(spacing);
        }
		
		if (tookTurn) {
			// run all actors, timers and things here
		    if (tookTurn) {
		        for (Thing t : yourMap.getThings()){
		        	// fires burn water runs etc		        	
		        	if ( t instanceof Actor && t.isActive() &&!(t instanceof Player) ) {
		        		// just get a message for now call get action later
		        		yourMap.getMessages().add(new Message(t.getLocation(),t.getMessage()));
		        		t.act();
		        	}
		        }
		    }
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
