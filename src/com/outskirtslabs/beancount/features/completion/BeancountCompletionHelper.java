package com.outskirtslabs.beancount.features.completion;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

public class BeancountCompletionHelper
{

    static void addStringToResult(String string, CompletionResultSet resultSet)
    {
        resultSet.addElement(LookupElementBuilder.create(string));
    }

    /*
    static void addPsiElementToResult(PsiElement element, CompletionResultSet resultSet)
    {
        addStringToResult(element.getText(), resultSet);
    }

    static <T> void forEachUntilNonPresent(Stream<Optional<T>> stream, Consumer<T> consumer)
    {
        stream.peek(e -> e.ifPresent(consumer::consume))
              .filter(e -> !e.isPresent())
              .findFirst();
    }
    */
}
