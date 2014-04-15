package ca.lavoiedn.ToroidalList;

/**
 * A four-directional, circular node.
 * 
 * @author lavoiedn
 */

public class ToroidalNode {

	protected ToroidalNode up;
	protected ToroidalNode down;
	protected ToroidalNode left;
	protected ToroidalNode right;

	protected ColumnNode header;
	protected boolean isHeader;

	public static final int UP = 3;
	public static final int RIGHT = 2;
	public static final int LEFT = 1;
	public static final int DOWN = 0;

	public ToroidalNode() {
		header = null;
		isHeader = false;
		up = this;
		down = this;
		left = this;
		right = this;
	}

	/**
	 * List constructor.
	 * 
	 * @param parent
	 *            The parent node.
	 * @param dir
	 *            The direction of this node relative to the parent node. (0 =
	 *            down, 1 = left, 2 = right, 3 = up)
	 */
	public ToroidalNode(ToroidalNode parent, int dir) {
		isHeader = false;
		up = this;
		down = this;
		left = this;
		right = this;
		header = parent.getHeader();
		parent.getHeader().setSize(parent.getHeader().getSize() + 1);
		this.set(dir, parent);
	}

	/*
	 * This is the key of the DancingLinks algorithm. The fact that the function
	 * to remove a node and to return it to its original position (backtracking)
	 * are reverse operations.
	 */

	/**
	 * Removes this node from the X axis.
	 */
	public void removeX() {
		right.left = left;
		left.right = right;
	}

	/**
	 * Removes this node from the Y axis.
	 */
	public void removeY() {
		up.down = down;
		down.up = up;
	}

	/**
	 * Restores this node to its previous location on the X axis.
	 */
	public void restoreX() {
		left.right = this;
		right.left = this;
	}

	/**
	 * Restores this node to its previous location on the Y axis.
	 */
	public void restoreY() {
		down.up = this;
		up.down = this;
	}

	/*
	 * Getter and setter methods.
	 */
	public void setUp(ToroidalNode node) {
		up = node.up;
		down = node;
		up.down = this;
		node.up = this;
	}

	public void setDown(ToroidalNode node) {
		down = node.down;
		up = node;
		down.up = this;
		node.down = this;
	}

	public void setLeft(ToroidalNode node) {
		left = node.left;
		right = node;
		left.right = this;
		node.left = this;
	}

	public void setRight(ToroidalNode node) {
		right = node.right;
		left = node;
		right.left = this;
		node.right = this;
	}

	/**
	 * @param dir
	 *            The direction of this node relative to the parent node. (0 =
	 *            down, 1 = left, 2 = right, 3 = up)
	 * @param parent
	 *            The parent node of this node in the given direction.
	 */
	protected void set(int dir, ToroidalNode parent) {
		switch (dir) {
		case DOWN:
			setDown(parent);
			break;
		case LEFT:
			setLeft(parent);
			break;
		case RIGHT:
			setRight(parent);
			break;
		case UP:
			setUp(parent);
		}
	}

	/**
	 * Returns this node's upper neighbor.
	 * 
	 * @return This node's upper neighbor.
	 */
	public ToroidalNode getUp() {
		return up;
	}

	/**
	 * Returns this node's lower neighbor.
	 * 
	 * @return This node's lower neighbor.
	 */
	public ToroidalNode getDown() {
		return down;
	}

	/**
	 * Returns this node's left neighbor.
	 * 
	 * @return This node's left neighbor.
	 */
	public ToroidalNode getLeft() {
		return left;
	}

	/**
	 * Returns this node's right neighbor.
	 * 
	 * @return This node's right neighbor.
	 */
	public ToroidalNode getRight() {
		return right;
	}

	/**
	 * Getter method for the node in the given direction.
	 * 
	 * @param dir
	 *            The direction of the node to get. (0 = down, 1 = left, 2 =
	 *            right, 3 = up)
	 * @return Returns the node in the specified direction.
	 */
	public ToroidalNode get(int dir) {
		switch (dir) {
		case DOWN:
			return down;
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case UP:
			return up;
		}
		return null;
	}

	/**
	 * Returns whether or not this node is a list header.
	 * 
	 * @return <code>true</code> if this node is the column header, else
	 *         <code>false</code>.
	 */
	public boolean isHeader() {
		return isHeader;
	}

	/**
	 * Setter method for the header value of this node.
	 * 
	 * @param val
	 *            <code>true</code> if this node is a column head, else
	 *            <code>false</code>.
	 */
	public void setHeader(boolean val) {
		isHeader = val;
	}

	/**
	 * Getter method for the column header.
	 * 
	 * @return Returns the <code>ColumnNode</code> associated with this
	 *         <code>ToroidalNode</code>.
	 */
	public ColumnNode getHeader() {
		return header;
	}
}
