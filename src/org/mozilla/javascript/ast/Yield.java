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
 * AST node for JavaScript 1.7 {@code yield} expression or statement.
 * Node type is {@link Token#YIELD}.<p>
 *
 * <pre><i>Yield</i> :
 *   <b>yield</b> [<i>no LineTerminator here</i>] [non-paren Expression] ;</pre>
 */
public class Yield extends AstNode {

    private AstNode value;

    {
        type = Token.YIELD;
    }

    public Yield() {
    }

    public Yield(int pos) {
        super(pos);
    }

    public Yield(int pos, int len) {
        super(pos, len);
    }

    public Yield(int pos, int len, AstNode value) {
        super(pos, len);
        setValue(value);
    }

    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
    		JsonBuilderFactory factory = Json.createBuilderFactory(null);
    		JsonObject value = this.getValue() == null ? null : this.getValue().getJsonObject();
    		return factory.createObjectBuilder()
    				.add("type", "YieldExpression")
    				.add("expression", value)
    				.add("delegate", false)
    				.add("change", changeType.toString())
    				.add("moved", String.valueOf(isMoved())).build();
    }

    /**
     * Returns yielded expression, {@code null} if none
     */
    public AstNode getValue() {
        return value;
    }

    /**
     * Sets yielded expression, and sets its parent to this node.
     * @param expr the value to yield. Can be {@code null}.
     */
    public void setValue(AstNode expr) {
        this.value = expr;
        if (expr != null)
            expr.setParent(this);
    }

    @Override
    public String toSource(int depth) {
        return value == null
                ? "yield"
                : "yield " + value.toSource(0);
    }

    /**
     * Visits this node, and if present, the yielded value.
     */
    @Override
    public void visit(NodeVisitor v) {
        if (v.visit(this) && value != null) {
            value.visit(v);
        }
    }

	@Override
	public boolean isStatement() {
		return true;
	}
}
