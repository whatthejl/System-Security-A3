import java.nio.file.Paths;
import java.io.*; 
import java.util.*;
import java.math.*;
import java.text.DecimalFormat;

public class AnalysisEngine
{
	static ArrayList<String> eventdata = new ArrayList<>();
	static ArrayList<String> statsdata = new ArrayList<>();
	static ArrayList<String> log = new ArrayList<>();
	static int days;
	static int dataread;
	static ArrayList<Double> mean = new ArrayList<>();
	static ArrayList<Double> sd = new ArrayList<>();
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public AnalysisEngine(ArrayList<String> eventdata, ArrayList<String> statsdata, int days, ArrayList<String> log)
	{
		this.eventdata = eventdata;
		this.statsdata = statsdata;
		this.days = days;
		this.log = log;
		this.dataread = 5;
		calculatemean();
		calculatesd();
	}
	
	public ArrayList<Double> getMean ()
	{
		return mean;
	}
	
	public ArrayList<Double> getSD ()
	{
		return sd;
	}
	
	public void calculatemean()
	{
		for (int i = 0; i < dataread; i++)
		{
			double total = 0.0;
			for (int k = i; k < log.size(); k+=5)
			{
				double temp = Double.parseDouble(log.get(k));
				total += temp;
			}
			double ave = total/days;
			mean.add(ave);
		}
	}
	
	public void calculatesd()
	{
		//formula found online
		for (int k = 0; k < dataread; k++)
		{
			double standarddeviation = 0.0;
			double sq = 0.0;
			double res = 0.0;
			double tempmean = mean.get(k);
			for (int i = k; i < log.size(); i+=5)
			{
				double temp = Double.parseDouble(log.get(i));
				standarddeviation = standarddeviation + Math.pow((temp - tempmean), 2);
			}
			sq = standarddeviation / days;
			res = Math.sqrt(sq);
			sd.add(res);
		}
	}
	
	public void createAnalysisFile(String fileName)
	{
		//prints a stats file format event name:total:mean:std dev
		File myObj = new File (fileName);
		try
		{
			FileWriter myWriter = new FileWriter(fileName);
			myWriter.write(dataread + "\n");
			int headercounter = 0;
			for (int i = 0; i < dataread; i++)
			{
				myWriter.write(eventdata.get(headercounter));
				myWriter.write(":" + df.format(mean.get(i)* days) + ":" + df.format(mean.get(i)) + ":" + df.format(sd.get(i)) + ":\n");
				headercounter += 5;
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