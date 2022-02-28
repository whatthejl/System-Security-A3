import java.util.ArrayList;
import java.util.Random;
import java.text.DecimalFormat;
import java.nio.file.Paths;
import java.io.*; 
import java.util.*;
import java.math.*;

public class ActivityEngine
{
	static ArrayList<String> eventdata = new ArrayList<>();
	static ArrayList<String> statsdata = new ArrayList<>();
	static int days;
	static int dataread;
	Random r = new Random();
	static ArrayList<String> log = new ArrayList<>();
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public ActivityEngine(ArrayList<String> eventdata, ArrayList<String> statsdata, int days)
	{
		this.eventdata = eventdata;
		this.statsdata = statsdata;
		this.days = days;
		this.dataread = 5;
		initiate();
	}
	
	public ArrayList<String> retrievelog ()
	{
		return log;
	}
	
	public void initiate()
	{
		//generates random numbers as data and stores into ArrayList log
		//with the format of [login, time online, emails sent, emails opened, emails deleted]
		//and recurring to the number of days
		//log arraylist size is 5*days 
		for(int d = 1; d <= days; d++)
		{
			int i = 1;
			int j = 1;
			int count = 0;
			while(count < dataread)
			{
				double min = Double.parseDouble(eventdata.get(i+1));
				double max = Double.parseDouble(eventdata.get(i+2));
				double mean = Double.parseDouble(statsdata.get(j));
				double sd = Double.parseDouble(statsdata.get(j+1));
				double k;
				//random double based on mean and std dev 
				do
				{
					k = r.nextGaussian() * sd;
					k += mean;
				} while ((k <= min) && (k >= max));
				
				if (eventdata.get(i).contains("D"))
				{
					int t = (int)Math.round(k);
					String temp = Integer.toString(t);
					log.add(temp);
				}
				else if (eventdata.get(i).contains("C"))
				{
					String temp = df.format(k);
					log.add(temp);
				}
				else
				{
					log.add("-1");
				}
				i += 5;
				j += 3;
				count++;
			}
		}
	}
	
	public void createFile(String fileName)
	{
		//prints individual data for each day
		File myObj = new File (fileName);
		try
		{
			int counter = 0;
			FileWriter myWriter = new FileWriter(fileName);
			for (int i = 0; i < days; i++)
			{
				//print arraylist
				myWriter.write("Day " + (i+1) + ": \n");
				int headercounter = 0;
				for (int j = 0; j < dataread; j++)
				{
					//j represents data within the day, i represents each days' data
					String header = eventdata.get(headercounter);
					String content = log.get(counter);
					String test = header + " : " + content + "\n";
					myWriter.write(test);
					counter++;
					headercounter += 5;
				}
			}
			myWriter.flush();
			myWriter.close();
		}
		catch (IOException e)
		{
			System.out.println("An error occurred.");
		}
	}
}