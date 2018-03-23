package com.outskirtslabs.beancount.psi.stub;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.outskirtslabs.beancount.BeancountLanguage;
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.impl.BeancountAccountImpl;
import com.outskirtslabs.beancount.psi.stub.index.BeancountAccountKeyIndex;

public class AccountStubElementType extends IStubElementType<AccountStub, BeancountAccount>
{
    public AccountStubElementType()
    {
        super("ACCOUNT", BeancountLanguage.INSTANCE);
    }

    public BeancountAccount createPsi(@NotNull final AccountStub stub)
    {
        return new BeancountAccountImpl(stub, this);
    }

    @NotNull
    public AccountStub createStub(@NotNull final BeancountAccount psi, final StubElement parentStub)
    {
        return new AccountStubImpl(parentStub, psi.getName());
    }

    @NotNull
    public String getExternalId()
    {
        return "Beancount.Account";
    }

    public void serialize(@NotNull final AccountStub stub,
        @NotNull final StubOutputStream dataStream) throws
        IOException
    {
        dataStream.writeName(stub.getName());
    }

    @NotNull
    public AccountStub deserialize(@NotNull final StubInputStream dataStream,
        final StubElement parentStub) throws IOException
    {
        final StringRef ref = dataStream.readName();
        return new AccountStubImpl(parentStub, ref.getString());
    }

    public void indexStub(@NotNull final AccountStub stub, @NotNull final IndexSink sink)
    {
        sink.occurrence(BeancountAccountKeyIndex.KEY, stub.getName());
    }

}
