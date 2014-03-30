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
		return bestMove(board);
	}

	public CCMove bestMove(CCBoard inputBoard)
	{
		double currScore = boardAnalyzer.analyize(inputBoard, inputBoard.getTurn());
		double bestScore = currScore;
		CCMove bestMove = inputBoard.getLegalMoves().get(r.nextInt(inputBoard.getLegalMoves().size()));
		for (CCMove move:inputBoard.getLegalMoves())
		{
			if (move.getFrom()!=null)
			{
				CCBoard newBoard = (CCBoard)inputBoard.clone();
				newBoard.move(move);
				if ((boardAnalyzer.analyize(newBoard, inputBoard.getTurn())>bestScore))
				{
					bestScore = boardAnalyzer.analyize(newBoard, inputBoard.getTurn());
					bestMove = move;
				}
			}
		}
		System.out.println("before: "+currScore+" after: "+bestScore+" by: "+bestMove.toPrettyString());
		return bestMove;
	}






}
