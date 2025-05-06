package Final_Project;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import uk.ac.leedsbeckett.oop.LBUGraphics;
public class MainClass extends LBUGraphics{
	public static void main(String[] args)
	{
		GraphicsSystem graphics = new GraphicsSystem();
		new MainClass();
	}

	public MainClass()
	{
		JFrame MainFrame = new JFrame();       //create a frame to display the
		MainFrame.setLayout(new FlowLayout()); //not strictly necessary
		MainFrame.add(this);
		MainFrame.pack();
		MainFrame.setVisible(true);
		about();
	}
	@Override


	public void processCommand(String command) {
		// TODO Auto-generated method stub

	}

}