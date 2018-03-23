package com.outskirtslabs.beancount.features.references;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.outskirtslabs.beancount.parser.BeancountLexer;
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.BeancountTypes;

public class BeancountFindUsagesProvider implements FindUsagesProvider
{
    private static Logger LOG = Logger.getInstance(BeancountFindUsagesProvider.class);

    @Nullable
    @Override
    public WordsScanner getWordsScanner()
    {
        return new DefaultWordsScanner(new BeancountLexer(),
            TokenSet.create(BeancountTypes.ACCOUNT, BeancountTypes.ACCOUNT_WORD),
            TokenSet.create(BeancountTypes.COMMENT),
            TokenSet.create(BeancountTypes.STRING));
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement)
    {
        return psiElement instanceof BeancountAccount;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement)
    {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element)
    {
        if (element instanceof BeancountAccount)
        {
            return "account";
        } else
        {
            LOG.info("getType: " + element.getClass());
            return "";
        }
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element)
    {
        if (element instanceof BeancountAccount)
        {
            return ((BeancountAccount) element).getName();
        } else
        {
            LOG.info("getDEscName: " + element.getClass());
            return "";
        }
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName)
    {
        if (element instanceof BeancountAccount)
        {
            return ((BeancountAccount) element).getName();
        } else
        {
            LOG.info("getNodeT: " + element.getClass());
            return "";
        }
    }
}

