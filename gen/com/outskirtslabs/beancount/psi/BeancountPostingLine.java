// This is a generated file. Not intended for manual editing.
package com.outskirtslabs.beancount.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface BeancountPostingLine extends PsiElement {

  @NotNull
  BeancountAccount getAccount();

  @NotNull
  BeancountAmount getAmount();

  @Nullable
  BeancountCostSpec getCostSpec();

  @NotNull
  List<BeancountKeyValue> getKeyValueList();

  @Nullable
  BeancountPostingPrice getPostingPrice();

}
