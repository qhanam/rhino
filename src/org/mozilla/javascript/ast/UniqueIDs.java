package org.mozilla.javascript.ast;

/**
 * Generates unique identifiers for AST nodes.
 */
public class UniqueIDs {

    private static int nextId = 0;

    public static int getNextId() {
	if (nextId == Integer.MAX_VALUE) {
	    // Reset the id. No file will ever have this many AST nodes, so this is safe.
	    nextId = 0;
	}
	nextId++;
	return nextId;
    }

}