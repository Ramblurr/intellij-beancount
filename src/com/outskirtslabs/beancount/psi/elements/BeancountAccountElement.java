package com.outskirtslabs.beancount.psi.elements;

import com.intellij.psi.StubBasedPsiElement;
import com.outskirtslabs.beancount.psi.stub.AccountStub;

public interface BeancountAccountElement
    extends BeancountNamedElement, StubBasedPsiElement<AccountStub>
{
}
