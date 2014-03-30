package s260488774;

import halma.CCMove;

import java.awt.Point;
import java.util.ArrayList;

public class turnAnalyzer
{
	final static Point[] basePoints={new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0),
		new Point(0,1), new Point(1,1), new Point(2,1), new Point(3,1),
		new Point(0,2), new Point(1,2), new Point(2,2),
		new Point(0,3), new Point(1,3)};

	/**
	 * @param moves the list of all possible legal moves
	 * @return the list of the "best" moves.
	 */
	public static ArrayList<CCMove> findBestMoves(ArrayList<CCMove> moves)
	{
		ArrayList<CCMove> bestMoves = new ArrayList<CCMove>();
		bestMoves.add(moves.get(0));
		int bestMoveScore = moveScore(moves.get(0));
		for (CCMove move : moves)
		{
			if (moveScore(move)>bestMoveScore)
			{
				bestMoveScore = moveScore(move);
				bestMoves.clear();
				bestMoves.add(move);
			}
			else if (moveScore(move)==bestMoveScore)
			{
				bestMoves.add(move);
			}
		}
		return bestMoves;
	}
	/**
	 * @param move the move to be analyzed
	 * @return the score (between 1 and 4) of the move. Higher is better.
	 */
	private static int moveScore(CCMove move)
	{
		return IsGoodDiagnalMove(move)+IsGoodMove(move)+2*moveFromEndzone(move)+isHop(move);
	}

	/**
	 * @param move the move to analyze
	 * @return 1 if the move is a diagonal one in the right direction; 0 otherwise.
	 */
	private static int IsGoodDiagnalMove(CCMove move)
	{
		if ((move.getFrom().getX()<move.getTo().getX()) && (move.getFrom().getY()<move.getTo().getY())){
			return 1;
		}
		else return 0;
	}

	/**
	 * @param move the move to analyze
	 * @return 1 if the move is in the right horizonal/vertical direction; 0 otherwise.
	 */
	private static int IsGoodMove(CCMove move)
	{
		if ((move.getFrom().getX()<move.getTo().getX()) || (move.getFrom().getY()<move.getTo().getY())){
			return 1;
		}
		else return 0;
	}

	/**
	 * @param move the move to analyze
	 * @return 1 if the move originates from a basepoint; 0 otherwise.
	 */
	private static int moveFromEndzone(CCMove move)
	{
		for (Point basePt: basePoints)
		{
			if (move.getFrom().equals(basePt)) return 1;
		}
		return 0;
	}

	/**
	 * @param move the move to analyze
	 * @return 1 if the move is a hop; 0 otherwise.
	 */
	private static int isHop(CCMove move)
	{
		if (move.isHop()) return 1;
		return 0;
	}

}
