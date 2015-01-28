import java.util.*;
import java.lang.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class PuzzlePiece 
{
	//Piece that is a part of the jigsaw puzzle
	BufferedImage b = null;
	Coordinate original = null;
	Coordinate current = null;
	
	public PuzzlePiece(BufferedImage b, Coordinate og)
	{
		this.b = b;
		original = og;
		current = og;
	}
	
	public boolean correctSpot()
	{
		return((original.getX() == current.getX()) && (original.getY() == current.getY()));
	}
	
	public BufferedImage getImage()
	{
		return b;
	}
	
	public Coordinate getCurrent()
	{
		return current;
	}
	
	public void changeSpot(int x, int y)
	{
		current = new Coordinate(x, y);
	}
	
	public Coordinate getOG()
	{
		return original;
	}
	
}
