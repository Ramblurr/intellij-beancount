package com.outskirtslabs.beancount.features.completion;

import static com.outskirtslabs.beancount.features.completion.BeancountCompletionHelper.addStringToResult;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.diagnostic.Logger;
import com.outskirtslabs.beancount.psi.BeancountFile;

public class BeancountAccountCompletionProvider
{
    private static Logger LOG = Logger.getInstance(BeancountAccountCompletionProvider.class);
    void addCompletions(BeancountFile file, String prefix, CompletionResultSet resultSet)
    {
        new AccountsCompleter(file)
            .getPrefixMatches(prefix)
            .forEach(s -> {
                LOG.info(" " + s);
                addStringToResult(s, resultSet);
            });
    }
}
