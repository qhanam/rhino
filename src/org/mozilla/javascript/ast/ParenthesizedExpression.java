/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.mozilla.javascript.Token;

/**
 * AST node for a parenthesized expression.
 * Node type is {@link Token#LP}.<p>
 */
public class ParenthesizedExpression extends AstNode {

    private AstNode expression;

    {
        type = Token.LP;
    }

    public ParenthesizedExpression() {
    }

    public ParenthesizedExpression(int pos) {
        super(pos);
    }

    public ParenthesizedExpression(int pos, int len) {
        super(pos, len);
    }

    public ParenthesizedExpression(AstNode expr) {
        this(expr != null ? expr.getPosition() : 0,
             expr != null ? expr.getLength() : 1,
             expr);
    }

    public ParenthesizedExpression(int pos, int len, AstNode expr) {
        super(pos, len);
        setExpression(expr);
    }

    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
    		return this.getExpression().getJsonObject();
    }

    /**
     * Clones the AstNode.
     * @return The clone of the AstNode.
     * @throws CloneNotSupportedException
     */
    @Override
    public AstNode clone(AstNode parent) {

    	/* Get the shallow clone. */
    	ParenthesizedExpression clone = (ParenthesizedExpression)super.clone();
    	clone.setParent(parent);
    	clone.changeType = this.changeType;
    	clone.fixedPosition = this.fixedPosition;
    	clone.ID = this.ID;

    	/* Clone the children. */
    	AstNode expression = null;

    	if(this.getExpression() != null) expression = this.getExpression().clone(clone);
    	clone.setExpression(expression);
    	if(expression != null) expression.setParent(clone);

    	return clone;

    }

    /**
     * Returns the expression between the parens
     */
    public AstNode getExpression() {
        return expression;
    }

    /**
     * Sets the expression between the parens, and sets the parent
     * to this node.
     * @param expression the expression between the parens
     * @throws IllegalArgumentException} if expression is {@code null}
     */
    public void setExpression(AstNode expression) {
        assertNotNull(expression);
        this.expression = expression;
        expression.setParent(this);
    }

    @Override
    public String toSource(int depth) {
        return makeIndent(depth) + "(" + expression.toSource(0) + ")";
    }

    /**
     * Visits this node, then the child expression.
     */
    @Override
    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            expression.visit(v);
        }
    }

	@Override
	public boolean isStatement() {
		return false;
	}
}
