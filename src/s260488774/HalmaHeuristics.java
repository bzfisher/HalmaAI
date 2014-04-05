package s260488774;

import halma.CCBoard;

import java.awt.Point;

public class HalmaHeuristics
{
	private final static int DISTANCE_FROM_BASE_MULTIPLIER = 1;
	private final static int OFF_CENTRE_DISTANCE_MULTIPLIER = 1;
	private final static int SPLIT_DISTANCE_MULTIPLIER = 1;


	/**
	 * the "farthest" base point for each player.
	 */
	private static Point[] bases = {	new Point(0,0), 
		new Point(15,0),
		new Point(0,15),
		new Point(15,15)};
	
	/**
	 * the furthest target zone point for each player.
	 */
	private static Point[] targets = {	new Point(15,15), 
		new Point(0,15),
		new Point(15,0),
		new Point(0,0)};

	
	/**
	 * @param board the current gameboard.
	 * @param currPlayer the player for which we wish to calculate the board score.
	 * @return the value of the current board to the current player.
	 */
	public static double boardUtility(CCBoard board, int currPlayer)
	{		
		double currPlayersScore = 0;
		double enemyPlayersScore = 0;

		for (int i=0;i<4;i++)
		{
			if (CCBoard.getTeamIndex(i)==CCBoard.getTeamIndex(currPlayer))
			{
				currPlayersScore = currPlayersScore+(DISTANCE_FROM_BASE_MULTIPLIER*distanceFromBase(i,board))-(OFF_CENTRE_DISTANCE_MULTIPLIER*offCentreDistance(i,board))-(SPLIT_DISTANCE_MULTIPLIER*splitDistance(i,board));
			}
			else
			{
				enemyPlayersScore = enemyPlayersScore+(DISTANCE_FROM_BASE_MULTIPLIER*distanceFromBase(i,board))-(OFF_CENTRE_DISTANCE_MULTIPLIER*offCentreDistance(i,board))-(SPLIT_DISTANCE_MULTIPLIER*splitDistance(i,board));
			}
		}
		return currPlayersScore-enemyPlayersScore;
	}




	/**
	 * @param playerID the player ID for whom we wish to find the total manhattan distance.
	 * @param board the current game board.
	 * @return the total manhattan distance between all the player's pieces and the "furthest" base point (the corner point). Higher is better for the player.
	 */
	private static double distanceFromBase(int playerID, CCBoard board)
	{
		double playerDist = 0;
		for (Point pt: board.getPieces(playerID))
		{
			playerDist = playerDist+manhattanDistance(pt,bases[playerID]);
		}
		return playerDist;
	}

	/**
	 * @param location the current location.
	 * @param target the target point.
	 * @return the manhattan distance between current location and the target point.
	 */
	private static double manhattanDistance(Point location, Point target)
	{
		double distX = Math.abs(location.getX() - target.getX());
		double distY = Math.abs(location.getY() - target.getY());
		return distX+distY;
	}


	/**
	 * @param playerID the current player
	 * @param board the current board
	 * @return the value of how far "off center" the player's pieces are. Lower is better.
	 */
	private static double offCentreDistance(int playerID, CCBoard board)
	{
		double playerDist = 0;
		if (playerID == 0)
		{
			for (Point pt: board.getPieces(playerID))
			{
				playerDist = playerDist + Math.abs(pt.getX() - pt.getY());
			}
		}
		if (playerID == 1)
		{
			for (Point pt: board.getPieces(playerID))
			{
				playerDist = playerDist + Math.abs((15-pt.getX()) - pt.getY());
			}
		}
		if (playerID == 2)
		{
			for (Point pt: board.getPieces(playerID))
			{
				playerDist = playerDist + Math.abs(pt.getX() - (15-pt.getY()));
			}
		}
		if (playerID == 3)
		{
			for (Point pt: board.getPieces(playerID))
			{
				playerDist = playerDist + Math.abs((15-pt.getX()) - (15-pt.getY()));
			}
		}
		return playerDist;
	}

	/**
	 * @param playerID the current player.
	 * @param board the current board.
	 * @return the manahattan distance between the frontmost piece and the backmost piece. Lower is better.
	 */
	private static double splitDistance(int playerID, CCBoard board)
	{
		Point furthestPt = bases[playerID];
		Point closestPt = targets[playerID];
		for (Point pt: board.getPieces(playerID))
		{
			if (manhattanDistance(closestPt, bases[playerID])>manhattanDistance(pt, bases[playerID]))
			{
				closestPt = pt;
			}
			if (manhattanDistance(furthestPt, bases[playerID])<manhattanDistance(pt, bases[playerID]))
			{
				furthestPt = pt;
			} 
		}
		return manhattanDistance(furthestPt, closestPt);
	}
}
