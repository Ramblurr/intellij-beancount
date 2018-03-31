// This is a generated file. Not intended for manual editing.
package com.outskirtslabs.beancount.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.outskirtslabs.beancount.psi.BeancountTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.outskirtslabs.beancount.psi.*;

public class BeancountKeyValueValueImpl extends ASTWrapperPsiElement implements BeancountKeyValueValue {

  public BeancountKeyValueValueImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BeancountVisitor visitor) {
    visitor.visitKeyValueValue(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BeancountVisitor) accept((BeancountVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public BeancountAccount getAccount() {
    return PsiTreeUtil.getChildOfType(this, BeancountAccount.class);
  }

  @Override
  @Nullable
  public BeancountAmount getAmount() {
    return PsiTreeUtil.getChildOfType(this, BeancountAmount.class);
  }

  @Override
  @Nullable
  public BeancountCurrencySymbol getCurrencySymbol() {
    return PsiTreeUtil.getChildOfType(this, BeancountCurrencySymbol.class);
  }

  @Override
  @Nullable
  public BeancountExpr getExpr() {
    return PsiTreeUtil.getChildOfType(this, BeancountExpr.class);
  }

  @Override
  @Nullable
  public PsiElement getNumber() {
    return findChildByType(NUMBER);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findChildByType(STRING);
  }

}
