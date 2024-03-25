package wordle.model;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import wordle.controller.ReadWordsRunnable;

public class WordleModel {
	
	private char[] currentWord, guess;
	
	private int columnCount, maximumRows;
	private int currentColumn, currentRow;
	
	private List<String> wordList;
	
	private final Random random;
	
	private Statistics statistics;
	
	private WordleResponse[][] wordleGrid;
	
	private String mode;    // store current mode
	
	public WordleModel() {
		this.mode = "medium";
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = 5;
		this.maximumRows = 6;
		this.random = new Random();
		
		createWordList();
		
		this.wordleGrid = initializeWordleGrid();
		this.guess = new char[columnCount];
		this.statistics = new Statistics(this);
	}
	
	private void createWordList() {
		ReadWordsRunnable runnable = new ReadWordsRunnable(this);
		new Thread(runnable).start();
	}
	
	public void initialize() {
		this.wordleGrid = initializeWordleGrid();
		this.currentColumn = -1;
		this.currentRow = 0;
		generateCurrentWord();
		this.guess = new char[columnCount];
	}

	public void generateCurrentWord() {
		String word = getCurrentWord();
		this.currentWord = word.toUpperCase().toCharArray();

		//uncomment line below to show secret word on the console for debugging
		System.out.println("Current word set to " + word);
	}

	private String getCurrentWord() {
		return wordList.get(getRandomIndex());
	}

	private int getRandomIndex() {
		int size = wordList.size();
		return random.nextInt(size);
	}
	
	private WordleResponse[][] initializeWordleGrid() {
		WordleResponse[][] wordleGrid = new WordleResponse[maximumRows][columnCount];

		for (int row = 0; row < wordleGrid.length; row++) {
			for (int column = 0; column < wordleGrid[row].length; column++) {
				wordleGrid[row][column] = null;
			}
		}

		return wordleGrid;
	}
	
	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}
	
	public void setCurrentWord() {
		int index = getRandomIndex();
		currentWord = wordList.get(index).toCharArray();
	}
	
	public void setCurrentColumn(char c) {
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		guess[currentColumn] = c;
		wordleGrid[currentRow][currentColumn] = new WordleResponse(c,
				Color.WHITE, Color.BLACK);
	}
	
	public void backspace() {
		if (this.currentColumn > -1) { //only backspace if there's room
			wordleGrid[currentRow][currentColumn] = null;
			guess[currentColumn] = ' ';
			this.currentColumn--;
			this.currentColumn = Math.max(currentColumn, -1);
		}
	}
	
	public WordleResponse[] getCurrentRow() {
		return wordleGrid[getCurrentRowNumber()];
	}
	
	public int getCurrentRowNumber() {
		return currentRow - 1;
	}
	
	public boolean setCurrentRow() {	
		if(!validGuess()) {
			return false;
		}
		for (int column = 0; column < guess.length; column++) {
			Color backgroundColor = AppColors.GRAY;
			Color foregroundColor = Color.WHITE;
			if (guess[column] == currentWord[column]) {
				backgroundColor = AppColors.GREEN;
			} else if (contains(currentWord, guess, column)) {
				backgroundColor = AppColors.YELLOW;
			}
			
			wordleGrid[currentRow][column] = new WordleResponse(guess[column],
					backgroundColor, foregroundColor);
		}
		
		currentColumn = -1;
		currentRow++;
		guess = new char[columnCount];
		
		return currentRow < maximumRows;
	}
	
	private boolean contains(char[] currentWord, char[] guess, int column) {
		for (int index = 0; index < currentWord.length; index++) {
			if (index != column && guess[column] == currentWord[index]) {
				return true;
			}
		}
		
		return false;
	}

	public WordleResponse[][] getWordleGrid() {
		return wordleGrid;
	}
	
	public int getMaximumRows() {
		return maximumRows;
	}

	public int getColumnCount() {
		return columnCount;
	}
	
	public int getCurrentColumn() {
		return currentColumn;
	}

	public int getTotalWordCount() {
		return wordList.size();
	}

	public Statistics getStatistics() {
		return statistics;
	}
	
	// return a boolean indicating the guess in a valid guess (actual word)
	public boolean validGuess() {
		return this.wordList.contains(new String(this.guess).toLowerCase());
	}
	
	// return the current answer
	public String getCurWord() {
		return new String(this.currentWord);
	}
	
	// return the current mode
	public String getCurrentMode() {
		return this.mode;
	}
	
	// switch to easy mode
	public void switchToEasy() {
		this.mode = "easy";
		this.statistics = new Statistics(this);
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = 4;
		this.maximumRows = 5;
		this.guess = new char[columnCount];
		createWordList();
		// stop the main thread and wait for ReadWordsRunnable to complete
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.wordleGrid = initializeWordleGrid();
		this.statistics = new Statistics(this);
	}
	
	// switch to medium mode
	public void switchToMedium() {
		this.mode = "medium";
		this.statistics = new Statistics(this);
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = 5;
		this.maximumRows = 6;
		this.guess = new char[columnCount];
		createWordList();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.wordleGrid = initializeWordleGrid();
	}
	
	// switch to hard mode
	public void switchToHard() {
		this.mode = "hard";
		this.statistics = new Statistics(this);
		this.currentColumn = -1;
		this.currentRow = 0;
		this.columnCount = 6;
		this.maximumRows = 7;
		this.guess = new char[columnCount];
		createWordList();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.wordleGrid = initializeWordleGrid();
	}
}
