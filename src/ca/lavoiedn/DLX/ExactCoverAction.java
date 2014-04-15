package ca.lavoiedn.DLX;

import ca.lavoiedn.ToroidalList.ToroidalNode;

/**
 * Abstract class for actions on an {@link ca.lavoiedn.DLX.ExactCoverProblem}.
 * 
 * @author lavoiedn
 */
public abstract class ExactCoverAction {

	public ExactCoverAction() {

	}

	public ExactCoverAction(String[] keys) {
		setFromConstraintKeys(keys);
	}

	public ExactCoverAction(ToroidalNode node) {
		setFromToroidalNode(node);
	}

	/**
	 * Returns whether or not this <code>ExactCoverAction</code> is valid.
	 * 
	 * @return <code>true</code> if this <code>ExactCoverAction</code> is valid,
	 *         else <code>false</code>.
	 */
	public abstract boolean isValid();

	/**
	 * Sets this action's values to the given constraint keys.
	 * 
	 * @param keys
	 *            The constraint keys to use for this action's values.
	 * @return <code>true</code> if the constraint keys were valid, else
	 *         <code>false</code>
	 */
	protected abstract boolean setFromConstraintKeys(String[] keys);

	/**
	 * Sets this action's values from the given
	 * {@link ca.lavoiedn.ToroidalList.ToroidalNode}, which requires exploring a
	 * full row.
	 * 
	 * @param node
	 *            The given {@link ca.lavoiedn.ToroidalList.ToroidalNode}.
	 * @return <code>true</code> if the <code>node</code> contained a valid key
	 *         for this action, else <code>false</code>.
	 */
	protected abstract boolean setFromToroidalNode(ToroidalNode node);

	/**
	 * Returns the constraint keys corresponding to this action.
	 * 
	 * @return The constraint keys corresponding to this action.
	 */
	public abstract String[] getConstraintKeys();
}
