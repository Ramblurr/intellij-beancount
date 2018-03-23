package com.outskirtslabs.beancount.psi.stub.index;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.outskirtslabs.beancount.psi.BeancountAccount;

public class BeancountAccountKeyIndex extends StringStubIndexExtension<BeancountAccount>
{
    public static final StubIndexKey<String, BeancountAccount> KEY = StubIndexKey
        .createIndexKey("beancount.account.index");

    @NotNull
    @Override
    public StubIndexKey<String, BeancountAccount> getKey()
    {
        return KEY;
    }
}
