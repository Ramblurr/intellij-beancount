package com.outskirtslabs.beancount.psi;

import static com.outskirtslabs.beancount.psi.BeancountTypes.CUSTOM;
import static com.outskirtslabs.beancount.psi.BeancountTypes.DOCUMENT;
import static com.outskirtslabs.beancount.psi.BeancountTypes.EVENT;
import static com.outskirtslabs.beancount.psi.BeancountTypes.INCLUDE;
import static com.outskirtslabs.beancount.psi.BeancountTypes.NOTE;
import static com.outskirtslabs.beancount.psi.BeancountTypes.OPEN;
import static com.outskirtslabs.beancount.psi.BeancountTypes.OPTION;
import static com.outskirtslabs.beancount.psi.BeancountTypes.PAD;
import static com.outskirtslabs.beancount.psi.BeancountTypes.PRICE;
import static com.outskirtslabs.beancount.psi.BeancountTypes.QUERY;
import static com.outskirtslabs.beancount.psi.BeancountTypes.TXN;

import com.intellij.psi.tree.TokenSet;

public class BeancountTypeUtil
{
    public final static TokenSet DIRECTIVE_KEYWORDS = TokenSet.create(
        OPTION,
        OPEN,
        PRICE,
        TXN,
        EVENT,
        PRICE,
        CUSTOM,
        PAD,
        QUERY,
        DOCUMENT,
        NOTE,
        INCLUDE

    );

}
