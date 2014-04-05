package s260488774;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class HalmaAutoRun
{
	static Random r = new Random();

	public static void main( String[] args ) throws IOException {

		for(int i = 0; i<3; i++)
		{
			ArrayList<String> runCommands = new ArrayList<String>();
			runCommands.add("java -cp ~/projectsrc.jar:/Users/Ben/git/HalmaAI/src boardgame.Client s260488774.player1");
			runCommands.add("java -cp ~/projectsrc.jar:/Users/Ben/git/HalmaAI/src boardgame.Client s260488774.player1");
			runCommands.add("java -cp ~/projectsrc.jar:/Users/Ben/git/HalmaAI/src boardgame.Client s260488774.player1");
			runCommands.add("java -cp ~/projectsrc.jar:/Users/Ben/git/HalmaAI/src boardgame.Client s260488774.player1");
			StringBuffer combinedCommands = new StringBuffer();
			while(!runCommands.isEmpty())
			{
				int nextCommandIndex = r.nextInt(runCommands.size());
				combinedCommands.append(runCommands.get(nextCommandIndex));
				runCommands.remove(nextCommandIndex);
				combinedCommands.append(" & ");
			}
			String[] cmd = {
					"/bin/sh",
					"-c",
					combinedCommands.toString()
			};
			Runtime.getRuntime().exec(cmd);

			try
			{
				Thread.sleep(30000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void recalculateWeights(String playerFile, String outcomeFile) throws IOException
	{
		ArrayList<Double> previousWeights = new ArrayList<Double>();
		BufferedReader br = new BufferedReader(new FileReader(playerFile));
		try {
			String line = br.readLine();
			while (line!=null)
			{
				previousWeights.add(Double.parseDouble(line));
				line = br.readLine();
			}
		}
		finally {
			br.close();
		}
		BufferedReader outcomeFileReader = new BufferedReader(new FileReader(outcomeFile));
		String lastLine = null;
		try {
			String line = outcomeFileReader.readLine();
			while (line!=null)
			{
				lastLine = line;
				line = br.readLine();
			}
		}
		finally {
			br.close();
		}
		boolean team0Winner = false;
		boolean team1Winner = false;
		if (lastLine.split("Player-0").length>1) team0Winner = true;
		else if (lastLine.split("Player-1").length>1) team1Winner = true;
		
	}
	
//	public boolean isWinner(int playerID)
//	{
//		
//	}

	private double updateWeight(double previousWeight, int n, int v, double k)
	{
		double top = (previousWeight*(n-1))  + (k*v);
		double bottom = (n+k-1);
		return top/bottom;
	}

}
