package com.outskirtslabs.beancount.psi;

import com.intellij.psi.PsiElement;

public class BeancountRecursiveVisitor extends BeancountVisitor
{
    @Override
    public void visitElement(PsiElement element)
    {
        super.visitElement(element);
        element.acceptChildren(this);
    }
}
