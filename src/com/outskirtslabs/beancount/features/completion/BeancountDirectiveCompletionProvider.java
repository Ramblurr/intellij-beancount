package com.outskirtslabs.beancount.features.completion;

import static com.outskirtslabs.beancount.features.completion.BeancountCompletionHelper.addStringToResult;

import java.util.Arrays;

import com.intellij.codeInsight.completion.CompletionResultSet;

public class BeancountDirectiveCompletionProvider
{
    private static final String[] KEYWORDS = new String[] {
        "option",
        "price",
        "balance",
        "event",
        "txn"
    };

    void addCompletions(CompletionResultSet resultSet)
    {
        Arrays.stream(KEYWORDS)
              .forEach(s -> addStringToResult(s, resultSet));
    }
}
