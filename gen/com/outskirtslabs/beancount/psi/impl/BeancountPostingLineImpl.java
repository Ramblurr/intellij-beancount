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

public class BeancountPostingLineImpl extends ASTWrapperPsiElement implements BeancountPostingLine {

  public BeancountPostingLineImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BeancountVisitor visitor) {
    visitor.visitPostingLine(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BeancountVisitor) accept((BeancountVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public BeancountAccount getAccount() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, BeancountAccount.class));
  }

  @Override
  @NotNull
  public BeancountAmount getAmount() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, BeancountAmount.class));
  }

  @Override
  @Nullable
  public BeancountCostSpec getCostSpec() {
    return PsiTreeUtil.getChildOfType(this, BeancountCostSpec.class);
  }

  @Override
  @NotNull
  public List<BeancountKeyValue> getKeyValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, BeancountKeyValue.class);
  }

  @Override
  @Nullable
  public BeancountPostingPrice getPostingPrice() {
    return PsiTreeUtil.getChildOfType(this, BeancountPostingPrice.class);
  }

}
