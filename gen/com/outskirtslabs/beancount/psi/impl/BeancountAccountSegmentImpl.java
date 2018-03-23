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

public class BeancountAccountSegmentImpl extends ASTWrapperPsiElement implements BeancountAccountSegment {

  public BeancountAccountSegmentImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BeancountVisitor visitor) {
    visitor.visitAccountSegment(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BeancountVisitor) accept((BeancountVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public BeancountAccountDelimiter getAccountDelimiter() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, BeancountAccountDelimiter.class));
  }

  @Override
  @NotNull
  public BeancountAccountName getAccountName() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, BeancountAccountName.class));
  }

}
