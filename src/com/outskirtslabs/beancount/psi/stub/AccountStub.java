package com.outskirtslabs.beancount.psi.stub;

import com.intellij.psi.stubs.StubElement;
import com.outskirtslabs.beancount.psi.BeancountAccount;

public interface AccountStub extends StubElement<BeancountAccount>
{
    String getName();
}
