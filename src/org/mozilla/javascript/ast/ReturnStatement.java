/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import com.google.gson.JsonObject;

import org.mozilla.javascript.Token;

/**
 * Return statement.  Node type is {@link Token#RETURN}.<p>
 *
 * <pre><i>ReturnStatement</i> :
 *      <b>return</b> [<i>no LineTerminator here</i>] [Expression] ;</pre>
 */
public class ReturnStatement extends AstNode {

    private AstNode returnValue;

    {
        type = Token.RETURN;
    }

    public ReturnStatement() {
    }

    public ReturnStatement(int pos) {
        super(pos);
    }

    public ReturnStatement(int pos, int len) {
        super(pos, len);
    }

    public ReturnStatement(int pos, int len, AstNode returnValue) {
        super(pos, len);
        setReturnValue(returnValue);
    }


    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
    		JsonObject object = new JsonObject();
		object.addProperty("type", "ReturnStatement");
		object.add("argument", this.getReturnValue().getJsonObject());
		object.addProperty("change", changeType.toString());
		object.addProperty("moved", String.valueOf(isMoved()));
		return object;
    }

    /**
     * Clones the AstNode.
     * @return The clone of the AstNode.
     * @throws CloneNotSupportedException
     */
    @Override
    public AstNode clone(AstNode parent) {

    	/* Get the shallow clone. */
    	ReturnStatement clone = (ReturnStatement)super.clone();
    	clone.setParent(parent);
    	clone.changeType = this.changeType;
    	clone.fixedPosition = this.fixedPosition;
    	clone.ID = this.ID;

    	/* Clone the children. */
    	AstNode returnValue = null;
    	if(this.getReturnValue() != null) returnValue = this.getReturnValue().clone(clone);
    	clone.setReturnValue(returnValue);

    	return clone;

    }

    /**
     * Returns return value, {@code null} if return value is void
     */
    public AstNode getReturnValue() {
        return returnValue;
    }

    /**
     * Sets return value expression, and sets its parent to this node.
     * Can be {@code null}.
     */
    public void setReturnValue(AstNode returnValue) {
        this.returnValue = returnValue;
        if (returnValue != null)
            returnValue.setParent(this);
    }

    @Override
    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append("return");
        if (returnValue != null) {
            sb.append(" ");
            sb.append(returnValue.toSource(0));
        }
        sb.append(";\n");
        return sb.toString();
    }

    /**
     * Visits this node, then the return value if specified.
     */
    @Override
    public void visit(NodeVisitor v) {
        if (v.visit(this) && returnValue != null) {
            returnValue.visit(v);
        }
    }

	@Override
	public boolean isStatement() {
		return true;
	}
}
