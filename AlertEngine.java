import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.io.*; 

public class AlertEngine
{
	static ArrayList<String> eventdata = new ArrayList<>();
	static ArrayList<String> statsdata = new ArrayList<>();
	String fileName;
	static int newdays;
	static int dataread;
	static ArrayList<Double> mean = new ArrayList<>();
	static ArrayList<Double> sd = new ArrayList<>();
	static ArrayList<Double> anomaly = new ArrayList<>();
	static ArrayList<Double> anomalyperday = new ArrayList<>();
	static ArrayList<String> newlog = new ArrayList<>();
	int threshold = 0;
	
	public AlertEngine(ArrayList<String> eventdata, ArrayList<String> statsdata)
	{
		this.eventdata = eventdata;
		this.statsdata = statsdata;
		this.dataread = 5;
		
		getuserinput();
		createLiveData();
		calculateAnomaly();
		checkAnomaly();
		createAlertFile();
	}
	
	public void getuserinput()
	{
		//userinput for live data file name
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter new file name: ");
		fileName = sc.nextLine();
		
		boolean check = false;
		while (check == false)
		{
			//userinput for number of days
			System.out.println("Please enter number of days: ");
			String userInput = sc.nextLine();
			if (isInteger(userInput))
			{
				check = true;
			}
			newdays = Integer.parseInt(userInput);
		}
	}
	
	public void createLiveData()
	{
		//create file from ActivityEngine class with the name from first userinput
		ActivityEngine newactivity = new ActivityEngine(eventdata, statsdata, newdays);
		newactivity.createFile(fileName);
		//new log will be generated and retrieved
		this.newlog = newactivity.retrievelog();
		
		//create new analysis file
		AnalysisEngine newanalysis = new AnalysisEngine(eventdata, statsdata, newdays, newlog);
		//don't need to create the analysis file for live data
		newanalysis.createAnalysisFile("NewAnalysis.txt");
		this.mean = newanalysis.getMean();
		this.sd = newanalysis.getSD();
	}
	
	public void calculateAnomaly()
	{
		int sumsOfWeight = 0;
		for (int k = 0; k < dataread; k++)
		{
			double weight = Double.parseDouble(eventdata.get(k*5+4));
			sumsOfWeight += weight;
			for (int i = k; i < newlog.size(); i+=5)
			{
				double tempdata = Double.parseDouble(newlog.get(i));
				double tempanomaly;
				if (tempdata > mean.get(k))
				{
					tempanomaly = (tempdata - mean.get(k)) / sd.get(k) * weight;
				}
				else
				{
					tempanomaly = (mean.get(k) - tempdata) / sd.get(k) * weight;
				}
				anomaly.add(tempanomaly);
			}
		}
		
		for (int i = 0; i < newdays; i++)
		{
			double tempanomaly = 0.0;
			for (int k = i; k < anomaly.size(); k+=newdays)
			{
				tempanomaly += anomaly.get(k);
			}
			anomalyperday.add(tempanomaly);
		}
		threshold = 2 * sumsOfWeight;
	}
	
	public void checkAnomaly()
	{
		for (int i = 0; i < anomalyperday.size(); i++)
		{
			if (anomalyperday.get(i) > threshold)
			{
				System.out.println("Day " + (i+1) + " exceeds the threshold");
			}
		}
	}
	
	public void createAlertFile()
	{
		File myObj = new File ("Anomaly.txt");
		try
		{
			FileWriter myWriter = new FileWriter("Anomaly.txt");
			myWriter.write("Threshold : " + threshold);
			for (int i = 0; i < anomalyperday.size(); i++)
			{
				myWriter.write("Day " + (i+1) + ": " + anomalyperday.get(i) + "\n");
			}
			myWriter.flush();
			myWriter.close();
		}
		catch (IOException e)
		{
			System.out.println("An error occurred.");
		}
	}
	
	public static boolean isInteger(String input)
	{
		try 
		{
			Integer.parseInt(input);
			return true;
		}
		catch( Exception e )
		{
			return false;
		}
	}
}