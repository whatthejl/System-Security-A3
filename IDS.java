import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Paths;

/*	
	CSCI262 Assignment 3
	Name: Yong Jia Liang
	UOW ID: 7083609
*/
public class IDS 
{
	static Scanner eventinput;
	static Scanner statsinput;
	static ArrayList<String> eventdata = new ArrayList<>();
	static ArrayList<String> statsdata = new ArrayList<>();
	public static void main(String args[])
	{
		String event = null;
		String stats = null;
		int days = 0;
		boolean close = false;
		
		if (args.length == 3)
		{
			event = args[0];
			stats = args[1];
			days = Integer.parseInt(args[2]);
		}
		else
		{
			System.out.println("Try again");
			System.exit(0);
		}
		
		//Initialize the files
		Initialize(event, stats, days);
		
		ActivityEngine activity = new ActivityEngine(eventdata, statsdata, days);
		activity.createFile("Activity.txt");
		ArrayList <String> log = activity.retrievelog();
		
		AnalysisEngine analysis = new AnalysisEngine(eventdata, statsdata, days, log);
		analysis.createAnalysisFile("Analysis.txt");
		
		AlertEngine alert = new AlertEngine(eventdata, statsdata);
		
	}
	
	public static void Initialize(String event, String stats, int days)
	{
		String line = "";
		try
		{	
			eventinput = new Scanner (Paths.get (event));
			statsinput = new Scanner (Paths.get (stats));
		}
		catch (FileNotFoundException e)
		{
			System.err.println ("Error in opening files");
		}
		catch (IOException e)
		{
			System.err.println ("Error in IO");
		}
		try
		{
			while(eventinput.hasNext())
			{
				line = eventinput.nextLine();
				if (line.contains(":"))
				{
					String[] temp = line.split(":");
					for(String data:temp)
					{
						if(data.trim() == "")
						{
							eventdata.add("99999");
						}
						else
						{
							eventdata.add(data);
						}
					}
				}
			}
			while(statsinput.hasNext())
			{
				line = statsinput.nextLine();
				if (line.contains(":"))
				{
					String[] temp = line.split(":");
					for(String data:temp)
					{
						statsdata.add(data);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in reading file.");
		}
	}
}
