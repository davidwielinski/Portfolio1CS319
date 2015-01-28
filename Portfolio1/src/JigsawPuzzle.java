import java.util.*;
import java.lang.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;



public class JigsawPuzzle implements ActionListener
{
	JFrame f;
	JLabel whattodo, sizetodo;
	JRadioButton pic1, pic2, userpic, three, four;
	JLabel lionlabel, kenlabel;
	JButton play, toptimes;
	JLabel pathforpic;
	String userpicpath = "";
	JFileChooser fc;
	ButtonGroup picbuttons, numbuttons;
	BufferedImage lionimage, kenimage;
	
	BufferedImage imageUsed = null;
	int size = 0;
	int imgwid, imghei;
	
	JFrame f2;
	PuzzlePiece[][] board = null;
	PuzzlePiece sidespot = null;
	Coordinate emptyspot = null;
	JLabel moves, time;
	JLabel intmoves;
	int movecount = 0;
	ArrayList<JLabel> pieces = new ArrayList<JLabel>();
	JButton quit, pause;
	
	
	JFrame f3;
	JLabel winorloss;
	JButton exitgame, newgame;
	ArrayList<JLabel> topScores = new ArrayList<JLabel>();
	JLabel yourscore;
	String winlose = "";
	int totalscore = 0;
	

	
	public JigsawPuzzle() throws IOException
	{
		f = new JFrame("Jigsaw Puzzle: Selection Screen");
		f.setSize(450, 600);
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//Labels for choosing a pic/size
		whattodo = new JLabel("Choose a picture");
		whattodo.setBounds(5, 5, 100, 20);
		f.add(whattodo);
		sizetodo = new JLabel("Choose a size");
		sizetodo.setBounds(320, 5, 100, 20);
		f.add(sizetodo);
		//Button to hit play
		play = new JButton("Play Game");
		play.setBounds(320, 100, 100, 50);
		play.addActionListener(this);
		f.add(play);
		toptimes = new JButton("Top Scores");
		toptimes.setBounds(320, 180, 100, 50);
		toptimes.addActionListener(this);
		f.add(toptimes);
		//Buttons for choosing the 3 by 3 or 4 by 4
		numbuttons = new ButtonGroup();
		three=new JRadioButton("3 by 3 Puzzle");
		three.addActionListener(this);
		three.setBounds(320, 25, 100, 20);
		four = new JRadioButton("4 by 4 Puzzle");
		four.addActionListener(this);
		four.setBounds(320, 50, 100, 20);
		f.add(three);
		f.add(four);
		numbuttons.add(three);
		numbuttons.add(four);
		//The Images in there
		String lionpath = "C:\\CodingSutff\\EclipseWorks\\Portfolio1\\src\\lion.jpg";
		File lionfile = new File(lionpath);
		lionimage = ImageIO.read(lionfile);
		lionlabel = new JLabel(new ImageIcon(lionimage));
		lionlabel.setBounds(5, 50, 300, 168);
		lionlabel.setVisible(true);
		f.add(lionlabel);
		String kenpath = "C:\\CodingSutff\\EclipseWorks\\Portfolio1\\src\\kennedy.jpg";
		File kenfile = new File(kenpath);
		kenimage = ImageIO.read(kenfile);
		kenlabel = new JLabel(new ImageIcon(kenimage));
		kenlabel.setBounds(5, 250, 297, 211);
		kenlabel.setVisible(true);
		f.add(kenlabel);
		//Buttons for the Picture
		picbuttons = new ButtonGroup();
		pic1=new JRadioButton("Lion Picture");
		pic1.addActionListener(this);
		pic1.setBounds(5, 25, 100, 20);
		pic2=new JRadioButton("Kennedy Picture");
		pic2.setBounds(5, 220, 170, 20);
		pic2.addActionListener(this);
		userpic=new JRadioButton("Picture from your computer");
		userpic.setBounds(5, 470, 200, 20);
		userpic.addActionListener(this);
		f.add(pic1);
		f.add(pic2);
		f.add(userpic);
		picbuttons.add(pic1);
		picbuttons.add(pic2);
		picbuttons.add(userpic);
		f.setLocation(200, 200);
		f.setLayout(null);
		f.setVisible(true);
		
		
		
		f2 = new JFrame("Jigsaw Puzzle: Game");
		f2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if(f2.isVisible())
				{
					int x = arg0.getX();
					int y = arg0.getY();
					
					int xpos = -1, ypos = -1;
					int xwid = imageUsed.getWidth()/size;
					int yhei = imageUsed.getHeight()/size;
					for(int i = 0; i < size; i++)
					{
						if((y > (125 + (yhei*i))) && (y < (125 + (yhei * (i+1)))))
						{
							ypos = i;
							break;
						}
					}
					for(int i = 0; i < size+1; i++)
					{
						if((x > (55 + (xwid*i))) && (x < (55 + (xwid * (i+1)))))
						{
							xpos = i;
							break;
						}
					}
					if(xpos != -1 && ypos != -1)
					{
						PuzzlePiece potentialmove;
						if(xpos == 3)
							potentialmove = sidespot;
						else
							potentialmove = board[xpos][ypos];
						makeMove(potentialmove, board, emptyspot, sidespot);
						SwingUtilities.updateComponentTreeUI(f2);
					}
				}
			}
		});
		f2.setSize(450, 600);
		moves = new JLabel("Total Moves");
		moves.setBounds(50, 25, 100, 20);
		f2.add(moves);
		time = new JLabel("Time Remaining");
		time.setBounds(275, 25, 100, 20);
		f2.add(time);
		intmoves = new JLabel("" + movecount);
		intmoves.setBounds(60, 55, 50, 20);
		f2.add(intmoves);
		for(int i = 0; i < pieces.size(); i++)
			f2.add(pieces.get(i));
		quit = new JButton("Quit");
		quit.setBounds(50, 400, 100, 50);
		quit.addActionListener(this);
		f2.add(quit);
		pause = new JButton("Pause the Game");
		pause.setBounds(250, 400, 150, 50);
		pause.addActionListener(this);
		f2.add(pause);
		f2.setLocation(200, 200);
		f2.setLayout(null);
		f2.setVisible(false);
		
		
		
		
		f3 = new JFrame("Jigsaw Puzzle: Selection Screen");
		f3.setSize(300, 350);
		exitgame = new JButton("Exit Game");
		exitgame.setBounds(25, 225, 100, 50);
		exitgame.addActionListener(this);
		f3.add(exitgame);
		newgame = new JButton ("Play Again");
		newgame.setBounds(175, 225, 100, 50);
		newgame.addActionListener(this);
		f3.add(newgame);
		yourscore = new JLabel("Score: " + totalscore);
		yourscore.setBounds(175, 5, 100, 20);
		f3.add(yourscore);
		String txtpath = "C:\\CodingSutff\\EclipseWorks\\Portfolio1\\src\\topscores.txt";
		File file = new File(txtpath);
		JLabel l = new JLabel("   Top Scores");
		l.setBounds(75, 50, 150, 20);
		f3.add(l);
		topScores.add(l);
		Scanner fileScanner = null;
		try 
		{
			fileScanner = new Scanner(file);
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		int count = 1;
		while(fileScanner.hasNextLine())
		{
			String n = "";
			n += count + ". ";
			n += fileScanner.nextLine().trim();
			JLabel jay = new JLabel(n);
			jay.setBounds(75, 50 + (25 * count), 150, 20);
			f3.add(jay);
			topScores.add(jay);
			count++;
		}
		f3.setLocation(200, 200);
		f3.setLayout(null);
		f3.setVisible(false);
		
	}
	
	public static void main(String args[]) throws IOException
	{
		//Initialization of the JFramework
		new JigsawPuzzle();
	}
	
	//Makes a move on the board, changing the puzzle pieces current position as well
	//as the empty spots current position. And if it is the sideSlot, then it's the sideSlot.
	public void makeMove(PuzzlePiece p, PuzzlePiece[][] board, Coordinate empty, PuzzlePiece sideSlot)
	{
		boolean moveWorks;
		int x = p.getCurrent().getX();
		int y = p.getCurrent().getY();
		
		if(empty.getX() == x)
		{
			if(empty.getY()-1 == y || empty.getY()+1 == y)
				moveWorks = true;
			else
				moveWorks = false;
		}
		else
		{
			if(empty.getY() == y)
			{
				if(empty.getX()-1 == x || empty.getX()+1 == x)
					moveWorks = true;
				else
					moveWorks = false;
			}
			else
				moveWorks = false;	
		}
		
		if(moveWorks)
		{
			if(empty.getY() == size)
			{
				sideSlot = p;
				sideSlot.changeSpot(size-1, size);
				board[x][y] = null;
				empty = new Coordinate(x, y);
				movecount++;
			}
			else
			{
				board[empty.getX()][empty.getY()] = p;
				board[empty.getX()][empty.getY()].changeSpot(empty.getX(), empty.getY());
				board[x][y] = null;
				empty = new Coordinate(x, y);
				movecount++;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(f2, "This spot is not adjacent to the open spot.");
		}
	}
	
	//creates the board given the image, and then shuffles the images.
	public PuzzlePiece[][] createBoard(BufferedImage b, int size)
	{
		int wid = b.getWidth()/size;
		int hei = b.getHeight()/size;
		PuzzlePiece[][] board = new PuzzlePiece[size][size];
		//Create The board
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				BufferedImage img = b.getSubimage(j*wid, i*hei, wid, hei);
				PuzzlePiece p = new PuzzlePiece(img, new Coordinate(i, j));
				board[i][j] = p;
			}
		}
		//Shuffle the board
		do
		{
			for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++)
				{
					if(!((j == size-1) && (i == size-1)))
					{
						int randx, randy;
						do
						{
							randx = (int)(Math.random()*size);
							randy = (int)(Math.random()*size);
						} while((randx == size-1) && (randy == size-1));
					
						PuzzlePiece temp = board[randx][randy];
						board[randx][randy] = board[i][j];
						board[randx][randy].changeSpot(randx, randy);
						board[i][j] = temp;	
						board[i][j].changeSpot(i, j);
					}
				}
			}
		} while(isWinner(board) == true);
		return board;
	}
	
	public PuzzlePiece createOpenSpot(int size)
	{
		return new PuzzlePiece(null, new Coordinate(size-1, size));
	}
	
	public Coordinate setEmptySpot(int size)
	{
		return new Coordinate(size-1, size);
	}
	
	public boolean isWinner(PuzzlePiece[][] piece)
	{
		for(int i = 0; i < piece[0].length; i++)
		{
			for(int j = 0; j < piece[0].length; j++)
			{
				if(piece[i][j].correctSpot() == false)
					return false;
			}
		}
		return true;
	}
	
	public int getScore(int moves, int minutes, int seconds)
	{
		int returnable = 200 - moves + seconds + (minutes*60);
		return returnable;
	}
	
	public static BufferedImage restructure(BufferedImage b)
	{
		int w = b.getWidth();  
	    int h = b.getHeight();
	    int ratio = w/300;
	    BufferedImage dimg = new BufferedImage(w/ratio, h/ratio, b.getType());  
	    Graphics2D g = dimg.createGraphics();  
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	    g.drawImage(b, 0, 0, w/ratio, h/ratio, 0, 0, w, h, null);  
	    g.dispose();  
	    return dimg;
	}
	
	//add a winner to the top scores
	//JFrame isn't set yet
	public void addToTopScores(String name, int score)
	{
		String txtpath = "C:\\CodingSutff\\EclipseWorks\\Portfolio1\\src\\topscores.txt";
		File file = new File(txtpath);
		String s = name + "    " + score;
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(s);
		}
		catch (IOException e)
		{
			//temporary JFrame
			JOptionPane.showMessageDialog(f2, s);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == pic1)
		{
			imageUsed = lionimage;
		}
		else if(e.getSource() == pic2)
		{
			imageUsed = kenimage;
		}
		else if(e.getSource() == userpic)
		{
			int returnVal = fc.showOpenDialog(userpic);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				userpicpath = file.getAbsolutePath();
				pathforpic = new JLabel(userpicpath);
				pathforpic.setBounds(5, 500, 200, 20);
				f.add(pathforpic);
				try 
				{
					imageUsed = ImageIO.read(file);
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(f);
			}
		}
		else if(e.getSource() == three)
		{
			size = 3;
		}
		else if(e.getSource() == four)
		{
			size = 4;
		}
		else if(e.getSource() == toptimes)
		{
			//implement
			String txtpath = "C:\\CodingSutff\\EclipseWorks\\Portfolio1\\src\\topscores.txt";
			File file = new File(txtpath);
			String s = "   Top Scores \n   Name \tScore \n";
			Scanner fileScanner = null;
			try 
			{
				fileScanner = new Scanner(file);
			} 
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
			}
			int count = 1;
			while(fileScanner.hasNextLine())
			{
				s += count + ". ";
				count++;
				s += fileScanner.nextLine().trim();
				s += '\n';
			}
			JOptionPane.showMessageDialog(f, s);
		}
		else if(e.getSource() == play)
		{
			if(size == 0)
			{
				JOptionPane.showMessageDialog(f, "You must choose a size board.");
			}
			else if(imageUsed == null)
			{
				JOptionPane.showMessageDialog(f,  "Must select an image to play with.");
			}
			else
			{
				if(!(imageUsed == kenimage || imageUsed == lionimage))
					imageUsed = restructure(imageUsed);
				imgwid = imageUsed.getWidth();
				imghei = imageUsed.getHeight();
				
				board = createBoard(imageUsed, size);
				emptyspot = setEmptySpot(size);
				sidespot = createOpenSpot(size);
				f.setVisible(false);
				for(int i = 0; i < size; i++)
				{
					for(int j = 0; j < size; j++)
					{
						if(board[i][j] != null)
						{
							JLabel temp = new JLabel(new ImageIcon(board[i][j].getImage()));
							temp.setBounds(55 + (imgwid/size)*j, 125 + (imghei/size)*i, imgwid/size, imghei/size);
							temp.setVisible(true);
							pieces.add(temp);
							f2.add(temp);
						}
					}
				}
				JLabel sidespot = new JLabel();
				sidespot.setBounds(55 + imgwid, 125 + (imghei/size)*(size-1),  imgwid/size, imghei/size);
				sidespot.setVisible(true);
				pieces.add(sidespot);
				f2.add(sidespot);
				
				f2.setVisible(true);
				SwingUtilities.updateComponentTreeUI(f2);
			}
		}
		else if(e.getSource() == pause)
		{
			//yet to implement
			//still need to implement the timer
		}
		else if(e.getSource() == quit)
		{
			winlose = "You lost.";
			//also need to add when the win occurs
			winorloss = new JLabel(winlose);
			winorloss.setBounds(45, 5, 100, 20);
			f3.add(winorloss);
			//add this part inbetween the comments
			f2.setVisible(false);
			f3.setVisible(true);
			SwingUtilities.updateComponentTreeUI(f3);
			
		}
		else if(e.getSource() == newgame)
		{
			f3.setVisible(false);
			imageUsed = null;
			size = 0;
			f.setVisible(true);
			SwingUtilities.updateComponentTreeUI(f);
		}
		else if(e.getSource() == exitgame)
		{
			System.exit(0);
		}
	}
}

	