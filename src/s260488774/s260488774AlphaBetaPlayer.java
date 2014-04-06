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
	private final static int STOP_WHEN_MILLISECONDS_LEFT = 75;
	
	private Random r = new Random();
	int startTime;

	//Constructors
	public s260488774AlphaBetaPlayer() { super("s260488774Player"); }
	public s260488774AlphaBetaPlayer(String s) { super(s); }
	public Board createBoard() { return new CCBoard(); }

	@Override
	public Move chooseMove(Board inputBoard)
	{
		startTime = Calendar.getInstance().get(Calendar.MILLISECOND);
		
		//cast the board
		CCBoard board = (CCBoard) inputBoard;
		
		//if we are nearing the end of the game, pursue less iteration depth.
		if (HalmaHeuristics.NumberOfPiecesAtNonEdgeOfTarget(board.getTurn(), board)!=0 || HalmaHeuristics.NumberOfPiecesAtEdgeOfTarget(board.getTurn(), board)!=0)
		{
			return alphaBeta(board, 3);
		}

		//else, pursue a depth of 4
		return alphaBeta(board, 4);
	}

	/**
	 * Top level function for alpha-beta pruning recursion algorithm. Calls the helper (which does the actual recursion), and proccesses and results.
	 * @param board the current game board. 
	 * @param iterationsLeft number of iterations we wish to perform. 
	 * @return The best move according to the alpha-beta algorithm.
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

		if (bestScore==originalScore)
		{
			if (nullMoveExists) bestMove =  new CCMove(board.getTurn(), null, null);
		}

		return bestMove;
	}

	private double alphaBetaHelper(CCBoard board,  int depth, double alpha, double beta,int turnPlayer, int actualPlayer)
	{
		//if we are at the last turn or are running low on time, cut the calculation short.
		int currentTime = Calendar.getInstance().get(Calendar.MILLISECOND);
		if (depth<1 || (currentTime-startTime) < STOP_WHEN_MILLISECONDS_LEFT) return HalmaHeuristics.boardUtility(board, actualPlayer);

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
}
