package com.outskirtslabs.beancount.features.completion;

import static com.outskirtslabs.beancount.psi.BeancountTreeUtil.getGrandGrandParent;
import static com.outskirtslabs.beancount.psi.BeancountTreeUtil.getGrandParent;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.BeancountFile;
import com.outskirtslabs.beancount.psi.BeancountTreeUtil;

public class BeancountMainCompletionProvider extends CompletionProvider<CompletionParameters>
{
    private static Logger LOG = Logger.getInstance(BeancountMainCompletionProvider.class);
    private final BeancountAccountCompletionProvider accountProvider;

    public BeancountMainCompletionProvider(
        final BeancountAccountCompletionProvider accountCompletionProvider)
    {
        accountProvider = accountCompletionProvider;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
        ProcessingContext processingContext, @NotNull CompletionResultSet resultSet)
    {
        PsiElement position = parameters.getPosition();
        // debugTree(position);

        addPrefixAccountNameCompletions(position,
            (BeancountFile) position.getContainingFile(),
            resultSet);

    }

    private void debugElement(String label, PsiElement element)
    {
        if (element != null)
            LOG.info(
                label + ": " + element.getClass().getSimpleName() + " " + element.getText()
                    + " refs:"
                    + element.getReference());
    }

    private void debugTree(PsiElement position)
    {
        PsiElement parent = position.getParent();
        PsiElement grandp = getGrandParent(position);
        PsiElement ggrandp = getGrandGrandParent(position);

        debugElement("position", position);
        debugElement("parent", parent);
        debugElement("grandp", grandp);
        debugElement("ggrandp", ggrandp);
    }

    private void addPrefixAccountNameCompletions(PsiElement position, BeancountFile file,
        CompletionResultSet resultSet)
    {
        Optional<PsiElement> accountParentOpt = BeancountTreeUtil
            .findParent(position, element -> element instanceof BeancountAccount);

        if (accountParentOpt.isPresent())
        {
            BeancountAccount parent = (BeancountAccount) accountParentOpt.get();
            this.accountProvider
                .addCompletions(
                    file,
                    parent.getText(),
                    resultSet);
        }

    }
}
