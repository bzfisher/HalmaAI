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


	/** Provide a default public constructor */
	public s260488774Player() { super("random"); }
	public s260488774Player(String s) { super(s); }

	public Board createBoard() { return new CCBoard(); }

	@Override
	public Move chooseMove(Board inputBoard)
	{
		CCBoard board = (CCBoard) inputBoard;
		System.out.println("Original: "+boardAnalyzer.maxMove(board).toPrettyString());
		System.out.println("New: "+minimaxValue(board, null, board.getTurn(), board.getTurn(), 1).getaMove().toPrettyString());
		if (board.getTurn()==0 || board.getTurn()==3) return boardAnalyzer.maxMove(board);
		return minimaxValue(board, null, board.getTurn(), board.getTurn(), 2).getaMove();
	}

	private MoveAndScore minimaxValue(CCBoard board, CCMove lastMove, int turnPlayer, int actualPlayer, int iterationsLeft)
	{
		if (iterationsLeft<1) return new MoveAndScore(lastMove, boardAnalyzer.boardUtility(board, actualPlayer));
		if (CCBoard.getTeamIndex(turnPlayer)==CCBoard.getTeamIndex(actualPlayer))
		{
			ArrayList<CCMove> legalMoves = board.getLegalMoves();
			double currScore = boardAnalyzer.boardUtility(board, board.getTurn());
			double bestScore = currScore;
			CCMove bestMove = legalMoves.get(r.nextInt(legalMoves.size()));
			boolean endTurnMoveExists = false;
			for (CCMove move:legalMoves)
			{
				if (move.getFrom()!=null)
				{
					CCBoard newBoard = (CCBoard)board.clone();
					newBoard.move(move);
					MoveAndScore nextMove = minimaxValue(newBoard, move, newBoard.getTurn(), actualPlayer, iterationsLeft-1);
					if (nextMove.getaScore()>bestScore)
					{
						bestScore = nextMove.getaScore();
						bestMove = nextMove.getaMove();
					}					
				}
				else endTurnMoveExists = true;
			}
			if (bestScore==currScore) 
			{
				if (endTurnMoveExists) return new MoveAndScore(new CCMove(turnPlayer, null, null), currScore);
			}
			return new MoveAndScore(bestMove, bestScore);
		}
		
		
		ArrayList<CCMove> legalMoves = board.getLegalMoves();
		double currScore = boardAnalyzer.boardUtility(board, board.getTurn());
		double bestScore = currScore;
		CCMove bestMove = legalMoves.get(r.nextInt(legalMoves.size()));
		boolean endTurnMoveExists = false;
		for (CCMove move:legalMoves)
		{
			if (move.getFrom()!=null)
			{
				CCBoard newBoard = (CCBoard)board.clone();
				newBoard.move(move);
				MoveAndScore nextMove = minimaxValue(newBoard, move, newBoard.getTurn(), actualPlayer, iterationsLeft-1);
				if (nextMove.getaScore()<bestScore)
				{
					bestScore = nextMove.getaScore();
					bestMove = nextMove.getaMove();
				}
			}
			else endTurnMoveExists = true;
		}
		if (bestScore==currScore) 
		{
			if (endTurnMoveExists) return new MoveAndScore(new CCMove(turnPlayer, null, null), currScore);
		}
		return new MoveAndScore(bestMove, bestScore);
	}
}
