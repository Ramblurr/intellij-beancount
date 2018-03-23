package com.outskirtslabs.beancount.features.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.outskirtslabs.beancount.BeancountLanguage;

public class BeancountCompletionContributor extends CompletionContributor
{

    public BeancountCompletionContributor()
    {
        extend(
            CompletionType.BASIC,
            PlatformPatterns
                .psiElement()
                .withLanguage(BeancountLanguage.INSTANCE),
            getProvider()
        );
    }

    private static CompletionProvider<CompletionParameters> getProvider()
    {
        return new BeancountMainCompletionProvider(
            new BeancountAccountCompletionProvider()
        );
    }
}
