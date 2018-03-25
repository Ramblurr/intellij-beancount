package com.outskirtslabs.beancount.features.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.outskirtslabs.beancount.BeancountLanguage;
import com.outskirtslabs.beancount.psi.BeancountTypes;

public class BeancountCompletionContributor extends CompletionContributor
{

    public BeancountCompletionContributor()
    {
        extend(
            CompletionType.BASIC,
            PlatformPatterns
                .psiElement(BeancountTypes.ACCOUNT_WORD)
                .withLanguage(BeancountLanguage.INSTANCE),
            new BeancountAccountCompletionProvider()
        );
    }
}
