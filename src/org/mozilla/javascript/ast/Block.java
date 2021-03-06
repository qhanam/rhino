/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A block statement delimited by curly braces. The node position is the
 * position of the open-curly, and the length extends to the position of the
 * close-curly. Node type is {@link Token#BLOCK}.
 *
 * <pre>
 * <i>Block</i> :
 *     <b>{</b> Statement* <b>}</b>
 * </pre>
 */
public class Block extends AstNode {

    {
	this.type = Token.BLOCK;
    }

    public Block() {
    }

    public Block(int pos) {
	super(pos);
    }

    public Block(int pos, int len) {
	super(pos, len);
    }

    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
	JsonObject object = new JsonObject();
	JsonArray array = new JsonArray();
	for (Node element : this)
	    array.add(((AstNode) element).getJsonObject());
	object.addProperty("type", "BlockStatement");
	object.add("body", array);
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
	Block clone = (Block) super.clone();
	clone.setParent(parent);
	clone.moved = this.moved;
	clone.changeType = this.changeType;
	clone.changeTypeNoProp = this.changeTypeNoProp;
	clone.fixedPosition = this.fixedPosition;
	clone.ID = this.ID;

	/* Clone the children. */
	clone.removeChildren();

	/* Clone the children and add them. */
	for (Node c : this) {
	    AstNode child = (AstNode) c;
	    clone.addChild(child.clone(clone));
	}

	return clone;

    }

    /**
     * Alias for {@link #addChild}.
     */
    public void addStatement(AstNode statement) {
	addChild(statement);
    }

    @Override
    public String toSource(int depth) {
	StringBuilder sb = new StringBuilder();
	sb.append(makeIndent(depth));
	sb.append("{\n");
	for (Node kid : this) {
	    sb.append(((AstNode) kid).toSource(depth + 1));
	}
	sb.append(makeIndent(depth));
	sb.append("}\n");
	return sb.toString();
    }

    @Override
    public void visit(NodeVisitor v) {
	if (v.visit(this)) {
	    for (Node kid : this) {
		((AstNode) kid).visit(v);
	    }
	}
    }

    @Override
    public boolean isStatement() {
	return true;
    }
}
