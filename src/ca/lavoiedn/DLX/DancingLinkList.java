package ca.lavoiedn.DLX;

import java.util.Set;

import ca.lavoiedn.ToroidalList.ColumnNode;
import ca.lavoiedn.ToroidalList.ToroidalNode;

/**
 * Doubly linked circular list implementation.
 * 
 * @author lavoiedn
 * 
 */

public class DancingLinkList {

	private ColumnNode head;

	/**
	 * Empty constructor.
	 */
	public DancingLinkList() {
		head = new ColumnNode("START");
	}

	/**
	 * Constructor for a list with its content. (Column)
	 */
	public DancingLinkList(String key) {
		head = new ColumnNode("START");
		add(key);
	}

	/**
	 * Adds a <code>ColumnNode</code> to the toroidal list.
	 * 
	 * @param key
	 *            The <code>String</code> key for this node.
	 */
	public void add(String key) {
		ColumnNode toBuild = new ColumnNode(key);
		toBuild.setLeft(head);
	}

	/**
	 * @param keys
	 *            The column key of the nodes to add.
	 * @return Returns the first node in this row.
	 */
	public ToroidalNode buildRow(Set<String> keys) {
		ColumnNode currentNode = (ColumnNode) head.getRight();
		ToroidalNode rowNode = null;

		while (!keys.isEmpty() && currentNode != head) {
			if (keys.contains(currentNode.getName())) {
				keys.remove(currentNode.getName());

				if (rowNode == null) {
					rowNode = new ToroidalNode(currentNode, ToroidalNode.UP);
				} else {
					ToroidalNode toSet = new ToroidalNode(currentNode,
							ToroidalNode.UP);
					toSet.setLeft(rowNode);
				}
			}
			currentNode = (ColumnNode) currentNode.getRight();
		}
		return rowNode;
	}

	/**
	 * Returns the size of this list.
	 * 
	 * @return The number of <code>ColumnNode</code> and of
	 *         <code>ToroidalNode</code> in this list, in an array and in this
	 *         order.
	 */
	public int[] size() {
		int[] size = new int[2];
		ToroidalNode searchX = head.getRight();
		while (searchX != head) {
			size[0]++;
			ToroidalNode searchY = searchX.getDown();
			while (searchY != searchX) {
				searchY = searchY.getDown();
				size[1]++;
			}
			searchX = searchX.getRight();
		}
		return size;
	}

	/**
	 * Returns the next column header node.
	 * 
	 * @return The next column header node.
	 */
	public ToroidalNode next() {
		return head.getRight();
	}

	/**
	 * Returns the previous column header node.
	 * 
	 * @return The previous column header node.
	 */
	public ToroidalNode prev() {
		return head.getLeft();
	}

	/**
	 * Column getter method.
	 * 
	 * @param key
	 *            The key of a column node.
	 * @return Returns the <code>ColumnNode</code> with the corresponding key.
	 */
	public ColumnNode getCol(String key) {
		ColumnNode start = head;
		ToroidalNode check = head.getRight();
		ColumnNode target = null;
		while (target == null && check != start) {
			if (((ColumnNode) check).getName().equals(key)) {
				target = (ColumnNode) check;
			}
			check = check.getRight();
		}
		return target;
	}

	/**
	 * Returns the head of this <code>DancingLinkList</code>.
	 * 
	 * @return The head of this <code>DancingLinkList</code>.
	 */
	public ColumnNode getHead() {
		return head;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "";
		ColumnNode t = head;
		while (t.getLeft() != head) {
			s += t.getName() + "\n";
		}
		return s;
	}
}
