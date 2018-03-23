package com.outskirtslabs.beancount.features.references;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.outskirtslabs.beancount.psi.BeancountAccount;

public class BeancountRefactoringSupportProvider extends RefactoringSupportProvider
{
    private static Logger LOG = Logger.getInstance(BeancountRefactoringSupportProvider.class);

    @Override
    public boolean isMemberInplaceRenameAvailable(PsiElement element, PsiElement context)
    {
        return element instanceof BeancountAccount;
    }
}
