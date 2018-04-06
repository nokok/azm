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
package net.nokok.azm.tree;

import net.nokok.azm.ClassVisitor;
import net.nokok.azm.Type;

import java.util.Objects;

/**
 * A node that represents an inner class.
 *
 * @author Eric Bruneton
 */
public class InnerClassNode {

    private String name;

    private String outerName;

    private String innerName;

    private int access;

    /**
     * Constructs a new {@link InnerClassNode}.
     *
     * @param name      the internal name of an inner class (see {@link
     *                  Type#getInternalName()}).
     * @param outerName the internal name of the class to which the inner class belongs (see {@link
     *                  Type#getInternalName()}). May be <tt>null</tt>.
     * @param innerName the (simple) name of the inner class inside its enclosing class. May be
     *                  <tt>null</tt> for anonymous inner classes.
     * @param access    the access flags of the inner class as originally declared in the enclosing
     *                  class.
     */
    public InnerClassNode(
            final String name, final String outerName, final String innerName, final int access) {
        this.setName(name);
        this.setOuterName(outerName);
        this.setInnerName(innerName);
        this.setAccess(access);
    }

    /**
     * Makes the given class visitor visit this inner class.
     *
     * @param classVisitor a class visitor.
     */
    public void accept(final ClassVisitor classVisitor) {
        classVisitor.visitInnerClass(getName(), getOuterName(), getInnerName(), getAccess());
    }

    /**
     * The internal name of an inner class (see {@link Type#getInternalName()}).
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The internal name of the class to which the inner class belongs (see {@link
     * Type#getInternalName()}). May be <tt>null</tt>.
     */
    public String getOuterName() {
        return outerName;
    }

    public void setOuterName(String outerName) {
        this.outerName = outerName;
    }

    /**
     * The (simple) name of the inner class inside its enclosing class. May be <tt>null</tt> for
     * anonymous inner classes.
     */
    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    /**
     * The access flags of the inner class as originally declared in the enclosing class.
     */
    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerClassNode that = (InnerClassNode) o;
        return getAccess() == that.getAccess() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getOuterName(), that.getOuterName()) &&
                Objects.equals(getInnerName(), that.getInnerName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getOuterName(), getInnerName(), getAccess());
    }

    @Override
    public String toString() {
        return "InnerClassNode{" +
                "name='" + name + '\'' +
                ", outerName='" + outerName + '\'' +
                ", innerName='" + innerName + '\'' +
                ", access=" + access +
                '}';
    }
}
