package ca.lavoiedn.Sudoku;

import ca.lavoiedn.DLX.ExactCoverAction;
import ca.lavoiedn.ToroidalList.ToroidalNode;

/**
 * A simple class to illustrate a "move" in a game of Sudoku.
 * 
 * @author lavoiedn
 * 
 */
public class SudokuAction extends ExactCoverAction {

	private int position;
	private int value;

	public SudokuAction(int index, int value) {
		position = index;
		this.value = value;
	}

	public SudokuAction(String[] keys) {
		super(keys);
	}

	public SudokuAction(ToroidalNode node) {
		super(node);
	}

	/**
	 * Getter method for the action's position index.
	 * 
	 * @return Returns the position of this action
	 */
	public int getPos() {
		return position;
	}

	/**
	 * Getter method for the action's digit.
	 * 
	 * @return Returns the digit associated with this action.
	 */
	public int getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + position + "," + value + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aima.search.Sudoku.SudokuBoard#getRow()
	 */
	public int getRow() {
		return SudokuBoard.getRow(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aima.search.Sudoku.SudokuBoard#getCol()
	 */
	public int getCol() {
		return SudokuBoard.getCol(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aima.search.Sudoku.SudokuBoard#getBox()
	 */
	public int getBox() {
		return SudokuBoard.getBox(getRow(), getCol());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SudokuAction other = (SudokuAction) obj;
		if (position != other.position)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.lavoiedn.DLX.ExactCoverAction#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean noOp = false;
		if (position < 0 || position > 80)
			noOp = true;
		if (value < 1 || value > 9)
			noOp = true;
		return noOp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.lavoiedn.DLX.ExactCoverAction#getConstraintKeys()
	 */
	@Override
	public String[] getConstraintKeys() {
		String[] keys = new String[5];
		keys[0] = "R" + (getRow() + 1) + "C" + (getCol() + 1);
		keys[1] = "R" + (getRow() + 1) + "#" + value;
		keys[2] = "C" + (getCol() + 1) + "#" + value;
		keys[3] = "B" + (getBox() + 1) + "#" + value;
		keys[4] = "R" + (getRow() + 1) + "C" + (getCol() + 1) + "#" + value;
		return keys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.lavoiedn.DLX.ExactCoverAction#setFromConstraintKey(java.lang.String)
	 */
	@Override
	protected boolean setFromConstraintKeys(String[] keys) {
		int row = -1;
		int col = -1;
		value = -1;
		
		for (String key : keys) {
			for (int i = 0; i < key.length(); i += 2) {
				if (key.charAt(i) == 'R') {
					row = (Character.getNumericValue(key.charAt(i + 1))) - 1;
				} else if (key.charAt(i) == 'C') {
					col = (Character.getNumericValue(key.charAt(i + 1))) - 1;
				} else if (key.charAt(i) == '#') {
					value = Character.getNumericValue(key.charAt(i + 1));
				}
			}
		}
		position = SudokuBoard.getIndex(col, row);
		return row != -1 && col != -1 && value != -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.lavoiedn.DLX.ExactCoverAction#setFromToroidalNode(ca.lavoiedn.DLX.
	 * ToroidalNode)
	 */
	@Override
	public boolean setFromToroidalNode(ToroidalNode node) {
		int row = -1;
		int col = -1;
		value = -1;
		ToroidalNode start = node;
		node = node.getRight();
		while (node != start) {
			String key = node.getHeader().getName();
			for (int i = 0; i < key.length(); i += 2) {
				if (key.charAt(i) == 'R') {
					row = (Character.getNumericValue(key.charAt(i + 1))) - 1;
				} else if (key.charAt(i) == 'C') {
					col = (Character.getNumericValue(key.charAt(i + 1))) - 1;
				} else if (key.charAt(i) == '#') {
					value = Character.getNumericValue(key.charAt(i + 1));
				}
			}
			node = node.getRight();
		}
		position = SudokuBoard.getIndex(col, row);
		return row != -1 && col != -1 && value != -1;
	}
}
