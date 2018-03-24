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

import net.nokok.ow2asm.ClassVisitor;
import net.nokok.ow2asm.ModuleVisitor;
import net.nokok.ow2asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * A node that represents a module declaration.
 *
 * @author Remi Forax
 */
public class ModuleNode extends ModuleVisitor {

    private String name;

    private int access;

    private String version;

    private String mainClass;

    private List<String> packages;

    private List<ModuleRequireNode> requires;

    private List<ModuleExportNode> exports;

    private List<ModuleOpenNode> opens;

    private List<String> uses;

    private List<ModuleProvideNode> provides;

    /**
     * Constructs a {@link ModuleNode}. <i>Subclasses must not use this constructor</i>. Instead, they
     * must use the {@link #ModuleNode(int, String, int, String, List, List, List, List, List)} version.
     *
     * @param name    the fully qualified name (using dots) of the module.
     * @param access  the module access flags, among {@code ACC_OPEN}, {@code ACC_SYNTHETIC} and {@code
     *                ACC_MANDATED}.
     * @param version the module version, or <tt>null</tt>.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public ModuleNode(final String name, final int access, final String version) {
        super(Opcodes.ASM6);
        if (getClass() != ModuleNode.class) {
            throw new IllegalStateException();
        }
        this.setName(name);
        this.setAccess(checkAccess(access));
        this.setVersion(version);
    }

    private static int checkAccess(int access) {
        switch (access) {
        case Opcodes.ACC_OPEN:
        case Opcodes.ACC_SYNTHETIC:
        case Opcodes.ACC_MANDATED:
            return access;
        default:
            throw new IllegalArgumentException();
        }
    }

    // TODO(forax): why is there no 'mainClass' and 'packages' parameters in this constructor?

    /**
     * Constructs a {@link ModuleNode}.
     *
     * @param api      the ASM API version implemented by this visitor. Must be {@link Opcodes#ASM6}.
     * @param name     the fully qualified name (using dots) of the module.
     * @param access   the module access flags, among {@code ACC_OPEN}, {@code ACC_SYNTHETIC} and {@code
     *                 ACC_MANDATED}.
     * @param version  the module version, or <tt>null</tt>.
     * @param requires The dependencies of this module. May be <tt>null</tt>.
     * @param exports  The packages exported by this module. May be <tt>null</tt>.
     * @param opens    The packages opened by this module. May be <tt>null</tt>.
     * @param uses     The internal names of the services used by this module. May be <tt>null</tt>.
     * @param provides The services provided by this module. May be <tt>null</tt>.
     */
    public ModuleNode(
            final int api,
            final String name,
            final int access,
            final String version,
            final List<ModuleRequireNode> requires,
            final List<ModuleExportNode> exports,
            final List<ModuleOpenNode> opens,
            final List<String> uses,
            final List<ModuleProvideNode> provides) {
        super(api);
        this.setName(name);
        this.setAccess(checkAccess(access));
        this.setVersion(version);
        this.setRequires(requires);
        this.setExports(exports);
        this.setOpens(opens);
        this.setUses(uses);
        this.setProvides(provides);
    }

    @Override
    public void visitMainClass(final String mainClass) {
        this.setMainClass(mainClass);
    }

    @Override
    public void visitPackage(final String packaze) {
        if (getPackages() == null) {
            setPackages(new ArrayList<String>(5));
        }
        getPackages().add(packaze);
    }

    @Override
    public void visitRequire(final String module, final int access, final String version) {
        if (getRequires() == null) {
            setRequires(new ArrayList<ModuleRequireNode>(5));
        }
        getRequires().add(new ModuleRequireNode(module, access, version));
    }

    @Override
    public void visitExport(final String packaze, final int access, final String... modules) {
        if (getExports() == null) {
            setExports(new ArrayList<ModuleExportNode>(5));
        }
        getExports().add(new ModuleExportNode(packaze, access, Util.asArrayList(modules)));
    }

    @Override
    public void visitOpen(final String packaze, final int access, final String... modules) {
        if (getOpens() == null) {
            setOpens(new ArrayList<ModuleOpenNode>(5));
        }
        getOpens().add(new ModuleOpenNode(packaze, access, Util.asArrayList(modules)));
    }

    @Override
    public void visitUse(final String service) {
        if (getUses() == null) {
            setUses(new ArrayList<String>(5));
        }
        getUses().add(service);
    }

    @Override
    public void visitProvide(final String service, final String... providers) {
        if (getProvides() == null) {
            setProvides(new ArrayList<ModuleProvideNode>(5));
        }
        getProvides().add(new ModuleProvideNode(service, Util.asArrayList(providers)));
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    public void accept(final ClassVisitor classVisitor) {
        ModuleVisitor moduleVisitor = classVisitor.visitModule(getName(), getAccess(), getVersion());
        if (moduleVisitor == null) {
            return;
        }
        if (getMainClass() != null) {
            moduleVisitor.visitMainClass(getMainClass());
        }
        if (getPackages() != null) {
            for (int i = 0, n = getPackages().size(); i < n; i++) {
                moduleVisitor.visitPackage(getPackages().get(i));
            }
        }
        if (getRequires() != null) {
            for (int i = 0, n = getRequires().size(); i < n; i++) {
                getRequires().get(i).accept(moduleVisitor);
            }
        }
        if (getExports() != null) {
            for (int i = 0, n = getExports().size(); i < n; i++) {
                getExports().get(i).accept(moduleVisitor);
            }
        }
        if (getOpens() != null) {
            for (int i = 0, n = getOpens().size(); i < n; i++) {
                getOpens().get(i).accept(moduleVisitor);
            }
        }
        if (getUses() != null) {
            for (int i = 0, n = getUses().size(); i < n; i++) {
                moduleVisitor.visitUse(getUses().get(i));
            }
        }
        if (getProvides() != null) {
            for (int i = 0, n = getProvides().size(); i < n; i++) {
                getProvides().get(i).accept(moduleVisitor);
            }
        }
    }

    /**
     * The fully qualified name (using dots) of this module.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The module's access flags, among {@code ACC_OPEN}, {@code ACC_SYNTHETIC} and {@code
     * ACC_MANDATED}.
     */
    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    /**
     * The version of this module. May be <tt>null</tt>.
     */
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * The internal name of the main class of this module. May be <tt>null</tt>.
     */
    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    /**
     * The internal name of the packages declared by this module. May be <tt>null</tt>.
     */
    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    /**
     * The dependencies of this module. May be <tt>null</tt>.
     */
    public List<ModuleRequireNode> getRequires() {
        return requires;
    }

    public void setRequires(List<ModuleRequireNode> requires) {
        this.requires = requires;
    }

    /**
     * The packages exported by this module. May be <tt>null</tt>.
     */
    public List<ModuleExportNode> getExports() {
        return exports;
    }

    public void setExports(List<ModuleExportNode> exports) {
        this.exports = exports;
    }

    /**
     * The packages opened by this module. May be <tt>null</tt>.
     */
    public List<ModuleOpenNode> getOpens() {
        return opens;
    }

    public void setOpens(List<ModuleOpenNode> opens) {
        this.opens = opens;
    }

    /**
     * The internal names of the services used by this module. May be <tt>null</tt>.
     */
    public List<String> getUses() {
        return uses;
    }

    public void setUses(List<String> uses) {
        this.uses = uses;
    }

    /**
     * The services provided by this module. May be <tt>null</tt>.
     */
    public List<ModuleProvideNode> getProvides() {
        return provides;
    }

    public void setProvides(List<ModuleProvideNode> provides) {
        this.provides = provides;
    }
}
