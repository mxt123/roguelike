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
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Direction;
import model.Message;
import model.Point;
import model.world.Actor;
import model.world.Map;
import model.world.Player;
import model.world.Projectile;
import model.world.Thing;
import model.world.Tile;
import util.AStar;
import util.Fov;
import worldgen.MapGenCaves;
import worldgen.MapGenDungeon;
import worldgen.MapGenWorld;
import worldgen.TestRoom;

public class DrawMap2 extends JPanel  implements KeyListener {
	
	private static final int STATS_WIDTH = 300;
	private static final int GAME_Y = 50;
	private static final int GAME_X = 50;
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
    private Image ghost = getImage("g.png");
    private Image wall = getImage("wall.png");
        
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
    	
    	if (yourMap != null) {
    		
        int displayWidth = f.getWidth()-STATS_WIDTH;
        int displayHeight = f.getHeight();
    	
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font(Font.MONOSPACED,Font.PLAIN,fontSize);
		g2.setFont(font);            
            				
        final int spacing = getSpacing();
        
        Player p = yourMap.getPlayer();
		Point pLoc = p.getLocation();
		int[][] lightMap = Fov.getFov(yourMap, Arrays.asList(pLoc), Arrays.asList(LIGHT_RADIUS));    
        
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
                        				g2.setColor(t.getColorLight().darker()); //TODO remove this suck and change to a alpha level                     
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
                    			System.out.println("Something is very wrong here...");
                    		}
                        }
                }
                g2.setColor(Color.YELLOW);
                g2.drawString(p.getMaxHp() +"\\" + p.getHp(),10,10);          
        }             
            
        // add the things
        
        for (Thing thing : yourMap.getThings()) {
        	boolean isActor = thing instanceof Actor;
        	boolean liveActor = isActor && ((Actor) thing).getHp() > 0;
        	int x = thing.getLocation().getX();
			int y = thing.getLocation().getY();
			if (!thing.getName().equals("Player") && lightMap[x][y] > 0)  {
				if (!isActor || (isActor && liveActor)) {
					thing.setActive(true);
	    			g2.setColor(thing.getColor());
	    			g2.drawString(String.valueOf(thing.getTile().getCharacter()), (x*spacing)+mapX, (y*spacing)+mapY);
				} else if (isActor && !liveActor){ 
					g2.setColor(Color.WHITE); //TODO change this to the proper image
					g2.drawString("Arrrrggg!", (x*spacing)+mapX, (y*spacing)+mapY);
				}
			}		

			// draw player last so appears on top of any passable things
			int playerX = p.getLocation().getX();
			int playerY = p.getLocation().getY();
			
			g2.drawImage(
					ghost,
					(int) ((playerX*spacing)+mapX),
					(int) ( (playerY*spacing)+mapY) -spacing,
					spacing,
					spacing,
					null);
        }
    
    	// remove dead things
 		for (int i = 0 ; i < yourMap.getThings().size();i++){ 
 			Thing thing = yourMap.getThings().get(i);
 			boolean isActor = thing instanceof Actor;
 			boolean isProjectile = thing instanceof Projectile;
        	boolean liveActor = isActor && ((Actor) thing).getHp() > 0;
 			if (((!(thing instanceof Player)) && isActor && !liveActor) || isProjectile) {
 				thing.destroy();
 				yourMap.getThings().remove(thing);	
 			}
 		}
                        
        for (Message m: yourMap.getMessages()) { 
        	if (m.getMessage() != null) {
	     		g2.setFont(m.getFont() != null ? m.getFont() : font);  
	        	g2.setColor(m.getColor() != null ? m.getColor() : Color.yellow); // messages should store a color
	    		g2.drawString(m.getMessage(), ((m.getP().getX() ) * spacing )+mapX,((m.getP().getY() -1)   *spacing)+mapY); 
        	}
        }
        yourMap.setMessages(new ArrayList<Message>()); // TODO add a clearMessages
        
        for (Message m: yourMap.getPermanentMessages()) { 
        	// TODO permanent messages should be toggled
        	g2.setFont(m.getFont() != null ? m.getFont() : font);  
        	g2.setColor(m.getColor() != null ? m.getColor() : Color.yellow); // messages should store a color
    		g2.drawString(m.getMessage(), ((m.getP().getX() +1) * spacing )+mapX,((m.getP().getY() -1)   *spacing)+mapY); 
        }
        
    	}
       
    }

    public static void main(String[] args) throws IOException {
        f = new JFrame("xzz");
        f.getContentPane().setBackground(Color.BLACK);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawMap2 map = new DrawMap2(GAME_X,GAME_Y);
        f.getContentPane().add(map);
        JTextArea displayArea = new JTextArea();
      //  JTextArea statsArea = new JTextArea();
       // statsArea.setPreferredSize(new Dimension(STATS_WIDTH, GAME_Y));
        displayArea.setEditable(false);
      //  statsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(40, 40));
        f.getContentPane().add(scrollPane, BorderLayout.SOUTH);
      //  f.getContentPane().add(statsArea, BorderLayout.EAST);
        displayArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN, 20));
        displayArea.setText("[5] generate [1,2] zoom, [arrows] move [shift + arrows] [6] toggle follow scroll [space] center view, shift space to reset view");
      //  statsArea.setText("Hello stats inv and other stuf goes here XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        f.setSize(1024, 768);
        f.setPreferredSize(new Dimension(1026, 768));                           
        f.setVisible(true);
        // todo add glass pane over top of all
        displayArea.addKeyListener(map);
     //   statsArea.addKeyListener(map);
    }

	@Override
	public void keyPressed(KeyEvent key) {	 
		
		// check player hp
		Player player = null;
		if (yourMap != null) {
			player = yourMap.getPlayer();
		}
		
		boolean tookTurn = false;
		
		int spacing = getSpacing();
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_RIGHT ) {
			mapX -= spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.move(Direction.EAST, 1);
			tookTurn = true;
		}
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_LEFT ) {
			mapX += spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			player.move(Direction.WEST, 1);
			tookTurn = true;
		}
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_DOWN ) {
			mapY -= spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			player.move(Direction.SOUTH, 1);
			tookTurn = true;
		}
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_UP ) {
			mapY += spacing;
		} else if (key.getKeyCode() == KeyEvent.VK_UP) {
			player.move(Direction.NORTH, 1);
			tookTurn = true;
		}
		
		if (FOLLOW) {centreView(spacing);}
		
		//skip
		if (key.getKeyCode() == KeyEvent.VK_SPACE) {
			tookTurn = true;
		}
		
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
			this.yourMap = MapGenWorld.newWorld("world",GAME_X,GAME_Y,3);
        }
		
		if (key.getKeyCode() == KeyEvent.VK_8) {
			this.yourMap = MapGenCaves.newWorld("world",GAME_X,GAME_Y,5);
        }
		
		if (key.getKeyCode() == KeyEvent.VK_9) {
			for (int i = 0 ; i < yourMap.getVisited().length; i ++ ) {
				for (int j = 0; j <  yourMap.getVisited()[0].length; j++) {
					yourMap.getVisited()[i][j] = true;
				}
			}
        }
		
		if (key.isShiftDown() && key.getKeyCode() == KeyEvent.VK_R ) {
			mapY = 0;
			mapX = 0;
			fontSize = 14;
		} else if (key.getKeyCode() == KeyEvent.VK_R) {
			centreView(spacing);
        }
		
		if ( key.getKeyCode() == KeyEvent.VK_Z ) {
			Actor a = player.getNearestActor(player);
			if (a != null) {
				Point target = a.getLocation();
				if (target != null) {
					yourMap.getThings().add(
						new Projectile(yourMap,player.getLocation() , Tile.ARROW, Color.RED, "arow", "arrow", true, true, target, 1, 20)
					);
				}
			}
			tookTurn = true;
        }
		
		if (key.getKeyCode() == KeyEvent.VK_M) {
			
			List<Point> path = player.getOnPath();
				
			Point target = null;
			if (key.isShiftDown()) {
				Actor nearest = player.getNearestActor(player);
				if (nearest != null) {
					target = nearest.getLocation();
				}
			} else {
				target = yourMap.getNearestUnVisitedCellToActor(player);
			}
			
			if (target != null) {
				if (path == null || path.size()<1) {
					path = AStar.aStar(player.getLocation(),target,yourMap,Arrays.asList(Tile.SPACE,Tile.LAND,Tile.SEA));
				}
				
				if (path !=null && path.size() > 0) {
				
					/*// display path on map as messages
					int count = 0;
					for (Point pnt : path ){
						yourMap.getMessages().add(new Message(new Point(pnt.getX()-1,pnt.getY()+1),Integer.toString(count++)));
					}
					*/
					
					Point calcStep = path.get(path.size()-1);
					path.remove(calcStep);
					player.setOnPath(path);
					player.moveTowards(calcStep);
				}
				
			}
			
			tookTurn = true;
			
		}
		
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
		
		if (tookTurn) {
			// run all actors, timers and things here
		        for (Thing t : yourMap.getThings()){
		        	
	        	// fires burn water runs etc		        	
	        	if (t instanceof Projectile) {
	        		while(((Projectile)t).getPower() > 0) {
		        		t.act();	
	        		}
	        		for (Point p : t.getMovementHistory()) {
	         			int x = p.getX();
	        			int y = p.getY()+1;
	        			yourMap.getMessages().add(new Message( new Point(x,y),"#"));
	        		}
	        		f.repaint();
	        	}
	        	
	        	if ( t instanceof Actor ) {
	        		Actor a = (Actor) t;
	        		if (a.getHp() < 1) {
	        		//	yourMap.getThings().remove(t); // conccurent mod exception whoops
	        			// call the destroy here and create loot add xp etc
	        		//	f.repaint();
	        		} else if (  t.isActive() && !(t instanceof Player) ) {
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

	public void mouseClicked(MouseEvent arg0) {
		java.awt.Point clickPoint = arg0.getPoint();
		yourMap.getMessages().add(
				new Message(
						new Point(
								(int)clickPoint.getX()*fontSize,
								(int)clickPoint.getY()*fontSize),
								"click"));
	}


}
