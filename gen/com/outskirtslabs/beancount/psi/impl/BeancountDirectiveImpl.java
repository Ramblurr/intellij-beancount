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

public class BeancountDirectiveImpl extends ASTWrapperPsiElement implements BeancountDirective {

  public BeancountDirectiveImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BeancountVisitor visitor) {
    visitor.visitDirective(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BeancountVisitor) accept((BeancountVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public BeancountBalanceDir getBalanceDir() {
    return PsiTreeUtil.getChildOfType(this, BeancountBalanceDir.class);
  }

  @Override
  @Nullable
  public BeancountCommodityDir getCommodityDir() {
    return PsiTreeUtil.getChildOfType(this, BeancountCommodityDir.class);
  }

  @Override
  @Nullable
  public BeancountEventDir getEventDir() {
    return PsiTreeUtil.getChildOfType(this, BeancountEventDir.class);
  }

  @Override
  @Nullable
  public BeancountOpenDir getOpenDir() {
    return PsiTreeUtil.getChildOfType(this, BeancountOpenDir.class);
  }

  @Override
  @Nullable
  public BeancountPriceDir getPriceDir() {
    return PsiTreeUtil.getChildOfType(this, BeancountPriceDir.class);
  }

  @Override
  @Nullable
  public BeancountTransactionDir getTransactionDir() {
    return PsiTreeUtil.getChildOfType(this, BeancountTransactionDir.class);
  }

}
