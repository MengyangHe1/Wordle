package wordle;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import wordle.model.WordleModel;
import wordle.view.WordleFrame;

public class Wordle implements Runnable {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Wordle());
		
		//use cross-platform look and feel so button backgrounds work on Mac
		try {
		    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	@Override
	public void run() {
		new WordleFrame(new WordleModel());
	}

}
