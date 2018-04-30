/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.ast;

import org.mozilla.javascript.Token;

import com.google.gson.JsonObject;

/**
 * AST node representing an infix (binary operator) expression.
 * The operator is the node's {@link Token} type.
 */
public class InfixExpression extends AstNode {

    protected AstNode left;
    protected AstNode right;
    protected int operatorPosition = -1;

    public InfixExpression() {
    }

    public InfixExpression(int pos) {
        super(pos);
    }

    public InfixExpression(int pos, int len) {
        super(pos, len);
    }

    public InfixExpression(int pos, int len,
                           AstNode left,
                           AstNode right) {
        super(pos, len);
        setLeft(left);
        setRight(right);
    }

    /**
     * Constructs a new {@code InfixExpression}.  Updates bounds to include
     * left and right nodes.
     */
    public InfixExpression(AstNode left, AstNode right) {
        setLeftAndRight(left, right);
    }

    /**
     * Constructs a new {@code InfixExpression}.
     * @param operatorPos the <em>absolute</em> position of the operator
     */
    public InfixExpression(int operator, AstNode left,
                           AstNode right, int operatorPos) {
        setType(operator);
        setOperatorPosition(operatorPos - left.getPosition());
        setLeftAndRight(left, right);
    }

    /**
     * @return This node as a JSON object in Esprima format.
     * @author qhanam
     */
    @Override
    public JsonObject getJsonObject() {
    		JsonObject object = new JsonObject();
    		String type = null;
    		String operator = null;
    		
    		switch(this.getOperator()) {
    		case Token.ASSIGN: // Assignment Operators
    		case Token.ASSIGN_ADD:
    		case Token.ASSIGN_SUB:
    		case Token.ASSIGN_MUL:
    		case Token.ASSIGN_DIV:
    		case Token.ASSIGN_MOD:
    		case Token.ASSIGN_LSH:
    		case Token.ASSIGN_RSH:
    		case Token.URSH:
    		case Token.ASSIGN_BITAND:
    		case Token.ASSIGN_BITXOR:
    		case Token.ASSIGN_BITOR:
    			type = "AssignmentExpression";
    			break;
    		case Token.EQ: // Comparison Operators
    		case Token.NE:
    		case Token.SHEQ:
    		case Token.SHNE:
    		case Token.GT:
    		case Token.GE:
    		case Token.LT:
    		case Token.LE:
    		case Token.IN:
    		case Token.INSTANCEOF:
    			type = "BinaryExpression";
    			break;
    		case Token.ADD: // Arithmetic Operators
    		case Token.SUB:
    		case Token.MUL:
    		case Token.DIV:
    		case Token.MOD:
    			type = "BinaryExpression";
    			break;
    		case Token.BITAND: // Bitwise Operators
    		case Token.BITOR:
    		case Token.BITXOR:
    		case Token.LSH:
    		case Token.RSH:
    			type = "BinaryExpression";
    			break;
    		case Token.AND:	// Logical Operators
    		case Token.OR:
    			type = "LogicalExpression";
    			break;
		default:
			type= "BinaryExpression";
			break;
    		}

    		switch(this.getOperator()) {
    		case Token.ASSIGN: // Assignment Operators
    			operator = "="; break;
    		case Token.ASSIGN_ADD:
    			operator = "+="; break;
    		case Token.ASSIGN_SUB:
    			operator = "-="; break;
    		case Token.ASSIGN_MUL:
    			operator = "*="; break;
    		case Token.ASSIGN_DIV:
    			operator = "/="; break;
    		case Token.ASSIGN_MOD:
    			operator = "%="; break;
    		case Token.ASSIGN_LSH:
    			operator = "<<="; break;
    		case Token.ASSIGN_RSH:
    			operator = ">>="; break;
    		case Token.URSH:
    			operator = ">>>="; break;
    		case Token.ASSIGN_BITAND:
    			operator = "&="; break;
    		case Token.ASSIGN_BITXOR:
    			operator = "^="; break;
    		case Token.ASSIGN_BITOR:
    			operator = "|="; break;
    		case Token.EQ: // Comparison Operators
    			operator = "=="; break;
    		case Token.NE:
    			operator = "!="; break;
    		case Token.SHEQ:
    			operator = "==="; break;
    		case Token.SHNE:
    			operator = "!=="; break;
    		case Token.GT:
    			operator = ">"; break;
    		case Token.GE:
    			operator = ">="; break;
    		case Token.LT:
    			operator = "<"; break;
    		case Token.LE:
    			operator = "<="; break;
    		case Token.IN:
    			operator = "in"; break;
    		case Token.INSTANCEOF:
    			operator = "instanceof"; break;
    		case Token.ADD: // Arithmetic Operators
    			operator = "+"; break;
    		case Token.SUB:
    			operator = "-"; break;
    		case Token.MUL:
    			operator = "*"; break;
    		case Token.DIV:
    			operator = "/"; break;
    		case Token.MOD:
    			operator = "%"; break;
    		case Token.BITAND: // Bitwise Operators
    			operator = "&"; break;
    		case Token.BITOR:
    			operator = "|"; break;
    		case Token.BITXOR:
    			operator = "^"; break;
    		case Token.LSH:
    			operator = "<<"; break;
    		case Token.RSH:
    			operator = ">>"; break;
    		case Token.AND:	// Logical Operators
    			operator = "&&"; break;
    		case Token.OR:
    			operator = "||"; break;
		default:
			operator = null; break;
    		}

		object.addProperty("type", type);
		object.addProperty("operator", operator);
		object.add("left", this.getLeft().getJsonObject());
		object.add("right", this.getRight().getJsonObject());
    		object.addProperty("change", changeType.toString());
    		object.addProperty("change-noprop", changeTypeNoProp.toString());
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
    	InfixExpression clone = (InfixExpression)super.clone();
    	clone.setParent(parent);
    	clone.changeType = this.changeType;
    	clone.fixedPosition = this.fixedPosition;
    	clone.ID = this.ID;

    	/* Clone the children. */
    	AstNode left = this.getLeft().clone(clone);
    	AstNode right = this.getRight().clone(clone);

    	clone.setLeft(left);
    	clone.setRight(right);

    	return clone;

    }

    public void setLeftAndRight(AstNode left, AstNode right) {
        assertNotNull(left);
        assertNotNull(right);
        // compute our bounds while children have absolute positions
        int beg = left.getPosition();
        int end = right.getPosition() + right.getLength();
        setBounds(beg, end);

        // this updates their positions to be parent-relative
        setLeft(left);
        setRight(right);
    }

    /**
     * Returns operator token &ndash; alias for {@link #getType}
     */
    public int getOperator() {
        return getType();
    }

    /**
     * Sets operator token &ndash; like {@link #setType}, but throws
     * an exception if the operator is invalid.
     * @throws IllegalArgumentException if operator is not a valid token
     * code
     */
    public void setOperator(int operator) {
        if (!Token.isValidToken(operator))
            throw new IllegalArgumentException("Invalid token: " + operator);
        setType(operator);
    }

    /**
     * Returns the left-hand side of the expression
     */
    public AstNode getLeft() {
        return left;
    }

    /**
     * Sets the left-hand side of the expression, and sets its
     * parent to this node.
     * @param left the left-hand side of the expression
     * @throws IllegalArgumentException} if left is {@code null}
     */
    public void setLeft(AstNode left) {
        assertNotNull(left);
        this.left = left;
        // line number should agree with source position
        setLineno(left.getLineno());
        left.setParent(this);
    }

    /**
     * Returns the right-hand side of the expression
     * @return the right-hand side.  It's usually an
     * {@link AstNode} node, but can also be a {@link FunctionNode}
     * representing Function expressions.
     */
    public AstNode getRight() {
        return right;
    }

    /**
     * Sets the right-hand side of the expression, and sets its parent to this
     * node.
     * @throws IllegalArgumentException} if right is {@code null}
     */
    public void setRight(AstNode right) {
        assertNotNull(right);
        this.right = right;
        right.setParent(this);
    }

    /**
     * Returns relative offset of operator token
     */
    public int getOperatorPosition() {
        return operatorPosition;
    }

    /**
     * Sets operator token's relative offset
     * @param operatorPosition offset in parent of operator token
     */
    public void setOperatorPosition(int operatorPosition) {
        this.operatorPosition = operatorPosition;
    }

    @Override
    public boolean hasSideEffects() {
        // the null-checks are for malformed expressions in IDE-mode
        switch (getType()) {
          case Token.COMMA:
              return right != null && right.hasSideEffects();
          case Token.AND:
          case Token.OR:
              return left != null && left.hasSideEffects()
                      || (right != null && right.hasSideEffects());
          default:
              return super.hasSideEffects();
        }
    }

    @Override
    public String toSource(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(makeIndent(depth));
        sb.append(left.toSource());
        sb.append(" ");
        sb.append(operatorToString(getType()));
        sb.append(" ");
        sb.append(right.toSource());
        return sb.toString();
    }

    /**
     * Visits this node, the left operand, and the right operand.
     */
    @Override
    public void visit(NodeVisitor v) {
        if (v.visit(this)) {
            left.visit(v);
            right.visit(v);
        }
    }

	@Override
	public boolean isStatement() {
		return false;
	}
}
