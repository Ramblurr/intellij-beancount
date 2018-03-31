package com.outskirtslabs.beancount.psi.stub.index;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.outskirtslabs.beancount.psi.BeancountCurrencySymbol;

public class CurrencySymbolStubIndex extends StringStubIndexExtension<BeancountCurrencySymbol>
{
    public static final int VERSION = 2;

    public CurrencySymbolStubIndex()
    {}

    public static Collection<BeancountCurrencySymbol> find(Project project, String currencySymbol)
    {
        if (DumbService.isDumb(project))
        {
            // idea is indexing
            return Collections.emptyList();
        }

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        return StubIndex.getElements(
            BeancountCurrencySymbolKeyIndex.KEY,
            currencySymbol,
            project,
            scope,
            BeancountCurrencySymbol.class
        );
    }

    public static Collection<String> findAllCurrencySymbols(Project project)
    {
        if (DumbService.isDumb(project))
        {
            // idea is indexing
            return Collections.emptyList();
        }

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        return StubIndex.getInstance()
                        .getAllKeys(BeancountCurrencySymbolKeyIndex.KEY, project);
    }

    @Override
    public int getVersion()
    {
        return super.getVersion() + VERSION;
    }

    @NotNull
    @Override
    public StubIndexKey<String, BeancountCurrencySymbol> getKey()
    {
        return BeancountCurrencySymbolKeyIndex.KEY;
    }

}
