/*
 * Sonar PL/SQL Plugin (Community)
 * Copyright (C) 2015 Felipe Zorzo
 * felipe.b.zorzo@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.plsqlopen.api;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.sonar.plugins.plsqlopen.api.PlSqlKeyword.*;
import static org.sonar.plugins.plsqlopen.api.PlSqlPunctuator.*;
import static org.sonar.plugins.plsqlopen.api.PlSqlTokenType.*;

import java.util.List;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

public enum PlSqlGrammar implements GrammarRuleKey {
    
    // Data types
    DATATYPE,
    CHARACTER_SET_CLAUSE,
    NUMERIC_DATATYPE,
    LOB_DATATYPE,
    CHARACTER_DATAYPE,
    BOOLEAN_DATATYPE,
    WITH_TIME_ZONE,
    DATE_DATATYPE,
    CUSTOM_SUBTYPE,
    ANCHORED_DATATYPE,
    CUSTOM_DATATYPE,
    REF_DATATYPE,
    
    // Literals
    LITERAL,
    BOOLEAN_LITERAL,
    NULL_LITERAL,
    NUMERIC_LITERAL,
    CHARACTER_LITERAL,
    
    // Expressions
    EXPRESSION,
    AND_EXPRESSION,
    OR_EXPRESSION,
    BOOLEAN_EXPRESSION,
    PRIMARY_EXPRESSION, 
    BRACKED_EXPRESSION, 
    MEMBER_EXPRESSION, 
    OBJECT_REFERENCE, 
    POSTFIX_EXPRESSION, 
    IN_EXPRESSION,
    EXISTS_EXPRESSION,
    UNARY_EXPRESSION, 
    MULTIPLICATIVE_EXPRESSION, 
    ADDITIVE_EXPRESSION, 
    CONCATENATION_EXPRESSION, 
    RELATIONAL_OPERATOR,
    COMPARISON_EXPRESSION, 
    EXPONENTIATION_EXPRESSION, 
    ARGUMENT, 
    ARGUMENTS,
    METHOD_CALL,
    CALL_EXPRESSION,
    CASE_EXPRESSION,
    EXTRACT_DATETIME_EXPRESSION,
    XMLSERIALIZE_EXPRESSION,
    XML_COLUMN,
    XMLATTRIBUTES_EXPRESSION,
    XMLELEMENT_EXPRESSION,
    XMLFOREST_EXPRESSION,
    CAST_EXPRESSION,
    AT_TIME_ZONE_EXPRESSION,
    
    // DML
    PARTITION_BY_CLAUSE,
    WINDOWING_LIMIT,
    WINDOWING_CLAUSE,
    ANALYTIC_CLAUSE,
    ON_OR_USING_EXPRESSION,
    INNER_CROSS_JOIN_CLAUSE,
    OUTER_JOIN_TYPE,
    QUERY_PARTITION_CLAUSE,
    OUTER_JOIN_CLAUSE,
    JOIN_CLAUSE,
    SELECT_COLUMN,
    FROM_CLAUSE,
    WHERE_CLAUSE,
    INTO_CLAUSE,
    GROUP_BY_CLAUSE,
    HAVING_CLAUSE,
    ORDER_BY_ITEM,
    ORDER_BY_CLAUSE,
    FOR_UPDATE_CLAUSE,
    CONNECT_BY_CLAUSE,
    START_WITH_CLAUSE,
    HIERARCHICAL_QUERY_CLAUSE,
    SUBQUERY_FACTORING_CLAUSE,
    SELECT_EXPRESSION,
    
    // Statements
    LABEL,
    STATEMENTS_SECTION,
    BLOCK_STATEMENT,
    NULL_STATEMENT,
    ASSIGNMENT_STATEMENT,
    ELSIF_CLAUSE,
    ELSE_CLAUSE,
    IF_STATEMENT,
    LOOP_STATEMENT,
    EXIT_STATEMENT,
    CONTINUE_STATEMENT,
    FOR_STATEMENT,
    WHILE_STATEMENT,
    RETURN_STATEMENT,
    COMMIT_STATEMENT,
    ROLLBACK_STATEMENT,
    SAVEPOINT_STATEMENT,
    RAISE_STATEMENT,
    SELECT_STATEMENT,
    INSERT_COLUMNS,
    INSERT_STATEMENT,
    UPDATE_COLUMN,
    UPDATE_STATEMENT,
    DELETE_STATEMENT,
    CALL_STATEMENT,
    UNNAMED_ACTUAL_PAMETER,
    EXECUTE_IMMEDIATE_STATEMENT,
    OPEN_STATEMENT,
    OPEN_FOR_STATEMENT,
    FETCH_STATEMENT,
    CLOSE_STATEMENT,
    PIPE_ROW_STATEMENT,
    CASE_STATEMENT,
    STATEMENT,
    
    // Declarations
    DEFAULT_VALUE_ASSIGNMENT,
    VARIABLE_DECLARATION,
    PARAMETER_DECLARATION,
    CURSOR_PARAMETER_DECLARATION,
    CURSOR_DECLARATION,
    RECORD_FIELD_DECLARATION,
    RECORD_DECLARATION,
    TABLE_OF_DECLARATION,
    REF_CURSOR_DECLARATION,
    AUTONOMOUS_TRANSACTION_PRAGMA,
    EXCEPTION_INIT_PRAGMA,
    SERIALLY_REUSABLE_PRAGMA,
    PRAGMA_DECLARATION,
    HOST_AND_INDICATOR_VARIABLE,
    
    DECLARE_SECTION,
    EXCEPTION_HANDLER,
    IDENTIFIER_NAME,
    NON_RESERVED_KEYWORD,
    EXECUTE_PLSQL_BUFFER,
    
    // Program units
    COMPILATION_UNIT,
    UNIT_NAME,
    ANONYMOUS_BLOCK,
    PROCEDURE_DECLARATION,
    FUNCTION_DECLARATION,
    CREATE_PROCEDURE,
    CREATE_FUNCTION,
    CREATE_PACKAGE,
    CREATE_PACKAGE_BODY,
    VIEW_RESTRICTION_CLAUSE,
    CREATE_VIEW,
    
    // SQL Plus commands
    SQLPLUS_COMMAND,
    SQLPLUS_SHOW,
    
    // DDL
    DDL_COMMENT,
    DDL_COMMAND,
    TABLE_COLUMN_DEFINITION,
    TABLE_RELATIONAL_PROPERTIES,
    CREATE_TABLE,
    
    // Top-level components
    FILE_INPUT;

    public static LexerfulGrammarBuilder create() {
        LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

        List<PlSqlKeyword> keywords = PlSqlKeyword.getNonReservedKeywords();
        PlSqlKeyword[] rest = keywords.subList(2, keywords.size()).toArray(new PlSqlKeyword[keywords.size() - 2]);
        b.rule(NON_RESERVED_KEYWORD).is(b.firstOf(keywords.get(0), keywords.get(1), (Object[]) rest));
        
        b.rule(IDENTIFIER_NAME).is(b.firstOf(IDENTIFIER, NON_RESERVED_KEYWORD));
        b.rule(FILE_INPUT).is(b.oneOrMore(b.firstOf(
                COMPILATION_UNIT,
                SQLPLUS_COMMAND,
                DDL_COMMAND,
                EXECUTE_PLSQL_BUFFER)), EOF);

        createLiterals(b);
        createDatatypes(b);
        createStatements(b);
        createDmlStatements(b);
        createExpressions(b);
        createDeclarations(b);
        createProgramUnits(b);
        createSqlPlusCommands(b);
        createDdlCommands(b);
        
        b.setRootRule(FILE_INPUT);
        b.buildWithMemoizationOfMatchesForAllRules();
        
        return b;
    }

    private static void createLiterals(LexerfulGrammarBuilder b) {
        b.rule(NULL_LITERAL).is(NULL);
        b.rule(BOOLEAN_LITERAL).is(b.firstOf(TRUE, FALSE));
        b.rule(NUMERIC_LITERAL).is(b.firstOf(INTEGER_LITERAL, REAL_LITERAL, SCIENTIFIC_LITERAL));
        b.rule(CHARACTER_LITERAL).is(STRING_LITERAL);
        
        b.rule(LITERAL).is(b.firstOf(NULL_LITERAL, BOOLEAN_LITERAL, NUMERIC_LITERAL, CHARACTER_LITERAL, DATE_LITERAL));
    }
    
    private static void createDatatypes(LexerfulGrammarBuilder b) {
        b.rule(NUMERIC_DATATYPE).is(
                b.firstOf(
                        BINARY_DOUBLE,
                        BINARY_FLOAT,
                        BINARY_INTEGER,
                        DEC,
                        DECIMAL,
                        b.sequence(DOUBLE, PRECISION),
                        FLOAT,
                        INT,
                        INTEGER,
                        NATURAL,
                        NATURALN,
                        NUMBER,
                        NUMERIC,
                        PLS_INTEGER,
                        POSITIVE,
                        POSITIVEN,
                        REAL,
                        SIGNTYPE,
                        SMALLINT), 
                b.optional(LPARENTHESIS, INTEGER_LITERAL, b.optional(COMMA, INTEGER_LITERAL), RPARENTHESIS));
        
        b.rule(CHARACTER_SET_CLAUSE).is(CHARACTER, SET, b.firstOf(ANY_CS, b.sequence(IDENTIFIER_NAME, MOD, CHARSET)));
        
        b.rule(LOB_DATATYPE).is(b.firstOf(BFILE, BLOB, CLOB, NCLOB), b.optional(CHARACTER_SET_CLAUSE));
        
        b.rule(CHARACTER_DATAYPE).is(
                b.firstOf(
                        CHAR,
                        CHARACTER,
                        b.sequence(LONG, b.optional(RAW)),
                        NCHAR,
                        NVARCHAR2,
                        RAW,
                        ROWID,
                        STRING,
                        UROWID,
                        VARCHAR,
                        VARCHAR2), 
                b.optional(LPARENTHESIS, INTEGER_LITERAL, RPARENTHESIS),
                b.optional(CHARACTER_SET_CLAUSE));
        
        b.rule(BOOLEAN_DATATYPE).is(BOOLEAN);
        
        b.rule(DATE_DATATYPE).is(b.firstOf(
                DATE,
                b.sequence(TIMESTAMP, b.optional(WITH, b.optional(LOCAL), TIME, ZONE))));
        
        b.rule(ANCHORED_DATATYPE).is(CUSTOM_DATATYPE, MOD, b.firstOf(TYPE, ROWTYPE));

        b.rule(CUSTOM_DATATYPE).is(MEMBER_EXPRESSION);
        
        b.rule(REF_DATATYPE).is(REF, CUSTOM_DATATYPE);
        
        b.rule(DATATYPE).is(b.firstOf(
                NUMERIC_DATATYPE,
                LOB_DATATYPE,
                CHARACTER_DATAYPE,
                BOOLEAN_DATATYPE,
                DATE_DATATYPE,
                ANCHORED_DATATYPE,
                CUSTOM_DATATYPE,
                REF_DATATYPE));
    }

    private static void createStatements(LexerfulGrammarBuilder b) {
        b.rule(HOST_AND_INDICATOR_VARIABLE).is(COLON, IDENTIFIER_NAME, b.optional(COLON, IDENTIFIER_NAME));
        
        b.rule(NULL_STATEMENT).is(NULL, SEMICOLON);
        
        b.rule(EXCEPTION_HANDLER).is(WHEN, b.firstOf(OTHERS, OBJECT_REFERENCE), THEN, b.oneOrMore(STATEMENT));
        
        b.rule(LABEL).is(LLABEL, IDENTIFIER_NAME, RLABEL);
        
        b.rule(STATEMENTS_SECTION).is(
                BEGIN,
                b.oneOrMore(STATEMENT),
                b.optional(EXCEPTION, b.oneOrMore(EXCEPTION_HANDLER)), 
                END, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        b.rule(BLOCK_STATEMENT).is(
                b.optional(LABEL),
                b.optional(DECLARE, b.zeroOrMore(DECLARE_SECTION)),
                STATEMENTS_SECTION);
        
        b.rule(ASSIGNMENT_STATEMENT).is(b.optional(LABEL), OBJECT_REFERENCE, ASSIGNMENT, EXPRESSION, SEMICOLON);
        
        b.rule(ELSIF_CLAUSE).is(ELSIF, EXPRESSION, THEN, b.oneOrMore(STATEMENT));
        
        b.rule(ELSE_CLAUSE).is(ELSE, b.oneOrMore(STATEMENT));
        
        b.rule(IF_STATEMENT).is(
                b.optional(LABEL),
                IF, EXPRESSION, THEN,
                b.oneOrMore(STATEMENT),
                b.zeroOrMore(ELSIF_CLAUSE),
                b.optional(ELSE_CLAUSE),
                END, IF, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        b.rule(LOOP_STATEMENT).is(b.optional(LABEL), LOOP, b.oneOrMore(STATEMENT), END, LOOP, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        b.rule(EXIT_STATEMENT).is(b.optional(LABEL), EXIT, b.optional(WHEN, EXPRESSION), SEMICOLON);
        
        b.rule(CONTINUE_STATEMENT).is(b.optional(LABEL), CONTINUE, b.optional(WHEN, EXPRESSION), SEMICOLON);
        
        b.rule(FOR_STATEMENT).is(
                b.optional(LABEL), 
                FOR, IDENTIFIER_NAME, IN, b.optional(REVERSE),
                b.firstOf(
                        b.sequence(EXPRESSION, RANGE, EXPRESSION),
                        OBJECT_REFERENCE),
                LOOP,
                b.oneOrMore(STATEMENT),
                END, LOOP, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        b.rule(WHILE_STATEMENT).is(
                b.optional(LABEL), 
                WHILE, EXPRESSION, LOOP,
                b.oneOrMore(STATEMENT),
                END, LOOP, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        b.rule(RETURN_STATEMENT).is(b.optional(LABEL), RETURN, b.optional(EXPRESSION), SEMICOLON);
        
        b.rule(COMMIT_STATEMENT).is(
                b.optional(LABEL), 
                COMMIT,
                b.optional(WORK),
                b.optional(b.firstOf(
                        b.sequence(FORCE, STRING_LITERAL, b.optional(COMMA, INTEGER_LITERAL)),
                        b.sequence(
                                b.optional(COMMENT, STRING_LITERAL),
                                b.optional(WRITE, b.optional(b.firstOf(IMMEDIATE, BATCH)), b.optional(b.firstOf(WAIT, NOWAIT)))))),
                SEMICOLON);
        
        b.rule(ROLLBACK_STATEMENT).is(
                b.optional(LABEL), 
                ROLLBACK,
                b.optional(WORK),
                b.optional(b.firstOf(
                        b.sequence(FORCE, STRING_LITERAL),
                        b.sequence(TO, b.optional(SAVEPOINT), IDENTIFIER_NAME))),
                SEMICOLON);
        
        b.rule(SAVEPOINT_STATEMENT).is(b.optional(LABEL), SAVEPOINT, IDENTIFIER_NAME, SEMICOLON);
        
        b.rule(RAISE_STATEMENT).is(b.optional(LABEL), RAISE, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        b.rule(SELECT_STATEMENT).is(b.optional(LABEL), SELECT_EXPRESSION, SEMICOLON);
        
        b.rule(INSERT_COLUMNS).is(LPARENTHESIS, MEMBER_EXPRESSION, b.zeroOrMore(COMMA, MEMBER_EXPRESSION), RPARENTHESIS);
        
        b.rule(INSERT_STATEMENT).is(
                b.optional(LABEL), 
                INSERT, INTO, MEMBER_EXPRESSION, b.optional(IDENTIFIER_NAME),
                b.optional(INSERT_COLUMNS),
                b.firstOf(
                        b.sequence(VALUES, LPARENTHESIS, EXPRESSION, b.zeroOrMore(COMMA, EXPRESSION), RPARENTHESIS),
                        b.sequence(VALUES, EXPRESSION),
                        SELECT_EXPRESSION),
                SEMICOLON);
        
        b.rule(UPDATE_COLUMN).is(OBJECT_REFERENCE, EQUALS, EXPRESSION);
        
        b.rule(UPDATE_STATEMENT).is(
                b.optional(LABEL), 
                UPDATE, OBJECT_REFERENCE, b.optional(IDENTIFIER_NAME), SET, UPDATE_COLUMN, b.zeroOrMore(COMMA, UPDATE_COLUMN),
                b.optional(WHERE_CLAUSE),
                SEMICOLON);
        
        b.rule(DELETE_STATEMENT).is(
                b.optional(LABEL), 
                DELETE, b.optional(FROM), OBJECT_REFERENCE,  b.optional(IDENTIFIER_NAME),
                b.optional(b.firstOf(
                        WHERE_CLAUSE,
                        b.sequence(WHERE, CURRENT, OF, IDENTIFIER_NAME))), 
                SEMICOLON);
        
        b.rule(CALL_STATEMENT).is(b.optional(LABEL), OBJECT_REFERENCE, SEMICOLON);
        
        b.rule(UNNAMED_ACTUAL_PAMETER).is(
                b.optional(b.firstOf(b.sequence(IN, b.optional(OUT)), OUT)),
                EXPRESSION);
        
        b.rule(EXECUTE_IMMEDIATE_STATEMENT).is(
                b.optional(LABEL), 
                EXECUTE, IMMEDIATE, CONCATENATION_EXPRESSION,
                b.optional(b.optional(BULK, COLLECT), INTO, IDENTIFIER_NAME, b.zeroOrMore(COMMA, IDENTIFIER_NAME)),
                b.optional(USING, UNNAMED_ACTUAL_PAMETER, b.zeroOrMore(COMMA, UNNAMED_ACTUAL_PAMETER)),
                SEMICOLON);
        
        b.rule(OPEN_STATEMENT).is(
                b.optional(LABEL), 
                OPEN, IDENTIFIER_NAME,
                b.optional(LPARENTHESIS, EXPRESSION, b.zeroOrMore(COMMA, EXPRESSION), RPARENTHESIS),
                SEMICOLON);
        
        b.rule(OPEN_FOR_STATEMENT).is(
                b.optional(LABEL), 
                OPEN, PRIMARY_EXPRESSION, FOR, EXPRESSION,
                b.optional(USING, UNNAMED_ACTUAL_PAMETER, b.zeroOrMore(COMMA, UNNAMED_ACTUAL_PAMETER)),
                SEMICOLON);
        
        b.rule(FETCH_STATEMENT).is(
                b.optional(LABEL), 
                FETCH, PRIMARY_EXPRESSION,
                b.firstOf(
                        b.sequence(INTO, UNNAMED_ACTUAL_PAMETER, b.zeroOrMore(COMMA, UNNAMED_ACTUAL_PAMETER)),
                        b.sequence(
                                BULK, COLLECT, INTO, UNNAMED_ACTUAL_PAMETER, b.zeroOrMore(COMMA, UNNAMED_ACTUAL_PAMETER),
                                b.optional(LIMIT, EXPRESSION))),
                SEMICOLON);
        
        b.rule(CLOSE_STATEMENT).is(b.optional(LABEL), CLOSE, PRIMARY_EXPRESSION, SEMICOLON);
        
        b.rule(PIPE_ROW_STATEMENT).is(b.optional(LABEL), PIPE, ROW, LPARENTHESIS, EXPRESSION, RPARENTHESIS, SEMICOLON);
        
        b.rule(CASE_STATEMENT).is(
                b.optional(LABEL), 
                CASE, b.optional(OBJECT_REFERENCE),
                b.oneOrMore(WHEN, EXPRESSION, THEN, b.oneOrMore(STATEMENT)),
                b.optional(ELSE, b.oneOrMore(STATEMENT)),
                END, CASE, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        b.rule(STATEMENT).is(b.firstOf(NULL_STATEMENT,
                                       BLOCK_STATEMENT,
                                       ASSIGNMENT_STATEMENT, 
                                       IF_STATEMENT, 
                                       LOOP_STATEMENT, 
                                       EXIT_STATEMENT, 
                                       CONTINUE_STATEMENT,
                                       FOR_STATEMENT,
                                       WHILE_STATEMENT,
                                       RETURN_STATEMENT,
                                       COMMIT_STATEMENT,
                                       ROLLBACK_STATEMENT,
                                       SAVEPOINT_STATEMENT,
                                       RAISE_STATEMENT,
                                       SELECT_STATEMENT,
                                       INSERT_STATEMENT,
                                       UPDATE_STATEMENT,
                                       DELETE_STATEMENT,
                                       CALL_STATEMENT,
                                       EXECUTE_IMMEDIATE_STATEMENT,
                                       OPEN_STATEMENT,
                                       OPEN_FOR_STATEMENT,
                                       FETCH_STATEMENT,
                                       CLOSE_STATEMENT,
                                       PIPE_ROW_STATEMENT,
                                       CASE_STATEMENT));
    }
    
    private static void createDmlStatements(LexerfulGrammarBuilder b) {
        
        b.rule(PARTITION_BY_CLAUSE).is(PARTITION, BY, EXPRESSION, b.optional(COMMA, EXPRESSION));
        
        b.rule(WINDOWING_LIMIT).is(b.firstOf(
                b.sequence(UNBOUNDED, b.firstOf(PRECEDING, FOLLOWING)),
                b.sequence(CURRENT, ROW),
                b.sequence(EXPRESSION, b.firstOf(PRECEDING, FOLLOWING))));
        
        b.rule(WINDOWING_CLAUSE).is(
                b.firstOf(ROWS, RANGE_KEYWORD),
                b.firstOf(
                        b.sequence(BETWEEN, WINDOWING_LIMIT, AND, WINDOWING_LIMIT),
                        WINDOWING_LIMIT));
        
        b.rule(ANALYTIC_CLAUSE).is(
                OVER, LPARENTHESIS, 
                b.optional(PARTITION_BY_CLAUSE), b.optional(ORDER_BY_CLAUSE, b.optional(WINDOWING_CLAUSE)),
                RPARENTHESIS);
        
        b.rule(ON_OR_USING_EXPRESSION).is(
                b.firstOf(
                        b.sequence(ON, EXPRESSION),
                        b.sequence(USING, LPARENTHESIS, IDENTIFIER_NAME, b.zeroOrMore(COMMA, IDENTIFIER_NAME), RPARENTHESIS)));
        
        b.rule(OUTER_JOIN_TYPE).is(b.firstOf(FULL, LEFT, RIGHT), b.optional(OUTER));
        
        b.rule(QUERY_PARTITION_CLAUSE).is(
                PARTITION, BY,
                b.firstOf(
                        b.sequence(IDENTIFIER, b.zeroOrMore(COMMA, IDENTIFIER)),
                        b.sequence(LPARENTHESIS, IDENTIFIER, b.zeroOrMore(COMMA, IDENTIFIER), RPARENTHESIS)));
        
        b.rule(INNER_CROSS_JOIN_CLAUSE).is(b.firstOf(
                b.sequence(b.optional(INNER), JOIN, OBJECT_REFERENCE, ON_OR_USING_EXPRESSION),
                b.sequence(
                        b.firstOf(
                                CROSS,
                                b.sequence(NATURAL, b.optional(INNER))),
                        JOIN, OBJECT_REFERENCE)
                ));
        
        b.rule(OUTER_JOIN_CLAUSE).is(
                b.optional(QUERY_PARTITION_CLAUSE),
                b.firstOf(
                        b.sequence(OUTER_JOIN_TYPE, JOIN),
                        b.sequence(NATURAL, b.optional(OUTER_JOIN_TYPE), JOIN)),
                OBJECT_REFERENCE, b.optional(QUERY_PARTITION_CLAUSE),
                b.optional(ON_OR_USING_EXPRESSION));
        
        b.rule(JOIN_CLAUSE).is(OBJECT_REFERENCE, b.oneOrMore(b.firstOf(INNER_CROSS_JOIN_CLAUSE, OUTER_JOIN_CLAUSE)));
        
        b.rule(SELECT_COLUMN).is(EXPRESSION, b.optional(b.optional(AS), IDENTIFIER_NAME));
        
        b.rule(FROM_CLAUSE).is(
                b.firstOf(
                        b.sequence(OBJECT_REFERENCE, b.optional(REMOTE, IDENTIFIER_NAME)),
                        b.sequence(b.optional(THE), LPARENTHESIS, SELECT_EXPRESSION, RPARENTHESIS)),
                b.optional(IDENTIFIER_NAME));
        
        b.rule(WHERE_CLAUSE).is(WHERE, EXPRESSION);
        
        b.rule(INTO_CLAUSE).is(
                b.optional(BULK, COLLECT), INTO,
                OBJECT_REFERENCE, b.zeroOrMore(COMMA, OBJECT_REFERENCE));
        
        b.rule(GROUP_BY_CLAUSE).is(
                GROUP, BY, EXPRESSION, b.zeroOrMore(COMMA, EXPRESSION));
        
        b.rule(HAVING_CLAUSE).is(HAVING, EXPRESSION);
        
        b.rule(ORDER_BY_ITEM).is(EXPRESSION, b.optional(b.firstOf(ASC, DESC)), b.optional(NULLS, b.firstOf(FIRST, LAST)));
        
        b.rule(ORDER_BY_CLAUSE).is(
                ORDER, b.optional(SIBLINGS), BY, ORDER_BY_ITEM, b.zeroOrMore(COMMA, ORDER_BY_ITEM));
        
        b.rule(FOR_UPDATE_CLAUSE).is(
                FOR, UPDATE,
                b.optional(OF, OBJECT_REFERENCE, b.zeroOrMore(COMMA, OBJECT_REFERENCE)),
                b.optional(b.firstOf(NOWAIT, b.sequence(WAIT, INTEGER_LITERAL), b.sequence(SKIP, LOCKED))));
        
        b.rule(CONNECT_BY_CLAUSE).is(CONNECT, BY, b.optional(NOCYCLE), EXPRESSION);
        
        b.rule(START_WITH_CLAUSE).is(START, WITH, EXPRESSION);
        
        b.rule(HIERARCHICAL_QUERY_CLAUSE).is(b.firstOf(
                b.sequence(CONNECT_BY_CLAUSE, b.optional(START_WITH_CLAUSE)),
                b.sequence(START_WITH_CLAUSE, CONNECT_BY_CLAUSE)));
        
        b.rule(SUBQUERY_FACTORING_CLAUSE).is(
                WITH,
                b.oneOrMore(IDENTIFIER_NAME, AS, LPARENTHESIS, SELECT_EXPRESSION, RPARENTHESIS, b.optional(COMMA)));
        
        b.rule(SELECT_EXPRESSION).is(
                b.optional(SUBQUERY_FACTORING_CLAUSE),
                b.firstOf(
                    b.sequence(
                            SELECT, b.optional(b.firstOf(ALL, DISTINCT, UNIQUE)), SELECT_COLUMN, b.zeroOrMore(COMMA, SELECT_COLUMN),
                            b.optional(INTO_CLAUSE),
                            FROM, b.firstOf(
                                    JOIN_CLAUSE,
                                    b.sequence(FROM_CLAUSE, b.zeroOrMore(COMMA, FROM_CLAUSE))),
                            b.optional(WHERE_CLAUSE),
                            b.optional(b.firstOf(
                                    b.sequence(GROUP_BY_CLAUSE, b.optional(HAVING_CLAUSE)),
                                    b.sequence(HAVING_CLAUSE, b.optional(GROUP_BY_CLAUSE)))),
                            b.optional(HAVING_CLAUSE),
                            b.optional(HIERARCHICAL_QUERY_CLAUSE),
                            b.optional(ORDER_BY_CLAUSE)),
                    b.sequence(LPARENTHESIS, SELECT_EXPRESSION, RPARENTHESIS)),
                b.optional(b.firstOf(MINUS_KEYWORD, INTERSECT, b.sequence(UNION, b.optional(ALL))), SELECT_EXPRESSION),
                b.optional(FOR_UPDATE_CLAUSE));
    }
    
    private static void createExpressions(LexerfulGrammarBuilder b) {
        // Reference: http://docs.oracle.com/cd/B28359_01/appdev.111/b28370/expression.htm
        
        b.rule(PRIMARY_EXPRESSION).is(
                b.firstOf(LITERAL, IDENTIFIER_NAME, HOST_AND_INDICATOR_VARIABLE, SQL, MULTIPLICATION));
        
        b.rule(BRACKED_EXPRESSION).is(b.firstOf(
                PRIMARY_EXPRESSION,
                b.sequence(LPARENTHESIS, EXPRESSION, b.zeroOrMore(COMMA, EXPRESSION), RPARENTHESIS))).skipIfOneChild();
        
        b.rule(MEMBER_EXPRESSION).is(
                BRACKED_EXPRESSION,
                b.zeroOrMore(
                        b.firstOf(DOT, MOD, REMOTE),
                        b.nextNot(ROWTYPE),
                        b.nextNot(TYPE),
                        b.firstOf(
                                IDENTIFIER_NAME,
                                COUNT,
                                ROWCOUNT,
                                BULK_ROWCOUNT,
                                FIRST,
                                LAST,
                                LIMIT,
                                NEXT,
                                PRIOR,
                                EXISTS,
                                FOUND,
                                NOTFOUND,
                                ISOPEN,
                                DELETE)
                        )).skipIfOneChild();
        
        b.rule(EXTRACT_DATETIME_EXPRESSION).is(EXTRACT, LPARENTHESIS, IDENTIFIER, FROM, EXPRESSION, RPARENTHESIS);
        
        b.rule(XMLSERIALIZE_EXPRESSION).is(
                XMLSERIALIZE, LPARENTHESIS,
                b.firstOf(DOCUMENT, CONTENT), EXPRESSION,
                b.optional(AS, DATATYPE),
                b.optional(ENCODING, EXPRESSION),
                b.optional(VERSION, STRING_LITERAL),
                b.optional(b.firstOf(b.sequence(NO, IDENT), b.sequence(IDENT, b.optional(SIZE, EQUALS, EXPRESSION)))),
                b.optional(b.firstOf(HIDE, SHOW), DEFAULTS),
                RPARENTHESIS);
        
        b.rule(XML_COLUMN).is(
                EXPRESSION, b.optional(AS, b.firstOf(b.sequence(EVALNAME, EXPRESSION), IDENTIFIER_NAME)));
        
        b.rule(XMLATTRIBUTES_EXPRESSION).is(
                XMLATTRIBUTES, LPARENTHESIS,
                b.optional(b.firstOf(ENTITYESCAPING, NOENTITYESCAPING)),
                b.optional(b.firstOf(SCHEMACHECK, NOSCHEMACHECK)),
                XML_COLUMN, b.zeroOrMore(COMMA, XML_COLUMN),
                RPARENTHESIS);
        
        b.rule(XMLELEMENT_EXPRESSION).is(
                XMLELEMENT, LPARENTHESIS,
                b.optional(b.firstOf(ENTITYESCAPING, NOENTITYESCAPING)),
                b.firstOf(
                        b.sequence(EVALNAME, EXPRESSION),
                        b.sequence(b.optional(NAME), IDENTIFIER_NAME)
                        ),
                b.optional(COMMA, XMLATTRIBUTES_EXPRESSION),
                b.zeroOrMore(COMMA, EXPRESSION, b.optional(AS, IDENTIFIER_NAME)),
                RPARENTHESIS);
        
        b.rule(XMLFOREST_EXPRESSION).is(
                XMLFOREST, LPARENTHESIS,
                XML_COLUMN, b.zeroOrMore(COMMA, XML_COLUMN),
                RPARENTHESIS);
        
        b.rule(CAST_EXPRESSION).is(
                CAST, LPARENTHESIS,
                b.firstOf(b.sequence(MULTISET, EXPRESSION), EXPRESSION),
                AS, DATATYPE,
                RPARENTHESIS);
        
        b.rule(ARGUMENT).is(b.optional(IDENTIFIER_NAME, ASSOCIATION), b.optional(DISTINCT), EXPRESSION);
        
        b.rule(ARGUMENTS).is(LPARENTHESIS, b.optional(ARGUMENT, b.zeroOrMore(COMMA, ARGUMENT)), RPARENTHESIS);
        
        b.rule(METHOD_CALL).is(MEMBER_EXPRESSION, ARGUMENTS);
        
        b.rule(CALL_EXPRESSION).is(b.firstOf(
                EXTRACT_DATETIME_EXPRESSION,
                XMLELEMENT_EXPRESSION,
                XMLFOREST_EXPRESSION,
                XMLSERIALIZE_EXPRESSION,
                CAST_EXPRESSION,
                METHOD_CALL)).skipIfOneChild();
        
        b.rule(OBJECT_REFERENCE).is(
                b.firstOf(CALL_EXPRESSION, MEMBER_EXPRESSION),
                b.zeroOrMore(DOT, b.firstOf(CALL_EXPRESSION, MEMBER_EXPRESSION)),
                b.optional(LPARENTHESIS, PLUS, RPARENTHESIS)).skipIfOneChild();
        
        b.rule(POSTFIX_EXPRESSION).is(OBJECT_REFERENCE, 
                b.optional(b.firstOf(
                        b.sequence(IS, b.optional(NOT), NULL),
                        ANALYTIC_CLAUSE))).skipIfOneChild();
        
        b.rule(IN_EXPRESSION).is(POSTFIX_EXPRESSION, 
                b.optional(b.sequence(
                        b.optional(NOT), IN, 
                        b.firstOf(
                                b.sequence(LPARENTHESIS, EXPRESSION, b.zeroOrMore(COMMA, EXPRESSION), RPARENTHESIS),
                                EXPRESSION)))).skipIfOneChild();
        
        b.rule(EXISTS_EXPRESSION).is(EXISTS , LPARENTHESIS, EXPRESSION, b.zeroOrMore(COMMA, EXPRESSION), RPARENTHESIS).skipIfOneChild();
        
        b.rule(CASE_EXPRESSION).is(
                CASE, b.optional(OBJECT_REFERENCE),
                b.oneOrMore(WHEN, EXPRESSION, THEN, EXPRESSION),
                b.optional(ELSE, EXPRESSION),
                END);
        
        b.rule(AT_TIME_ZONE_EXPRESSION).is(AT, b.firstOf(LOCAL, b.sequence(TIME, ZONE, EXPRESSION)));
        
        b.rule(UNARY_EXPRESSION).is(b.firstOf(
                        b.sequence(NOT, UNARY_EXPRESSION),
                        b.sequence(PLUS, UNARY_EXPRESSION),
                        b.sequence(MINUS, UNARY_EXPRESSION),
                        b.sequence(PRIOR, UNARY_EXPRESSION),
                        IN_EXPRESSION,
                        SELECT_EXPRESSION,
                        CASE_EXPRESSION,
                        EXISTS_EXPRESSION),
                b.optional(AT_TIME_ZONE_EXPRESSION)).skipIfOneChild();
        
        b.rule(EXPONENTIATION_EXPRESSION).is(UNARY_EXPRESSION, b.zeroOrMore(EXPONENTIATION, UNARY_EXPRESSION)).skipIfOneChild();
        
        b.rule(MULTIPLICATIVE_EXPRESSION).is(EXPONENTIATION_EXPRESSION, b.zeroOrMore(b.firstOf(MULTIPLICATION, DIVISION), EXPONENTIATION_EXPRESSION)).skipIfOneChild();
        
        b.rule(ADDITIVE_EXPRESSION).is(MULTIPLICATIVE_EXPRESSION, b.zeroOrMore(b.firstOf(PLUS, MINUS), MULTIPLICATIVE_EXPRESSION)).skipIfOneChild();
        
        b.rule(CONCATENATION_EXPRESSION).is(ADDITIVE_EXPRESSION, b.zeroOrMore(CONCATENATION, ADDITIVE_EXPRESSION)).skipIfOneChild();
       
        b.rule(RELATIONAL_OPERATOR).is(b.firstOf(
                EQUALS,
                NOTEQUALS, 
                NOTEQUALS2, 
                NOTEQUALS3, 
                NOTEQUALS4, 
                LESSTHAN, 
                GREATERTHAN, 
                LESSTHANOREQUAL, 
                GREATERTHANOREQUAL));
        
        b.rule(COMPARISON_EXPRESSION).is(CONCATENATION_EXPRESSION, 
                b.zeroOrMore(b.firstOf(
                        b.sequence(
                                b.firstOf(
                                    RELATIONAL_OPERATOR, 
                                    b.sequence(b.optional(NOT), LIKE)),
                                CONCATENATION_EXPRESSION),
                        b.sequence(
                                b.optional(NOT),
                                BETWEEN, 
                                CONCATENATION_EXPRESSION, 
                                AND, 
                                CONCATENATION_EXPRESSION)))).skipIfOneChild();   
        
        b.rule(AND_EXPRESSION).is(COMPARISON_EXPRESSION, b.zeroOrMore(AND, COMPARISON_EXPRESSION)).skipIfOneChild();
        b.rule(OR_EXPRESSION).is(AND_EXPRESSION, b.zeroOrMore(OR, AND_EXPRESSION)).skipIfOneChild();
        
        b.rule(BOOLEAN_EXPRESSION).is(OR_EXPRESSION).skip();
        
        b.rule(EXPRESSION).is(BOOLEAN_EXPRESSION).skipIfOneChild();
    }
    
    private static void createDeclarations(LexerfulGrammarBuilder b) {
        b.rule(DEFAULT_VALUE_ASSIGNMENT).is(b.firstOf(ASSIGNMENT, DEFAULT), EXPRESSION);
        
        b.rule(PARAMETER_DECLARATION).is(
                IDENTIFIER_NAME,
                b.optional(IN),
                b.firstOf(
                        b.sequence(DATATYPE, b.optional(DEFAULT_VALUE_ASSIGNMENT)),
                        b.sequence(OUT, b.optional(NOCOPY), DATATYPE))
                );
        
     // http://docs.oracle.com/cd/B28359_01/appdev.111/b28370/procedure.htm
        b.rule(PROCEDURE_DECLARATION).is(
                PROCEDURE, IDENTIFIER_NAME,
                b.optional(LPARENTHESIS, b.oneOrMore(PARAMETER_DECLARATION, b.optional(COMMA)), RPARENTHESIS),
                b.firstOf(
                        SEMICOLON,
                        b.sequence(b.firstOf(IS, AS), b.zeroOrMore(DECLARE_SECTION), STATEMENTS_SECTION))
                );
        
        // http://docs.oracle.com/cd/B28359_01/appdev.111/b28370/function.htm
        b.rule(FUNCTION_DECLARATION).is(
                FUNCTION, IDENTIFIER_NAME,
                b.optional(LPARENTHESIS, b.oneOrMore(PARAMETER_DECLARATION, b.optional(COMMA)), RPARENTHESIS),
                RETURN, DATATYPE, b.zeroOrMore(b.firstOf(DETERMINISTIC, PIPELINED)),
                b.firstOf(
                        SEMICOLON,
                        b.sequence(b.firstOf(IS, AS), b.zeroOrMore(DECLARE_SECTION), STATEMENTS_SECTION))
                );
        
        b.rule(VARIABLE_DECLARATION).is(IDENTIFIER_NAME,
                b.optional(CONSTANT),
                b.firstOf(
                        b.sequence(
                                DATATYPE,
                                b.optional(b.optional(NOT, NULL), DEFAULT_VALUE_ASSIGNMENT)),
                        EXCEPTION),                                           
                SEMICOLON);
        
        b.rule(CUSTOM_SUBTYPE).is(
                SUBTYPE, IDENTIFIER_NAME, IS, DATATYPE,
                b.optional(NOT, NULL),
                b.optional(RANGE_KEYWORD, NUMERIC_LITERAL, RANGE, NUMERIC_LITERAL),
                SEMICOLON);
        
        b.rule(CURSOR_PARAMETER_DECLARATION).is(
                IDENTIFIER_NAME,
                b.optional(IN),
                DATATYPE, b.optional(DEFAULT_VALUE_ASSIGNMENT));
        
        b.rule(CURSOR_DECLARATION).is(
                CURSOR, IDENTIFIER_NAME,
                b.optional(LPARENTHESIS, b.oneOrMore(CURSOR_PARAMETER_DECLARATION, b.optional(COMMA)), RPARENTHESIS),
                IS, SELECT_EXPRESSION, SEMICOLON);
        
        b.rule(RECORD_FIELD_DECLARATION).is(
                IDENTIFIER_NAME, DATATYPE,
                b.optional(b.optional(NOT, NULL), DEFAULT_VALUE_ASSIGNMENT));
        
        b.rule(RECORD_DECLARATION).is(
                TYPE, IDENTIFIER_NAME, IS, RECORD,
                LPARENTHESIS, RECORD_FIELD_DECLARATION, b.zeroOrMore(COMMA, RECORD_FIELD_DECLARATION), RPARENTHESIS,
                SEMICOLON);
        
        b.rule(TABLE_OF_DECLARATION).is(
                TYPE, IDENTIFIER_NAME, IS, TABLE, OF, DATATYPE,
                b.optional(INDEX, BY, DATATYPE),
                SEMICOLON);
        
        b.rule(REF_CURSOR_DECLARATION).is(TYPE, IDENTIFIER_NAME, IS, REF, CURSOR, b.optional(RETURN, DATATYPE), SEMICOLON);
        
        b.rule(AUTONOMOUS_TRANSACTION_PRAGMA).is(PRAGMA, AUTONOMOUS_TRANSACTION, SEMICOLON);
        
        b.rule(EXCEPTION_INIT_PRAGMA).is(PRAGMA, EXCEPTION_INIT, LPARENTHESIS, EXPRESSION, COMMA, EXPRESSION, RPARENTHESIS, SEMICOLON);
        
        b.rule(SERIALLY_REUSABLE_PRAGMA).is(PRAGMA, SERIALLY_REUSABLE, SEMICOLON);
        
        b.rule(PRAGMA_DECLARATION).is(b.firstOf(
                EXCEPTION_INIT_PRAGMA,
                AUTONOMOUS_TRANSACTION_PRAGMA,
                SERIALLY_REUSABLE_PRAGMA));
        
        b.rule(DECLARE_SECTION).is(b.oneOrMore(b.firstOf(
                PRAGMA_DECLARATION,
                VARIABLE_DECLARATION,
                PROCEDURE_DECLARATION,
                FUNCTION_DECLARATION,
                CUSTOM_SUBTYPE,
                CURSOR_DECLARATION,
                RECORD_DECLARATION,
                TABLE_OF_DECLARATION,
                REF_CURSOR_DECLARATION)));
    }
    
    private static void createProgramUnits(LexerfulGrammarBuilder b) {
        b.rule(EXECUTE_PLSQL_BUFFER).is(DIVISION);
        
        b.rule(UNIT_NAME).is(b.optional(IDENTIFIER_NAME, DOT), IDENTIFIER_NAME);
        
        // http://docs.oracle.com/cd/B28359_01/appdev.111/b28370/create_procedure.htm
        b.rule(CREATE_PROCEDURE).is(
                CREATE, b.optional(OR, REPLACE),
                PROCEDURE, UNIT_NAME, b.optional(TIMESTAMP, STRING_LITERAL),
                b.optional(LPARENTHESIS, b.oneOrMore(PARAMETER_DECLARATION, b.optional(COMMA)), RPARENTHESIS),
                b.optional(AUTHID, b.firstOf(CURRENT_USER, DEFINER)),
                b.firstOf(IS, AS),
                b.firstOf(
                        b.sequence(b.zeroOrMore(DECLARE_SECTION), STATEMENTS_SECTION),
                        b.sequence(LANGUAGE, JAVA, STRING_LITERAL, SEMICOLON),
                        b.sequence(EXTERNAL, SEMICOLON))
                );
        
        // http://docs.oracle.com/cd/B28359_01/appdev.111/b28370/create_function.htm
        b.rule(CREATE_FUNCTION).is(
                CREATE, b.optional(OR, REPLACE),
                FUNCTION, UNIT_NAME, b.optional(TIMESTAMP, STRING_LITERAL),
                b.optional(LPARENTHESIS, b.oneOrMore(PARAMETER_DECLARATION, b.optional(COMMA)), RPARENTHESIS),
                RETURN, DATATYPE, b.optional(DETERMINISTIC), b.optional(PIPELINED),
                b.optional(AUTHID, b.firstOf(CURRENT_USER, DEFINER)),
                b.firstOf(IS, AS),
                b.firstOf(
                        b.sequence(b.zeroOrMore(DECLARE_SECTION), STATEMENTS_SECTION),
                        b.sequence(LANGUAGE, JAVA, STRING_LITERAL, SEMICOLON))
                );
        
        // http://docs.oracle.com/cd/B28359_01/appdev.111/b28370/create_package.htm
        b.rule(CREATE_PACKAGE).is(
                CREATE, b.optional(OR, REPLACE),
                PACKAGE, UNIT_NAME, b.optional(TIMESTAMP, STRING_LITERAL),
                b.optional(AUTHID, b.firstOf(CURRENT_USER, DEFINER)),
                b.firstOf(IS, AS),
                b.zeroOrMore(DECLARE_SECTION),
                END, b.optional(IDENTIFIER_NAME), SEMICOLON);
        
        // http://docs.oracle.com/cd/B28359_01/appdev.111/b28370/create_package_body.htm
        b.rule(CREATE_PACKAGE_BODY).is(
                CREATE, b.optional(OR, REPLACE),
                PACKAGE, BODY, UNIT_NAME, b.optional(TIMESTAMP, STRING_LITERAL),
                b.firstOf(IS, AS),
                b.zeroOrMore(DECLARE_SECTION),
                b.firstOf(
                        STATEMENTS_SECTION,
                        b.sequence(END, b.optional(IDENTIFIER_NAME), SEMICOLON)));
        
        b.rule(VIEW_RESTRICTION_CLAUSE).is(
                WITH, b.firstOf(
                        b.sequence(READ, ONLY),
                        b.sequence(CHECK, OPTION, b.optional(CONSTRAINT, IDENTIFIER_NAME))
                        )
                );
                
        // http://docs.oracle.com/cd/B28359_01/server.111/b28286/statements_8004.htm
        b.rule(CREATE_VIEW).is(
                CREATE, b.optional(OR, REPLACE), b.optional(b.optional(NO), FORCE),
                VIEW, UNIT_NAME,
                b.optional(LPARENTHESIS, IDENTIFIER_NAME, b.zeroOrMore(COMMA, IDENTIFIER_NAME), RPARENTHESIS),
                AS,
                SELECT_EXPRESSION,
                b.optional(VIEW_RESTRICTION_CLAUSE),
                b.optional(SEMICOLON));
        
        b.rule(ANONYMOUS_BLOCK).is(BLOCK_STATEMENT);
        
        b.rule(COMPILATION_UNIT).is(b.firstOf(
                ANONYMOUS_BLOCK,
                CREATE_PROCEDURE, 
                CREATE_FUNCTION, 
                CREATE_PACKAGE,
                CREATE_PACKAGE_BODY,
                CREATE_VIEW));
    }
    
    private static void createSqlPlusCommands(LexerfulGrammarBuilder b) {
        b.rule(SQLPLUS_SHOW).is(SHOW, b.tillNewLine());
        
        b.rule(SQLPLUS_COMMAND).is(SQLPLUS_SHOW);
    }
    
    private static void createDdlCommands(LexerfulGrammarBuilder b) {
        b.rule(DDL_COMMENT).is(
                COMMENT, ON,
                b.firstOf(
                        b.sequence(
                                COLUMN,
                                IDENTIFIER_NAME, 
                                b.optional(DOT, IDENTIFIER_NAME), 
                                b.optional(DOT, IDENTIFIER_NAME)),
                        b.sequence(
                                b.firstOf(
                                        TABLE,
                                        COLUMN,
                                        OPERATOR,
                                        INDEXTYPE,
                                        b.sequence(MATERIALIZED, VIEW),
                                        b.sequence(MINING, MODEL)),
                                IDENTIFIER_NAME, b.optional(DOT, IDENTIFIER_NAME))
                        ),
                IS, CHARACTER_LITERAL, b.optional(SEMICOLON));
        
        b.rule(TABLE_COLUMN_DEFINITION).is(
                IDENTIFIER_NAME, DATATYPE,
                b.optional(SORT),
                b.optional(DEFAULT, EXPRESSION),
                b.optional(ENCRYPT));
        
        b.rule(TABLE_RELATIONAL_PROPERTIES).is(b.oneOrMore(TABLE_COLUMN_DEFINITION, b.optional(COMMA)));
        
        b.rule(CREATE_TABLE).is(
                CREATE, b.optional(GLOBAL, TEMPORARY), TABLE, UNIT_NAME,
                b.optional(LPARENTHESIS, TABLE_RELATIONAL_PROPERTIES, RPARENTHESIS),
                b.optional(ON, COMMIT, b.firstOf(DELETE, PRESERVE), ROWS),
                SEMICOLON);
        
        b.rule(DDL_COMMAND).is(DDL_COMMENT);
    }
}