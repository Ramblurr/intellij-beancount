package com.outskirtslabs.beancount;

import com.intellij.lang.Language;

public class BeancountLanguage extends Language
{
    public static final BeancountLanguage INSTANCE = new BeancountLanguage();

    private BeancountLanguage()
    {
        super("Beancount");
    }
}
