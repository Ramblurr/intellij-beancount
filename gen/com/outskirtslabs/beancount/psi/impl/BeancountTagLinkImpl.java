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

public class BeancountTagLinkImpl extends ASTWrapperPsiElement implements BeancountTagLink {

  public BeancountTagLinkImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BeancountVisitor visitor) {
    visitor.visitTagLink(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BeancountVisitor) accept((BeancountVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public BeancountLinkValue getLinkValue() {
    return PsiTreeUtil.getChildOfType(this, BeancountLinkValue.class);
  }

  @Override
  @Nullable
  public BeancountTagValue getTagValue() {
    return PsiTreeUtil.getChildOfType(this, BeancountTagValue.class);
  }

}
