package edu.wm.cs.cs301.wordle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.wordle.model.WordleModel;

public class WordleFrame {
	
	private JFrame frame;
	
	private final KeyboardPanel keyboardPanel;
	
	private final WordleModel model;
	
	private WordleGridPanel wordleGridPanel;

	public WordleFrame(WordleModel model) {
		this.model = model;
		this.keyboardPanel = new KeyboardPanel(this, model);
		int width = keyboardPanel.getPanel().getPreferredSize().width;
		this.wordleGridPanel = new WordleGridPanel(this, model, width);
		this.frame = createAndShowGUI();
	}
	
	private JFrame createAndShowGUI() {
		JFrame frame = new JFrame("Wordle");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setJMenuBar(createMenuBar());
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			 public void windowClosing(WindowEvent event) {
				shutdown();
			}
		});
		
		frame.add(createTitlePanel(), BorderLayout.NORTH);
		frame.add(wordleGridPanel, BorderLayout.CENTER);
		frame.add(keyboardPanel.getPanel(), BorderLayout.SOUTH);
		
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		System.out.println("Frame size: " + frame.getSize());
		
		return frame;
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		// add Menu Difficulty
		JMenu diffcultyMenu = new JMenu("Difficulty");
		menuBar.add(diffcultyMenu);
		// add easy/medium/hard menu item and their action listener
		JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(event -> easyMode());
		diffcultyMenu.add(easy);
		JMenuItem medium = new JMenuItem("Medium");
		medium.addActionListener(event -> mediumMode());
		diffcultyMenu.add(medium);
		JMenuItem hard = new JMenuItem("Hard");
		hard.addActionListener(event -> hardMode());
		diffcultyMenu.add(hard);
		
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		JMenuItem instructionsItem = new JMenuItem("Instructions...");
		instructionsItem.addActionListener(event -> new InstructionsDialog(this));
		helpMenu.add(instructionsItem);
		
		JMenuItem aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(event -> new AboutDialog(this));
		helpMenu.add(aboutItem);
		
		return menuBar;
	}
	
	private JPanel createTitlePanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelAction");
		ActionMap actionMap = panel.getActionMap();
		actionMap.put("cancelAction", new CancelAction());
		
		JLabel label = new JLabel("Wordle");
		label.setFont(AppFonts.getTitleFont());
		panel.add(label);
		
		return panel;
	}
	
	public void shutdown() {
		model.getStatistics().writeStatistics();
		frame.dispose();
		System.exit(0);
	}
	
	public void resetDefaultColors() {
		keyboardPanel.resetDefaultColors();
	}
	
	public void setColor(String letter, Color backgroundColor, Color foregroundColor) {
		keyboardPanel.setColor(letter, backgroundColor, foregroundColor);
	}
	
	public void repaintWordleGridPanel() {
		wordleGridPanel.repaint();
	}

	public JFrame getFrame() {
		return frame;
	}
	
	private class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			shutdown();
		}
		
	}
	
	// action when user choose easy
	public void easyMode() {
		// if mode is already easy, nothing changes
		if(!model.getCurrentMode().equals("easy")) {
			frame.dispose();      // close the current frame
			model.switchToEasy();        // update model
			// create a new frame with new mode
			int width = keyboardPanel.getPanel().getPreferredSize().width;
			this.wordleGridPanel = new WordleGridPanel(this, model, width);
			this.keyboardPanel.resetDefaultColors();
			// create and show the new frame
			this.frame = createAndShowGUI();
		}
	}
	
	// action when user choose medium
	public void mediumMode() {
		// if mode is already medium, nothing changes
		if(!model.getCurrentMode().equals("medium")) {
			frame.dispose();
			model.switchToMedium();
			int width = keyboardPanel.getPanel().getPreferredSize().width;
			this.wordleGridPanel = new WordleGridPanel(this, model, width);
			this.keyboardPanel.resetDefaultColors();
			this.frame = createAndShowGUI();
		}
	}
	
	// action when user choose hard
	public void hardMode() {
		// if mode is already hard, nothing changes
		if(!model.getCurrentMode().equals("hard")) {
			frame.dispose();
			model.switchToHard();
			int width = keyboardPanel.getPanel().getPreferredSize().width;
			this.wordleGridPanel = new WordleGridPanel(this, model, width);
			this.keyboardPanel.resetDefaultColors();
			this.frame = createAndShowGUI();
		}
	}

}
