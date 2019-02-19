/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * New expression. Node type is {@link Token#NEW}.
 * <p>
 *
 * <pre>
 * <i>NewExpression</i> :
 *      MemberExpression
 *      <b>new</b> NewExpression
 * </pre>
 *
 * This node is a subtype of {@link FunctionCall}, mostly for internal code
 * sharing. Structurally a {@code NewExpression} node is very similar to a
 * {@code FunctionCall}, so it made a certain amount of sense.
 */
public class NewExpression extends FunctionCall {

    private ObjectLiteral initializer;

    {
	type = Token.NEW;
    }

    public NewExpression() {
    }

    public NewExpression(int pos) {
	super(pos);
    }

    public NewExpression(int pos, int len) {
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
	for (AstNode argument : this.getArguments()) {
	    array.add(argument.getJsonObject());
	}
	object.addProperty("type", "NewExpression");
	object.add("callee", this.getTarget().getJsonObject());
	object.add("arguments", array);
	object.add("criteria", getCriteriaAsJson());
	object.add("dependencies", getDependenciesAsJson());
	object.addProperty("change", changeType.toString());
	object.addProperty("change-noprop", changeTypeNoProp.toString());
	return object;
    }

    /**
     * Returns initializer object, if any.
     * 
     * @return extra initializer object-literal expression, or {@code null} if not
     *         specified.
     */
    public ObjectLiteral getInitializer() {
	return initializer;
    }

    /**
     * Sets initializer object. Rhino supports an experimental syntax of the form
     * {@code new expr [ ( arglist ) ] [initializer]}, in which initializer is an
     * object literal that is used to set additional properties on the newly-created
     * {@code expr} object.
     *
     * @param initializer
     *            extra initializer object. Can be {@code null}.
     */
    public void setInitializer(ObjectLiteral initializer) {
	this.initializer = initializer;
	if (initializer != null)
	    initializer.setParent(this);
    }

    @Override
    public String toSource(int depth) {
	StringBuilder sb = new StringBuilder();
	sb.append(makeIndent(depth));
	sb.append("new ");
	sb.append(target.toSource(0));
	sb.append("(");
	if (arguments != null) {
	    printList(arguments, sb);
	}
	sb.append(")");
	if (initializer != null) {
	    sb.append(" ");
	    sb.append(initializer.toSource(0));
	}
	return sb.toString();
    }

    /**
     * Visits this node, the target, and each argument. If there is a trailing
     * initializer node, visits that last.
     */
    @Override
    public void visit(NodeVisitor v) {
	if (v.visit(this)) {
	    target.visit(v);
	    for (AstNode arg : getArguments()) {
		arg.visit(v);
	    }
	    if (initializer != null) {
		initializer.visit(v);
	    }
	}
    }
}
