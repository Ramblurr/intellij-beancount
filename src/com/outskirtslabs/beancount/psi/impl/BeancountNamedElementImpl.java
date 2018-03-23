package com.outskirtslabs.beancount.psi.impl;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.ASTNode;
import com.outskirtslabs.beancount.psi.elements.BeancountNamedElement;

public abstract class BeancountNamedElementImpl extends BeancountPsiElement
    implements BeancountNamedElement
{
    public BeancountNamedElementImpl(@NotNull ASTNode node)
    {
        super(node);
    }
}
