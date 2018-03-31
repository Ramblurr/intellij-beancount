// This is a generated file. Not intended for manual editing.
package com.outskirtslabs.beancount.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.outskirtslabs.beancount.psi.BeancountTypes.*;
import com.outskirtslabs.beancount.psi.*;
import com.intellij.psi.PsiReference;
import com.outskirtslabs.beancount.psi.stub.CurrencySymbolStub;
import com.intellij.psi.stubs.IStubElementType;

public class BeancountCurrencySymbolImpl extends BeancountCurrencyElementImpl implements BeancountCurrencySymbol {

  public BeancountCurrencySymbolImpl(CurrencySymbolStub stub, IStubElementType type) {
    super(stub, type);
  }

  public BeancountCurrencySymbolImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BeancountVisitor visitor) {
    visitor.visitCurrencySymbol(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BeancountVisitor) accept((BeancountVisitor)visitor);
    else super.accept(visitor);
  }

  public String getName() {
    return BeancountPsiImplUtil.getName(this);
  }

  public PsiElement setName(String newName) {
    return BeancountPsiImplUtil.setName(this, newName);
  }

  public PsiElement getNameIdentifier() {
    return BeancountPsiImplUtil.getNameIdentifier(this);
  }

  public PsiReference getReference() {
    return BeancountPsiImplUtil.getReference(this);
  }

}
