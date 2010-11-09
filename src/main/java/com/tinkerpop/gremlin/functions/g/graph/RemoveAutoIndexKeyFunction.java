package com.tinkerpop.gremlin.functions.g.graph;

import com.tinkerpop.blueprints.pgm.AutomaticIndex;
import com.tinkerpop.blueprints.pgm.Index;
import com.tinkerpop.blueprints.pgm.IndexableGraph;
import com.tinkerpop.gremlin.compiler.context.GremlinScriptContext;
import com.tinkerpop.gremlin.compiler.operations.Operation;
import com.tinkerpop.gremlin.compiler.types.Atom;
import com.tinkerpop.gremlin.functions.AbstractFunction;
import com.tinkerpop.gremlin.functions.FunctionHelper;

import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RemoveAutoIndexKeyFunction extends AbstractFunction<Object> {

    private static final String FUNCTION_NAME = "remove-aidx-key";

    public Atom<Object> compute(final List<Operation> arguments, final GremlinScriptContext context) throws RuntimeException {
        final int size = arguments.size();
        if (size != 2 && size != 3)
            throw new RuntimeException(this.createUnsupportedArgumentMessage());

        final IndexableGraph graph = FunctionHelper.getIndexableGraph(arguments, 0, context);
        String indexName;
        String indexKey;
        if (size == 2) {
            indexName = (String) arguments.get(0).compute().getValue();
            indexKey = (String) arguments.get(1).compute().getValue();
        } else {
            indexName = (String) arguments.get(1).compute().getValue();
            indexKey = (String) arguments.get(2).compute().getValue();
        }

        AutomaticIndex autoIndex = null;
        for (Index index : graph.getIndices()) {
            if (index.getIndexName().equals(indexName) && index instanceof AutomaticIndex)
                autoIndex = (AutomaticIndex) index;
        }
        if (null == autoIndex)
            throw new RuntimeException("No automatic index named " + indexName + " found");

        autoIndex.removeAutoIndexKey(indexKey);
        return new Atom<Object>(null);
    }

    public String getFunctionName() {
        return FUNCTION_NAME;
    }
}