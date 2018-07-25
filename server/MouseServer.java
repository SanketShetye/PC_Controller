import java.util.*;
import java.io.*;
import java.awt.*;
import java.net.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

class MobileServer{
	
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader in;
	
	private static String line;
	private static boolean isConnected = false;
	private static Robot robot;
	private static final int SERVER_PORT = 8282;
	
	
	public static void main(String args[]){
		
		boolean leftpressed = false;
		boolean rightpressed = false;
		System.out.println("Starting server...");
		
		System.out.print("Starting server On - IP: ");
		try(final DatagramSocket socket = new DatagramSocket()){
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
				System.out.println(socket.getLocalAddress().getHostAddress());
		}catch(Exception e){
			System.out.println("Ip could not be found.");			
		}
		
		try{
			
			System.out.println("Client listening..");
			
			robot = new Robot();
			server = new ServerSocket(SERVER_PORT);
			client = server.accept();
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			isConnected = true;
			System.out.println("Client Connected");
		}catch(IOException e){
			System.out.println("ERROR in opening socket");
			System.exit(-1);
		}catch(AWTException e){
			System.out.println("ERROR in robot");
			System.exit(-1);
		}
		
		
		while(isConnected){
			
			try{
				line = in.readLine();
				//System.out.println("**"+line+"**");
				/*if(line == null || line.equals(""))
				{
					System.out.println("EMPTY-ERROR");
					continue;
				}*/
				//Mouse is moved.
				if(line.contains(",")){
					
					//System.out.println("Move request - "+line);
					
					float movex = Float.parseFloat(line.split(",")[0]);
					float movey = Float.parseFloat(line.split(",")[1]);
					
					Point point = MouseInfo.getPointerInfo().getLocation();
					
					float nowx = point.x;
					float nowy = point.y;
					
					//System.out.println("\tMove request - "+(int)(nowx)+","+(int)(nowy));
					robot.mouseMove((int)(nowx+movex),(int)(nowy+movey));
				}
				else if(line.equals("left_button_pressed")){
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
				else if(line.equals("right_button_pressed")){
					robot.mousePress(InputEvent.BUTTON3_MASK);
					robot.mouseRelease(InputEvent.BUTTON3_MASK);
				}
				else if(line.contains("vlc_")){
					controlVLC();
				}
				else if(line.contains("pwr_")){
					controlPowerPoint();
				}
				else if(line.equalsIgnoreCase("exit")){
					isConnected=false;
					server.close();
					client.close();
				}
			}catch(IOException e){
				System.out.println("Read Failed");
				System.exit(-1);
			}
		}
	}
	
	public static void controlPowerPoint(){
		if(line.equals("pwr_next_slide")){
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
		}
		else if(line.equals("pwr_prev_slide")){
			robot.keyPress(KeyEvent.VK_LEFT);
			robot.keyRelease(KeyEvent.VK_LEFT);
		}
	}
	
	public static void controlVLC(){
		
		if(line.equals("vlc_play")){
			robot.keyPress(KeyEvent.VK_SPACE);
			robot.keyRelease(KeyEvent.VK_SPACE);
		}
		else if(line.equals("vlc_pause")){
			robot.keyPress(KeyEvent.VK_SPACE);
			robot.keyRelease(KeyEvent.VK_SPACE);
		}
		else if(line.equals("vlc_increase_volume")){
			
			robot.keyPress(KeyEvent.VK_CONTROL);
			
			robot.keyPress(KeyEvent.VK_UP);
			robot.keyRelease(KeyEvent.VK_UP);
			
			robot.keyRelease(KeyEvent.VK_CONTROL);
			
		}
		else if(line.equals("vlc_decrease_volume")){
			
			robot.keyPress(KeyEvent.VK_CONTROL);
			
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
			
			robot.keyRelease(KeyEvent.VK_CONTROL);
			
		}
		else if(line.equals("vlc_fast_forward")){
			
			robot.keyPress(KeyEvent.VK_CONTROL);
			
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
			
			robot.keyRelease(KeyEvent.VK_CONTROL);
			
		}
		else if(line.equals("vlc_fast_backward")){
			
			robot.keyPress(KeyEvent.VK_CONTROL);

			robot.keyPress(KeyEvent.VK_LEFT);
			robot.keyRelease(KeyEvent.VK_LEFT);
			
			robot.keyRelease(KeyEvent.VK_CONTROL);
			
		}
		else if(line.equals("vlc_mute")){
			robot.keyPress(KeyEvent.VK_M);
			robot.keyRelease(KeyEvent.VK_M);
		}
		else if(line.equals("vlc_next")){
			robot.keyPress(KeyEvent.VK_N);
			robot.keyRelease(KeyEvent.VK_N);
		}
		else if(line.equals("vlc_previous")){
			robot.keyPress(KeyEvent.VK_P);
			robot.keyRelease(KeyEvent.VK_P);
		}
		else if(line.equals("vlc_fullscreen")){
			robot.keyPress(KeyEvent.VK_F);
			robot.keyRelease(KeyEvent.VK_F);
		}
		else if(line.equals("vlc_aspect_ratio")){
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_A);
		}
	}
}