// This is a generated file. Not intended for manual editing.
package com.outskirtslabs.beancount.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface BeancountDirective extends PsiElement {

  @Nullable
  BeancountBalanceDir getBalanceDir();

  @Nullable
  BeancountCommodityDir getCommodityDir();

  @Nullable
  BeancountCustomDir getCustomDir();

  @Nullable
  BeancountEventDir getEventDir();

  @Nullable
  BeancountOpenDir getOpenDir();

  @Nullable
  BeancountPadDir getPadDir();

  @Nullable
  BeancountPriceDir getPriceDir();

  @Nullable
  BeancountTransactionDir getTransactionDir();

}
