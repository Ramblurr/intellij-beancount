package com.outskirtslabs.beancount.psi.stub;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;

public interface BeancountStub<T extends PsiElement> extends StubElement<T>
{
    String getName();
}
