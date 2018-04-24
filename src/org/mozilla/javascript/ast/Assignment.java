/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import com.google.gson.JsonObject;

/**
 * AST node representing the set of assignment operators such as {@code =},
 * {@code *=} and {@code +=}.
 */
public class Assignment extends InfixExpression {

    public Assignment() {
    }

    public Assignment(int pos) {
        super(pos);
    }

    public Assignment(int pos, int len) {
        super(pos, len);
    }

    public Assignment(int pos, int len, AstNode left, AstNode right) {
        super(pos, len, left, right);
    }

    public Assignment(AstNode left, AstNode right) {
        super(left, right);
    }

    public Assignment(int operator, AstNode left,
                      AstNode right, int operatorPos) {
        super(operator, left, right, operatorPos);
    }

    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
    		JsonObject object = new JsonObject();
		object.addProperty("type", "AssignmentExpression");
		object.addProperty("operator", "=");
		object.add("left", this.getLeft().getJsonObject());
		object.add("right", this.getRight().getJsonObject());
		object.addProperty("change", changeType.toString());
		object.addProperty("moved", String.valueOf(isMoved()));
		return object;
    }

}
