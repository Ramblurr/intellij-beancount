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
  BeancountDocumentDir getDocumentDir();

  @Nullable
  BeancountEventDir getEventDir();

  @Nullable
  BeancountNoteDir getNoteDir();

  @Nullable
  BeancountOpenDir getOpenDir();

  @Nullable
  BeancountPadDir getPadDir();

  @Nullable
  BeancountPriceDir getPriceDir();

  @Nullable
  BeancountQueryDir getQueryDir();

  @Nullable
  BeancountTransactionDir getTransactionDir();

}
