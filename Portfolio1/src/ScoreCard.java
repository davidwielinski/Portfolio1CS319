
public class ScoreCard 
{
	int score;
	String name;
	
	public ScoreCard(int score, String name)
	{
		this.name = name;
		this.score = score;
	}
	
	public int getScore()
	{
		return score;
	}
	
	@Override
	public String toString()
	{
		return name + "    " + score;
	}
}
