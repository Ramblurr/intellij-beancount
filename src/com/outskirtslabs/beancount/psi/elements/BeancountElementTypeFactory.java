package com.outskirtslabs.beancount.psi.elements;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.tree.IElementType;
import com.outskirtslabs.beancount.psi.stub.AccountStubElementType;
import com.outskirtslabs.beancount.psi.stub.CurrencyStubElementType;

public class BeancountElementTypeFactory
{
    @NotNull
    public static IElementType factoryAccount(@NotNull String name)
    {
        return new AccountStubElementType();
    }

    @NotNull
    public static IElementType factoryCurrency(@NotNull String name)
    {
        return new CurrencyStubElementType();
    }

    private BeancountElementTypeFactory() {}
}
