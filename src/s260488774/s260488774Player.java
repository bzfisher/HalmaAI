package s260488774;

import halma.CCBoard;
import halma.CCMove;

import java.awt.Point;
import java.util.ArrayList;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class s260488774Player extends Player
{

	/** Provide a default public constructor */
	public s260488774Player() { super("random"); }
	public s260488774Player(String s) { super(s); }

	public Board createBoard() { return new CCBoard(); }
	final static Point[] basePoints={new Point(0,0), new Point(1,0), new Point(2,0), new Point(3,0),
		new Point(0,1), new Point(1,1), new Point(2,1), new Point(3,1),
		new Point(0,2), new Point(1,2), new Point(2,2),
		new Point(0,3), new Point(1,3)};

	@Override
	public Move chooseMove(Board inputBoard)
	{
		CCBoard board = (CCBoard) inputBoard;

		//Get all the legal moves, minus endTurn.
		ArrayList<CCMove> moves = new ArrayList<CCMove>();
		CCMove endTurn = board.getLegalMoves().get(0);
		for (CCMove move: board.getLegalMoves())
		{
			if (move.getFrom()!=null) moves.add(move);
			else endTurn=move;
		}

		if (moves.size()>0)
		{
			CCMove bestMove = findBestMove(moves);
			return bestMove;
		}
		return endTurn;
	}

	/**
	 * @param moves the list of legal moves, without endTurn move.
	 * @return the move with the highest score.
	 */
	private CCMove findBestMove(ArrayList<CCMove> moves)
	{
		CCMove bestMove = moves.get(0);
		int bestMoveScore = moveScore(bestMove);
		System.out.println("initial bestMoveScore = "+bestMoveScore);
		for (CCMove move : moves)
		{
			System.out.println(move.toPrettyString()+", "+moveScore(move));
			if (moveScore(move)>bestMoveScore)
			{
				bestMoveScore = moveScore(move);
				bestMove = move;
			}
		}
		return bestMove;
	}

	/**
	 * @param move the move to be analyzed
	 * @return the score (between 1 and 4) of the move. Higher is better.
	 */
	private int moveScore(CCMove move)
	{
		return IsGoodDiagnalMove(move)+IsGoodMove(move)+moveFromEndzone(move)+isHop(move);
	}

	/**
	 * @param move the move to analyze
	 * @return 1 if the move is a diagonal one in the right direction; 0 otherwise.
	 */
	private int IsGoodDiagnalMove(CCMove move)
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
	private int IsGoodMove(CCMove move)
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
	private int moveFromEndzone(CCMove move)
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
	private int isHop(CCMove move)
	{
		if (move.isHop()) return 1;
		return 0;
	}



}
