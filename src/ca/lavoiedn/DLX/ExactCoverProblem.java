package ca.lavoiedn.DLX;

import java.util.List;

import ca.lavoiedn.ToroidalList.ToroidalNode;

/**
 * The abstract class representing exact cover problems.
 * 
 * @author lavoiedn
 * 
 */
public abstract class ExactCoverProblem implements Cloneable {

	/**
	 * Changes the <code>ExactCoverProblem</code>'s state according to the given
	 * {@link ca.lavoiedn.DLX.ExactCoverAction}.
	 * 
	 * @param key
	 *            The {@link ca.lavoiedn.DLX.ExactCoverAction} for the desired
	 *            state change action.
	 * @return <code>true</code> if the state was successfully changed, else
	 *         <code>false</code>.
	 */
	public abstract boolean changeState(ExactCoverAction key);
	
	
	/**
	 * Returns the {@link ca.lavoiedn.DLX.ExactCoverAction} associated with this
	 * <code>ExactCoverProblem</code>.
	 * 
	 * @param node
	 *            The {@link ca.lavoiedn.ToroidalList.DLC.ToroidalNode} used to build the action.
	 * @return The generic {@link ca.lavoiedn.DLX.ExactCoverAction} associated
	 *         with this <code>ExactCoverProblem</code>.
	 */
	public abstract ExactCoverAction getSpecificAction(ToroidalNode node);

	/**
	 * Returns the sparse matrix representing this problem.
	 * 
	 * @return The sparse matrix representing this problem's state.
	 */
	public abstract DancingLinkList getSparseMatrix();

	/**
	 * Returns a list of this problem's initial state constraints.
	 * 
	 * @return A list of this problem's initial state constraints.
	 */
	public abstract List<? extends ExactCoverAction> getInitialStateActions();

	/**
	 * Returns whether or not this <code>ExactCoverProblem</code> is solved (in
	 * a goal state).
	 * 
	 * @return <code>true</code> if this problem is solved, else
	 *         <code>false</code>
	 */
	public abstract boolean isSolved();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public abstract ExactCoverProblem clone();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public abstract boolean equals(Object obj);
}
