package com.outskirtslabs.beancount.features.completion;

import static com.intellij.lang.parser.GeneratedParserUtilBase.DUMMY_BLOCK;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.ERROR_ELEMENT;
import static com.outskirtslabs.beancount.features.completion.BeancountCompletionHelper.addStringToResult;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.outskirtslabs.beancount.psi.BeancountTreeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeancountDirectiveCompletionProvider extends CompletionProvider<CompletionParameters>
{
    private static final List<String> KEYWORDS = Lists.newArrayList(
        "*",
        "balance",
        "custom",
        "document",
        "event",
        "note",
        "open",
        "pad",
        "price",
        "query"
    );

    @Override
    protected void addCompletions(@NotNull final CompletionParameters parameters,
        final ProcessingContext context, @NotNull final CompletionResultSet resultSet)
    {

        /* so wtf is going on here?
           well, when the user has a line like this
                2016-01-01 <cursor>
           we want to suggest all the possible directive keywords.
           but when a user has a line like this
                2016-01-01 X<cursor>
           we want to suggest all the possible directives that begin with X
           same with this one, but for Xyz
                2016-01-01 Xyz<cursor>

           however the parser is not happy with both of these lines, so dummy characters, ERROR_ELEMENTs
           abound and the tree is a bit messy.

           now in the 2nd case above, the 'X' element will actually be a PsiErrorElement with a 'X' BAD_CHARACTER child
           and the remaining text will be sibling of the error element and NOT a sibling of the BAD_CHARACTER

           effectively:

           DATE ERROR_ELEMENT         DUMMY_BLOCK
                | - BAD_CHARACTER(X)  | - BAD_CHARACTER(y) BAD_CHARACTER(z)<cursor>

           We want to extract the prefix: Xyz. That's what this code does.
         */

        String initialLetter = parameters.getPosition()
                                         .getParent() // DUMMY_BLOCK
                                         .getPrevSibling() // ERROR_ELEMENT
                                         .getText();

        String remaining = BeancountTreeUtil
            .getPreviousSiblingsWhile(parameters.getPosition(),
                sibling -> psiElement(BAD_CHARACTER).accepts(sibling) || psiElement(ERROR_ELEMENT)
                    .accepts(sibling) || psiElement(DUMMY_BLOCK).accepts(sibling))
            .map(PsiElement::getText)
            .collect(Collectors.joining(""));

        String dirSoFar = initialLetter + remaining;

        log.info("prefix is {} ", dirSoFar);
        KEYWORDS.forEach(s -> addStringToResult(s, resultSet.withPrefixMatcher(dirSoFar)));

    }
}
