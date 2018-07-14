/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2016 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
package com.github.javaparser.ast.expr;

import com.github.javaparser.Range;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import static com.github.javaparser.utils.Utils.assertNotNull;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.DerivedProperty;
import com.github.javaparser.metamodel.UnaryExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.printer.Printable;

/**
 * An expression where an operator is applied to a single expression.
 * It supports the operators that are found the the UnaryExpr.Operator enum.
 * <br/><code>11++</code>
 * <br/><code>++11</code>
 * <br/><code>~1</code>
 * <br/><code>-333</code>
 *
 * @author Julio Vilmar Gesser
 */
public final class UnaryExpr extends Expression implements NodeWithExpression<UnaryExpr> {

    public enum Operator implements Printable {

        PLUS("+", false), MINUS("-", false), PREFIX_INCREMENT("++", false), PREFIX_DECREMENT("--", false), LOGICAL_COMPLEMENT("!", false), BITWISE_COMPLEMENT("~", false), POSTFIX_INCREMENT("++", true), POSTFIX_DECREMENT("--", true);

        private final String codeRepresentation;

        private final boolean isPostfix;

        Operator(String codeRepresentation, boolean isPostfix) {
            this.codeRepresentation = codeRepresentation;
            this.isPostfix = isPostfix;
        }

        public String asString() {
            return codeRepresentation;
        }

        public boolean isPostfix() {
            return isPostfix;
        }

        public boolean isPrefix() {
            return !isPostfix();
        }
    }

    private Expression expression;

    private Operator operator;

    public UnaryExpr() {
        this(null, new IntegerLiteralExpr(), Operator.POSTFIX_INCREMENT);
    }

    @AllFieldsConstructor
    public UnaryExpr(final Expression expression, final Operator operator) {
        this(null, expression, operator);
    }

    public UnaryExpr(final Range range, final Expression expression, final Operator operator) {
        super(range);
        setExpression(expression);
        setOperator(operator);
    }

    @Override
    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.visit(this, arg);
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public UnaryExpr setExpression(final Expression expression) {
        assertNotNull(expression);
        if (expression == this.expression) {
            return (UnaryExpr) this;
        }
        notifyPropertyChange(ObservableProperty.EXPRESSION, this.expression, expression);
        if (this.expression != null)
            this.expression.setParentNode(null);
        this.expression = expression;
        setAsParentNodeOf(expression);
        return this;
    }

    public UnaryExpr setOperator(final Operator operator) {
        assertNotNull(operator);
        if (operator == this.operator) {
            return (UnaryExpr) this;
        }
        notifyPropertyChange(ObservableProperty.OPERATOR, this.operator, operator);
        this.operator = operator;
        return this;
    }

    @Override
    public boolean remove(Node node) {
        if (node == null)
            return false;
        return super.remove(node);
    }

    @DerivedProperty
    public boolean isPostfix() {
        return operator.isPostfix();
    }

    @DerivedProperty
    public boolean isPrefix() {
        return !isPostfix();
    }

    @Override
    public UnaryExpr clone() {
        return (UnaryExpr) accept(new CloneVisitor(), null);
    }

    @Override
    public UnaryExprMetaModel getMetaModel() {
        return JavaParserMetaModel.unaryExprMetaModel;
    }
}
