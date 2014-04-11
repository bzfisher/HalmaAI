package s260488774.mytools;

import halma.CCBoard;
import halma.CCMove;

import java.util.ArrayList;

/**
 * A tree node, containing all the necessary information for a Monte Carlo search.
 *
 */
public class monteCarloTreeNode implements Comparable<monteCarloTreeNode>
{
	private int ID;
	private CCBoard gameBoard;
	private double score;
	private ArrayList<monteCarloTreeNode> children = new ArrayList<monteCarloTreeNode>();
	private int numberOfVisits;
	private CCMove parentMove;

	public CCMove getParentMove()
	{
		return parentMove;
	}

	/**
	 * constructor that does not take into account the move that last to here. 
	 */
	public monteCarloTreeNode(CCBoard pGameBoard, int currPlayerID, int NumberOfVisits, CCMove pParentMove)
	{
		parentMove = pParentMove;
		numberOfVisits = NumberOfVisits;
		ID = currPlayerID;
		gameBoard = pGameBoard;
		score = HalmaHeuristics.boardUtility(gameBoard, currPlayerID);
	}
	
	/**
	 * constructor that does take into account the move that last to here. 
	 */
	public monteCarloTreeNode(CCBoard pGameBoard, int currPlayerID, int NumberOfVisits, CCMove pParentMove, CCMove lastMove)
	{
		parentMove = pParentMove;
		numberOfVisits = NumberOfVisits;
		ID = currPlayerID;
		gameBoard = pGameBoard;
		score = HalmaHeuristics.boardUtility(gameBoard, lastMove, currPlayerID);
		if (lastMove.getTo()!=null)
		if (HalmaHeuristics.manhattanDistance(lastMove.getFrom(),HalmaHeuristics.cornerBasePoint[currPlayerID]) < HalmaHeuristics.manhattanDistance(lastMove.getTo(),HalmaHeuristics.cornerBasePoint[currPlayerID]))
		if (lastMove.isHop()) score = score+.1;
	}

	/**
	 * populates the list of children for this node. Allows for lazy evalution of the search tree.
	 */
	public void generateChildren()
	{
		for (CCMove legalMove : gameBoard.getLegalMoves())
		{
			CCBoard newboard = (CCBoard) gameBoard.clone();
			newboard.move(legalMove);
			children.add(new monteCarloTreeNode(newboard, ID, 1, parentMove, legalMove));
		}
	}

	/**
	 * @return the score of the current node. 
	 */
	public double getScore()
	{
		return score;
	}
	
	/**
	 * @param pScore the score we wish to assign to current node. 
	 */
	public void setScore(double pScore)
	{
		score = pScore;
	}

	/**
	 * @return the list of children of this node. 
	 */
	public ArrayList<monteCarloTreeNode> getChildren()
	{
		return children;
	}
	/**
	 * @return the number of times this node has been "visited" (i.e. its score has been updated)
	 */
	public int getNumberOfVisits()
	{
		return numberOfVisits;
	}

	/**
	 * @param numberOfVisits the number of visits to this node. 
	 */
	public void setNumberOfVisits(int numberOfVisits)
	{
		this.numberOfVisits = numberOfVisits;
	}
	
	@Override
	public int compareTo(monteCarloTreeNode arg0)
	{
		if(score<arg0.getScore()) return -1;
		if(score>arg0.getScore()) return 1;
		return 0;
	}
}
