/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.mozilla.javascript.Token;

/**
 * AST node for keyword literals:  currently, {@code this},
 * {@code null}, {@code true}, {@code false}, and {@code debugger}.
 * Node type is one of
 * {@link Token#THIS},
 * {@link Token#NULL},
 * {@link Token#TRUE},
 * {@link Token#FALSE}, or
 * {@link Token#DEBUGGER}.
 */
public class KeywordLiteral extends AstNode {

    public KeywordLiteral() {
    }

    public KeywordLiteral(int pos) {
        super(pos);
    }

    public KeywordLiteral(int pos, int len) {
        super(pos, len);
    }

    /**
     * Constructs a new KeywordLiteral
     * @param nodeType the token type
     */
    public KeywordLiteral(int pos, int len, int nodeType) {
        super(pos, len);
        setType(nodeType);
    }

    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
    		JsonBuilderFactory factory = Json.createBuilderFactory(null);
    		switch(this.getType()) {
    		case Token.THIS:
				return factory.createObjectBuilder()
						.add("type", "ThisExpression")
						.add("change", changeType.toString())
						.add("moved", String.valueOf(isMoved())).build();
    		case Token.TRUE:
				return factory.createObjectBuilder()
						.add("type", "Literal")
						.add("value", true)
						.add("raw", "true")
						.add("change", changeType.toString())
						.add("moved", String.valueOf(isMoved())).build();
    		case Token.FALSE:
				return factory.createObjectBuilder()
						.add("type", "Literal")
						.add("value", false)
						.add("raw", "false")
						.add("change", changeType.toString())
						.add("moved", String.valueOf(isMoved())).build();
    		case Token.DEBUGGER:
				return factory.createObjectBuilder()
						.add("type", "DebuggerStatement")
						.add("change", changeType.toString())
						.add("moved", String.valueOf(isMoved())).build();
    		case Token.NULL:
    		default:
				return factory.createObjectBuilder()
						.add("type", "Literal")
						.add("value", (String)null)
						.add("raw", "null")
						.add("change", changeType.toString())
						.add("moved", String.valueOf(isMoved())).build();
    		}

    }

    /**
     * Sets node token type
     * @throws IllegalArgumentException if {@code nodeType} is unsupported
     */
    @Override
    public KeywordLiteral setType(int nodeType) {
        if (!(nodeType == Token.THIS
              || nodeType == Token.NULL
              || nodeType == Token.TRUE
              || nodeType == Token.FALSE
              || nodeType == Token.DEBUGGER))
            throw new IllegalArgumentException("Invalid node type: "
                                               + nodeType);
        type = nodeType;
        return this;
    }

    /**
     * Returns true if the token type is {@link Token#TRUE} or
     * {@link Token#FALSE}.
     */
    public boolean isBooleanLiteral() {
        return type == Token.TRUE || type == Token.FALSE;
    }

    @Override
    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        switch (getType()) {
        case Token.THIS:
            sb.append("this");
            break;
        case Token.NULL:
            sb.append("null");
            break;
        case Token.TRUE:
            sb.append("true");
            break;
        case Token.FALSE:
            sb.append("false");
            break;
        case Token.DEBUGGER:
            sb.append("debugger;\n");
            break;
        default:
            throw new IllegalStateException("Invalid keyword literal type: "
                                            + getType());
        }
        return sb.toString();
    }

    /**
     * Visits this node.  There are no children to visit.
     */
    @Override
    public void visit(NodeVisitor v) {
        v.visit(this);
    }

	@Override
	public boolean isStatement() {
		return false;
	}
}
