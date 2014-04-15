package ca.lavoiedn.Sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import ca.lavoiedn.DLX.DancingLinkList;
import ca.lavoiedn.DLX.ExactCoverAction;
import ca.lavoiedn.DLX.ExactCoverProblem;
import ca.lavoiedn.ToroidalList.ToroidalNode;

/**
 * A simple Sudoku board, described by an array of 81 numbers.
 * 
 * @author lavoiedn
 * 
 */

public class SudokuBoard extends ExactCoverProblem {

	private int[] boardState = new int[81];

	/**
	 * <code>SudokuBoard</code> constructor.
	 * 
	 * @param board
	 *            The sudoku layout, given by an array of 81 integer.
	 */
	public SudokuBoard(int[] board) {
		if (board.length == 81) {
			System.arraycopy(board, 0, boardState, 0, 81);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.lavoiedn.DLX.ExactCoverProblem#changeState(java.lang.String)
	 */
	@Override
	public boolean changeState(ExactCoverAction action) {
		return this.play((SudokuAction) action);
	}

	/**
	 * Shortcut <code>play</code> method.
	 * 
	 * @param action
	 *            <code>SudokuAction</code> to play.
	 * @return Returns <code>true</code> if the action was valid, else
	 *         <code>false</code>.
	 */
	public boolean play(SudokuAction action) {
		return this.play(action.getPos(), action.getValue());
	}

	/**
	 * The <code>play</code> method, places the given digit at the specified
	 * index if possible then returns <code>true</code>, else returns
	 * <code>false</code>.
	 * 
	 * @param index
	 *            The index where the digit will be placed. Must have a value
	 *            between 0 and 80.
	 * @param digit
	 *            The digit to place at the given index. Must have a value
	 *            between 1 and 9.
	 * @return Returns <code>true</code> if the action was successful, else
	 *         <code>false</code>
	 */
	public boolean play(int index, int digit) {
		if (index >= 0 && index < boardState.length && 0 < digit && digit < 10) {
			if (boardState[index] == 0) {
				if (!inRow(getRow(index), digit)
						&& !inCol(getCol(index), digit)
						&& !inBox(getBox(index), digit)) {
					boardState[index] = digit;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Finds the moves that are valid on this board and returns them.
	 * 
	 * @return Returns the list of valid moves.
	 */
	public ArrayList<SudokuAction> validMoves() {
		ArrayList<SudokuAction> validMoves = new ArrayList<SudokuAction>();
		HashSet<Integer> invalid = new HashSet<Integer>();
		int mPos = 0;
		for (int value : boardState) {
			if (value == 0) {
				for (int i = mPos - mPos % 9; i < (mPos - mPos % 9) + 9; i++) {
					if (boardState[i] != 0) {
						invalid.add(boardState[i]);
					}
				}
				for (int i = mPos % 9; i < boardState.length; i += 9) {
					if (boardState[i] != 0) {
						invalid.add(boardState[i]);
					}
				}
				int topLeft = mPos - ((mPos % 27) - (mPos % 9));
				topLeft -= topLeft % 3;
				for (int x = topLeft; x <= topLeft + 18; x += 9) {
					for (int i = x; i < x + 3; i++) {
						if (boardState[i] != 0) {
							invalid.add(boardState[i]);
						}
					}
				}
				for (int i = 1; i < 10; i++) {
					if (!invalid.contains(i)) {
						validMoves.add(new SudokuAction(mPos, i));
					}
				}
			}
			invalid = new HashSet<Integer>();
			mPos++;
		}
		return validMoves;
	}

	/**
	 * Finds the digit at the given index and returns it.
	 * 
	 * @param index
	 *            An <code>int</code> between 0 and 80.
	 * @return Returns the digit at the given index.
	 */
	public int getValue(int index) {
		return boardState[index];
	}

	/**
	 * Finds the digit at the given coordinates and returns it.
	 * 
	 * @param x
	 *            The abscissa.
	 * @param y
	 *            The ordinate.
	 * @return Returns the digit at the given coordinates.
	 */
	public int getValue(int x, int y) {
		return boardState[(x * 9) + y];
	}

	/**
	 * Determines if the given digit is in the specified row.
	 * 
	 * @param row
	 *            The row to check in.
	 * @param digit
	 *            The digit to check for.
	 * @return Returns <code>true</code> if <code>v</code> is in <code>y</code>,
	 *         else <code>false</code>.
	 */
	public boolean inRow(int row, int digit) {
		boolean isIn = false;
		if (row >= 0 && row < 9) {
			for (int i = row * 9; i < row + 9; i++) {
				isIn = boardState[i] == digit ? true : false;
			}
		}
		return isIn;
	}

	/**
	 * Determines if the given digit is in the specified column.
	 * 
	 * @param col
	 *            The column to check in. (0 to 8)
	 * @param digit
	 *            The digit to check for.
	 * @return Returns <code>true</code> if <code>v</code> is in <code>x</code>,
	 *         else <code>false</code>.
	 */
	public boolean inCol(int col, int digit) {
		boolean isIn = false;
		if (col >= 0 && col < 9) {
			for (int i = col; i < 81; i += 9) {
				isIn = boardState[i] == digit ? true : false;
			}
		}
		return isIn;
	}

	/**
	 * Determines if the given digit is in the specified box.
	 * 
	 * @param box
	 *            The box to check in. (0 to 8)
	 * @param digit
	 *            The digit to check for.
	 * @return Returns <code>true</code> if <code>v</code> is in <code>x</code>,
	 *         else <code>false</code>.
	 */
	public boolean inBox(int box, int digit) {
		boolean isIn = false;
		if (box >= 0 && box < 9) {
			int topLeft = 27 * ((int) Math.floor(box / 3)) + (box % 3) * 3;
			for (int j = topLeft; j <= topLeft + 18; j += 9) {
				for (int i = j; i < j + 3; i++) {
					if (boardState[i] == digit) {
						isIn = true;
					}
				}
			}
		}
		return isIn;
	}

	/**
	 * Determines which row the given index is in.
	 * 
	 * @param index
	 *            The index to evaluate.
	 * @return Returns the row where the given index is.
	 */
	public static int getRow(int index) {
		return (int) Math.floor(index / 9);
	}

	/**
	 * Determines which column the given index is in.
	 * 
	 * @param index
	 *            The index to evaluate.
	 * @return Returns the column where the given index is.
	 */
	public static int getCol(int index) {
		return index % 9;
	}

	/**
	 * Determines which box the given coordinates are in.
	 * 
	 * @param x
	 *            The abscissa.
	 * @param y
	 *            The ordinate.
	 * @return The box which contains the given coordinates.
	 */
	public static int getBox(int x, int y) {
		return x >= 0 && x < 9 && y >= 0 && y < 9 ? 3
				* ((int) Math.floor(y / 3)) + ((int) Math.floor(x / 3)) : -1;
	}

	/**
	 * Determines which box the given index is in.
	 * 
	 * @param index
	 *            The position index to find the box for.
	 * @return The box which contains the given index.
	 */
	public static int getBox(int index) {
		return getBox(getCol(index), getRow(index));
	}

	/**
	 * Returns the index corresponding to the given coordinates.
	 * 
	 * @param x
	 *            The abscissa.
	 * @param y
	 *            The ordinate.
	 * @return The index corresponding to the given coordinates.
	 */
	public static int getIndex(int x, int y) {
		return x >= 0 && x < 9 && y >= 0 && y < 9 ? x + y * 9 : -1;
	}

	/**
	 * Allows the user to receive a copy of the current board.
	 * 
	 * @return Returns a copy of the current board.
	 */
	public int[] getBoard() {
		int[] copy = new int[boardState.length];
		System.arraycopy(boardState, 0, copy, 0, boardState.length);
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.lavoiedn.DLX.ExactCoverProblem#getAction(ca.lavoiedn.DLX.ToroidalNode)
	 */
	@Override
	public SudokuAction getSpecificAction(ToroidalNode node) {
		return new SudokuAction(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.lavoiedn.DLX.ExactCoverProblem#isSolved()
	 */
	@Override
	public boolean isSolved() {
		for (int digit : boardState) {
			if (digit == 0) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		SudokuBoard other = (SudokuBoard) obj;
		for (int index = 0; index < other.getBoard().length; index++) {
			if (other.getBoard()[index] != this.boardState[index]) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.lavoiedn.DLX.ExactCoverProblem#asSparseMatrix()
	 */
	@Override
	public DancingLinkList getSparseMatrix() {
		DancingLinkList sparseMatrix = new DancingLinkList();
		ArrayList<String> keys = new ArrayList<String>();
		boolean[][] cover = asExactCover();
		String key = "";
		for (int y = 0; y < cover[0].length; y++) {
			if (y < (int) ((cover[0].length + 1) / 4)) {
				key = "R" + (((int) Math.floor(y / 9)) + 1);
				key += "C" + (y % 9 + 1);
			} else if (y < (int) ((cover[0].length + 1) / 2)) {
				key = "R" + (((int) Math.floor((y - 81) / 9)) + 1);
				key += "#" + (y % 9 + 1);
			} else if (y < (int) ((cover[0].length + 1) * 0.75)) {
				key = "C"
						+ (((int) Math
								.floor((y - (int) ((cover[0].length + 1) / 2)) / 9)) + 1);
				key += "#" + (y % 9 + 1);
			} else {
				key = "B"
						+ (((int) Math
								.floor((y - (int) ((cover[0].length + 1) * 0.75)) / 9)) + 1);
				key += "#" + (y % 9 + 1);
			}
			keys.add(key);
			sparseMatrix.add(key);
			key = "";
		}
		for (int x = 0; x < cover.length; x++) {
			HashSet<String> buildKeys = new HashSet<String>();

			for (int y = 0; y < cover[x].length; y++) {
				if (cover[x][y]) {
					buildKeys.add(keys.get(y));
				}
			}

			sparseMatrix.buildRow(buildKeys);
		}
		return sparseMatrix;
	}

	/**
	 * Builds the constraint matrix to change the problem to its exact cover
	 * form. (For the basic sudoku problem.)
	 * 
	 * @return Returns the constraint matrix of the exact cover problem.
	 */
	private static boolean[][] asExactCover() {
		boolean[][] constraints = new boolean[729][324];
		int cNum = 0;
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 8; k++) {
					for (int x = 0; x < 9; x++) {
						for (int y = 0; y < 9; y++) {
							constraints[(i * 81 + j * 9 + k)][cNum * 81
									+ (x * 9 + y)] = x == i && j == y ? true
									: false;
						}
					}
				}
			}
		}
		cNum++;
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 8; k++) {
					for (int x = 0; x < 9; x++) {
						for (int y = 0; y < 9; y++) {
							constraints[(i * 81 + j * 9 + k)][cNum * 81
									+ (x * 9 + y)] = x == i && k == y ? true
									: false;
						}
					}
				}
			}
		}
		cNum++;
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 8; k++) {
					for (int x = 0; x < 9; x++) {
						for (int y = 0; y < 9; y++) {
							constraints[(i * 81 + j * 9 + k)][cNum * 81
									+ (x * 9 + y)] = x == j && k == y ? true
									: false;
						}
					}
				}
			}
		}
		cNum++;
		for (int i = 0; i <= 8; i++) {
			for (int j = 0; j <= 8; j++) {
				for (int k = 0; k <= 8; k++) {
					for (int x = 0; x < 9; x++) {
						for (int y = 0; y < 9; y++) {
							constraints[(i * 81 + j * 9 + k)][cNum * 81
									+ (x * 9 + y)] = getBox(i, j) == x
									&& y == k ? true : false;
						}
					}
				}
			}
		}
		return constraints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String sudoku = "";
		int index = 0;
		for (int i : boardState) {
			if (index % 9 == 8) {
				sudoku += i;
			} else if (index % 9 == 0) {
				sudoku += "\n" + i + " ";
			} else {
				sudoku += i + " ";
			}
			index++;
		}
		sudoku += "\n";
		return sudoku;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.lavoiedn.DLX.ExactCoverProblem#getInitialStateActions()
	 */
	@Override
	public List<SudokuAction> getInitialStateActions() {
		int mPos = 0;
		List<SudokuAction> initialState = new LinkedList<SudokuAction>();
		for (int digit : getBoard()) {
			if (digit != 0) {
				SudokuAction pre = new SudokuAction(mPos, digit);
				initialState.add(pre);
			}
			mPos++;
		}
		return initialState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SudokuBoard clone() {
		return new SudokuBoard(boardState);
	}
}