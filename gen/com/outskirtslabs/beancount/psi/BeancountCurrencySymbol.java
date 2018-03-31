// This is a generated file. Not intended for manual editing.
package com.outskirtslabs.beancount.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.outskirtslabs.beancount.psi.elements.BeancountCurrencyElement;
import com.intellij.psi.StubBasedPsiElement;
import com.outskirtslabs.beancount.psi.stub.CurrencySymbolStub;
import com.intellij.psi.PsiReference;

public interface BeancountCurrencySymbol extends BeancountCurrencyElement, StubBasedPsiElement<CurrencySymbolStub> {

  String getName();

  PsiElement setName(String newName);

  PsiElement getNameIdentifier();

  PsiReference getReference();

}
