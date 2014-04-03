package s260488774.heusticiPlayer;

import halma.CCMove;

public class MoveAndScore
{
	private CCMove aMove;
	private double aScore;

	public MoveAndScore(CCMove move, double score)
	{
		aMove = move;
		aScore = score;
	}

	public CCMove getaMove()
	{
		return aMove;
	}

	public double getaScore()
	{
		return aScore;
	}

}
