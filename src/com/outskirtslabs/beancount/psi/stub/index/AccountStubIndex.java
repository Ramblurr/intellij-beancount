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
import com.outskirtslabs.beancount.psi.BeancountAccount;

public class AccountStubIndex extends StringStubIndexExtension<BeancountAccount>
{
    public static final int VERSION = 1;

    public AccountStubIndex()
    {}

    public static Collection<BeancountAccount> find(Project project, String accountName)
    {
        if (DumbService.isDumb(project))
        {
            // idea is indexing
            return Collections.emptyList();
        }

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        return StubIndex.getElements(
            BeancountAccountKeyIndex.KEY,
            accountName,
            project,
            scope,
            BeancountAccount.class
        );
    }

    @Override
    public int getVersion()
    {
        return super.getVersion() + VERSION;
    }

    @NotNull
    @Override
    public StubIndexKey<String, BeancountAccount> getKey()
    {
        return BeancountAccountKeyIndex.KEY;
    }

}
