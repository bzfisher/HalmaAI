package s260488774;

import halma.CCBoard;
import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class s260488774Player extends Player
{

	/** Provide a default public constructor */
	public s260488774Player() { super("random"); }
	public s260488774Player(String s) { super(s); }

	public Board createBoard() { return new CCBoard(); }

	@Override
	public Move chooseMove(Board inputBoard)
	{
		CCBoard board = (CCBoard) inputBoard;
		return boardAnalyzer.maxMove(board);
	}
}
