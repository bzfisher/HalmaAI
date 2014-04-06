package previousPlayers;

import halma.CCBoard;
import halma.CCMove;

import java.util.Random;

import previousPlayers.PreviousHalmaHeuristics;
import boardgame.Board;
import boardgame.Move;
import boardgame.Player;


public class PreviousPlayerHeuristic extends Player
{
	private static Random r = new Random();


	/** Provide a default public constructor */
	public PreviousPlayerHeuristic() { super("random"); }
	public PreviousPlayerHeuristic(String s) { super(s); }
	
	public Board createBoard() { return new CCBoard(); }
	
	@Override
	public Move chooseMove(Board inputBoard)
	{
		CCBoard board = (CCBoard) inputBoard;
		return heuristicSearch(board);
	}
	
	public static CCMove heuristicSearch(CCBoard inputBoard)
	{
		//initialize the best score to be the current score; this way we won't do any worse.
		double currScore = PreviousHalmaHeuristics.boardUtility(inputBoard, inputBoard.getTurn());
		double bestScore = currScore;

		CCMove bestMove = inputBoard.getLegalMoves().get(r.nextInt(inputBoard.getLegalMoves().size()));
		boolean endTurnMoveExists =false;

		for (CCMove move:inputBoard.getLegalMoves())
		{
			if (move.getFrom()!=null)
			{
				CCBoard newBoard = (CCBoard)inputBoard.clone();
				newBoard.move(move);
				if ((PreviousHalmaHeuristics.boardUtility(newBoard, inputBoard.getTurn())>bestScore))
				{
					bestScore = PreviousHalmaHeuristics.boardUtility(newBoard, inputBoard.getTurn());
					bestMove = move;
				}
			}
			else
			{
				endTurnMoveExists = true;
			}
		}
		
		//if we didn't improve our score, than return an empty move if possible
		if (bestScore==currScore) 
		{
			if (endTurnMoveExists) return new CCMove(inputBoard.getTurn(), null, null);
		}
		//else, if we can't return an empty move or we improved our score, return the best move.
		return bestMove;
	}
}
