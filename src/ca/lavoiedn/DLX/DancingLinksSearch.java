package ca.lavoiedn.DLX;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ca.lavoiedn.ToroidalList.ColumnNode;
import ca.lavoiedn.ToroidalList.ToroidalNode;

/**
 * This class implements the Dancing Links (or DLX) algorithm, as seen in Donald
 * E. Knuth's paper, "Dancing Links", for an <code>ExactCoverProblem</code> with
 * a given initial state.
 * 
 * The ability to determine the constraints of the initial state is of the
 * utmost importance. They need to be explored prior to beginning the actual
 * search, since these rows and columns will be omitted from the search.
 * 
 * @author lavoiedn
 */

public class DancingLinksSearch {

	private DancingLinkList sparseMatrix;
	private ExactCoverProblem problem;
	private List<ExactCoverAction> actions;
	private List<ExactCoverAction> solution;

	private TreeMap<String, Long> metrics;

	public static final String METRICS_NUMBER_OF_NODES = "torsize";
	public static final String METRICS_ELAPSED_TIME = "elapsedtime";
	public static final String METRICS_NODES_EXPLORED = "nodesexplored";
	public static final String METRICS_SET_CONSTRAINTS = "numberinitialconstraints";

	/**
	 * Solves the given {@link ca.lavoiedn.DLX.ExactCoverProblem}.
	 * 
	 * @param problem
	 *            The {@link ca.lavoiedn.DLX.ExactCoverProblem} to solve.
	 * @return A list of actions to solve the given <code>problem</code>.
	 */
	public List<ExactCoverAction> solve(ExactCoverProblem problem) {
		long elapsedTime = System.currentTimeMillis();
		actions = new LinkedList<ExactCoverAction>();
		solution = new LinkedList<ExactCoverAction>();
		metrics = new TreeMap<String, Long>();

		this.problem = problem;
		sparseMatrix = problem.getSparseMatrix();

		metrics.put(METRICS_SET_CONSTRAINTS, new Long(problem
				.getInitialStateActions().size()));

		// We explore the initial constraints, since those were already
		// "removed" from the constraint table of the problem given.
		// We could also code "getSparseMatrix" to return a specific
		// sparse matrix rather than the generic form for problems of this
		// kind. I like this way, however, because it helps illustrate how the
		// algorithm works.
		for (ExactCoverAction primerAction : problem.getInitialStateActions()) {
			String[] constraints = primerAction.getConstraintKeys();
			for (String constraintKey : constraints) {
				ColumnNode preConstraint = sparseMatrix.getCol(constraintKey);
				if (preConstraint != null) {
					// Note: these nodes are not counted as expanded nodes
					// for the search,since they are
					// merely explored to build the initial state.
					explore(preConstraint);
				}
			}
		}

		int[] toroidSize = sparseMatrix.size();
		metrics.put(METRICS_NUMBER_OF_NODES, new Long(toroidSize[0]
				+ toroidSize[1]));

		int nodes = search(0);

		elapsedTime = System.currentTimeMillis() - elapsedTime;
		metrics.put(METRICS_NODES_EXPLORED, new Long(nodes));
		metrics.put(METRICS_ELAPSED_TIME, elapsedTime);

		return solution;
	}

	/**
	 * 
	 * This is the recursive portion of the dancing links algorithm. A
	 * <code>ColumnNode</code> is selected for exploration for this step of the
	 * search, it is then added to the list of potential solutions before being
	 * explored. If the exploration is successful, it will keep going deeper
	 * until there are no more columns to explore or backtrack if there is no
	 * solution to be found down the current path of constraints.
	 * 
	 * @param step
	 *            The step index of this search.
	 * @return Returns the number of nodes visited for this step.
	 */
	private int search(int step) {
		int nodesVisited = 0;
		ColumnNode toSearch = columnSelection();

		if (toSearch == sparseMatrix.getHead()) {
			ExactCoverProblem toSolve = problem.clone();

			for (ExactCoverAction action : actions) {
				toSolve.changeState(action);
				solution.add(action);
			}

			// Failsafe. In theory, if we get this far, the problem is solved.
			if (!toSolve.isSolved()) {
				solution = new LinkedList<ExactCoverAction>();
			}

			return nodesVisited;
		}

		nodesVisited += explore(toSearch);
		ToroidalNode currentRowNode = toSearch.getDown();
		while (currentRowNode != toSearch) {
			ExactCoverAction action = problem.getSpecificAction(currentRowNode);
			actions.add(action);

			ToroidalNode rightRowNode = currentRowNode.getRight();
			while (rightRowNode != currentRowNode) {
				nodesVisited += explore(rightRowNode.getHeader());
				rightRowNode = rightRowNode.getRight();
			}

			nodesVisited += search(step + 1);
			actions.remove(action);

			toSearch = currentRowNode.getHeader();

			rightRowNode = currentRowNode.getLeft();
			while (rightRowNode != currentRowNode) {
				backtrack(rightRowNode.getHeader());
				rightRowNode = rightRowNode.getLeft();
			}

			currentRowNode = currentRowNode.getDown();
		}
		backtrack(toSearch);
		return nodesVisited;
	}

	/**
	 * A nifty little function that uses, along with its opposite,
	 * <code>backtrack</code> an interesting property of nodes in a doubly
	 * linked list. This property dictates that, to remove a node, all you need
	 * to do is change the references of its left and right (or up and down)
	 * neighbors to the opposite neighbor, thus removing the node from that
	 * axis.
	 * 
	 * @param toExplore
	 *            The column to temporarily remove from the toroidal list.
	 * @return Returns the number of nodes covered by this function.
	 */
	private int explore(ToroidalNode toExplore) {
		int nodesExplored = 0;
		toExplore.removeX();
		ToroidalNode currentRowNode = toExplore.getDown();
		while (currentRowNode != toExplore) {
			ToroidalNode rowExplore = currentRowNode.getRight();
			while (rowExplore != currentRowNode) {
				rowExplore.getHeader().setSize(
						rowExplore.getHeader().getSize() - 1);
				rowExplore.removeY();
				nodesExplored++;
				rowExplore = rowExplore.getRight();
			}
			currentRowNode = currentRowNode.getDown();
			nodesExplored++;
		}
		return nodesExplored;
	}

	/**
	 * This function uses the fact that every node removed from a doubly linked
	 * list keeps references to its previous neighbors. Thus, the only required
	 * operation to restore it in the list (as seen in the
	 * <code>ToroidalNode</code> "restore" and "remove" functions) is to change
	 * its former neighbors' references back to this node. This simple operation
	 * is the reason this algorithm is so effective.
	 * 
	 * @param toBacktrack
	 *            The column to restore in the list.
	 */
	private void backtrack(ToroidalNode toBacktrack) {
		ToroidalNode currentRowNode = toBacktrack.getUp();
		while (currentRowNode != toBacktrack) {
			ToroidalNode rowExplore = currentRowNode.getLeft();
			while (rowExplore != currentRowNode) {
				rowExplore.restoreY();
				rowExplore.getHeader().setSize(
						rowExplore.getHeader().getSize() + 1);
				rowExplore = rowExplore.getLeft();
			}
			currentRowNode = currentRowNode.getUp();
		}
		toBacktrack.restoreX();
	}

	/**
	 * Returns the <code>ColumnNode</code> with the smallest amount of rows
	 * associated to it. Used to optimize the search.
	 * 
	 * @return The <code>ColumnNode</code> with the smallest amount of rows
	 *         associated to it.
	 */
	private ColumnNode columnSelection() {
		ColumnNode start = sparseMatrix.getHead();

		ColumnNode current = (ColumnNode) start.getRight();
		ColumnNode best = null;
		int size = Integer.MAX_VALUE;

		while (current != start) {
			if (current.getSize() < size) {
				size = current.getSize();
				best = current;
			}
			current = (ColumnNode) current.getRight();
		}

		return best == null ? start : best;
	}

	/**
	 * Returns a map containing the metrics associated with this research.
	 * 
	 * @return A map containing the metrics associated with this research.
	 */
	public Map<String, Long> getMetrics() {
		return metrics;
	}

}
