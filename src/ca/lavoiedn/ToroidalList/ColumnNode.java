package ca.lavoiedn.ToroidalList;

/**
 * Column marker node for a toroidal list implementation.
 * 
 * @author lavoiedn
 */

public class ColumnNode extends ToroidalNode {

	private String key;
	private int size;

	public ColumnNode(String key) {
		up = this;
		down = this;
		left = this;
		right = this;
		header = this;
		isHeader = true;
		this.key = key;
		size = 0;
	}

	/**
	 * Constructor for a list of <code>ColumnNode</code>.
	 */
	public ColumnNode(String key, int size, ColumnNode prev) {
		up = this;
		down = this;
		left = this;
		right = this;
		header = this;
		isHeader = true;
		this.key = key;
		this.size = size;
	}

	/**
	 * Sets the value of this node's left neighbor to the given
	 * <code>ColumnNode</code>.
	 * 
	 * @param node
	 *            The <code>ColumnNode</code> that will be set as this node's
	 *            left neighbor.
	 */
	public void setLeft(ColumnNode node) {
		super.setLeft(node);
	}

	/**
	 * Sets the value of this node's right neighbor to the given
	 * <code>ColumnNode</code>.
	 * 
	 * @param node
	 *            The <code>ColumnNode</code> that will be set as this node's
	 *            right neighbor.
	 */
	public void setRight(ColumnNode node) {
		super.setRight(node);
	}

	/**
	 * Returns this column's name.
	 * 
	 * @return This column's name.
	 */
	public String getName() {
		return key;
	}

	/**
	 * Returns the number of children this this column.
	 * 
	 * @return The number of children for this column.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets this column's number of children to the given value.
	 * 
	 * @param size
	 *            This column's number of children.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + key + ", " + size + ")";
	}
}
