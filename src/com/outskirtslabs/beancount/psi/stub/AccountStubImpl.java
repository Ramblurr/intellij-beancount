package com.outskirtslabs.beancount.psi.stub;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.BeancountTypes;

public class AccountStubImpl extends NamedStubBase<BeancountAccount> implements AccountStub
{
    private final String accountName;

    public AccountStubImpl(@NotNull final StubElement parent, @NotNull String accountName)
    {
        super(parent, (IStubElementType) BeancountTypes.ACCOUNT, accountName);
        this.accountName = accountName;
    }

    @Override
    public String getName()
    {
        return accountName;
    }
}
