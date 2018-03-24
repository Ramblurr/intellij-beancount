// This is a generated file. Not intended for manual editing.
package com.outskirtslabs.beancount.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.outskirtslabs.beancount.psi.elements.BeancountAccountElement;
import com.outskirtslabs.beancount.psi.elements.BeancountExprElement;

public class BeancountVisitor extends PsiElementVisitor {

  public void visitAccount(@NotNull BeancountAccount o) {
    visitAccountElement(o);
  }

  public void visitAccountDelimiter(@NotNull BeancountAccountDelimiter o) {
    visitPsiElement(o);
  }

  public void visitAccountName(@NotNull BeancountAccountName o) {
    visitPsiElement(o);
  }

  public void visitAccountSegment(@NotNull BeancountAccountSegment o) {
    visitPsiElement(o);
  }

  public void visitAmount(@NotNull BeancountAmount o) {
    visitPsiElement(o);
  }

  public void visitBalanceDir(@NotNull BeancountBalanceDir o) {
    visitPsiElement(o);
  }

  public void visitCommodityDir(@NotNull BeancountCommodityDir o) {
    visitPsiElement(o);
  }

  public void visitCompoundAmount(@NotNull BeancountCompoundAmount o) {
    visitPsiElement(o);
  }

  public void visitCostComp(@NotNull BeancountCostComp o) {
    visitPsiElement(o);
  }

  public void visitCostCompList(@NotNull BeancountCostCompList o) {
    visitPsiElement(o);
  }

  public void visitCostSpec(@NotNull BeancountCostSpec o) {
    visitPsiElement(o);
  }

  public void visitCustomDir(@NotNull BeancountCustomDir o) {
    visitPsiElement(o);
  }

  public void visitDirective(@NotNull BeancountDirective o) {
    visitPsiElement(o);
  }

  public void visitDivExpr(@NotNull BeancountDivExpr o) {
    visitExpr(o);
  }

  public void visitEventDir(@NotNull BeancountEventDir o) {
    visitPsiElement(o);
  }

  public void visitExpr(@NotNull BeancountExpr o) {
    visitExprElement(o);
  }

  public void visitKeyValue(@NotNull BeancountKeyValue o) {
    visitPsiElement(o);
  }

  public void visitKeyValueValue(@NotNull BeancountKeyValueValue o) {
    visitPsiElement(o);
  }

  public void visitLinkValue(@NotNull BeancountLinkValue o) {
    visitPsiElement(o);
  }

  public void visitLiteralExpr(@NotNull BeancountLiteralExpr o) {
    visitExpr(o);
  }

  public void visitMinusExpr(@NotNull BeancountMinusExpr o) {
    visitExpr(o);
  }

  public void visitMulExpr(@NotNull BeancountMulExpr o) {
    visitExpr(o);
  }

  public void visitOpenDir(@NotNull BeancountOpenDir o) {
    visitPsiElement(o);
  }

  public void visitOptionDir(@NotNull BeancountOptionDir o) {
    visitPsiElement(o);
  }

  public void visitParenExpr(@NotNull BeancountParenExpr o) {
    visitExpr(o);
  }

  public void visitPlusExpr(@NotNull BeancountPlusExpr o) {
    visitExpr(o);
  }

  public void visitPostingLine(@NotNull BeancountPostingLine o) {
    visitPsiElement(o);
  }

  public void visitPostingList(@NotNull BeancountPostingList o) {
    visitPsiElement(o);
  }

  public void visitPostingPrice(@NotNull BeancountPostingPrice o) {
    visitPsiElement(o);
  }

  public void visitPriceDir(@NotNull BeancountPriceDir o) {
    visitPsiElement(o);
  }

  public void visitTagLink(@NotNull BeancountTagLink o) {
    visitPsiElement(o);
  }

  public void visitTagValue(@NotNull BeancountTagValue o) {
    visitPsiElement(o);
  }

  public void visitTransactionDir(@NotNull BeancountTransactionDir o) {
    visitPsiElement(o);
  }

  public void visitUnaryMinExpr(@NotNull BeancountUnaryMinExpr o) {
    visitExpr(o);
  }

  public void visitUnaryPlusExpr(@NotNull BeancountUnaryPlusExpr o) {
    visitExpr(o);
  }

  public void visitAccountElement(@NotNull BeancountAccountElement o) {
    visitPsiElement(o);
  }

  public void visitExprElement(@NotNull BeancountExprElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
