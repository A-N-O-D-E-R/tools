package com.anode.tool.document;

/**
 * Represents the difference information between two paths in documents.
 */
public class DiffInfo {

	private PathDiffResult diffResult;
	private PathValue left;
	private PathValue right;

	/**
	 * Constructs a DiffInfo with the specified difference information.
	 * @param diffResult the type of difference
	 * @param left the left path value
	 * @param right the right path value
	 */
	public DiffInfo(PathDiffResult diffResult, PathValue left, PathValue right) {
		this.diffResult = diffResult;
		this.left = left;
		this.right = right;
	}

	/**
	 * Gets the difference result.
	 * @return the difference result
	 */
	public PathDiffResult getDiffResult() {
		return diffResult;
	}

	/**
	 * Gets the left path value.
	 * @return the left path value
	 */
	public PathValue getLeft() {
		return left;
	}

	/**
	 * Gets the right path value.
	 * @return the right path value
	 */
	public PathValue getRight() {
		return right;
	}

}
