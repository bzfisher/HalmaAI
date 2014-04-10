package s260488774;

import halma.CCBoard;
import halma.CCMove;

import java.util.ArrayList;

import s260488774.mytools.HalmaHeuristics;

public class monteCarloTreeNode implements Comparable<monteCarloTreeNode>
{
	private int ID;
	private CCBoard gameBoard;
	private double score;
	private ArrayList<monteCarloTreeNode> children = new ArrayList<monteCarloTreeNode>();
	private int numberOfVisits;
	private 	CCMove parentMove;

	public CCMove getParentMove()
	{
		return parentMove;
	}

	public monteCarloTreeNode(CCBoard pGameBoard, int currPlayerID, int NumberOfVisits, CCMove pParentMove)
	{
		parentMove = pParentMove;
		numberOfVisits = NumberOfVisits;
		ID = currPlayerID;
		gameBoard = pGameBoard;
		score = HalmaHeuristics.boardUtility(gameBoard, currPlayerID);
	}
	
	public monteCarloTreeNode(CCBoard pGameBoard, int currPlayerID, int NumberOfVisits, CCMove pParentMove, CCMove lastMove)
	{
		parentMove = pParentMove;
		numberOfVisits = NumberOfVisits;
		ID = currPlayerID;
		gameBoard = pGameBoard;
		score = HalmaHeuristics.boardUtility(gameBoard, lastMove, currPlayerID);
	}

	public void generateChildren()
	{
		for (CCMove legalMove : gameBoard.getLegalMoves())
		{
			CCBoard newboard = (CCBoard) gameBoard.clone();
			newboard.move(legalMove);
			children.add(new monteCarloTreeNode(newboard, ID, 1, parentMove, legalMove));
		}
	}

	public double getScore()
	{
		return score;
	}
	
	public void setScore(double pScore)
	{
		score = pScore;
	}

	public ArrayList<monteCarloTreeNode> getChildren()
	{
		return children;
	}
	public int getNumberOfVisits()
	{
		return numberOfVisits;
	}

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
