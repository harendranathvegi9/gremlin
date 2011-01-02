// $ANTLR 3.2 Sep 23, 2009 12:02:23 src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g 2010-12-01 11:00:42

package com.tinkerpop.gremlin.compiler;

import com.tinkerpop.gremlin.GremlinScriptEngine;
import com.tinkerpop.gremlin.compiler.context.GremlinScriptContext;
import com.tinkerpop.gremlin.compiler.context.StepLibrary;
import com.tinkerpop.gremlin.compiler.operations.Operation;
import com.tinkerpop.gremlin.compiler.operations.UnaryOperation;
import com.tinkerpop.gremlin.compiler.operations.logic.*;
import com.tinkerpop.gremlin.compiler.operations.math.*;
import com.tinkerpop.gremlin.compiler.operations.util.DeclareVariable;
import com.tinkerpop.gremlin.compiler.pipes.GremlinPipesHelper;
import com.tinkerpop.gremlin.compiler.statements.Foreach;
import com.tinkerpop.gremlin.compiler.statements.If;
import com.tinkerpop.gremlin.compiler.statements.Repeat;
import com.tinkerpop.gremlin.compiler.statements.While;
import com.tinkerpop.gremlin.compiler.types.*;
import com.tinkerpop.gremlin.compiler.util.CodeBlock;
import com.tinkerpop.gremlin.compiler.util.Pair;
import com.tinkerpop.gremlin.compiler.util.Tokens;
import com.tinkerpop.gremlin.functions.Function;
import com.tinkerpop.gremlin.functions.NativeFunction;
import com.tinkerpop.gremlin.steps.NativePipe;
import com.tinkerpop.gremlin.steps.Step;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.Pipeline;
import com.tinkerpop.pipes.filter.FilterPipe;
import com.tinkerpop.pipes.filter.FutureFilterPipe;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import javax.script.ScriptContext;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GremlinEvaluator extends TreeParser {
    public static final String[] tokenNames = new String[]{"<invalid>", "<EOR>", "<DOWN>", "<UP>", "VAR", "ARG", "ARGS", "FUNC", "NS", "NAME", "FUNC_NAME", "GPATH", "NATIVE_STEP", "STEP", "TOKEN", "PREDICATE", "PREDICATES", "SELF", "HISTORY", "FUNC_CALL", "RETURN", "IF", "ELSE", "COND", "BLOCK", "NATIVE", "FOREACH", "WHILE", "REPEAT", "INCLUDE", "SCRIPT", "INT", "LONG", "FLOAT", "DOUBLE", "STR", "ARR", "BOOL", "NULL", "RANGE", "PROPERTY_CALL", "VARIABLE_CALL", "COLLECTION_CALL", "COMMENT", "NEWLINE", "G_INT", "G_LONG", "G_FLOAT", "G_DOUBLE", "StringLiteral", "PROPERTY", "VARIABLE", "IDENTIFIER", "BOOLEAN", "DoubleStringCharacter", "SingleStringCharacter", "WS", "EscapeSequence", "CharacterEscapeSequence", "HexEscapeSequence", "UnicodeEscapeSequence", "SingleEscapeCharacter", "NonEscapeCharacter", "EscapeCharacter", "DecimalDigit", "HexDigit", "'/'", "'['", "']'", "'..'", "'('", "')'", "'{'", "';'", "'}'", "':='", "'and'", "'or'", "'include'", "'script'", "'if'", "'else'", "'end'", "'foreach'", "'in'", "'while'", "'repeat'", "'step'", "'func'", "','", "'return'", "'='", "'!='", "'<'", "'<='", "'>'", "'>='", "'+'", "'-'", "'*'", "'div'", "'mod'", "':'"};
    public static final int WHILE = 27;
    public static final int DecimalDigit = 64;
    public static final int EOF = -1;
    public static final int FUNC_CALL = 19;
    public static final int SingleStringCharacter = 55;
    public static final int TOKEN = 14;
    public static final int T__93 = 93;
    public static final int HISTORY = 18;
    public static final int T__94 = 94;
    public static final int T__91 = 91;
    public static final int T__92 = 92;
    public static final int NAME = 9;
    public static final int T__90 = 90;
    public static final int ARG = 5;
    public static final int G_INT = 45;
    public static final int SingleEscapeCharacter = 61;
    public static final int INCLUDE = 29;
    public static final int RETURN = 20;
    public static final int DOUBLE = 34;
    public static final int ARGS = 6;
    public static final int VAR = 4;
    public static final int GPATH = 11;
    public static final int COMMENT = 43;
    public static final int T__99 = 99;
    public static final int T__98 = 98;
    public static final int T__97 = 97;
    public static final int T__96 = 96;
    public static final int T__95 = 95;
    public static final int SCRIPT = 30;
    public static final int T__80 = 80;
    public static final int T__81 = 81;
    public static final int T__82 = 82;
    public static final int T__83 = 83;
    public static final int NS = 8;
    public static final int NULL = 38;
    public static final int ELSE = 22;
    public static final int BOOL = 37;
    public static final int NATIVE = 25;
    public static final int INT = 31;
    public static final int DoubleStringCharacter = 54;
    public static final int ARR = 36;
    public static final int T__85 = 85;
    public static final int T__84 = 84;
    public static final int T__87 = 87;
    public static final int T__86 = 86;
    public static final int T__89 = 89;
    public static final int T__88 = 88;
    public static final int WS = 56;
    public static final int T__71 = 71;
    public static final int T__72 = 72;
    public static final int PREDICATES = 16;
    public static final int VARIABLE = 51;
    public static final int T__70 = 70;
    public static final int G_DOUBLE = 48;
    public static final int PROPERTY = 50;
    public static final int FUNC = 7;
    public static final int G_LONG = 46;
    public static final int FOREACH = 26;
    public static final int REPEAT = 28;
    public static final int CharacterEscapeSequence = 58;
    public static final int FUNC_NAME = 10;
    public static final int T__76 = 76;
    public static final int T__75 = 75;
    public static final int T__74 = 74;
    public static final int EscapeSequence = 57;
    public static final int T__73 = 73;
    public static final int T__79 = 79;
    public static final int T__78 = 78;
    public static final int T__77 = 77;
    public static final int T__68 = 68;
    public static final int T__69 = 69;
    public static final int T__66 = 66;
    public static final int T__67 = 67;
    public static final int HexEscapeSequence = 59;
    public static final int STEP = 13;
    public static final int FLOAT = 33;
    public static final int HexDigit = 65;
    public static final int PREDICATE = 15;
    public static final int IF = 21;
    public static final int STR = 35;
    public static final int BOOLEAN = 53;
    public static final int IDENTIFIER = 52;
    public static final int EscapeCharacter = 63;
    public static final int NATIVE_STEP = 12;
    public static final int COLLECTION_CALL = 42;
    public static final int G_FLOAT = 47;
    public static final int PROPERTY_CALL = 40;
    public static final int UnicodeEscapeSequence = 60;
    public static final int RANGE = 39;
    public static final int T__102 = 102;
    public static final int T__101 = 101;
    public static final int T__100 = 100;
    public static final int StringLiteral = 49;
    public static final int NEWLINE = 44;
    public static final int BLOCK = 24;
    public static final int NonEscapeCharacter = 62;
    public static final int LONG = 32;
    public static final int COND = 23;
    public static final int SELF = 17;
    public static final int VARIABLE_CALL = 41;

    // delegates
    // delegators


    public GremlinEvaluator(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }

    public GremlinEvaluator(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);

    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() {
        return GremlinEvaluator.tokenNames;
    }

    public String getGrammarFileName() {
        return "src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g";
    }


    // debug mode
    public static boolean DEBUG = false;

    // script mode - for ScriptExecutor
    public static boolean SCRIPT_MODE = false;

    private boolean isGPath = false;
    private boolean inBlock = false;

    private int gpathScope = 0;

    private Pattern rangePattern = Pattern.compile("^(\\d+)..(\\d+)");

    private GremlinScriptContext context = new GremlinScriptContext();

    public GremlinEvaluator(final TreeNodeStream input, final GremlinScriptContext context) {
        this(input, new RecognizerSharedState());
        this.context = context;
    }

    private Atom getVariable(final String name) {
        return new Atom(context.getBindings(ScriptContext.ENGINE_SCOPE).get(name));
    }

    private Object getVariableValue(final String name) {
        return context.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
    }

    private Function getFunction(final String ns, final String functionName) {
        return this.context.getFunctionLibrary().getFunction(ns, functionName);
    }

    private void registerFunction(final String ns, final Function func) {
        this.context.getFunctionLibrary().registerFunction(ns, func);
    }

    private Atom compileCollectionCall(final Atom<Object> tokenAtom, final List<Operation> predicates, final List<Pipe> nativeSteps) {
        final List<Pipe> pipes = new ArrayList<Pipe>();
        final Atom<Object> root = makePipelineRoot(tokenAtom, pipes);

        pipes.addAll(GremlinPipesHelper.pipesForStep(predicates, this.context));
        pipes.addAll(nativeSteps);

        return new GPath(root, pipes, this.context);
    }

    private Atom compileGPathCall(final List<Pair<Atom<Object>, Pair<List<Operation>, List<Pipe>>>> steps) {
        Atom<Object> root = null;
        List<Pipe> pipes = new ArrayList<Pipe>();

        long count = 0;
        for (final Pair<Atom<Object>, Pair<List<Operation>, List<Pipe>>> pair : steps) {
            final Atom<Object> token = pair.getA();
            final List<Operation> predicates = pair.getB().getA();
            final List<Pipe> nativeSteps = pair.getB().getB();

            if (count == 0) {
                root = makePipelineRoot(token, pipes);
                pipes.addAll(GremlinPipesHelper.pipesForStep(predicates, this.context));
            } else if (token instanceof Id && token.getValue().equals("..")) {
                List<Pipe> currPipes = pipes;
                List<Pipe> newPipes = new ArrayList<Pipe>();
                LinkedList<Pipe> history = new LinkedList<Pipe>();

                if ((currPipes.size() == 1 && (currPipes.get(0) instanceof FutureFilterPipe)) || currPipes.size() == 0) {
                    pipes = new ArrayList();
                } else {
                    int idx;

                    for (idx = currPipes.size() - 1; idx >= 0; idx--) {
                        Pipe currentPipe = currPipes.get(idx);
                        history.addFirst(currentPipe);

                        if (!(currentPipe instanceof FilterPipe))
                            break;
                    }

                    for (int i = 0; i < idx; i++) {
                        newPipes.add(currPipes.get(i));
                    }

                    newPipes.add(new FutureFilterPipe(new Pipeline(history)));

                    pipes = newPipes;
                }

                pipes.addAll(GremlinPipesHelper.pipesForStep(predicates, this.context));
            } else {
                pipes.addAll(GremlinPipesHelper.pipesForStep(token, predicates, this.context));
            }

            pipes.addAll(nativeSteps);

            count++;
        }

        return (pipes.size() == 0) ? new Atom<Object>(null) : new GPath(root, pipes, this.context);
    }

    private Atom<Object> makePipelineRoot(final Atom<Object> token, final List<Pipe> pipes) {
        final StepLibrary steps = this.context.getStepLibrary();

        if (token instanceof DynamicEntity) {
            return token;
        } else if (token instanceof Id) {
            Step currentStep = steps.getStep(token.toString());

            if (null != currentStep) {
                pipes.add(currentStep.createPipe());
            }

            return (this.getVariableValue(Tokens.IN_BLOCK) == null) ? this.getVariable(Tokens.ROOT_VARIABLE) : new Atom<Object>(this.context.getCurrentPoint());
        } else {
            return token;
        }
    }

    private Atom<Object> singleGPathStep(final Atom<Object> token) {
        final StepLibrary steps = this.context.getStepLibrary();

        if (token instanceof DynamicEntity) {
            if (gpathScope == 1 && token instanceof Func) {
                Func function = (Func) token;
                Object root = this.getVariable(Tokens.ROOT_VARIABLE).getValue();
                List<Operation> arguments = GremlinPipesHelper.updateArguments(function.getArguments(), root);
                return new Func(function.getFunction(), arguments, this.context);
            }
            return token;
        } else if (token instanceof Property) {
            return (gpathScope == 1) ? new Atom<Object>(null) : token;
        } else if (token instanceof Id) {
            String identifier = (String) token.getValue();
            if (identifier.equals(Tokens.IDENTITY)) {
                if (this.getVariableValue(Tokens.IN_BLOCK) == null) {
                    return (gpathScope > 1) ? new Variable(Tokens.ROOT_VARIABLE, this.context) : new RootVariable(this.context);
                } else {
                    return new Atom<Object>(this.context.getCurrentPoint());
                }
            } else {
                final Step currentStep = steps.getStep(token.toString());
                if (null != currentStep) {
                    final List<Pipe> pipes = Arrays.asList(currentStep.createPipe());
                    return new GPath(this.getVariable(Tokens.ROOT_VARIABLE), pipes, this.context);
                }
            }
            return new Atom<Object>(null);
        }

        return token;
    }

    private Set createRange(final String min, final String max) throws RuntimeException {
        final int minimum = new Integer(min);
        final int maximum = new Integer(max);

        final Set<Integer> range = new LinkedHashSet<Integer>();

        for (int i = minimum; i < maximum; i++) {
            range.add(i);
        }

        return range;
    }

    private void formProgramResult(List<Object> resultList, Operation currentOperation) {
        Atom result = currentOperation.compute();
        Object value = null;

        try {
            value = result.getValue();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        if (currentOperation.getType() != Operation.Type.CONTROL_STATEMENT)
            resultList.add(value);

        if (DEBUG) {
            Iterator itty = null;
            if (value instanceof Iterable) {
                itty = ((Iterable) value).iterator();
            } else if (value instanceof Iterator) {
                itty = (Iterator) value;
            }

            if (null == itty) {
                if (value instanceof Map) {
                    Map map = (Map) value;
                    if (map.isEmpty()) {
                        this.context.writeOutput(Tokens.RESULT_PROMPT + "{}\n");
                    } else {
                        for (Object key : map.keySet()) {
                            this.context.writeOutput(Tokens.RESULT_PROMPT + key + "=" + map.get(key) + "\n");
                        }
                    }
                } else {
                    this.context.writeOutput(Tokens.RESULT_PROMPT + value + "\n");
                }
            } else {
                if (!itty.hasNext()) {
                    this.context.writeOutput(Tokens.RESULT_PROMPT + "[]\n");
                } else {
                    while (itty.hasNext()) {
                        this.context.writeOutput(Tokens.RESULT_PROMPT + itty.next() + "\n");
                    }
                }
            }
        } else if (SCRIPT_MODE) {
            if (value instanceof Iterable) {
                for (Object o : (Iterable) value) {
                }
            } else if (value instanceof Iterator) {
                Iterator itty = (Iterator) value;
                while (itty.hasNext())
                    itty.next();
            }
        }

        this.context.getBindings(ScriptContext.ENGINE_SCOPE).put(Tokens.LAST_VARIABLE, new Atom<Object>(value));
    }


    public static class program_return extends TreeRuleReturnScope {
        public Iterable results;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "program"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:306:1: program returns [Iterable results] : ( statement ( NEWLINE )* )+ ;
    public final GremlinEvaluator.program_return program() throws RecognitionException {
        GremlinEvaluator.program_return retval = new GremlinEvaluator.program_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree NEWLINE2 = null;
        GremlinEvaluator.statement_return statement1 = null;


        CommonTree NEWLINE2_tree = null;


        List<Object> resultList = new ArrayList<Object>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:310:5: ( ( statement ( NEWLINE )* )+ )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:310:7: ( statement ( NEWLINE )* )+
            {
                root_0 = (CommonTree) adaptor.nil();

                // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:310:7: ( statement ( NEWLINE )* )+
                int cnt2 = 0;
                loop2:
                do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);

                    if ((LA2_0 == VAR || LA2_0 == FUNC || (LA2_0 >= GPATH && LA2_0 <= NATIVE_STEP) || LA2_0 == IF || (LA2_0 >= FOREACH && LA2_0 <= DOUBLE) || LA2_0 == NULL || (LA2_0 >= 76 && LA2_0 <= 77) || (LA2_0 >= 91 && LA2_0 <= 101))) {
                        alt2 = 1;
                    }


                    switch (alt2) {
                        case 1:
                            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:310:8: statement ( NEWLINE )*
                        {
                            _last = (CommonTree) input.LT(1);
                            pushFollow(FOLLOW_statement_in_program70);
                            statement1 = statement();

                            state._fsp--;

                            adaptor.addChild(root_0, statement1.getTree());
                            formProgramResult(resultList, (statement1 != null ? statement1.op : null));
                            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:310:68: ( NEWLINE )*
                            loop1:
                            do {
                                int alt1 = 2;
                                int LA1_0 = input.LA(1);

                                if ((LA1_0 == NEWLINE)) {
                                    alt1 = 1;
                                }


                                switch (alt1) {
                                    case 1:
                                        // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:310:68: NEWLINE
                                    {
                                        _last = (CommonTree) input.LT(1);
                                        NEWLINE2 = (CommonTree) match(input, NEWLINE, FOLLOW_NEWLINE_in_program74);
                                        NEWLINE2_tree = (CommonTree) adaptor.dupNode(NEWLINE2);

                                        adaptor.addChild(root_0, NEWLINE2_tree);


                                    }
                                    break;

                                    default:
                                        break loop1;
                                }
                            } while (true);


                        }
                        break;

                        default:
                            if (cnt2 >= 1)
                                break loop2;
                            EarlyExitException eee = new EarlyExitException(2, input);
                            throw eee;
                    }
                    cnt2++;
                } while (true);


                retval.results = resultList;


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "program"

    public static class statement_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:316:1: statement returns [Operation op] : ( if_statement | foreach_statement | while_statement | repeat_statement | native_step_definition_statement | function_definition_statement | include_statement | script_statement | ^( VAR atom s= statement ) | ^( 'and' a= statement b= statement ) | ^( 'or' a= statement b= statement ) | expression );
    public final GremlinEvaluator.statement_return statement() throws RecognitionException {
        GremlinEvaluator.statement_return retval = new GremlinEvaluator.statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree VAR11 = null;
        CommonTree string_literal13 = null;
        CommonTree string_literal14 = null;
        GremlinEvaluator.statement_return s = null;

        GremlinEvaluator.statement_return a = null;

        GremlinEvaluator.statement_return b = null;

        GremlinEvaluator.if_statement_return if_statement3 = null;

        GremlinEvaluator.foreach_statement_return foreach_statement4 = null;

        GremlinEvaluator.while_statement_return while_statement5 = null;

        GremlinEvaluator.repeat_statement_return repeat_statement6 = null;

        GremlinEvaluator.native_step_definition_statement_return native_step_definition_statement7 = null;

        GremlinEvaluator.function_definition_statement_return function_definition_statement8 = null;

        GremlinEvaluator.include_statement_return include_statement9 = null;

        GremlinEvaluator.script_statement_return script_statement10 = null;

        GremlinEvaluator.atom_return atom12 = null;

        GremlinEvaluator.expression_return expression15 = null;


        CommonTree VAR11_tree = null;
        CommonTree string_literal13_tree = null;
        CommonTree string_literal14_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:317:2: ( if_statement | foreach_statement | while_statement | repeat_statement | native_step_definition_statement | function_definition_statement | include_statement | script_statement | ^( VAR atom s= statement ) | ^( 'and' a= statement b= statement ) | ^( 'or' a= statement b= statement ) | expression )
            int alt3 = 12;
            switch (input.LA(1)) {
                case IF: {
                    alt3 = 1;
                }
                break;
                case FOREACH: {
                    alt3 = 2;
                }
                break;
                case WHILE: {
                    alt3 = 3;
                }
                break;
                case REPEAT: {
                    alt3 = 4;
                }
                break;
                case NATIVE_STEP: {
                    alt3 = 5;
                }
                break;
                case FUNC: {
                    alt3 = 6;
                }
                break;
                case INCLUDE: {
                    alt3 = 7;
                }
                break;
                case SCRIPT: {
                    alt3 = 8;
                }
                break;
                case VAR: {
                    alt3 = 9;
                }
                break;
                case 76: {
                    alt3 = 10;
                }
                break;
                case 77: {
                    alt3 = 11;
                }
                break;
                case GPATH:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case NULL:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101: {
                    alt3 = 12;
                }
                break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 3, 0, input);

                    throw nvae;
            }

            switch (alt3) {
                case 1:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:317:4: if_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_if_statement_in_statement103);
                    if_statement3 = if_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, if_statement3.getTree());
                    retval.op = (if_statement3 != null ? if_statement3.op : null);

                }
                break;
                case 2:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:318:4: foreach_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_foreach_statement_in_statement133);
                    foreach_statement4 = foreach_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, foreach_statement4.getTree());
                    retval.op = (foreach_statement4 != null ? foreach_statement4.op : null);

                }
                break;
                case 3:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:319:7: while_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_while_statement_in_statement161);
                    while_statement5 = while_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, while_statement5.getTree());
                    retval.op = (while_statement5 != null ? while_statement5.op : null);

                }
                break;
                case 4:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:320:4: repeat_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_repeat_statement_in_statement188);
                    repeat_statement6 = repeat_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, repeat_statement6.getTree());
                    retval.op = (repeat_statement6 != null ? repeat_statement6.op : null);

                }
                break;
                case 5:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:321:4: native_step_definition_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_native_step_definition_statement_in_statement214);
                    native_step_definition_statement7 = native_step_definition_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, native_step_definition_statement7.getTree());
                    retval.op = (native_step_definition_statement7 != null ? native_step_definition_statement7.op : null);

                }
                break;
                case 6:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:322:4: function_definition_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_function_definition_statement_in_statement224);
                    function_definition_statement8 = function_definition_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, function_definition_statement8.getTree());
                    retval.op = (function_definition_statement8 != null ? function_definition_statement8.op : null);

                }
                break;
                case 7:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:323:4: include_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_include_statement_in_statement237);
                    include_statement9 = include_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, include_statement9.getTree());
                    retval.op = new UnaryOperation((include_statement9 != null ? include_statement9.result : null));

                }
                break;
                case 8:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:324:6: script_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_script_statement_in_statement264);
                    script_statement10 = script_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, script_statement10.getTree());
                    retval.op = new UnaryOperation((script_statement10 != null ? script_statement10.result : null));

                }
                break;
                case 9:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:325:4: ^( VAR atom s= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        VAR11 = (CommonTree) match(input, VAR, FOLLOW_VAR_in_statement291);
                        VAR11_tree = (CommonTree) adaptor.dupNode(VAR11);

                        root_1 = (CommonTree) adaptor.becomeRoot(VAR11_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_atom_in_statement293);
                        atom12 = atom();

                        state._fsp--;

                        adaptor.addChild(root_1, atom12.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_statement297);
                        s = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, s.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.op = new DeclareVariable((atom12 != null ? atom12.value : null), (s != null ? s.op : null), this.context);

                }
                break;
                case 10:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:326:9: ^( 'and' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        string_literal13 = (CommonTree) match(input, 76, FOLLOW_76_in_statement323);
                        string_literal13_tree = (CommonTree) adaptor.dupNode(string_literal13);

                        root_1 = (CommonTree) adaptor.becomeRoot(string_literal13_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_statement327);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_statement331);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.op = new And((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 11:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:327:9: ^( 'or' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        string_literal14 = (CommonTree) match(input, 77, FOLLOW_77_in_statement348);
                        string_literal14_tree = (CommonTree) adaptor.dupNode(string_literal14);

                        root_1 = (CommonTree) adaptor.becomeRoot(string_literal14_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_statement353);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_statement357);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.op = new Or((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 12:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:328:9: expression
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_expression_in_statement373);
                    expression15 = expression();

                    state._fsp--;

                    adaptor.addChild(root_0, expression15.getTree());
                    retval.op = (expression15 != null ? expression15.expr : null);

                }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "statement"

    public static class script_statement_return extends TreeRuleReturnScope {
        public Atom result;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "script_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:331:1: script_statement returns [Atom result] : ^( SCRIPT StringLiteral ) ;
    public final GremlinEvaluator.script_statement_return script_statement() throws RecognitionException {
        GremlinEvaluator.script_statement_return retval = new GremlinEvaluator.script_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree SCRIPT16 = null;
        CommonTree StringLiteral17 = null;

        CommonTree SCRIPT16_tree = null;
        CommonTree StringLiteral17_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:332:5: ( ^( SCRIPT StringLiteral ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:332:7: ^( SCRIPT StringLiteral )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    SCRIPT16 = (CommonTree) match(input, SCRIPT, FOLLOW_SCRIPT_in_script_statement419);
                    SCRIPT16_tree = (CommonTree) adaptor.dupNode(SCRIPT16);

                    root_1 = (CommonTree) adaptor.becomeRoot(SCRIPT16_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    StringLiteral17 = (CommonTree) match(input, StringLiteral, FOLLOW_StringLiteral_in_script_statement421);
                    StringLiteral17_tree = (CommonTree) adaptor.dupNode(StringLiteral17);

                    adaptor.addChild(root_1, StringLiteral17_tree);


                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                retval.result = new Atom<Boolean>(true);

                String filename = (StringLiteral17 != null ? StringLiteral17.getText() : null);
                filename = filename.substring(1, filename.length() - 1);

                try {
                    final GremlinScriptEngine engine = new GremlinScriptEngine();
                    engine.eval(new FileReader(filename), this.context);
                } catch (FileNotFoundException e) {
                    this.context.writeError("File '" + filename + "' does not exist");
                    this.context.flushErrorStream();

                    retval.result = new Atom<Boolean>(false);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "script_statement"

    public static class include_statement_return extends TreeRuleReturnScope {
        public Atom result;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "include_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:353:1: include_statement returns [Atom result] : ^( INCLUDE StringLiteral ) ;
    public final GremlinEvaluator.include_statement_return include_statement() throws RecognitionException {
        GremlinEvaluator.include_statement_return retval = new GremlinEvaluator.include_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree INCLUDE18 = null;
        CommonTree StringLiteral19 = null;

        CommonTree INCLUDE18_tree = null;
        CommonTree StringLiteral19_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:354:2: ( ^( INCLUDE StringLiteral ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:354:4: ^( INCLUDE StringLiteral )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    INCLUDE18 = (CommonTree) match(input, INCLUDE, FOLLOW_INCLUDE_in_include_statement449);
                    INCLUDE18_tree = (CommonTree) adaptor.dupNode(INCLUDE18);

                    root_1 = (CommonTree) adaptor.becomeRoot(INCLUDE18_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    StringLiteral19 = (CommonTree) match(input, StringLiteral, FOLLOW_StringLiteral_in_include_statement451);
                    StringLiteral19_tree = (CommonTree) adaptor.dupNode(StringLiteral19);

                    adaptor.addChild(root_1, StringLiteral19_tree);


                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                retval.result = new Atom<Boolean>(true);

                String className = (StringLiteral19 != null ? StringLiteral19.getText() : null);
                try {
                    this.context.loadLibrary(className);
                } catch (Exception e) {
                    this.context.writeError("Library '" + className + "' does not exist");
                    this.context.flushErrorStream();

                    retval.result = new Atom<Boolean>(false);
                }


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "include_statement"

    public static class native_step_definition_statement_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "native_step_definition_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:370:1: native_step_definition_statement returns [Operation op] : ^( NATIVE_STEP name= IDENTIFIER block ) ;
    public final GremlinEvaluator.native_step_definition_statement_return native_step_definition_statement() throws RecognitionException {
        GremlinEvaluator.native_step_definition_statement_return retval = new GremlinEvaluator.native_step_definition_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree name = null;
        CommonTree NATIVE_STEP20 = null;
        GremlinEvaluator.block_return block21 = null;


        CommonTree name_tree = null;
        CommonTree NATIVE_STEP20_tree = null;


        List<Pipe> pipes = new ArrayList<Pipe>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:374:2: ( ^( NATIVE_STEP name= IDENTIFIER block ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:374:4: ^( NATIVE_STEP name= IDENTIFIER block )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    NATIVE_STEP20 = (CommonTree) match(input, NATIVE_STEP, FOLLOW_NATIVE_STEP_in_native_step_definition_statement488);
                    NATIVE_STEP20_tree = (CommonTree) adaptor.dupNode(NATIVE_STEP20);

                    root_1 = (CommonTree) adaptor.becomeRoot(NATIVE_STEP20_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    name = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_native_step_definition_statement492);
                    name_tree = (CommonTree) adaptor.dupNode(name);

                    adaptor.addChild(root_1, name_tree);

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_block_in_native_step_definition_statement494);
                    block21 = block();

                    state._fsp--;

                    adaptor.addChild(root_1, block21.getTree());

                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                Step nativeStep = new Step((name != null ? name.getText() : null), NativePipe.class, new Object[]{(block21 != null ? block21.cb : null)});
                this.context.getStepLibrary().registerStep(nativeStep);
                retval.op = new UnaryOperation(new Atom<Boolean>(true));


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "native_step_definition_statement"

    protected static class gpath_statement_scope {
        int pipeCount;
        List<Pair<Atom<Object>, Pair<List<Operation>, List<Pipe>>>> steps;
    }

    protected Stack gpath_statement_stack = new Stack();

    public static class gpath_statement_return extends TreeRuleReturnScope {
        public Atom value;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "gpath_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:383:1: gpath_statement returns [Atom value] : ^( GPATH ( step )+ ) ;
    public final GremlinEvaluator.gpath_statement_return gpath_statement() throws RecognitionException {
        gpath_statement_stack.push(new gpath_statement_scope());
        GremlinEvaluator.gpath_statement_return retval = new GremlinEvaluator.gpath_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree GPATH22 = null;
        GremlinEvaluator.step_return step23 = null;


        CommonTree GPATH22_tree = null;


        isGPath = true;
        gpathScope++;
        ((gpath_statement_scope) gpath_statement_stack.peek()).steps = new LinkedList<Pair<Atom<Object>, Pair<List<Operation>, List<Pipe>>>>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:397:2: ( ^( GPATH ( step )+ ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:397:4: ^( GPATH ( step )+ )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    GPATH22 = (CommonTree) match(input, GPATH, FOLLOW_GPATH_in_gpath_statement550);
                    GPATH22_tree = (CommonTree) adaptor.dupNode(GPATH22);

                    root_1 = (CommonTree) adaptor.becomeRoot(GPATH22_tree, root_1);


                    match(input, Token.DOWN, null);
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:397:12: ( step )+
                    int cnt4 = 0;
                    loop4:
                    do {
                        int alt4 = 2;
                        int LA4_0 = input.LA(1);

                        if ((LA4_0 == STEP)) {
                            alt4 = 1;
                        }


                        switch (alt4) {
                            case 1:
                                // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:397:13: step
                            {
                                _last = (CommonTree) input.LT(1);
                                pushFollow(FOLLOW_step_in_gpath_statement553);
                                step23 = step();

                                state._fsp--;

                                adaptor.addChild(root_1, step23.getTree());

                            }
                            break;

                            default:
                                if (cnt4 >= 1)
                                    break loop4;
                                EarlyExitException eee = new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);


                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                if (!inBlock) {
                    List<Pair<Atom<Object>, Pair<List<Operation>, List<Pipe>>>> stepList = ((gpath_statement_scope) gpath_statement_stack.peek()).steps;

                    if (stepList.size() == 1) {
                        Pair<Atom<Object>, Pair<List<Operation>, List<Pipe>>> currentPair = stepList.get(0);

                        Atom<Object> token = currentPair.getA();
                        List<Operation> predicates = currentPair.getB().getA();
                        List<Pipe> nativeSteps = currentPair.getB().getB();

                        retval.value = (predicates.size() == 0 && nativeSteps.size() == 0) ? singleGPathStep(token) : compileCollectionCall(token, predicates, nativeSteps);
                    } else {
                        retval.value = compileGPathCall(stepList);
                    }
                }


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);


            isGPath = false;
            gpathScope--;

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            gpath_statement_stack.pop();
        }
        return retval;
    }
    // $ANTLR end "gpath_statement"

    public static class step_return extends TreeRuleReturnScope {
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "step"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:417:1: step : ^( STEP ^( TOKEN token ) ^( PREDICATES ( ^( PREDICATE statement ) )* ) (nativeStep= gremlin_native_path_definition )* ) ;
    public final GremlinEvaluator.step_return step() throws RecognitionException {
        GremlinEvaluator.step_return retval = new GremlinEvaluator.step_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree STEP24 = null;
        CommonTree TOKEN25 = null;
        CommonTree PREDICATES27 = null;
        CommonTree PREDICATE28 = null;
        GremlinEvaluator.gremlin_native_path_definition_return nativeStep = null;

        GremlinEvaluator.token_return token26 = null;

        GremlinEvaluator.statement_return statement29 = null;


        CommonTree STEP24_tree = null;
        CommonTree TOKEN25_tree = null;
        CommonTree PREDICATES27_tree = null;
        CommonTree PREDICATE28_tree = null;


        List<Operation> predicates = new ArrayList<Operation>();
        List<Pipe> nativeSteps = new ArrayList<Pipe>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:422:5: ( ^( STEP ^( TOKEN token ) ^( PREDICATES ( ^( PREDICATE statement ) )* ) (nativeStep= gremlin_native_path_definition )* ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:422:7: ^( STEP ^( TOKEN token ) ^( PREDICATES ( ^( PREDICATE statement ) )* ) (nativeStep= gremlin_native_path_definition )* )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    STEP24 = (CommonTree) match(input, STEP, FOLLOW_STEP_in_step591);
                    STEP24_tree = (CommonTree) adaptor.dupNode(STEP24);

                    root_1 = (CommonTree) adaptor.becomeRoot(STEP24_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        TOKEN25 = (CommonTree) match(input, TOKEN, FOLLOW_TOKEN_in_step594);
                        TOKEN25_tree = (CommonTree) adaptor.dupNode(TOKEN25);

                        root_2 = (CommonTree) adaptor.becomeRoot(TOKEN25_tree, root_2);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_token_in_step596);
                        token26 = token();

                        state._fsp--;

                        adaptor.addChild(root_2, token26.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        PREDICATES27 = (CommonTree) match(input, PREDICATES, FOLLOW_PREDICATES_in_step600);
                        PREDICATES27_tree = (CommonTree) adaptor.dupNode(PREDICATES27);

                        root_2 = (CommonTree) adaptor.becomeRoot(PREDICATES27_tree, root_2);


                        if (input.LA(1) == Token.DOWN) {
                            match(input, Token.DOWN, null);
                            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:422:42: ( ^( PREDICATE statement ) )*
                            loop5:
                            do {
                                int alt5 = 2;
                                int LA5_0 = input.LA(1);

                                if ((LA5_0 == PREDICATE)) {
                                    alt5 = 1;
                                }


                                switch (alt5) {
                                    case 1:
                                        // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:422:44: ^( PREDICATE statement )
                                    {
                                        _last = (CommonTree) input.LT(1);
                                        {
                                            CommonTree _save_last_3 = _last;
                                            CommonTree _first_3 = null;
                                            CommonTree root_3 = (CommonTree) adaptor.nil();
                                            _last = (CommonTree) input.LT(1);
                                            PREDICATE28 = (CommonTree) match(input, PREDICATE, FOLLOW_PREDICATE_in_step605);
                                            PREDICATE28_tree = (CommonTree) adaptor.dupNode(PREDICATE28);

                                            root_3 = (CommonTree) adaptor.becomeRoot(PREDICATE28_tree, root_3);


                                            match(input, Token.DOWN, null);
                                            _last = (CommonTree) input.LT(1);
                                            pushFollow(FOLLOW_statement_in_step607);
                                            statement29 = statement();

                                            state._fsp--;

                                            adaptor.addChild(root_3, statement29.getTree());
                                            predicates.add((statement29 != null ? statement29.op : null));

                                            match(input, Token.UP, null);
                                            adaptor.addChild(root_2, root_3);
                                            _last = _save_last_3;
                                        }


                                    }
                                    break;

                                    default:
                                        break loop5;
                                }
                            } while (true);


                            match(input, Token.UP, null);
                        }
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }

                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:422:107: (nativeStep= gremlin_native_path_definition )*
                    loop6:
                    do {
                        int alt6 = 2;
                        int LA6_0 = input.LA(1);

                        if ((LA6_0 == NATIVE)) {
                            alt6 = 1;
                        }


                        switch (alt6) {
                            case 1:
                                // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:422:109: nativeStep= gremlin_native_path_definition
                            {
                                _last = (CommonTree) input.LT(1);
                                pushFollow(FOLLOW_gremlin_native_path_definition_in_step621);
                                nativeStep = gremlin_native_path_definition();

                                state._fsp--;

                                adaptor.addChild(root_1, nativeStep.getTree());
                                nativeSteps.add((nativeStep != null ? nativeStep.pipe : null));

                            }
                            break;

                            default:
                                break loop6;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                ((gpath_statement_scope) gpath_statement_stack.peek()).steps.add(new Pair((token26 != null ? token26.atom : null), new Pair(predicates, nativeSteps)));


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "step"

    public static class gremlin_native_path_definition_return extends TreeRuleReturnScope {
        public Pipe pipe;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "gremlin_native_path_definition"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:428:1: gremlin_native_path_definition returns [Pipe pipe] : ^( NATIVE ( statement )+ ) ;
    public final GremlinEvaluator.gremlin_native_path_definition_return gremlin_native_path_definition() throws RecognitionException {
        GremlinEvaluator.gremlin_native_path_definition_return retval = new GremlinEvaluator.gremlin_native_path_definition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree NATIVE30 = null;
        GremlinEvaluator.statement_return statement31 = null;


        CommonTree NATIVE30_tree = null;


        List<Tree> statements = new LinkedList<Tree>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:432:5: ( ^( NATIVE ( statement )+ ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:432:7: ^( NATIVE ( statement )+ )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    NATIVE30 = (CommonTree) match(input, NATIVE, FOLLOW_NATIVE_in_gremlin_native_path_definition668);
                    NATIVE30_tree = (CommonTree) adaptor.dupNode(NATIVE30);

                    root_1 = (CommonTree) adaptor.becomeRoot(NATIVE30_tree, root_1);


                    match(input, Token.DOWN, null);
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:432:16: ( statement )+
                    int cnt7 = 0;
                    loop7:
                    do {
                        int alt7 = 2;
                        int LA7_0 = input.LA(1);

                        if ((LA7_0 == VAR || LA7_0 == FUNC || (LA7_0 >= GPATH && LA7_0 <= NATIVE_STEP) || LA7_0 == IF || (LA7_0 >= FOREACH && LA7_0 <= DOUBLE) || LA7_0 == NULL || (LA7_0 >= 76 && LA7_0 <= 77) || (LA7_0 >= 91 && LA7_0 <= 101))) {
                            alt7 = 1;
                        }


                        switch (alt7) {
                            case 1:
                                // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:432:17: statement
                            {
                                _last = (CommonTree) input.LT(1);
                                pushFollow(FOLLOW_statement_in_gremlin_native_path_definition671);
                                statement31 = statement();

                                state._fsp--;

                                adaptor.addChild(root_1, statement31.getTree());
                                statements.add((statement31 != null ? ((CommonTree) statement31.tree) : null));

                            }
                            break;

                            default:
                                if (cnt7 >= 1)
                                    break loop7;
                                EarlyExitException eee = new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                retval.pipe = new NativePipe(new CodeBlock(statements, this.context));


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "gremlin_native_path_definition"

    public static class token_return extends TreeRuleReturnScope {
        public Atom atom;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "token"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:438:1: token returns [Atom atom] : ( function_call | ^( STR StringLiteral ) | ^( VARIABLE_CALL VARIABLE ) | ^( PROPERTY_CALL PROPERTY ) | IDENTIFIER | '..' | ^( BOOL b= BOOLEAN ) | statement );
    public final GremlinEvaluator.token_return token() throws RecognitionException {
        GremlinEvaluator.token_return retval = new GremlinEvaluator.token_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree b = null;
        CommonTree STR33 = null;
        CommonTree StringLiteral34 = null;
        CommonTree VARIABLE_CALL35 = null;
        CommonTree VARIABLE36 = null;
        CommonTree PROPERTY_CALL37 = null;
        CommonTree PROPERTY38 = null;
        CommonTree IDENTIFIER39 = null;
        CommonTree string_literal40 = null;
        CommonTree BOOL41 = null;
        GremlinEvaluator.function_call_return function_call32 = null;

        GremlinEvaluator.statement_return statement42 = null;


        CommonTree b_tree = null;
        CommonTree STR33_tree = null;
        CommonTree StringLiteral34_tree = null;
        CommonTree VARIABLE_CALL35_tree = null;
        CommonTree VARIABLE36_tree = null;
        CommonTree PROPERTY_CALL37_tree = null;
        CommonTree PROPERTY38_tree = null;
        CommonTree IDENTIFIER39_tree = null;
        CommonTree string_literal40_tree = null;
        CommonTree BOOL41_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:439:5: ( function_call | ^( STR StringLiteral ) | ^( VARIABLE_CALL VARIABLE ) | ^( PROPERTY_CALL PROPERTY ) | IDENTIFIER | '..' | ^( BOOL b= BOOLEAN ) | statement )
            int alt8 = 8;
            switch (input.LA(1)) {
                case FUNC_CALL: {
                    alt8 = 1;
                }
                break;
                case STR: {
                    alt8 = 2;
                }
                break;
                case VARIABLE_CALL: {
                    alt8 = 3;
                }
                break;
                case PROPERTY_CALL: {
                    alt8 = 4;
                }
                break;
                case IDENTIFIER: {
                    alt8 = 5;
                }
                break;
                case 69: {
                    alt8 = 6;
                }
                break;
                case BOOL: {
                    alt8 = 7;
                }
                break;
                case VAR:
                case FUNC:
                case GPATH:
                case NATIVE_STEP:
                case IF:
                case FOREACH:
                case WHILE:
                case REPEAT:
                case INCLUDE:
                case SCRIPT:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case NULL:
                case 76:
                case 77:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101: {
                    alt8 = 8;
                }
                break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 8, 0, input);

                    throw nvae;
            }

            switch (alt8) {
                case 1:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:439:9: function_call
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_function_call_in_token707);
                    function_call32 = function_call();

                    state._fsp--;

                    adaptor.addChild(root_0, function_call32.getTree());
                    retval.atom = (function_call32 != null ? function_call32.value : null);

                }
                break;
                case 2:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:440:9: ^( STR StringLiteral )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        STR33 = (CommonTree) match(input, STR, FOLLOW_STR_in_token734);
                        STR33_tree = (CommonTree) adaptor.dupNode(STR33);

                        root_1 = (CommonTree) adaptor.becomeRoot(STR33_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        StringLiteral34 = (CommonTree) match(input, StringLiteral, FOLLOW_StringLiteral_in_token736);
                        StringLiteral34_tree = (CommonTree) adaptor.dupNode(StringLiteral34);

                        adaptor.addChild(root_1, StringLiteral34_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.atom = new Atom<String>((StringLiteral34 != null ? StringLiteral34.getText() : null));

                }
                break;
                case 3:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:441:4: ^( VARIABLE_CALL VARIABLE )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        VARIABLE_CALL35 = (CommonTree) match(input, VARIABLE_CALL, FOLLOW_VARIABLE_CALL_in_token752);
                        VARIABLE_CALL35_tree = (CommonTree) adaptor.dupNode(VARIABLE_CALL35);

                        root_1 = (CommonTree) adaptor.becomeRoot(VARIABLE_CALL35_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        VARIABLE36 = (CommonTree) match(input, VARIABLE, FOLLOW_VARIABLE_in_token754);
                        VARIABLE36_tree = (CommonTree) adaptor.dupNode(VARIABLE36);

                        adaptor.addChild(root_1, VARIABLE36_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.atom = (VARIABLE36 != null ? VARIABLE36.getText() : null).equals(Tokens.ROOT_VARIABLE) ? new RootVariable(context) : new Variable((VARIABLE36 != null ? VARIABLE36.getText() : null), this.context);

                }
                break;
                case 4:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:442:4: ^( PROPERTY_CALL PROPERTY )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        PROPERTY_CALL37 = (CommonTree) match(input, PROPERTY_CALL, FOLLOW_PROPERTY_CALL_in_token765);
                        PROPERTY_CALL37_tree = (CommonTree) adaptor.dupNode(PROPERTY_CALL37);

                        root_1 = (CommonTree) adaptor.becomeRoot(PROPERTY_CALL37_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        PROPERTY38 = (CommonTree) match(input, PROPERTY, FOLLOW_PROPERTY_in_token767);
                        PROPERTY38_tree = (CommonTree) adaptor.dupNode(PROPERTY38);

                        adaptor.addChild(root_1, PROPERTY38_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.atom = new Property<String>((PROPERTY38 != null ? PROPERTY38.getText() : null).substring(1));

                }
                break;
                case 5:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:443:7: IDENTIFIER
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    IDENTIFIER39 = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_token780);
                    IDENTIFIER39_tree = (CommonTree) adaptor.dupNode(IDENTIFIER39);

                    adaptor.addChild(root_0, IDENTIFIER39_tree);


                    String idText = (IDENTIFIER39 != null ? IDENTIFIER39.getText() : null);

                    if (idText.matches("^[\\d]+..[\\d]+")) {
                        Matcher range = rangePattern.matcher(idText);
                        retval.atom = (range.matches()) ? new Atom<Set>(this.createRange(range.group(1), range.group(2))) : new Atom<Object>(null);
                    } else {
                        retval.atom = new Id<String>((IDENTIFIER39 != null ? IDENTIFIER39.getText() : null));
                    }


                }
                break;
                case 6:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:454:6: '..'
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    string_literal40 = (CommonTree) match(input, 69, FOLLOW_69_in_token847);
                    string_literal40_tree = (CommonTree) adaptor.dupNode(string_literal40);

                    adaptor.addChild(root_0, string_literal40_tree);

                    retval.atom = new Id<String>("..");

                }
                break;
                case 7:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:455:9: ^( BOOL b= BOOLEAN )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        BOOL41 = (CommonTree) match(input, BOOL, FOLLOW_BOOL_in_token860);
                        BOOL41_tree = (CommonTree) adaptor.dupNode(BOOL41);

                        root_1 = (CommonTree) adaptor.becomeRoot(BOOL41_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        b = (CommonTree) match(input, BOOLEAN, FOLLOW_BOOLEAN_in_token864);
                        b_tree = (CommonTree) adaptor.dupNode(b);

                        adaptor.addChild(root_1, b_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.atom = new Atom<Boolean>(new Boolean((b != null ? b.getText() : null)));

                }
                break;
                case 8:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:456:4: statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_statement_in_token872);
                    statement42 = statement();

                    state._fsp--;

                    adaptor.addChild(root_0, statement42.getTree());

                    try {
                        retval.atom = (statement42 != null ? statement42.op : null).compute();
                    } catch (Exception e) {
                        retval.atom = new Atom<Operation>((statement42 != null ? statement42.op : null));
                    }


                }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "token"

    public static class if_statement_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "if_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:466:1: if_statement returns [Operation op] : ^( IF ^( COND cond= statement ) if_block= block ( ^( ELSE else_block= block ) )? ) ;
    public final GremlinEvaluator.if_statement_return if_statement() throws RecognitionException {
        GremlinEvaluator.if_statement_return retval = new GremlinEvaluator.if_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree IF43 = null;
        CommonTree COND44 = null;
        CommonTree ELSE45 = null;
        GremlinEvaluator.statement_return cond = null;

        GremlinEvaluator.block_return if_block = null;

        GremlinEvaluator.block_return else_block = null;


        CommonTree IF43_tree = null;
        CommonTree COND44_tree = null;
        CommonTree ELSE45_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:467:2: ( ^( IF ^( COND cond= statement ) if_block= block ( ^( ELSE else_block= block ) )? ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:467:4: ^( IF ^( COND cond= statement ) if_block= block ( ^( ELSE else_block= block ) )? )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    IF43 = (CommonTree) match(input, IF, FOLLOW_IF_in_if_statement894);
                    IF43_tree = (CommonTree) adaptor.dupNode(IF43);

                    root_1 = (CommonTree) adaptor.becomeRoot(IF43_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        COND44 = (CommonTree) match(input, COND, FOLLOW_COND_in_if_statement897);
                        COND44_tree = (CommonTree) adaptor.dupNode(COND44);

                        root_2 = (CommonTree) adaptor.becomeRoot(COND44_tree, root_2);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_if_statement901);
                        cond = statement();

                        state._fsp--;

                        adaptor.addChild(root_2, cond.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_block_in_if_statement906);
                    if_block = block();

                    state._fsp--;

                    adaptor.addChild(root_1, if_block.getTree());
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:467:47: ( ^( ELSE else_block= block ) )?
                    int alt9 = 2;
                    int LA9_0 = input.LA(1);

                    if ((LA9_0 == ELSE)) {
                        alt9 = 1;
                    }
                    switch (alt9) {
                        case 1:
                            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:467:49: ^( ELSE else_block= block )
                        {
                            _last = (CommonTree) input.LT(1);
                            {
                                CommonTree _save_last_2 = _last;
                                CommonTree _first_2 = null;
                                CommonTree root_2 = (CommonTree) adaptor.nil();
                                _last = (CommonTree) input.LT(1);
                                ELSE45 = (CommonTree) match(input, ELSE, FOLLOW_ELSE_in_if_statement911);
                                ELSE45_tree = (CommonTree) adaptor.dupNode(ELSE45);

                                root_2 = (CommonTree) adaptor.becomeRoot(ELSE45_tree, root_2);


                                match(input, Token.DOWN, null);
                                _last = (CommonTree) input.LT(1);
                                pushFollow(FOLLOW_block_in_if_statement915);
                                else_block = block();

                                state._fsp--;

                                adaptor.addChild(root_2, else_block.getTree());

                                match(input, Token.UP, null);
                                adaptor.addChild(root_1, root_2);
                                _last = _save_last_2;
                            }


                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                retval.op = new If((cond != null ? cond.op : null), (if_block != null ? if_block.cb : null), (else_block != null ? else_block.cb : null));


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "if_statement"

    public static class while_statement_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "while_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:473:1: while_statement returns [Operation op] : ^( WHILE ^( COND cond= statement ) block ) ;
    public final GremlinEvaluator.while_statement_return while_statement() throws RecognitionException {
        GremlinEvaluator.while_statement_return retval = new GremlinEvaluator.while_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree WHILE46 = null;
        CommonTree COND47 = null;
        GremlinEvaluator.statement_return cond = null;

        GremlinEvaluator.block_return block48 = null;


        CommonTree WHILE46_tree = null;
        CommonTree COND47_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:474:2: ( ^( WHILE ^( COND cond= statement ) block ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:474:4: ^( WHILE ^( COND cond= statement ) block )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    WHILE46 = (CommonTree) match(input, WHILE, FOLLOW_WHILE_in_while_statement948);
                    WHILE46_tree = (CommonTree) adaptor.dupNode(WHILE46);

                    root_1 = (CommonTree) adaptor.becomeRoot(WHILE46_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        COND47 = (CommonTree) match(input, COND, FOLLOW_COND_in_while_statement951);
                        COND47_tree = (CommonTree) adaptor.dupNode(COND47);

                        root_2 = (CommonTree) adaptor.becomeRoot(COND47_tree, root_2);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_while_statement955);
                        cond = statement();

                        state._fsp--;

                        adaptor.addChild(root_2, cond.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_block_in_while_statement958);
                    block48 = block();

                    state._fsp--;

                    adaptor.addChild(root_1, block48.getTree());

                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                retval.op = new While((cond != null ? cond.op : null), (block48 != null ? block48.cb : null));


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "while_statement"

    public static class foreach_statement_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "foreach_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:480:1: foreach_statement returns [Operation op] : ^( FOREACH VARIABLE arr= statement block ) ;
    public final GremlinEvaluator.foreach_statement_return foreach_statement() throws RecognitionException {
        GremlinEvaluator.foreach_statement_return retval = new GremlinEvaluator.foreach_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree FOREACH49 = null;
        CommonTree VARIABLE50 = null;
        GremlinEvaluator.statement_return arr = null;

        GremlinEvaluator.block_return block51 = null;


        CommonTree FOREACH49_tree = null;
        CommonTree VARIABLE50_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:481:2: ( ^( FOREACH VARIABLE arr= statement block ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:481:4: ^( FOREACH VARIABLE arr= statement block )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    FOREACH49 = (CommonTree) match(input, FOREACH, FOLLOW_FOREACH_in_foreach_statement985);
                    FOREACH49_tree = (CommonTree) adaptor.dupNode(FOREACH49);

                    root_1 = (CommonTree) adaptor.becomeRoot(FOREACH49_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    VARIABLE50 = (CommonTree) match(input, VARIABLE, FOLLOW_VARIABLE_in_foreach_statement987);
                    VARIABLE50_tree = (CommonTree) adaptor.dupNode(VARIABLE50);

                    adaptor.addChild(root_1, VARIABLE50_tree);

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_statement_in_foreach_statement991);
                    arr = statement();

                    state._fsp--;

                    adaptor.addChild(root_1, arr.getTree());
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_block_in_foreach_statement993);
                    block51 = block();

                    state._fsp--;

                    adaptor.addChild(root_1, block51.getTree());

                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                retval.op = new Foreach((VARIABLE50 != null ? VARIABLE50.getText() : null), (arr != null ? arr.op : null), (block51 != null ? block51.cb : null), this.context);


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "foreach_statement"

    public static class repeat_statement_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "repeat_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:487:1: repeat_statement returns [Operation op] : ^( REPEAT timer= statement block ) ;
    public final GremlinEvaluator.repeat_statement_return repeat_statement() throws RecognitionException {
        GremlinEvaluator.repeat_statement_return retval = new GremlinEvaluator.repeat_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree REPEAT52 = null;
        GremlinEvaluator.statement_return timer = null;

        GremlinEvaluator.block_return block53 = null;


        CommonTree REPEAT52_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:488:2: ( ^( REPEAT timer= statement block ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:488:4: ^( REPEAT timer= statement block )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    REPEAT52 = (CommonTree) match(input, REPEAT, FOLLOW_REPEAT_in_repeat_statement1021);
                    REPEAT52_tree = (CommonTree) adaptor.dupNode(REPEAT52);

                    root_1 = (CommonTree) adaptor.becomeRoot(REPEAT52_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_statement_in_repeat_statement1025);
                    timer = statement();

                    state._fsp--;

                    adaptor.addChild(root_1, timer.getTree());
                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_block_in_repeat_statement1027);
                    block53 = block();

                    state._fsp--;

                    adaptor.addChild(root_1, block53.getTree());

                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                retval.op = new Repeat((timer != null ? timer.op : null), (block53 != null ? block53.cb : null));


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "repeat_statement"

    public static class block_return extends TreeRuleReturnScope {
        public CodeBlock cb;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "block"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:494:1: block returns [CodeBlock cb] : ^( BLOCK (st= statement | ^( RETURN ret= statement ) )* ) ;
    public final GremlinEvaluator.block_return block() throws RecognitionException {
        GremlinEvaluator.block_return retval = new GremlinEvaluator.block_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree BLOCK54 = null;
        CommonTree RETURN55 = null;
        GremlinEvaluator.statement_return st = null;

        GremlinEvaluator.statement_return ret = null;


        CommonTree BLOCK54_tree = null;
        CommonTree RETURN55_tree = null;


        inBlock = true;
        List<Tree> statements = new LinkedList<Tree>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:502:5: ( ^( BLOCK (st= statement | ^( RETURN ret= statement ) )* ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:502:7: ^( BLOCK (st= statement | ^( RETURN ret= statement ) )* )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    BLOCK54 = (CommonTree) match(input, BLOCK, FOLLOW_BLOCK_in_block1075);
                    BLOCK54_tree = (CommonTree) adaptor.dupNode(BLOCK54);

                    root_1 = (CommonTree) adaptor.becomeRoot(BLOCK54_tree, root_1);


                    if (input.LA(1) == Token.DOWN) {
                        match(input, Token.DOWN, null);
                        // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:502:15: (st= statement | ^( RETURN ret= statement ) )*
                        loop10:
                        do {
                            int alt10 = 3;
                            int LA10_0 = input.LA(1);

                            if ((LA10_0 == VAR || LA10_0 == FUNC || (LA10_0 >= GPATH && LA10_0 <= NATIVE_STEP) || LA10_0 == IF || (LA10_0 >= FOREACH && LA10_0 <= DOUBLE) || LA10_0 == NULL || (LA10_0 >= 76 && LA10_0 <= 77) || (LA10_0 >= 91 && LA10_0 <= 101))) {
                                alt10 = 1;
                            } else if ((LA10_0 == RETURN)) {
                                alt10 = 2;
                            }


                            switch (alt10) {
                                case 1:
                                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:502:17: st= statement
                                {
                                    _last = (CommonTree) input.LT(1);
                                    pushFollow(FOLLOW_statement_in_block1081);
                                    st = statement();

                                    state._fsp--;

                                    adaptor.addChild(root_1, st.getTree());
                                    statements.add((st != null ? ((CommonTree) st.tree) : null));

                                }
                                break;
                                case 2:
                                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:502:62: ^( RETURN ret= statement )
                                {
                                    _last = (CommonTree) input.LT(1);
                                    {
                                        CommonTree _save_last_2 = _last;
                                        CommonTree _first_2 = null;
                                        CommonTree root_2 = (CommonTree) adaptor.nil();
                                        _last = (CommonTree) input.LT(1);
                                        RETURN55 = (CommonTree) match(input, RETURN, FOLLOW_RETURN_in_block1088);
                                        RETURN55_tree = (CommonTree) adaptor.dupNode(RETURN55);

                                        root_2 = (CommonTree) adaptor.becomeRoot(RETURN55_tree, root_2);


                                        match(input, Token.DOWN, null);
                                        _last = (CommonTree) input.LT(1);
                                        pushFollow(FOLLOW_statement_in_block1092);
                                        ret = statement();

                                        state._fsp--;

                                        adaptor.addChild(root_2, ret.getTree());
                                        statements.add((ret != null ? ((CommonTree) ret.tree) : null).getParent());

                                        match(input, Token.UP, null);
                                        adaptor.addChild(root_1, root_2);
                                        _last = _save_last_2;
                                    }


                                }
                                break;

                                default:
                                    break loop10;
                            }
                        } while (true);


                        match(input, Token.UP, null);
                    }
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }

                retval.cb = new CodeBlock(statements, this.context);

            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);


            inBlock = false;

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "block"

    public static class expression_return extends TreeRuleReturnScope {
        public Operation expr;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "expression"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:506:1: expression returns [Operation expr] : ( ^( '=' a= statement b= statement ) | ^( '!=' a= statement b= statement ) | ^( '<' a= statement b= statement ) | ^( '>' a= statement b= statement ) | ^( '<=' a= statement b= statement ) | ^( '>=' a= statement b= statement ) | operation );
    public final GremlinEvaluator.expression_return expression() throws RecognitionException {
        GremlinEvaluator.expression_return retval = new GremlinEvaluator.expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree char_literal56 = null;
        CommonTree string_literal57 = null;
        CommonTree char_literal58 = null;
        CommonTree char_literal59 = null;
        CommonTree string_literal60 = null;
        CommonTree string_literal61 = null;
        GremlinEvaluator.statement_return a = null;

        GremlinEvaluator.statement_return b = null;

        GremlinEvaluator.operation_return operation62 = null;


        CommonTree char_literal56_tree = null;
        CommonTree string_literal57_tree = null;
        CommonTree char_literal58_tree = null;
        CommonTree char_literal59_tree = null;
        CommonTree string_literal60_tree = null;
        CommonTree string_literal61_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:507:5: ( ^( '=' a= statement b= statement ) | ^( '!=' a= statement b= statement ) | ^( '<' a= statement b= statement ) | ^( '>' a= statement b= statement ) | ^( '<=' a= statement b= statement ) | ^( '>=' a= statement b= statement ) | operation )
            int alt11 = 7;
            switch (input.LA(1)) {
                case 91: {
                    alt11 = 1;
                }
                break;
                case 92: {
                    alt11 = 2;
                }
                break;
                case 93: {
                    alt11 = 3;
                }
                break;
                case 95: {
                    alt11 = 4;
                }
                break;
                case 94: {
                    alt11 = 5;
                }
                break;
                case 96: {
                    alt11 = 6;
                }
                break;
                case GPATH:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case NULL:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101: {
                    alt11 = 7;
                }
                break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 11, 0, input);

                    throw nvae;
            }

            switch (alt11) {
                case 1:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:507:9: ^( '=' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        char_literal56 = (CommonTree) match(input, 91, FOLLOW_91_in_expression1135);
                        char_literal56_tree = (CommonTree) adaptor.dupNode(char_literal56);

                        root_1 = (CommonTree) adaptor.becomeRoot(char_literal56_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1140);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1144);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.expr = new Equality((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 2:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:508:9: ^( '!=' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        string_literal57 = (CommonTree) match(input, 92, FOLLOW_92_in_expression1158);
                        string_literal57_tree = (CommonTree) adaptor.dupNode(string_literal57);

                        root_1 = (CommonTree) adaptor.becomeRoot(string_literal57_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1162);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1166);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.expr = new UnEquality((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 3:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:509:9: ^( '<' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        char_literal58 = (CommonTree) match(input, 93, FOLLOW_93_in_expression1180);
                        char_literal58_tree = (CommonTree) adaptor.dupNode(char_literal58);

                        root_1 = (CommonTree) adaptor.becomeRoot(char_literal58_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1185);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1189);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.expr = new LessThan((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 4:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:510:9: ^( '>' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        char_literal59 = (CommonTree) match(input, 95, FOLLOW_95_in_expression1203);
                        char_literal59_tree = (CommonTree) adaptor.dupNode(char_literal59);

                        root_1 = (CommonTree) adaptor.becomeRoot(char_literal59_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1208);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1212);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.expr = new GreaterThan((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 5:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:511:9: ^( '<=' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        string_literal60 = (CommonTree) match(input, 94, FOLLOW_94_in_expression1226);
                        string_literal60_tree = (CommonTree) adaptor.dupNode(string_literal60);

                        root_1 = (CommonTree) adaptor.becomeRoot(string_literal60_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1230);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1234);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.expr = new LessThanOrEqual((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 6:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:512:9: ^( '>=' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        string_literal61 = (CommonTree) match(input, 96, FOLLOW_96_in_expression1248);
                        string_literal61_tree = (CommonTree) adaptor.dupNode(string_literal61);

                        root_1 = (CommonTree) adaptor.becomeRoot(string_literal61_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1252);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_expression1256);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.expr = new GreaterThanOrEqual((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 7:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:513:9: operation
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_operation_in_expression1269);
                    operation62 = operation();

                    state._fsp--;

                    adaptor.addChild(root_0, operation62.getTree());
                    retval.expr = (operation62 != null ? operation62.op : null);

                }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "expression"

    public static class operation_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "operation"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:516:1: operation returns [Operation op] : ( ^( '+' a= statement b= statement ) | ^( '-' a= statement b= statement ) | binary_operation );
    public final GremlinEvaluator.operation_return operation() throws RecognitionException {
        GremlinEvaluator.operation_return retval = new GremlinEvaluator.operation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree char_literal63 = null;
        CommonTree char_literal64 = null;
        GremlinEvaluator.statement_return a = null;

        GremlinEvaluator.statement_return b = null;

        GremlinEvaluator.binary_operation_return binary_operation65 = null;


        CommonTree char_literal63_tree = null;
        CommonTree char_literal64_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:517:5: ( ^( '+' a= statement b= statement ) | ^( '-' a= statement b= statement ) | binary_operation )
            int alt12 = 3;
            switch (input.LA(1)) {
                case 97: {
                    alt12 = 1;
                }
                break;
                case 98: {
                    alt12 = 2;
                }
                break;
                case GPATH:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case NULL:
                case 99:
                case 100:
                case 101: {
                    alt12 = 3;
                }
                break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 12, 0, input);

                    throw nvae;
            }

            switch (alt12) {
                case 1:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:517:9: ^( '+' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        char_literal63 = (CommonTree) match(input, 97, FOLLOW_97_in_operation1314);
                        char_literal63_tree = (CommonTree) adaptor.dupNode(char_literal63);

                        root_1 = (CommonTree) adaptor.becomeRoot(char_literal63_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_operation1318);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_operation1322);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.op = new Addition((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 2:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:518:9: ^( '-' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        char_literal64 = (CommonTree) match(input, 98, FOLLOW_98_in_operation1336);
                        char_literal64_tree = (CommonTree) adaptor.dupNode(char_literal64);

                        root_1 = (CommonTree) adaptor.becomeRoot(char_literal64_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_operation1340);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_operation1344);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.op = new Subtraction((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 3:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:519:9: binary_operation
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_binary_operation_in_operation1357);
                    binary_operation65 = binary_operation();

                    state._fsp--;

                    adaptor.addChild(root_0, binary_operation65.getTree());
                    retval.op = (binary_operation65 != null ? binary_operation65.operation : null);

                }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "operation"

    public static class binary_operation_return extends TreeRuleReturnScope {
        public Operation operation;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "binary_operation"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:522:1: binary_operation returns [Operation operation] : ( ^( '*' a= statement b= statement ) | ^( 'div' a= statement b= statement ) | ^( 'mod' a= statement b= statement ) | atom );
    public final GremlinEvaluator.binary_operation_return binary_operation() throws RecognitionException {
        GremlinEvaluator.binary_operation_return retval = new GremlinEvaluator.binary_operation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree char_literal66 = null;
        CommonTree string_literal67 = null;
        CommonTree string_literal68 = null;
        GremlinEvaluator.statement_return a = null;

        GremlinEvaluator.statement_return b = null;

        GremlinEvaluator.atom_return atom69 = null;


        CommonTree char_literal66_tree = null;
        CommonTree string_literal67_tree = null;
        CommonTree string_literal68_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:523:5: ( ^( '*' a= statement b= statement ) | ^( 'div' a= statement b= statement ) | ^( 'mod' a= statement b= statement ) | atom )
            int alt13 = 4;
            switch (input.LA(1)) {
                case 99: {
                    alt13 = 1;
                }
                break;
                case 100: {
                    alt13 = 2;
                }
                break;
                case 101: {
                    alt13 = 3;
                }
                break;
                case GPATH:
                case INT:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case NULL: {
                    alt13 = 4;
                }
                break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 13, 0, input);

                    throw nvae;
            }

            switch (alt13) {
                case 1:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:523:9: ^( '*' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        char_literal66 = (CommonTree) match(input, 99, FOLLOW_99_in_binary_operation1394);
                        char_literal66_tree = (CommonTree) adaptor.dupNode(char_literal66);

                        root_1 = (CommonTree) adaptor.becomeRoot(char_literal66_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_binary_operation1400);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_binary_operation1404);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.operation = new Multiplication((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 2:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:524:9: ^( 'div' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        string_literal67 = (CommonTree) match(input, 100, FOLLOW_100_in_binary_operation1419);
                        string_literal67_tree = (CommonTree) adaptor.dupNode(string_literal67);

                        root_1 = (CommonTree) adaptor.becomeRoot(string_literal67_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_binary_operation1423);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_binary_operation1427);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.operation = new Division((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 3:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:525:7: ^( 'mod' a= statement b= statement )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        string_literal68 = (CommonTree) match(input, 101, FOLLOW_101_in_binary_operation1440);
                        string_literal68_tree = (CommonTree) adaptor.dupNode(string_literal68);

                        root_1 = (CommonTree) adaptor.becomeRoot(string_literal68_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_binary_operation1444);
                        a = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, a.getTree());
                        _last = (CommonTree) input.LT(1);
                        pushFollow(FOLLOW_statement_in_binary_operation1448);
                        b = statement();

                        state._fsp--;

                        adaptor.addChild(root_1, b.getTree());

                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.operation = new Modulo((a != null ? a.op : null), (b != null ? b.op : null));

                }
                break;
                case 4:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:526:9: atom
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_atom_in_binary_operation1462);
                    atom69 = atom();

                    state._fsp--;

                    adaptor.addChild(root_0, atom69.getTree());
                    retval.operation = new UnaryOperation((atom69 != null ? atom69.value : null));

                }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "binary_operation"

    public static class function_definition_statement_return extends TreeRuleReturnScope {
        public Operation op;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "function_definition_statement"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:529:1: function_definition_statement returns [Operation op] : ^( FUNC ^( FUNC_NAME ^( NS ns= IDENTIFIER ) ^( NAME fn_name= IDENTIFIER ) ) ^( ARGS ( ^( ARG VARIABLE ) )* ) block ) ;
    public final GremlinEvaluator.function_definition_statement_return function_definition_statement() throws RecognitionException {
        GremlinEvaluator.function_definition_statement_return retval = new GremlinEvaluator.function_definition_statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree ns = null;
        CommonTree fn_name = null;
        CommonTree FUNC70 = null;
        CommonTree FUNC_NAME71 = null;
        CommonTree NS72 = null;
        CommonTree NAME73 = null;
        CommonTree ARGS74 = null;
        CommonTree ARG75 = null;
        CommonTree VARIABLE76 = null;
        GremlinEvaluator.block_return block77 = null;


        CommonTree ns_tree = null;
        CommonTree fn_name_tree = null;
        CommonTree FUNC70_tree = null;
        CommonTree FUNC_NAME71_tree = null;
        CommonTree NS72_tree = null;
        CommonTree NAME73_tree = null;
        CommonTree ARGS74_tree = null;
        CommonTree ARG75_tree = null;
        CommonTree VARIABLE76_tree = null;


        List<String> params = new ArrayList<String>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:533:2: ( ^( FUNC ^( FUNC_NAME ^( NS ns= IDENTIFIER ) ^( NAME fn_name= IDENTIFIER ) ) ^( ARGS ( ^( ARG VARIABLE ) )* ) block ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:533:4: ^( FUNC ^( FUNC_NAME ^( NS ns= IDENTIFIER ) ^( NAME fn_name= IDENTIFIER ) ) ^( ARGS ( ^( ARG VARIABLE ) )* ) block )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    FUNC70 = (CommonTree) match(input, FUNC, FOLLOW_FUNC_in_function_definition_statement1518);
                    FUNC70_tree = (CommonTree) adaptor.dupNode(FUNC70);

                    root_1 = (CommonTree) adaptor.becomeRoot(FUNC70_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        FUNC_NAME71 = (CommonTree) match(input, FUNC_NAME, FOLLOW_FUNC_NAME_in_function_definition_statement1521);
                        FUNC_NAME71_tree = (CommonTree) adaptor.dupNode(FUNC_NAME71);

                        root_2 = (CommonTree) adaptor.becomeRoot(FUNC_NAME71_tree, root_2);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        {
                            CommonTree _save_last_3 = _last;
                            CommonTree _first_3 = null;
                            CommonTree root_3 = (CommonTree) adaptor.nil();
                            _last = (CommonTree) input.LT(1);
                            NS72 = (CommonTree) match(input, NS, FOLLOW_NS_in_function_definition_statement1524);
                            NS72_tree = (CommonTree) adaptor.dupNode(NS72);

                            root_3 = (CommonTree) adaptor.becomeRoot(NS72_tree, root_3);


                            match(input, Token.DOWN, null);
                            _last = (CommonTree) input.LT(1);
                            ns = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_function_definition_statement1528);
                            ns_tree = (CommonTree) adaptor.dupNode(ns);

                            adaptor.addChild(root_3, ns_tree);


                            match(input, Token.UP, null);
                            adaptor.addChild(root_2, root_3);
                            _last = _save_last_3;
                        }

                        _last = (CommonTree) input.LT(1);
                        {
                            CommonTree _save_last_3 = _last;
                            CommonTree _first_3 = null;
                            CommonTree root_3 = (CommonTree) adaptor.nil();
                            _last = (CommonTree) input.LT(1);
                            NAME73 = (CommonTree) match(input, NAME, FOLLOW_NAME_in_function_definition_statement1532);
                            NAME73_tree = (CommonTree) adaptor.dupNode(NAME73);

                            root_3 = (CommonTree) adaptor.becomeRoot(NAME73_tree, root_3);


                            match(input, Token.DOWN, null);
                            _last = (CommonTree) input.LT(1);
                            fn_name = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_function_definition_statement1536);
                            fn_name_tree = (CommonTree) adaptor.dupNode(fn_name);

                            adaptor.addChild(root_3, fn_name_tree);


                            match(input, Token.UP, null);
                            adaptor.addChild(root_2, root_3);
                            _last = _save_last_3;
                        }


                        match(input, Token.UP, null);
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        ARGS74 = (CommonTree) match(input, ARGS, FOLLOW_ARGS_in_function_definition_statement1541);
                        ARGS74_tree = (CommonTree) adaptor.dupNode(ARGS74);

                        root_2 = (CommonTree) adaptor.becomeRoot(ARGS74_tree, root_2);


                        if (input.LA(1) == Token.DOWN) {
                            match(input, Token.DOWN, null);
                            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:533:78: ( ^( ARG VARIABLE ) )*
                            loop14:
                            do {
                                int alt14 = 2;
                                int LA14_0 = input.LA(1);

                                if ((LA14_0 == ARG)) {
                                    alt14 = 1;
                                }


                                switch (alt14) {
                                    case 1:
                                        // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:533:80: ^( ARG VARIABLE )
                                    {
                                        _last = (CommonTree) input.LT(1);
                                        {
                                            CommonTree _save_last_3 = _last;
                                            CommonTree _first_3 = null;
                                            CommonTree root_3 = (CommonTree) adaptor.nil();
                                            _last = (CommonTree) input.LT(1);
                                            ARG75 = (CommonTree) match(input, ARG, FOLLOW_ARG_in_function_definition_statement1546);
                                            ARG75_tree = (CommonTree) adaptor.dupNode(ARG75);

                                            root_3 = (CommonTree) adaptor.becomeRoot(ARG75_tree, root_3);


                                            match(input, Token.DOWN, null);
                                            _last = (CommonTree) input.LT(1);
                                            VARIABLE76 = (CommonTree) match(input, VARIABLE, FOLLOW_VARIABLE_in_function_definition_statement1548);
                                            VARIABLE76_tree = (CommonTree) adaptor.dupNode(VARIABLE76);

                                            adaptor.addChild(root_3, VARIABLE76_tree);

                                            params.add((VARIABLE76 != null ? VARIABLE76.getText() : null));

                                            match(input, Token.UP, null);
                                            adaptor.addChild(root_2, root_3);
                                            _last = _save_last_3;
                                        }


                                    }
                                    break;

                                    default:
                                        break loop14;
                                }
                            } while (true);


                            match(input, Token.UP, null);
                        }
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_block_in_function_definition_statement1557);
                    block77 = block();

                    state._fsp--;

                    adaptor.addChild(root_1, block77.getTree());

                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                NativeFunction fn = new NativeFunction((fn_name != null ? fn_name.getText() : null), params, (block77 != null ? block77.cb : null));
                this.registerFunction((ns != null ? ns.getText() : null), fn);

                retval.op = new UnaryOperation(new Atom<Boolean>(true));


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "function_definition_statement"

    public static class function_call_return extends TreeRuleReturnScope {
        public Atom value;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "function_call"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:542:1: function_call returns [Atom value] : ^( FUNC_CALL ^( FUNC_NAME ^( NS ns= IDENTIFIER ) ^( NAME fn_name= IDENTIFIER ) ) ^( ARGS ( ^( ARG st= statement ) )* ) ) ;
    public final GremlinEvaluator.function_call_return function_call() throws RecognitionException {
        GremlinEvaluator.function_call_return retval = new GremlinEvaluator.function_call_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree ns = null;
        CommonTree fn_name = null;
        CommonTree FUNC_CALL78 = null;
        CommonTree FUNC_NAME79 = null;
        CommonTree NS80 = null;
        CommonTree NAME81 = null;
        CommonTree ARGS82 = null;
        CommonTree ARG83 = null;
        GremlinEvaluator.statement_return st = null;


        CommonTree ns_tree = null;
        CommonTree fn_name_tree = null;
        CommonTree FUNC_CALL78_tree = null;
        CommonTree FUNC_NAME79_tree = null;
        CommonTree NS80_tree = null;
        CommonTree NAME81_tree = null;
        CommonTree ARGS82_tree = null;
        CommonTree ARG83_tree = null;


        List<Operation> arguments = new ArrayList<Operation>();

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:546:2: ( ^( FUNC_CALL ^( FUNC_NAME ^( NS ns= IDENTIFIER ) ^( NAME fn_name= IDENTIFIER ) ) ^( ARGS ( ^( ARG st= statement ) )* ) ) )
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:546:4: ^( FUNC_CALL ^( FUNC_NAME ^( NS ns= IDENTIFIER ) ^( NAME fn_name= IDENTIFIER ) ) ^( ARGS ( ^( ARG st= statement ) )* ) )
            {
                root_0 = (CommonTree) adaptor.nil();

                _last = (CommonTree) input.LT(1);
                {
                    CommonTree _save_last_1 = _last;
                    CommonTree _first_1 = null;
                    CommonTree root_1 = (CommonTree) adaptor.nil();
                    _last = (CommonTree) input.LT(1);
                    FUNC_CALL78 = (CommonTree) match(input, FUNC_CALL, FOLLOW_FUNC_CALL_in_function_call1594);
                    FUNC_CALL78_tree = (CommonTree) adaptor.dupNode(FUNC_CALL78);

                    root_1 = (CommonTree) adaptor.becomeRoot(FUNC_CALL78_tree, root_1);


                    match(input, Token.DOWN, null);
                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        FUNC_NAME79 = (CommonTree) match(input, FUNC_NAME, FOLLOW_FUNC_NAME_in_function_call1597);
                        FUNC_NAME79_tree = (CommonTree) adaptor.dupNode(FUNC_NAME79);

                        root_2 = (CommonTree) adaptor.becomeRoot(FUNC_NAME79_tree, root_2);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        {
                            CommonTree _save_last_3 = _last;
                            CommonTree _first_3 = null;
                            CommonTree root_3 = (CommonTree) adaptor.nil();
                            _last = (CommonTree) input.LT(1);
                            NS80 = (CommonTree) match(input, NS, FOLLOW_NS_in_function_call1600);
                            NS80_tree = (CommonTree) adaptor.dupNode(NS80);

                            root_3 = (CommonTree) adaptor.becomeRoot(NS80_tree, root_3);


                            match(input, Token.DOWN, null);
                            _last = (CommonTree) input.LT(1);
                            ns = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_function_call1604);
                            ns_tree = (CommonTree) adaptor.dupNode(ns);

                            adaptor.addChild(root_3, ns_tree);


                            match(input, Token.UP, null);
                            adaptor.addChild(root_2, root_3);
                            _last = _save_last_3;
                        }

                        _last = (CommonTree) input.LT(1);
                        {
                            CommonTree _save_last_3 = _last;
                            CommonTree _first_3 = null;
                            CommonTree root_3 = (CommonTree) adaptor.nil();
                            _last = (CommonTree) input.LT(1);
                            NAME81 = (CommonTree) match(input, NAME, FOLLOW_NAME_in_function_call1608);
                            NAME81_tree = (CommonTree) adaptor.dupNode(NAME81);

                            root_3 = (CommonTree) adaptor.becomeRoot(NAME81_tree, root_3);


                            match(input, Token.DOWN, null);
                            _last = (CommonTree) input.LT(1);
                            fn_name = (CommonTree) match(input, IDENTIFIER, FOLLOW_IDENTIFIER_in_function_call1612);
                            fn_name_tree = (CommonTree) adaptor.dupNode(fn_name);

                            adaptor.addChild(root_3, fn_name_tree);


                            match(input, Token.UP, null);
                            adaptor.addChild(root_2, root_3);
                            _last = _save_last_3;
                        }


                        match(input, Token.UP, null);
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_2 = _last;
                        CommonTree _first_2 = null;
                        CommonTree root_2 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        ARGS82 = (CommonTree) match(input, ARGS, FOLLOW_ARGS_in_function_call1617);
                        ARGS82_tree = (CommonTree) adaptor.dupNode(ARGS82);

                        root_2 = (CommonTree) adaptor.becomeRoot(ARGS82_tree, root_2);


                        if (input.LA(1) == Token.DOWN) {
                            match(input, Token.DOWN, null);
                            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:546:83: ( ^( ARG st= statement ) )*
                            loop15:
                            do {
                                int alt15 = 2;
                                int LA15_0 = input.LA(1);

                                if ((LA15_0 == ARG)) {
                                    alt15 = 1;
                                }


                                switch (alt15) {
                                    case 1:
                                        // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:546:85: ^( ARG st= statement )
                                    {
                                        _last = (CommonTree) input.LT(1);
                                        {
                                            CommonTree _save_last_3 = _last;
                                            CommonTree _first_3 = null;
                                            CommonTree root_3 = (CommonTree) adaptor.nil();
                                            _last = (CommonTree) input.LT(1);
                                            ARG83 = (CommonTree) match(input, ARG, FOLLOW_ARG_in_function_call1622);
                                            ARG83_tree = (CommonTree) adaptor.dupNode(ARG83);

                                            root_3 = (CommonTree) adaptor.becomeRoot(ARG83_tree, root_3);


                                            match(input, Token.DOWN, null);
                                            _last = (CommonTree) input.LT(1);
                                            pushFollow(FOLLOW_statement_in_function_call1626);
                                            st = statement();

                                            state._fsp--;

                                            adaptor.addChild(root_3, st.getTree());
                                            arguments.add((st != null ? st.op : null));

                                            match(input, Token.UP, null);
                                            adaptor.addChild(root_2, root_3);
                                            _last = _save_last_3;
                                        }


                                    }
                                    break;

                                    default:
                                        break loop15;
                                }
                            } while (true);


                            match(input, Token.UP, null);
                        }
                        adaptor.addChild(root_1, root_2);
                        _last = _save_last_2;
                    }


                    match(input, Token.UP, null);
                    adaptor.addChild(root_0, root_1);
                    _last = _save_last_1;
                }


                if (!inBlock) {
                    try {
                        retval.value = new Func(this.getFunction((ns != null ? ns.getText() : null), (fn_name != null ? fn_name.getText() : null)), arguments, this.context);
                    } catch (Exception e) {
                        this.context.writeError(e.getMessage());
                    }
                }


            }

            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "function_call"

    public static class atom_return extends TreeRuleReturnScope {
        public Atom value;
        CommonTree tree;

        public Object getTree() {
            return tree;
        }
    }

    ;

    // $ANTLR start "atom"
    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:558:1: atom returns [Atom value] : ( ^( INT G_INT ) | ^( LONG G_LONG ) | ^( FLOAT G_FLOAT ) | ^( DOUBLE G_DOUBLE ) | gpath_statement | NULL );
    public final GremlinEvaluator.atom_return atom() throws RecognitionException {
        GremlinEvaluator.atom_return retval = new GremlinEvaluator.atom_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        CommonTree _first_0 = null;
        CommonTree _last = null;

        CommonTree INT84 = null;
        CommonTree G_INT85 = null;
        CommonTree LONG86 = null;
        CommonTree G_LONG87 = null;
        CommonTree FLOAT88 = null;
        CommonTree G_FLOAT89 = null;
        CommonTree DOUBLE90 = null;
        CommonTree G_DOUBLE91 = null;
        CommonTree NULL93 = null;
        GremlinEvaluator.gpath_statement_return gpath_statement92 = null;


        CommonTree INT84_tree = null;
        CommonTree G_INT85_tree = null;
        CommonTree LONG86_tree = null;
        CommonTree G_LONG87_tree = null;
        CommonTree FLOAT88_tree = null;
        CommonTree G_FLOAT89_tree = null;
        CommonTree DOUBLE90_tree = null;
        CommonTree G_DOUBLE91_tree = null;
        CommonTree NULL93_tree = null;

        try {
            // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:559:2: ( ^( INT G_INT ) | ^( LONG G_LONG ) | ^( FLOAT G_FLOAT ) | ^( DOUBLE G_DOUBLE ) | gpath_statement | NULL )
            int alt16 = 6;
            switch (input.LA(1)) {
                case INT: {
                    alt16 = 1;
                }
                break;
                case LONG: {
                    alt16 = 2;
                }
                break;
                case FLOAT: {
                    alt16 = 3;
                }
                break;
                case DOUBLE: {
                    alt16 = 4;
                }
                break;
                case GPATH: {
                    alt16 = 5;
                }
                break;
                case NULL: {
                    alt16 = 6;
                }
                break;
                default:
                    NoViableAltException nvae = new NoViableAltException("", 16, 0, input);

                    throw nvae;
            }

            switch (alt16) {
                case 1:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:559:6: ^( INT G_INT )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        INT84 = (CommonTree) match(input, INT, FOLLOW_INT_in_atom1663);
                        INT84_tree = (CommonTree) adaptor.dupNode(INT84);

                        root_1 = (CommonTree) adaptor.becomeRoot(INT84_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        G_INT85 = (CommonTree) match(input, G_INT, FOLLOW_G_INT_in_atom1665);
                        G_INT85_tree = (CommonTree) adaptor.dupNode(G_INT85);

                        adaptor.addChild(root_1, G_INT85_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.value = new Atom<Integer>(new Integer((G_INT85 != null ? G_INT85.getText() : null)));

                }
                break;
                case 2:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:560:6: ^( LONG G_LONG )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        LONG86 = (CommonTree) match(input, LONG, FOLLOW_LONG_in_atom1723);
                        LONG86_tree = (CommonTree) adaptor.dupNode(LONG86);

                        root_1 = (CommonTree) adaptor.becomeRoot(LONG86_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        G_LONG87 = (CommonTree) match(input, G_LONG, FOLLOW_G_LONG_in_atom1725);
                        G_LONG87_tree = (CommonTree) adaptor.dupNode(G_LONG87);

                        adaptor.addChild(root_1, G_LONG87_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }


                    String longStr = (G_LONG87 != null ? G_LONG87.getText() : null);
                    retval.value = new Atom<Long>(new Long(longStr.substring(0, longStr.length() - 1)));


                }
                break;
                case 3:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:564:6: ^( FLOAT G_FLOAT )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        FLOAT88 = (CommonTree) match(input, FLOAT, FOLLOW_FLOAT_in_atom1781);
                        FLOAT88_tree = (CommonTree) adaptor.dupNode(FLOAT88);

                        root_1 = (CommonTree) adaptor.becomeRoot(FLOAT88_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        G_FLOAT89 = (CommonTree) match(input, G_FLOAT, FOLLOW_G_FLOAT_in_atom1783);
                        G_FLOAT89_tree = (CommonTree) adaptor.dupNode(G_FLOAT89);

                        adaptor.addChild(root_1, G_FLOAT89_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }

                    retval.value = new Atom<Float>(new Float((G_FLOAT89 != null ? G_FLOAT89.getText() : null)));

                }
                break;
                case 4:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:565:6: ^( DOUBLE G_DOUBLE )
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    {
                        CommonTree _save_last_1 = _last;
                        CommonTree _first_1 = null;
                        CommonTree root_1 = (CommonTree) adaptor.nil();
                        _last = (CommonTree) input.LT(1);
                        DOUBLE90 = (CommonTree) match(input, DOUBLE, FOLLOW_DOUBLE_in_atom1837);
                        DOUBLE90_tree = (CommonTree) adaptor.dupNode(DOUBLE90);

                        root_1 = (CommonTree) adaptor.becomeRoot(DOUBLE90_tree, root_1);


                        match(input, Token.DOWN, null);
                        _last = (CommonTree) input.LT(1);
                        G_DOUBLE91 = (CommonTree) match(input, G_DOUBLE, FOLLOW_G_DOUBLE_in_atom1839);
                        G_DOUBLE91_tree = (CommonTree) adaptor.dupNode(G_DOUBLE91);

                        adaptor.addChild(root_1, G_DOUBLE91_tree);


                        match(input, Token.UP, null);
                        adaptor.addChild(root_0, root_1);
                        _last = _save_last_1;
                    }


                    String doubleStr = (G_DOUBLE91 != null ? G_DOUBLE91.getText() : null);
                    retval.value = new Atom<Double>(new Double(doubleStr.substring(0, doubleStr.length() - 1)));


                }
                break;
                case 5:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:569:9: gpath_statement
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    pushFollow(FOLLOW_gpath_statement_in_atom1893);
                    gpath_statement92 = gpath_statement();

                    state._fsp--;

                    adaptor.addChild(root_0, gpath_statement92.getTree());
                    retval.value = (gpath_statement92 != null ? gpath_statement92.value : null);

                }
                break;
                case 6:
                    // src/main/java/com/tinkerpop/gremlin/compiler/GremlinEvaluator.g:570:9: NULL
                {
                    root_0 = (CommonTree) adaptor.nil();

                    _last = (CommonTree) input.LT(1);
                    NULL93 = (CommonTree) match(input, NULL, FOLLOW_NULL_in_atom1949);
                    NULL93_tree = (CommonTree) adaptor.dupNode(NULL93);

                    adaptor.addChild(root_0, NULL93_tree);

                    retval.value = new Atom<Object>(null);

                }
                break;

            }
            retval.tree = (CommonTree) adaptor.rulePostProcessing(root_0);

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
        }
        return retval;
    }
    // $ANTLR end "atom"

    // Delegated rules


    public static final BitSet FOLLOW_statement_in_program70 = new BitSet(new long[]{0x00001047FC201892L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_NEWLINE_in_program74 = new BitSet(new long[]{0x00001047FC201892L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_if_statement_in_statement103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_foreach_statement_in_statement133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_while_statement_in_statement161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_repeat_statement_in_statement188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_native_step_definition_statement_in_statement214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_definition_statement_in_statement224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_include_statement_in_statement237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_script_statement_in_statement264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_in_statement291 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_atom_in_statement293 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_statement297 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_76_in_statement323 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_statement327 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_statement331 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_77_in_statement348 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_statement353 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_statement357 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_expression_in_statement373 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SCRIPT_in_script_statement419 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_StringLiteral_in_script_statement421 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INCLUDE_in_include_statement449 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_StringLiteral_in_include_statement451 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NATIVE_STEP_in_native_step_definition_statement488 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_native_step_definition_statement492 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_block_in_native_step_definition_statement494 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GPATH_in_gpath_statement550 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_step_in_gpath_statement553 = new BitSet(new long[]{0x0000000000002008L});
    public static final BitSet FOLLOW_STEP_in_step591 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_TOKEN_in_step594 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_token_in_step596 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PREDICATES_in_step600 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_PREDICATE_in_step605 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_step607 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_gremlin_native_path_definition_in_step621 = new BitSet(new long[]{0x0000000002000008L});
    public static final BitSet FOLLOW_NATIVE_in_gremlin_native_path_definition668 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_gremlin_native_path_definition671 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_function_call_in_token707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STR_in_token734 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_StringLiteral_in_token736 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VARIABLE_CALL_in_token752 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VARIABLE_in_token754 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PROPERTY_CALL_in_token765 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_PROPERTY_in_token767 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENTIFIER_in_token780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_token847 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOL_in_token860 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_BOOLEAN_in_token864 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_statement_in_token872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_if_statement894 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_COND_in_if_statement897 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_if_statement901 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_block_in_if_statement906 = new BitSet(new long[]{0x0000000000400008L});
    public static final BitSet FOLLOW_ELSE_in_if_statement911 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_if_statement915 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHILE_in_while_statement948 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_COND_in_while_statement951 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_while_statement955 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_block_in_while_statement958 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FOREACH_in_foreach_statement985 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VARIABLE_in_foreach_statement987 = new BitSet(new long[]{0x00001047FD201890L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_foreach_statement991 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_block_in_foreach_statement993 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_REPEAT_in_repeat_statement1021 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_repeat_statement1025 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_block_in_repeat_statement1027 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BLOCK_in_block1075 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_block1081 = new BitSet(new long[]{0x00001047FC301898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_RETURN_in_block1088 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_block1092 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_91_in_expression1135 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_expression1140 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_expression1144 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_92_in_expression1158 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_expression1162 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_expression1166 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_93_in_expression1180 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_expression1185 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_expression1189 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_95_in_expression1203 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_expression1208 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_expression1212 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_94_in_expression1226 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_expression1230 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_expression1234 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_96_in_expression1248 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_expression1252 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_expression1256 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_operation_in_expression1269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_operation1314 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_operation1318 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_operation1322 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_98_in_operation1336 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_operation1340 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_operation1344 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_binary_operation_in_operation1357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_binary_operation1394 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_binary_operation1400 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_binary_operation1404 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_100_in_binary_operation1419 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_binary_operation1423 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_binary_operation1427 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_101_in_binary_operation1440 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_binary_operation1444 = new BitSet(new long[]{0x00001047FC201898L, 0x0000003FF8003000L});
    public static final BitSet FOLLOW_statement_in_binary_operation1448 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_atom_in_binary_operation1462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNC_in_function_definition_statement1518 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_FUNC_NAME_in_function_definition_statement1521 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NS_in_function_definition_statement1524 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_function_definition_statement1528 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_function_definition_statement1532 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_function_definition_statement1536 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ARGS_in_function_definition_statement1541 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_in_function_definition_statement1546 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VARIABLE_in_function_definition_statement1548 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_block_in_function_definition_statement1557 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNC_CALL_in_function_call1594 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_FUNC_NAME_in_function_call1597 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_NS_in_function_call1600 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_function_call1604 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NAME_in_function_call1608 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENTIFIER_in_function_call1612 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ARGS_in_function_call1617 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARG_in_function_call1622 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_function_call1626 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INT_in_atom1663 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_G_INT_in_atom1665 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LONG_in_atom1723 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_G_LONG_in_atom1725 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FLOAT_in_atom1781 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_G_FLOAT_in_atom1783 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DOUBLE_in_atom1837 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_G_DOUBLE_in_atom1839 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_gpath_statement_in_atom1893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_atom1949 = new BitSet(new long[]{0x0000000000000002L});

}