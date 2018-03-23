package com.outskirtslabs.beancount.psi.elements;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.tree.IElementType;
import com.outskirtslabs.beancount.psi.stub.AccountStubElementType;

public class BeancountElementTypeFactory
{
    @NotNull
    public static IElementType factory(@NotNull String name)
    {
        return new AccountStubElementType();
    }

    private BeancountElementTypeFactory() {}
}
