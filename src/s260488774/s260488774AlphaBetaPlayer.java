package s260488774;

import halma.CCBoard;
import halma.CCMove;
import s260488774.HalmaHeuristics;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class s260488774AlphaBetaPlayer extends Player
{
	private Random r = new Random();
	int startTime;

	public ArrayList<CCMove> previousMovesInTurn = new ArrayList<CCMove>();
	/** Provide a default public constructor */
	public s260488774AlphaBetaPlayer() { super("random"); }
	public s260488774AlphaBetaPlayer(String s) { super(s); }

	public Board createBoard() { return new CCBoard(); }

	@Override
	public Move chooseMove(Board inputBoard)
	{
		startTime = Calendar.getInstance().get(Calendar.MILLISECOND);
		CCBoard board = (CCBoard) inputBoard;
		return alphaBeta(board, 4);
	}

	/**
	 * Top level function for minimax recursion algorithm. Calls the helper (which does the actual recursion), and proccesses and results.
	 * @param board the current game board. 
	 * @param iterationsLeft number of iterations we wish to perform. 
	 * @return The best move according to the mini-max algorithm.
	 */
	private CCMove alphaBeta(CCBoard board, int iterationsLeft)
	{
		ArrayList<CCMove> legalMoves = board.getLegalMoves();
		double bestScore = HalmaHeuristics.boardUtility(board, board.getTurn());
		double originalScore = bestScore;

		CCMove bestMove = legalMoves.get(r.nextInt(legalMoves.size()));
		boolean nullMoveExists = false;
		for (CCMove move: legalMoves)
		{
			if (move.getFrom()!=null)
			{
				CCBoard newBoard = (CCBoard)board.clone();
				newBoard.move(move);
				double currScore = alphaBetaHelper(newBoard, iterationsLeft-1,Double.MIN_VALUE, Double.MAX_VALUE,  newBoard.getTurn(), board.getTurn());
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

	private double alphaBetaHelper(CCBoard board,  int depth, double alpha, double beta,int turnPlayer, int actualPlayer)
	{
		int currentTime = Calendar.getInstance().get(Calendar.MILLISECOND);
		//if we are at the last turn, return the actual board score.
		if (depth<1 || (currentTime-startTime) < 30) return HalmaHeuristics.boardUtility(board, actualPlayer);

		if (CCBoard.getTeamIndex(turnPlayer)==CCBoard.getTeamIndex(actualPlayer))
		{
			ArrayList<CCMove> allowedMoves = board.getLegalMoves();
			for (CCMove move:allowedMoves)
			{
				if (move.getFrom()!=null)
				{
					CCBoard newboard = (CCBoard) board.clone();
					newboard.move(move);
					alpha = Math.max(alpha, alphaBetaHelper(newboard, depth-1, alpha, beta, newboard.getTurn(), actualPlayer));
					if (beta <= alpha) return beta;
				}
			}
			return alpha;
		}
		else
		{
			ArrayList<CCMove> allowedMoves = board.getLegalMoves();
			for (CCMove move:allowedMoves)
			{
				if (move.getFrom()!=null)
				{
					CCBoard newboard = (CCBoard) board.clone();
					newboard.move(move);
					beta = Math.min(beta, alphaBetaHelper(newboard, depth-1, alpha, beta, newboard.getTurn(), actualPlayer));
					if (beta<=alpha) return alpha;
				}
			}
			return beta;
		}
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
