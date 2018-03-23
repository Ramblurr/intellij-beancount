package com.outskirtslabs.beancount.psi.impl;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.outskirtslabs.beancount.psi.elements.BeancountAccountElement;
import com.outskirtslabs.beancount.psi.stub.AccountStub;

public abstract class BeancountAccountElementImpl extends StubBasedPsiElementBase<AccountStub>
    implements BeancountAccountElement
{
    public BeancountAccountElementImpl(@NotNull final AccountStub stub,
        @NotNull final IStubElementType nodeType)
    {
        super(stub, nodeType);
    }

    public BeancountAccountElementImpl(@NotNull final ASTNode node)
    {
        super(node);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + this.getElementType().toString() + ")";
    }
}
