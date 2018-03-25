package com.outskirtslabs.beancount.features.completion;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.psi.TokenType.ERROR_ELEMENT;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.outskirtslabs.beancount.psi.BeancountTypes.ACCOUNT_WORD;
import static com.outskirtslabs.beancount.psi.BeancountTypes.DATE;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.outskirtslabs.beancount.BeancountLanguage;

public class BeancountCompletionContributor extends CompletionContributor
{
    public BeancountCompletionContributor()
    {
        extend(
            CompletionType.BASIC,
            psiElement(ACCOUNT_WORD)
                .withLanguage(BeancountLanguage.INSTANCE),
            new BeancountAccountCompletionProvider()
        );
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(psiElement(ERROR_ELEMENT)
                    .afterSibling(psiElement(WHITE_SPACE)
                        .afterSibling(psiElement(DATE))))
            , new BeancountDirectiveCompletionProvider()
        );
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(psiElement()
                    .afterSibling(psiElement(ERROR_ELEMENT)
                        .afterSibling(psiElement(WHITE_SPACE)
                            .afterSibling(psiElement(DATE)))))
            , new BeancountDirectiveCompletionProvider()
        );
    }
}
