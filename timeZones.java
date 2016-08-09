/* File: timeZones.java
 * Ian Thomson
 * 4/12/16
 *
 * Program displays the running time for three time zones to user within a JFrame. 
 * Each displayed time is its own thread. The user can pause or resume the thread 
 * for each time zone using Jbuttons. 
 */

import javax.swing.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.*;
import static java.awt.Label.LEFT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/** Displays Jframe containing running times for three different time zones.
 *  Each time zone has its own thread. The times can be paused or resumed using
 *  jbuttons. 
 * @author Ian Thomson
 */
public class timeZones extends JFrame{
	
	//JLabels to display each time zone to user
	private final JLabel eastern = new JLabel("EST:",LEFT);
	private final JLabel central = new JLabel("CST:", LEFT);
	private final JLabel pacific = new JLabel("PST:  ", LEFT);
	
	//class setTime implements runnable executing a new thread for each time zone
	private setTime east;
	private setTime centrl;
	private setTime pacif;
	
	//timePanel is a JPanel containing the JLabel and buttons for each time zone
	private timePanel est  = new timePanel(eastern);
	private timePanel cst  = new timePanel(central);
	private timePanel pst  = new timePanel(pacific);
	
	/** creates new time zone Jframe, sets attributes, adds components, and starts executing threads*/
	public timeZones(){
		
		//set standard attributes of JFrame
		this.setTitle("Time Zone Threads");
		this.setLocation(500, 300);
		this.getContentPane().setBackground(new Color(0,0,0));
		this.setSize(new Dimension(600,250));	
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new GridLayout(3,1));
		
		//add timePanel(JPanel objects) to JFrame
		this.getContentPane().add(est);
		this.getContentPane().add(cst);
		this.getContentPane().add(pst);
		
		//instantiate Runnable objects for each time zone
		east = new setTime ("EST", est);
		centrl = new setTime ("CST", cst);
		pacif = new setTime("PST", pst);
		
		//create a thread pool for each time zone thread
		ExecutorService threadEx = Executors.newCachedThreadPool();
		
		//start executing the time zone threads
		threadEx.execute(east);
		threadEx.execute(centrl);
		threadEx.execute(pacif);
		
		//set JFrame visible after all components already added
		this.setVisible(true);
                this.pack();
	}
	
	/** Class setTime: nested class which handles execution of time zone threads*/
	private class setTime implements Runnable
	{
		private String tzone;//string representing time zone
		
		private timePanel display;//panel displaying time and buttons
		
		/** construct a new Runnable
		 * 
		 * @param tz		string representing time zone
		 * @param dis		panel displaying time zone and buttons
		 */
		public setTime(String tz, timePanel dis){
			
			tzone = tz;
			display = dis; 
		}
		/** standard run method which executes thread in Runnable*/
		public void run(){
			
			//run thread continuously	
			while(true){
					
                                //check boolean tag on panel to determine if user has set time to "running" 
                                if(display.running){

                                        //passing 'EST' does not account for daylight savings, had to modify
                                        if(tzone.equals("EST"))
                                                tzone = "America/New_York";

                                        //find the time using standard Java date and time classes
                                        Calendar cal = new GregorianCalendar();
                                        DateFormat fmt = DateFormat.getDateTimeInstance
                                                        (DateFormat.FULL,DateFormat.FULL, Locale.US);

                                        TimeZone tze = TimeZone.getTimeZone(tzone);//determine time zone from passed string

                                        //set time zone string back to est for output display
                                        if(tzone.equals("America/New_York"))
                                                        tzone = "EST";

                                        fmt.setTimeZone(tze);

                                        //update display with new time
                                        display.time.setText(" "+tzone+":   "+fmt.format(cal.getTime()));

                                } 
                                //if user has set display to "suspended" then have thread sleep
                                else

                                        try {
                                                Thread.sleep(1);
                                        } catch (InterruptedException e) {}
                }
            }	
	}
	/**Nested class is a JPanel displaying time buttons and time for each time zone*/
	private class timePanel extends JPanel{
		
		//buttons to pause and start time
		public JButton suspend = new JButton("Suspend");
		public JButton resume = new JButton("Resume");
      
		
		//JLabel displaying time
		public JLabel time;
		
		//flag to indicate if time is running or paused
		public boolean running = true; 
		
		/** constructor makes a new timePanel Jpanel
		 * 
		 * @param t      JLabel displaying time, passed from main JFrame constructor
		 */
		public timePanel(JLabel t){
			
			time = t;
			
			//add action listener to suspend button
			suspend.addActionListener(new ActionListener(){
				
			public void actionPerformed(ActionEvent e){
					
				//if suspend is clicked set flag to false and set background of panel to red
				running = false; 
				setBackground(new Color(180,0,0));
				
                            }
			}
			);
			
			//add action listener to resume button
			resume.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e){
					
					//if resume is clicked set flag to true and set background of panel to green
					running = true;
					setBackground(new Color(0,180,0));
					
				}
			}
			);
		
			

                        time.setFont(new Font("Times New Roman", Font.PLAIN, 24));
                        
                        //add buttons and label to panel in a flow layout
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			this.add(resume);
			this.add(suspend);
			this.add(time);
			
			t.setForeground(Color.white);
			suspend.setForeground(new Color(170,0,0));
			resume.setForeground(new Color(0,170,0));
                        suspend.setFont(new Font("Times New Roman", Font.BOLD, 24));
                        resume.setFont(new Font("Times New Roman", Font.BOLD, 24));
			this.setBorder(BorderFactory.createLineBorder(Color.gray));
			
			//set panel initially to green as time is running at start 
			this.setBackground(new Color(0,180,0));
                        this.setVisible(true);
			
		}
		
	}
	/** main initializes a new timeZones JFrame */
	public static void main(String[] args){
			
		new timeZones();
		
	}
}
