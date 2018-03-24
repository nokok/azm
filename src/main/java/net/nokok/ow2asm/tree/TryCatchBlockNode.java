// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package net.nokok.ow2asm.tree;

import net.nokok.ow2asm.MethodVisitor;

import java.util.List;

/**
 * A node that represents a try catch block.
 *
 * @author Eric Bruneton
 */
public class TryCatchBlockNode {

    private LabelNode start;

    private LabelNode end;

    private LabelNode handler;

    private String type;

    private List<TypeAnnotationNode> visibleTypeAnnotations;

    private List<TypeAnnotationNode> invisibleTypeAnnotations;

    /**
     * Constructs a new {@link TryCatchBlockNode}.
     *
     * @param start   the beginning of the exception handler's scope (inclusive).
     * @param end     the end of the exception handler's scope (exclusive).
     * @param handler the beginning of the exception handler's code.
     * @param type    the internal name of the type of exceptions handled by the handler, or
     *                <tt>null</tt> to catch any exceptions (for "finally" blocks).
     */
    public TryCatchBlockNode(
            final LabelNode start, final LabelNode end, final LabelNode handler, final String type) {
        this.setStart(start);
        this.setEnd(end);
        this.setHandler(handler);
        this.setType(type);
    }

    /**
     * Updates the index of this try catch block in the method's list of try catch block nodes. This
     * index maybe stored in the 'target' field of the type annotations of this block.
     *
     * @param index the new index of this try catch block in the method's list of try catch block
     *              nodes.
     */
    public void updateIndex(final int index) {
        int newTypeRef = 0x42000000 | (index << 8);
        if (getVisibleTypeAnnotations() != null) {
            for (int i = 0, n = getVisibleTypeAnnotations().size(); i < n; ++i) {
                getVisibleTypeAnnotations().get(i).setTypeRef(newTypeRef);
            }
        }
        if (getInvisibleTypeAnnotations() != null) {
            for (int i = 0, n = getInvisibleTypeAnnotations().size(); i < n; ++i) {
                getInvisibleTypeAnnotations().get(i).setTypeRef(newTypeRef);
            }
        }
    }

    /**
     * Makes the given visitor visit this try catch block.
     *
     * @param methodVisitor a method visitor.
     */
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitTryCatchBlock(
                getStart().getLabel(), getEnd().getLabel(), getHandler() == null ? null : getHandler().getLabel(), getType());
        if (getVisibleTypeAnnotations() != null) {
            for (int i = 0, n = getVisibleTypeAnnotations().size(); i < n; ++i) {
                TypeAnnotationNode typeAnnotation = getVisibleTypeAnnotations().get(i);
                typeAnnotation.accept(
                        methodVisitor.visitTryCatchAnnotation(
                                typeAnnotation.getTypeRef(), typeAnnotation.getTypePath(), typeAnnotation.getDesc(), true));
            }
        }
        if (getInvisibleTypeAnnotations() != null) {
            for (int i = 0, n = getInvisibleTypeAnnotations().size(); i < n; ++i) {
                TypeAnnotationNode typeAnnotation = getInvisibleTypeAnnotations().get(i);
                typeAnnotation.accept(
                        methodVisitor.visitTryCatchAnnotation(
                                typeAnnotation.getTypeRef(), typeAnnotation.getTypePath(), typeAnnotation.getDesc(), false));
            }
        }
    }

    /**
     * The end of the exception handler's scope (exclusive).
     */
    public LabelNode getEnd() {
        return end;
    }

    public void setEnd(LabelNode end) {
        this.end = end;
    }

    /**
     * The beginning of the exception handler's code.
     */
    public LabelNode getHandler() {
        return handler;
    }

    public void setHandler(LabelNode handler) {
        this.handler = handler;
    }

    /**
     * The internal name of the type of exceptions handled by the handler. May be <tt>null</tt> to
     * catch any exceptions (for "finally" blocks).
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * The runtime visible type annotations on the exception handler type. May be <tt>null</tt>.
     */
    public List<TypeAnnotationNode> getVisibleTypeAnnotations() {
        return visibleTypeAnnotations;
    }

    public void setVisibleTypeAnnotations(List<TypeAnnotationNode> visibleTypeAnnotations) {
        this.visibleTypeAnnotations = visibleTypeAnnotations;
    }

    /**
     * The runtime invisible type annotations on the exception handler type. May be <tt>null</tt>.
     */
    public List<TypeAnnotationNode> getInvisibleTypeAnnotations() {
        return invisibleTypeAnnotations;
    }

    public void setInvisibleTypeAnnotations(List<TypeAnnotationNode> invisibleTypeAnnotations) {
        this.invisibleTypeAnnotations = invisibleTypeAnnotations;
    }

    /**
     * The beginning of the exception handler's scope (inclusive).
     */
    public LabelNode getStart() {
        return start;
    }

    public void setStart(LabelNode start) {
        this.start = start;
    }
}
