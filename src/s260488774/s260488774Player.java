package s260488774;

import halma.CCBoard;
import halma.CCMove;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class s260488774Player extends Player
{
	private Random r = new Random();
	int startTime;
	
	public ArrayList<CCMove> previousMovesInTurn = new ArrayList<CCMove>();
	/** Provide a default public constructor */
	public s260488774Player() { super("random"); }
	public s260488774Player(String s) { super(s); }

	public Board createBoard() { return new CCBoard(); }

	@Override
	public Move chooseMove(Board inputBoard)
	{
		startTime = Calendar.getInstance().get(Calendar.MILLISECOND);
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
		
		//if we already played this move this turn, choose a random move instead.
		for (CCMove previousMove: previousMovesInTurn)
		{
			if (bestMove.getFrom()!=null)
			if (movesEqual(previousMove, bestMove))
			{
				bestMove = legalMoves.get(r.nextInt(legalMoves.size()));
			}
		}
		
		if (bestScore==originalScore)
		{
			if (nullMoveExists) bestMove =  new CCMove(board.getTurn(), null, null);
		}
		
		//if this is the last move this turn, clear our previousMovesInTurn list.
		CCBoard newBoard = (CCBoard)board.clone();
		newBoard.move(bestMove);
		if (newBoard.getTurn()!=board.getTurn())
		{
			previousMovesInTurn.clear();
			return bestMove;
		}
		
		//else, add the move to the previous moves list, and return it.
		previousMovesInTurn.add(bestMove);
		return bestMove;
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
		int currentTime = Calendar.getInstance().get(Calendar.MILLISECOND);
		//if we are at the last turn, return the actual board score.
		if (iterationsLeft<1 || (currentTime-startTime) < 100) return boardAnalyzer.boardUtility(board, actualPlayer);

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
	
	private boolean movesEqual(CCMove move1, CCMove move2)
	{
		Point from1 = move1.getFrom();
		Point from2 = move2.getFrom();
		Point to1 = move1.getTo();
		Point to2 = move2.getTo();
		if (from1.getX()==from2.getX() && from1.getY()==from2.getY())
		{
			if (to1.getX()==to2.getX() && to1.getY()==to2.getY())  return true;
		}
		return false;
	}
}
