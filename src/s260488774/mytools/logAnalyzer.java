package s260488774.mytools;

import halma.CCBoard;
import halma.CCMove;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class logAnalyzer
{
	private final static String LOG_FILES_LOCATION = "/Users/Ben/git/HalmaAI/logs";
	private static double[] weights = new double[18];
	private final static double ALPHA = 0.001;

	public static void main(String[] args) throws IOException
	{
		int [] positive = {0,3,4,5,6,9,10,11,12,15,16,17};
		int [] negatives = {1,2,7,8,13,14};

		for (int i: positive) weights[i] = .5;
		for (int i: negatives) weights[i] = -.5;

		File folder = new File(LOG_FILES_LOCATION);
		File[] files = folder.listFiles(); 

		for (File logFile: files)
		{
			if (logFile.getName().split("ame").length>1)
			{
				//first, find out which team won.
				int winner = 2;
				BufferedReader br = new BufferedReader(new FileReader(logFile));
				try {
					String line = br.readLine();
					while (line!=null)
					{
						if (line.split("INNER Player-").length>1)
						{
							winner = Integer.parseInt(line.split("INNER Player-")[1]);
						}
						line = br.readLine();
					}
				}
				finally {
					br.close();
				}

				//this list will store arrays with all the x values (first 18 values), and finally the y value(the last value).
				ArrayList<double[]> xAndYValues = new ArrayList<double[]>();

				BufferedReader br2 = new BufferedReader(new FileReader(logFile));
				CCBoard board = new CCBoard();
				try {
					String line = br2.readLine();
					while (line!=null)
					{
						if (line.charAt(0)=='0' || line.charAt(0)=='1' || line.charAt(0)=='2' || line.charAt(0)=='3')
						{
							CCMove nextMove = new CCMove(line);
							board.move(nextMove);
							for (int currID = 0; currID<2;currID++)
							{
								double[] currXValues = new double[19];
								for (int i=0; i<currXValues.length; i++) currXValues[i] = 0;

								if (HalmaHeuristics.piecesInBase(currID, board))
								{
									currXValues[0] = HalmaHeuristics.distanceFromBase(currID,board);
									currXValues[1] = HalmaHeuristics.offCentreDistance(currID,board);
									currXValues[2] = HalmaHeuristics.splitDistance(currID,board);
									currXValues[3] = HalmaHeuristics.checkIfWin(currID, board);
									currXValues[4] = HalmaHeuristics.NumberOfPiecesAtEdgeOfTarget(currID, board);
									currXValues[5] = HalmaHeuristics.NumberOfPiecesAtNonEdgeOfTarget(currID, board);
								}

								else if (HalmaHeuristics.NumberOfPiecesAtNonEdgeOfTarget(currID, board)!=0 || HalmaHeuristics.NumberOfPiecesAtEdgeOfTarget(currID, board)!=0)
								{
									currXValues[12] = HalmaHeuristics.distanceFromBase(currID,board);
									currXValues[13] = HalmaHeuristics.offCentreDistance(currID,board);
									currXValues[14] = HalmaHeuristics.splitDistance(currID,board);
									currXValues[15] = HalmaHeuristics.checkIfWin(currID, board);
									currXValues[16] = HalmaHeuristics.NumberOfPiecesAtEdgeOfTarget(currID, board);
									currXValues[17] = HalmaHeuristics.NumberOfPiecesAtNonEdgeOfTarget(currID, board);
								}
								else
								{
									currXValues[6] = HalmaHeuristics.distanceFromBase(currID,board);
									currXValues[7] = HalmaHeuristics.offCentreDistance(currID,board);
									currXValues[8] = HalmaHeuristics.splitDistance(currID,board);
									currXValues[9] = HalmaHeuristics.checkIfWin(currID, board);
									currXValues[10] =HalmaHeuristics.NumberOfPiecesAtEdgeOfTarget(currID, board);
									currXValues[11] =HalmaHeuristics.NumberOfPiecesAtNonEdgeOfTarget(currID, board);
								}
								if (currID==winner) currXValues[18] = 1;
								else currXValues[18] = 0;
								xAndYValues.add(currXValues);
							}
						}
						line = br2.readLine();
					}
				}
				finally {
					br2.close();
				}
				for (double[] array:xAndYValues)
				{
					calculateWeights(array);
				}

			}
		}
		for (double db:weights)
		{
			System.out.println(db);
		}

	}


	public static void calculateWeights(double[] logLine)
	{
		for (int i=0; i<logLine.length-1; i++)
		{
			double reward = logLine[logLine.length-1];
			if (logLine[i]!=0) weights[i] = weights[i] + ALPHA*(reward/logLine[i] - weights[i]);
		}
	}
}
