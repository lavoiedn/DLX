package ca.lavoiedn.Sudoku;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ca.lavoiedn.DLX.DancingLinksSearch;
import ca.lavoiedn.DLX.ExactCoverAction;

/**
 * Executes a few games of sudoku, using the DLX solving algorithm.
 * 
 * @author lavoiedn
 * 
 */

public class Sudoku {

	private final static String ARG_FORMAT = "\"FilePath\" \"NumberofGames\"";

	/**
	 * Executes the DLX solving algorithm on a certain number of sudoku boards
	 * chosen randomly from the file at the given file path.
	 * 
	 * @param args
	 *            The file containing the sudoku configurations and the
	 *            algorithm to use. Input arguments as such: java Sudoku
	 *            "filePath" "numberOfGames"
	 */
	public static void main(String[] args) {
		try {
			ArrayList<int[]> toSolve;
			if (args == null || args.length == 0 || args[0] == "help"
					|| args[0] == "man" || args[0] == "?") {
				System.out.println("The argument format should be as follow: "
						+ ARG_FORMAT);
				toSolve = new ArrayList<int[]>();
			} else {
				toSolve = SudokuParser.randomBoards(args[0],
						Integer.parseInt(args[1]));
			}

			SudokuBoard sudokuBoard;
			// We use a set of sets in case we want to implement searching for
			// every possible solution, which is a bit longer.
			List<ExactCoverAction> results;

			for (int[] board : toSolve) {
				DancingLinksSearch dancingLinksSearch = new DancingLinksSearch();
				results = new LinkedList<ExactCoverAction>();

				sudokuBoard = new SudokuBoard(board);
				results = dancingLinksSearch.solve(sudokuBoard);

				System.out.println("Metrics: "
						+ dancingLinksSearch.getMetrics());

				if (results.isEmpty())
					System.out.println("No solution found.\n");
				else {
					System.out.print("Actions: ");
					String actionOutputStr = "";
					for (ExactCoverAction action : results) {
						sudokuBoard.changeState(action);
						actionOutputStr += (action + ", ");
					}
					System.out.println(actionOutputStr.substring(0,
							actionOutputStr.length() - 2));
					System.out.print(sudokuBoard);
				}
			}
		} catch (Exception e) {
			System.out.println("Use the following argument format: "
					+ ARG_FORMAT);
			e.printStackTrace();
		}
	}
}
