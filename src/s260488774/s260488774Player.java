package s260488774;

import halma.CCBoard;
import halma.CCMove;

import java.util.ArrayList;
import java.util.Random;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class s260488774Player extends Player
{
	private Random r = new Random();

	public CCMove lastMove = new CCMove(0,null,null);
	/** Provide a default public constructor */
	public s260488774Player() { super("random"); }
	public s260488774Player(String s) { super(s); }

	public Board createBoard() { return new CCBoard(); }

	@Override
	public Move chooseMove(Board inputBoard)
	{
		CCBoard board = (CCBoard) inputBoard;
		return miniMax(board, 2);
	}

	/**
	 * Top level function for minimax recursion algorithm. Calls the helper (which does the actual recursion), and proccesses and results.
	 * @param board the current game board. 
	 * @param iterationsLeft number of iterations we wish to perform. 
	 * @return The best move according to the mini-max algorithm.
	 */
	private CCMove miniMax(CCBoard board, int iterationsLeft)
	{
		ArrayList<CCMove> legalMoves = board.getLegalMoves();
		double bestScore = boardAnalyzer.boardUtility(board, board.getTurn());
		double originalScore = bestScore;
		CCMove bestMove = legalMoves.get(r.nextInt(legalMoves.size()));
		boolean nullMoveExists = false;
		for (CCMove move: legalMoves)
		{
			if (move.getFrom()!=null)
			{
				CCBoard newBoard = (CCBoard)board.clone();
				newBoard.move(move);
				double currScore = minimaxHelper(newBoard, newBoard.getTurn(), board.getTurn(), iterationsLeft-1);
				if (currScore>bestScore)
				{
					bestScore = currScore;
					bestMove = move;
				}
			}
			else nullMoveExists=true;
		}
		if (bestScore==originalScore)
		{
			if (nullMoveExists) return new CCMove(board.getTurn(), null, null);
		}
		if (lastMove.getFrom()!=null)
		{
			if ((lastMove.getFrom().equals(bestMove.getTo())) && (lastMove.getTo().equals(bestMove.getFrom())))
			{
				lastMove = legalMoves.get(r.nextInt(legalMoves.size()));
				return lastMove;
			}
		}
		lastMove = bestMove;
		return lastMove;
	}



	/**
	 * Performs the actual minimax recursion. Takes into account which team the players are on. 
	 * @param board The board as it currently stands. 
	 * @param turnPlayer the current player
	 * @param actualPlayer the player who called the minimax algorithm from the top level. 
	 * @param iterationsLeft number of iterations we have left. 
	 * @return the score of the best/worst move.
	 */
	private double minimaxHelper(CCBoard board, int turnPlayer, int actualPlayer, int iterationsLeft)
	{
		//if we are at the last turn, return the actual board score.
		if (iterationsLeft<1) return boardAnalyzer.boardUtility(board, actualPlayer);

		//otherwise, analyize the possible moves with recursion.
		ArrayList<CCMove> allowedMoves = board.getLegalMoves();
		double topScore = boardAnalyzer.boardUtility(board, actualPlayer);
		for (CCMove move:allowedMoves)
		{
			//if the move is not an end-turn move, proceed.
			if (move.getFrom()!=null)
			{
				CCBoard newBoard = (CCBoard)board.clone();
				newBoard.move(move);
				double currScore = minimaxHelper(newBoard, newBoard.getTurn(), actualPlayer, iterationsLeft-1);
				
				//score improves for co-op players if it is greater. 
				if (CCBoard.getTeamIndex(turnPlayer)==CCBoard.getTeamIndex(actualPlayer))
				{
					if (currScore>topScore)
					{
						topScore = currScore;
					}	
				}
				
				//score improves for enemy players if it is lesser. 
				else if (CCBoard.getTeamIndex(turnPlayer)==CCBoard.getTeamIndex(actualPlayer))
				{
					if (currScore<topScore)
					{
						topScore = currScore;
					}
				}
			}
		}
		return topScore;
	}
}
