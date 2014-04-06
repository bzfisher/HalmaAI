package s260488774;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class HalmaAutoRun
{
	static Random r = new Random();

	public static void main( String[] args ) throws IOException {
		System.out.println("running!");
		ArrayList<String> runCommands = new ArrayList<String>();
		runCommands.add("java -cp /root/projectsrc.jar:/root/src boardgame.Client s260488774.s260488774AlphaBetaPlayer");
		runCommands.add("java -cp /root/projectsrc.jar:/root/src boardgame.Client s260488774.s260488774minimaxPlayer");
		runCommands.add("java -cp /root/projectsrc.jar:/root/src boardgame.Client s260488774.s260488774PlayerHeuristic");
		Process p;
		while(true)
		{
			StringBuffer combinedCommands = new StringBuffer();
			for (int j=0; j<4; j++)
			{
				int nextCommandIndex = r.nextInt(runCommands.size());
				combinedCommands.append(runCommands.get(nextCommandIndex));
				combinedCommands.append(" & ");
			}
			
			String[] cmd = {
					"/bin/sh",
					"-c",
					combinedCommands.toString()
			};
			p = Runtime.getRuntime().exec(cmd);

			try
			{
				Thread.sleep(90000);
				p.destroy();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
