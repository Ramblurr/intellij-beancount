package com.outskirtslabs.beancount.features.completion;

import static com.outskirtslabs.beancount.psi.BeancountTreeUtil.debugTree;

import java.util.Optional;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.ProcessingContext;
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.BeancountFile;
import com.outskirtslabs.beancount.psi.BeancountTreeUtil;
import com.outskirtslabs.beancount.psi.BeancountTypes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeancountAccountCompletionProvider extends CompletionProvider<CompletionParameters>
{

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
        ProcessingContext processingContext, @NotNull CompletionResultSet resultSet)
    {
        PsiElement position = parameters.getPosition();
        debugTree(position);

        addPrefixAccountNameCompletions(position,
            (BeancountFile) position.getContainingFile(),
            resultSet);

    }


    private void addPrefixAccountNameCompletions(PsiElement position, BeancountFile file,
        CompletionResultSet resultSet)
    {

        // case one: completion in a valid/existing line
        Optional<PsiElement> accountParentOpt = BeancountTreeUtil
            .findParent(position, element -> element instanceof BeancountAccount);
        if (accountParentOpt.isPresent())
        {
            BeancountAccount parent = (BeancountAccount) accountParentOpt.get();
            addCompletions(
                file,
                cleanAccountInput(parent.getText()),
                resultSet);
            return;
        }

        // case two: completion in a new/invalid posting line
        TokenSet ACCOUNT_PRIMITIVES = TokenSet
            .create(BeancountTypes.COLON, BeancountTypes.ACCOUNT_WORD);
        if (position.getParent() instanceof BeancountFile)
        {
            // this is the case where the posting is still invalid
            String accountSoFar = BeancountTreeUtil.getPreviousSiblingsWhile(position,
                sibling -> ACCOUNT_PRIMITIVES.contains(sibling.getNode().getElementType()))
                                                   .map(PsiElement::getText)
                                                   .collect(Collectors.joining(""));

            String input = cleanAccountInput(accountSoFar + position.getText());
            addCompletions(
                file,
                input,
                resultSet.withPrefixMatcher(input));
            return;
        }

    }

    private static String DUMMY_IDENT = "IntellijIdeaRulezzz";

    private String cleanAccountInput(String partialAccount)
    {
        if (partialAccount.endsWith(DUMMY_IDENT))
            partialAccount = partialAccount
                .substring(0, partialAccount.indexOf(DUMMY_IDENT));
        return partialAccount;
    }

    void addCompletions(BeancountFile file, String prefix, CompletionResultSet resultSet)
    {
        new AccountsCompleter(file)
            .getPrefixMatches(prefix)
            .forEach(s -> {
                log.info(" " + s);
                resultSet
                    .addElement(LookupElementBuilder.create(s));
            });

        log.info("prefix matcher: {}", resultSet.getPrefixMatcher().getPrefix());
    }
}
