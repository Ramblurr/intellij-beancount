package com.outskirtslabs.beancount.psi.elements;

import com.intellij.psi.PsiElement;

public interface BeancountExprElement extends PsiElement
{
    /**
     * Get the length of the string after the "." decimal.
     * <p>
     * example:
     * <p>
     * "12.00" -> 2
     * "12.000" -> 3
     * "12." -> 0
     * "12" -> 0
     */
    int getLengthPostDecimal();

    int getLengthPreDecimal();

    int getLengthPreDecimalWithAccount();
}
