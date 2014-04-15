package ca.lavoiedn.Sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple utility class to parse a file for sudoku configurations from a given
 * file path.
 * 
 * @author lavoiedn
 * 
 */

public class SudokuParser {

	/**
	 * Utility method that parses the file at the given file path for sudoku
	 * boards.
	 * 
	 * @param filePath
	 *            The path of the file, as a {@link String} containing the
	 *            sudoku boards.
	 * @return Returns an {@link ArrayList} containing the configurations in the
	 *         file.
	 */
	public static ArrayList<int[]> parseBoards(String filePath) {
		File sudokuFile = new File(filePath);
		ArrayList<int[]> sudokuBoards = new ArrayList<int[]>();
		int[] board;
		try (BufferedReader fileStream = new BufferedReader(new FileReader(
				sudokuFile))) {
			String lineFeed = fileStream.readLine();
			while (lineFeed != null) {
				board = new int[81];
				for (int index = 0; index < lineFeed.length(); index++) {
					board[index] = Character.getNumericValue(lineFeed
							.charAt(index));
				}
				sudokuBoards.add(board);
				lineFeed = fileStream.readLine();
			}
		} catch (FileNotFoundException e) {
			System.err
					.println("The file: " + filePath + " could not be found.");
		} catch (IOException e) {
			System.err.println(e);
		}
		return sudokuBoards;
	}

	/**
	 * Randomly chooses a given amount of sudoku layouts from the given file
	 * path.
	 * 
	 * @param filePath
	 *            The path of the file to parse for sudoku layouts.
	 * @param amount
	 *            The amount of layouts to choose from the file.
	 * @return Returns an <code>ArrayList</code> of the chosen layouts.
	 */
	public static ArrayList<int[]> randomBoards(String filePath, int amount) {
		ArrayList<int[]> sudokuBoards = parseBoards(filePath);
		ArrayList<int[]> randomBoards = new ArrayList<int[]>(amount);
		if (amount > sudokuBoards.size()) {
			randomBoards = sudokuBoards;
		} else {
			for (int i = 0; i < amount && sudokuBoards.size() > 0; i++) {
				int[] board = sudokuBoards
						.get((int) (Math.random() * sudokuBoards.size()));
				randomBoards.add(board);
				sudokuBoards.remove(board);
			}
		}
		return randomBoards;
	}
}
