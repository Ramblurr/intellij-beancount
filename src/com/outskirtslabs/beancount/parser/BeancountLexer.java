package com.outskirtslabs.beancount.parser;

import com.intellij.lexer.FlexAdapter;

public class BeancountLexer extends FlexAdapter
{
    public BeancountLexer()
    {
        super(new _BeancountLexer());
    }
}
