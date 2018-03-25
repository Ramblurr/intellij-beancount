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

public class BeancountTransactionDirImpl extends ASTWrapperPsiElement implements BeancountTransactionDir {

  public BeancountTransactionDirImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull BeancountVisitor visitor) {
    visitor.visitTransactionDir(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BeancountVisitor) accept((BeancountVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<BeancountKeyValue> getKeyValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, BeancountKeyValue.class);
  }

  @Override
  @Nullable
  public BeancountPostingList getPostingList() {
    return PsiTreeUtil.getChildOfType(this, BeancountPostingList.class);
  }

  @Override
  @NotNull
  public List<BeancountTagLink> getTagLinkList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, BeancountTagLink.class);
  }

}
