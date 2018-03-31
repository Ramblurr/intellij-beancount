package com.outskirtslabs.beancount.psi.elements;

import com.intellij.psi.StubBasedPsiElement;
import com.outskirtslabs.beancount.psi.stub.CurrencySymbolStub;

public interface BeancountCurrencyElement
    extends BeancountNamedElement, StubBasedPsiElement<CurrencySymbolStub>
{
}
