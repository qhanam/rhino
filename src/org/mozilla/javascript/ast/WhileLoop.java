/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;

import com.google.gson.JsonObject;

/**
 * While statement. Node type is {@link Token#WHILE}.
 * <p>
 *
 * <pre>
 * <i>WhileStatement</i>:
 *     <b>while</b> <b>(</b> Expression <b>)</b> Statement
 * </pre>
 */
public class WhileLoop extends Loop {

    private AstNode condition;

    {
	type = Token.WHILE;
    }

    public WhileLoop() {
    }

    public WhileLoop(int pos) {
	super(pos);
    }

    public WhileLoop(int pos, int len) {
	super(pos, len);
    }

    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
	JsonObject object = new JsonObject();
	object.addProperty("type", "WhileStatement");
	object.add("test", this.getCondition().getJsonObject());
	object.add("body", this.getBody().getJsonObject());
	object.add("criteria", getCriteriaAsJson());
	object.add("dependencies", getDependenciesAsJson());
	object.addProperty("change", changeType.toString());
	object.addProperty("change-noprop", changeTypeNoProp.toString());
	return object;
    }

    /**
     * Clones the AstNode.
     * 
     * @return The clone of the AstNode.
     * @throws CloneNotSupportedException
     */
    @Override
    public AstNode clone(AstNode parent) {

	/* Get the shallow clone. */
	WhileLoop clone = (WhileLoop) super.clone();
	clone.setParent(parent);
	clone.moved = this.moved;
	clone.changeType = this.changeType;
	clone.changeTypeNoProp = this.changeTypeNoProp;
	clone.fixedPosition = this.fixedPosition;
	clone.ID = this.ID;

	/* Clone the children. */
	AstNode condition = null;
	AstNode body = null;

	if (this.getCondition() != null)
	    condition = this.getCondition().clone(clone);
	if (this.getBody() != null)
	    body = this.getBody().clone(clone);

	clone.setCondition(condition);
	clone.setBody(body);

	return clone;

    }

    /**
     * Returns loop condition
     */
    public AstNode getCondition() {
	return condition;
    }

    /**
     * Sets loop condition
     * 
     * @throws IllegalArgumentException}
     *             if condition is {@code null}
     */
    public void setCondition(AstNode condition) {
	assertNotNull(condition);
	this.condition = condition;
	condition.setParent(this);
    }

    @Override
    public String toSource(int depth) {
	StringBuilder sb = new StringBuilder();
	sb.append(makeIndent(depth));
	sb.append("while (");
	sb.append(condition.toSource(0));
	sb.append(") ");
	if (body.getType() == Token.BLOCK) {
	    sb.append(body.toSource(depth).trim());
	    sb.append("\n");
	} else {
	    sb.append("\n").append(body.toSource(depth + 1));
	}
	return sb.toString();
    }

    /**
     * Visits this node, the condition, then the body.
     */
    @Override
    public void visit(NodeVisitor v) {
	if (v.visit(this)) {
	    condition.visit(v);
	    body.visit(v);
	}
    }

}
