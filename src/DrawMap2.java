import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	
//	private static final int STATS_WIDTH = 300;
	private static final int GAME_Y = 50;
	private static final int GAME_X = 50;
	private static final int LIGHT_RADIUS = 5;
	private static final long serialVersionUID = 1L;
	static JFrame f;
	int fontSize = 50;//14
	int mapHeight;
	int mapWidth;
    float mapX;
    float mapY;
    int displayWidth =1026; //Screen size width.
    int displayHeight = 768; //Screen size height.
    private Map yourMap;
    private boolean FOLLOW = false;
    private HashMap<String, Image> images = new HashMap<String,Image>();
    private HashMap<String, Image> darkimages = new HashMap<String,Image>();
        
    private void setupImages() {
	    Kernel kernel = new Kernel(1, 1, new float[]{0.2f});
	    ConvolveOp op = new ConvolveOp(kernel);
	    
    	for (Tile t : Tile.values()) {
    		if (!t.getFileName().isEmpty()) {
    			images.put(t.name(),getImage(t.getFileName()));
    		}
    	}
    	
    	for (Tile t : Tile.values()) {
    		if (!t.getFileName().isEmpty()) {
    			BufferedImage image = getImage(t.getFileName());
    			BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    			darkimages.put(t.name(),op.filter(image,bufferedImage));
    		}
    	}
    	
    }
    
    private BufferedImage getImage(String filename) {
    	try {
    		InputStream in = getClass().getResourceAsStream(filename);
    	    return ImageIO.read(in);
    	} catch (IOException e) {
    	    System.out.println("The image was not loaded.");
    	}
    	    return null;
    	}
   
    private void putImage(Graphics2D g2, Image image, float x, float y, int spacing) {
		g2.drawImage(image, (int)x,(int)y - spacing, spacing, spacing, null);
    }
    
    private void putSring(Graphics2D g2, String string, float x, float y, int spacing) {
    	g2.drawString(string, x, y);
    }
    
    public void putTile(Graphics2D g2, Tile tile, Float x, Float y, int spacing, boolean lit,  boolean visited) {
    	if (!tile.getFileName().isEmpty()) {
    		Image image = lit && visited ? images.get(tile.name()) : darkimages.get(tile.name());
    		putImage( g2, image, x, y, spacing);    		    		
    	} else {
    		putSring( g2, String.valueOf(tile.getCharacter()), x, y,  spacing);
    	}
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
    	
    	this.addComponentListener(new java.awt.event.ComponentAdapter() 
		{
			public void componentResized(ComponentEvent e)
			{
				init();
			}
		});
    	
    }
   
    public void paint(Graphics g) {
    	
    	if (yourMap != null) {
    		
        int displayWidth = f.getWidth();
        int displayHeight = f.getHeight();
    	
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font(Font.MONOSPACED,Font.PLAIN,fontSize);
        Font messageFont = new Font(Font.MONOSPACED,Font.PLAIN,fontSize/2);
		g2.setFont(font);            
            				
        final int spacing = getSpacing();
        
        Player p = yourMap.getPlayer();
		Point pLoc = p.getLocation();
		List<Point> lights = new ArrayList<Point>();
		lights.add(pLoc);
		lights.addAll(this.yourMap.getLights());
		int[][] lightMap = Fov.getFov(yourMap, lights , Arrays.asList(LIGHT_RADIUS));    
        
        // draw the map tiles
        for (int x = 0; x < mapWidth; x++) {
                for (int y = 0; y < mapHeight; y++) {
                        if ((x*spacing+mapX > -10) && (x*spacing+mapX < displayWidth+10) && (y*getSpacing()+mapY > -10) && (y*spacing+mapY < displayHeight+10)){
                    		final Tile t = yourMap.getLevel()[y][x]; 
                    		if (t!=null) {
                    			boolean visited = yourMap.getVisited()[x][y];
                    			boolean isLit = lightMap[x][y] > 0;
                    			if (lightMap[x][y] > 0) {
                    				yourMap.getVisited()[x][y] = true;
                    			} 
                    			if (visited || isLit) {
                    				putTile(g2, t, (x*spacing)+mapX, (y*spacing)+mapY, spacing, isLit, visited); // add is lit 
                    			}
                    		} else {
                    			System.out.println("Something is very wrong here...");
                    		}
                        }
                }
                g2.setColor(Color.YELLOW);
                g2.drawString(p.getMaxHp() +"\\" + p.getHp(),10+(fontSize/2) , 10+ (fontSize/2));          
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
	    			putTile(g2, thing.getTile(),(x*spacing)+mapX,  (y*spacing)+mapY,  spacing, true, true);
				} else if (isActor && !liveActor){ 
					g2.setFont(messageFont);
					g2.setColor(Color.WHITE); //TODO change this to the proper image
					g2.drawString("Arrrrggg!", (x*spacing)+mapX, (y*spacing)+mapY);
				}
			}		

			// draw player last so appears on top of any passable things
			int playerX = p.getLocation().getX();
			int playerY = p.getLocation().getY();
			putTile(g2, Tile.PERSON, (playerX*spacing)+mapX, ((playerY*spacing)+mapY), spacing, true, true);
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
        		g2.setFont(messageFont);  
	        	g2.setColor(m.getColor() != null ? m.getColor() : Color.yellow); // messages should store a color
	    		g2.drawString(m.getMessage(), ((m.getP().getX() ) * spacing )+mapX,((m.getP().getY() -1)   *spacing)+mapY); 
        	}
        }
        yourMap.setMessages(new ArrayList<Message>()); // TODO add a clearMessages
        
        for (Message m: yourMap.getPermanentMessages()) { 
        	g2.setFont(messageFont);  
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
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(40, 40));
        f.getContentPane().add(scrollPane, BorderLayout.SOUTH);
        displayArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN, 20));
        displayArea.setText("[5] dungeon [7] islands [8] caves");
        f.setSize(1024, 768);
        f.setPreferredSize(new Dimension(1026, 768));                           
        f.setVisible(true);
        displayArea.addKeyListener(map);
        map.setupImages();
        map.FOLLOW = true;
        map.init();
   
    }

    private void init() {
    	this.fontSize = ((int) f.getSize().width/26);
        f.paint(f.getGraphics());
        f.repaint();
        centreView(fontSize);
    	
    }
    
	@Override
	public void keyPressed(KeyEvent key) {	 
		
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
			init();
		} else if (key.getKeyCode() == KeyEvent.VK_5) {
			this.yourMap = MapGenDungeon.newFullMap(Map.newFilledMap("Dungeon!",Tile.SPACE,mapHeight,mapWidth));
			init();
        }
		
		if (key.getKeyCode() == KeyEvent.VK_7) {
			this.yourMap = MapGenWorld.newWorld("world",GAME_X,GAME_Y,3);
			init();
        }
		
		if (key.getKeyCode() == KeyEvent.VK_8) {
			this.yourMap = MapGenCaves.newWorld("world",GAME_X ,GAME_Y ,5);
			init();
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
		mapX = f.getWidth() /2 - yourMap.getPlayer().getLocation().getX() * spacing;
		mapY = f.getHeight() /2 - yourMap.getPlayer().getLocation().getY() * spacing;
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
