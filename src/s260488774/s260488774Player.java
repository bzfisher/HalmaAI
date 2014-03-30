package s260488774;

import halma.CCBoard;
import halma.CCMove;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

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
	private Random r = new Random();
	
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

		//if there are moves other than just the endTurn move, then return a random good move.
		if (moves.size()>0)
		{
			ArrayList<CCMove> bestMoves = turnAnalyzer.findBestMoves(moves);
			return bestMoves.get(r.nextInt(bestMoves.size()));
		}
		return endTurn;
	}

	/**
	 * @param moves the list of legal moves, without endTurn move.
	 * @return the move with the highest score.
	 */


	



}
