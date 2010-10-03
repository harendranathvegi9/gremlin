package com.tinkerpop.gremlin.steps;

import com.tinkerpop.gremlin.compiler.util.CodeBlock;
import com.tinkerpop.pipes.AbstractPipe;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Pavel A. Yaskevich
 */
public class NativePipe extends AbstractPipe<Object, Object> {

    private final CodeBlock block;
    private boolean blockEvaluated = false;
    private Iterator<Object> tempIterator;

    public NativePipe(final CodeBlock block) {
        this.block = block;
    }

    public Object processNextStart() {
        while (true) {
            if (this.blockEvaluated && null != this.tempIterator && this.tempIterator.hasNext())
                return this.tempIterator.next();
            else if (!this.blockEvaluated) {
                Object object = this.block.invoke();
                this.blockEvaluated = true;
                if (object instanceof Iterator)
                    this.tempIterator = (Iterator<Object>) object;
                else if (object instanceof Iterable)
                    this.tempIterator = ((Iterable<Object>) object).iterator();
                else
                    return object;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
