package com.outskirtslabs.beancount.psi.reference;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.outskirtslabs.beancount.psi.BeancountCommodityDir;
import com.outskirtslabs.beancount.psi.BeancountCurrencySymbol;
import com.outskirtslabs.beancount.psi.BeancountTreeUtil;
import com.outskirtslabs.beancount.psi.elements.BeancountNamedElement;
import com.outskirtslabs.beancount.psi.stub.index.CurrencySymbolStubIndex;

public class BeancountCurrencySymbolReference extends BeancountReference<BeancountCurrencySymbol>
{
    public BeancountCurrencySymbolReference(final BeancountCurrencySymbol element,
        final TextRange range)
    {
        super(element, range);
    }

    @Override
    Collection<BeancountCurrencySymbol> getFromCache(final Project project,
        final String currencySymbol)
    {
        return CurrencySymbolStubIndex.find(project, currencySymbol);
    }

    @Override
    protected boolean isElementToResolve(final BeancountCurrencySymbol elementToSearch,
        final BeancountNamedElement maybeElementDeclaration)
    {
        if (maybeElementDeclaration instanceof BeancountCurrencySymbol)
        {
            return isCurrencySymbolDeclaration(elementToSearch,
                (BeancountCurrencySymbol) maybeElementDeclaration);
        }
        return false;
    }

    /**
     * Is maybeCurrencySymbolDeclaration the instance that declares the elementText in symbolToSearch?
     *
     * @param symbolToSearch
     * @param maybeCurrencySymbolDeclaration
     * @return
     */
    private static boolean isCurrencySymbolDeclaration(BeancountCurrencySymbol symbolToSearch,
        BeancountCurrencySymbol maybeCurrencySymbolDeclaration)
    {

        if (StringUtils.equals(maybeCurrencySymbolDeclaration.getName(), symbolToSearch.getName()))
        {
            Optional<PsiElement> maybeCommodityDirective = BeancountTreeUtil
                .findParent(maybeCurrencySymbolDeclaration,
                    parent -> parent instanceof BeancountCommodityDir);

            return maybeCommodityDirective.isPresent();
        }
        return false;
    }
}
