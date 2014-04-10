package s260488774;

import halma.CCBoard;
import halma.CCMove;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import s260488774.mytools.HalmaHeuristics;
import s260488774.mytools.monteCarloTreeNode;
import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class s260488774Player extends Player
{
	private final static int STOP_WHEN_MILLISECONDS_LEFT = 30;

	int startTime;

	//Constructors
	public s260488774Player() { super("s260488774Player"); }
	public s260488774Player(String s) { super(s); }
	public Board createBoard() { return new CCBoard(); }

	@Override
	public Move chooseMove(Board inputBoard)
	{
		startTime = Calendar.getInstance().get(Calendar.MILLISECOND);

		//cast the board
		CCBoard board = (CCBoard) inputBoard;

		if (HalmaHeuristics.NumberOfPiecesAtEdgeOfTarget(board.getTurn(), board)>0.1)
			return endGamemonteCarloSearch(board, 1000000);
		return monteCarloSearch(board, 1000000);
	}

	public CCMove endGamemonteCarloSearch(CCBoard board, int numberOfIterations)
	{
		ArrayList<monteCarloTreeNode> nodes = new ArrayList<monteCarloTreeNode>();
		for (CCMove move : board.getLegalMoves())
		{
			CCBoard newboard = (CCBoard) board.clone();
			newboard.move(move);
			nodes.add(new monteCarloTreeNode(newboard, board.getTurn(), 1, move, move));
		}
		return monteCarloSearchHelper(numberOfIterations, nodes).getParentMove();
	}

	public CCMove monteCarloSearch(CCBoard board, int numberOfIterations)
	{
		ArrayList<monteCarloTreeNode> nodes = new ArrayList<monteCarloTreeNode>();
		for (CCMove move : board.getLegalMoves())
		{
			CCBoard newboard = (CCBoard) board.clone();
			newboard.move(move);
			nodes.add(new monteCarloTreeNode(newboard, board.getTurn(), 1, move));
		}
		return monteCarloSearchHelper(numberOfIterations, nodes).getParentMove();
	}

	public monteCarloTreeNode monteCarloSearchHelper(int iterationsLeft, ArrayList<monteCarloTreeNode> nodes)
	{
		int currentTime = Calendar.getInstance().get(Calendar.MILLISECOND);
		if (iterationsLeft < 1 || (currentTime-startTime) < STOP_WHEN_MILLISECONDS_LEFT) 
		{
			return bestNode(nodes);
		}

		monteCarloTreeNode expansionNode =  bestNode(nodes);
		expansionNode.generateChildren();
		for (monteCarloTreeNode n : expansionNode.getChildren())
		{
			nodes.add(n);
		}

		monteCarloTreeNode bestChildNode = bestNode(expansionNode.getChildren());

		expansionNode.setNumberOfVisits(expansionNode.getNumberOfVisits()+1);
		expansionNode.setScore((expansionNode.getScore()+bestChildNode.getScore())/expansionNode.getNumberOfVisits());

		return monteCarloSearchHelper(iterationsLeft-1, nodes);
	}

	private monteCarloTreeNode bestNode(ArrayList<monteCarloTreeNode> nodes)
	{
		Collections.sort(nodes);
		return nodes.get(nodes.size()-1);
	}

}
