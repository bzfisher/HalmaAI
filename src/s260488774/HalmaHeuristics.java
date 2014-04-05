package s260488774;

import halma.CCBoard;

import java.awt.Point;

public class HalmaHeuristics
{
	private final static int DISTANCE_FROM_BASE_MULTIPLIER = 1;
	private final static int OFF_CENTRE_DISTANCE_MULTIPLIER = 1;
	private final static int SPLIT_DISTANCE_MULTIPLIER = 1;
	private final static int CHECK_IF_WIN_MULTIPLIER = 1;
	private final static int NON_EDGE_TARGET_PIECES_MULTIPLIER = 1;
	private final static int EDGE_TARGET_PIECES_MULTIPLIER = 1;

	/**
	 * the "farthest" base point for each player.
	 */
	private static Point[] cornerBasePoint= {	new Point(0,0), 
		new Point(15,0),
		new Point(0,15),
		new Point(15,15)};

	private static Point[][] edgeBasePoints = 
		{
		{
			new Point(0,0),new Point(3,0),new Point(2,0),new Point(1,0),new Point(0,3),new Point(0,2),new Point(0,1)
		},
		{
			new Point(15,0),new Point(14,0),new Point(13,0),new Point(12,0),new Point(15,3),new Point(15,2),new Point(15,1)
		},
		{
			new Point(0,15),new Point(3,15),new Point(2,15),new Point(1,15),new Point(0,12),new Point(0,14),new Point(0,13)
		},
		{
			new Point(13,15),new Point(12,15),new Point(15,15),new Point(14,15),new Point(15,12),new Point(15,14),new Point(15,13)
		}
		};
	
	private static Point[][] nonEdgeBasePoints = 
		{
		{
			new Point(2,2),new Point(1,2),new Point(2,1),new Point(1,1),new Point(3,1),new Point(1,3)
		},
		{
			new Point(14,3),new Point(14,1),new Point(14,2),new Point(12,1),new Point(13,2),new Point(13,1)
		},
		{
			new Point(2,13),new Point(1,13),new Point(3,14),new Point(1,12),new Point(2,14),new Point(1,14)
		},
		{
			new Point(12,14),new Point(14,12),new Point(13,14),new Point(14,14),new Point(14,13),new Point(13,13)
		}
		};

	/**
	 * the furthest target zone point for each player.
	 */
	private static Point[] targetCorners = {	new Point(15,15), 
		new Point(0,15),
		new Point(15,0),
		new Point(0,0)};

	private static int[] oppositePlayerID = {3, 2, 1, 0};
	/**
	 * @param board the current game board.
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
				currPlayersScore = currPlayersScore+(DISTANCE_FROM_BASE_MULTIPLIER*distanceFromBase(i,board))-(OFF_CENTRE_DISTANCE_MULTIPLIER*offCentreDistance(i,board))-(SPLIT_DISTANCE_MULTIPLIER*splitDistance(i,board)) + (CHECK_IF_WIN_MULTIPLIER*checkIfWin(i, board)) + (EDGE_TARGET_PIECES_MULTIPLIER*NumberOfPiecesAtEdgeOfTarget(i, board)) + (NON_EDGE_TARGET_PIECES_MULTIPLIER*NumberOfPiecesAtNonEdgeOfTarget(i, board));
			}
			else
			{
				enemyPlayersScore = enemyPlayersScore+(DISTANCE_FROM_BASE_MULTIPLIER*distanceFromBase(i,board))-(OFF_CENTRE_DISTANCE_MULTIPLIER*offCentreDistance(i,board))-(SPLIT_DISTANCE_MULTIPLIER*splitDistance(i,board))+(CHECK_IF_WIN_MULTIPLIER*checkIfWin(i, board)) + (EDGE_TARGET_PIECES_MULTIPLIER*NumberOfPiecesAtEdgeOfTarget(i, board)) + (NON_EDGE_TARGET_PIECES_MULTIPLIER*NumberOfPiecesAtNonEdgeOfTarget(i, board));
			}
		}
		return currPlayersScore-enemyPlayersScore;
	}




	/**
	 * @param playerID the player ID for whom we wish to find the total Manhattan distance.
	 * @param board the current game board.
	 * @return the total Manhattan distance between all the player's pieces and the "farthest" base point (the corner point). Higher is better for the player.
	 */
	private static double distanceFromBase(int playerID, CCBoard board)
	{
		double playerDist = 0;
		for (Point pt: board.getPieces(playerID))
		{
			playerDist = playerDist+manhattanDistance(pt,cornerBasePoint[playerID]);
		}
		return playerDist;
	}

	/**
	 * @param location the current location.
	 * @param target the target point.
	 * @return the Manhattan distance between current location and the target point.
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
	 * @return the Manhattan distance between the front most piece and the back most piece. Lower is better.
	 */
	private static double splitDistance(int playerID, CCBoard board)
	{
		Point furthestPt = cornerBasePoint[playerID];
		Point closestPt = targetCorners[playerID];
		for (Point pt: board.getPieces(playerID))
		{
			if (manhattanDistance(closestPt, cornerBasePoint[playerID])>manhattanDistance(pt, cornerBasePoint[playerID]))
			{
				closestPt = pt;
			}
			if (manhattanDistance(furthestPt, cornerBasePoint[playerID])<manhattanDistance(pt, cornerBasePoint[playerID]))
			{
				furthestPt = pt;
			} 
		}
		return manhattanDistance(furthestPt, closestPt);
	}

	
	/**
	 * Checks if the player has won. Higher is better.
	 * @param ID the player ID we are checking for. 
	 * @param board the board game as it currently is.
	 * @return 1 if the player has won, 0 if not.
	 */
	private static double checkIfWin(int ID, CCBoard board){
		assert(ID<4);
		boolean win=true;
		int base_id= ID^3;
		Integer IDInteger= new Integer(ID);

		for(Point p: edgeBasePoints[base_id]){
			win &= IDInteger.equals(board.getPieceAt(p));
		}
		for(Point p: nonEdgeBasePoints[base_id]){
			win &= IDInteger.equals(board.getPieceAt(p));
		}
		if (win) return 1;
		return 0;
	}
	
	/**
	 * checks how many pieces are at the edges of the target zone. Higher is better. 
	 * @param ID the player ID we are checking for.
	 * @param board the current game board.
	 * @return number of pieces at edge of target zone.
	 */
	private static double NumberOfPiecesAtEdgeOfTarget(int ID, CCBoard board)
	{
		double edgeResult = 0;
		for (Point piece: board.getPieces(ID))
		{
			for (Point targetPoint: edgeBasePoints[oppositePlayerID[ID]])
			{
				if (manhattanDistance(piece, targetPoint)==0)
				{
					edgeResult++;
				}
			}
		}
		return edgeResult;
	}
	
	/**
	 * 	 * checks how many pieces are at the  non-edges of the target zone. Higher is better. 
	 * @param ID the ID to check for. 
	 * @param board the current game board.
	 * @return the number of pieces at the non-edge of the target zone.
	 */
	private static double NumberOfPiecesAtNonEdgeOfTarget(int ID, CCBoard board)
	{
		double nonEdgeResult = 0;
		for (Point piece: board.getPieces(ID))
		{
			for (Point targetPoint: nonEdgeBasePoints[oppositePlayerID[ID]])
			{
				if (manhattanDistance(piece, targetPoint)==0)
				{
					nonEdgeResult++;
				}
			}
		}
		return nonEdgeResult;
	}
	
	

}
