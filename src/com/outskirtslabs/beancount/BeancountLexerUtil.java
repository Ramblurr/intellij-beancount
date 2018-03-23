package com.outskirtslabs.beancount;

import org.apache.commons.logging.Log;

import com.intellij.openapi.diagnostic.Logger;

public class BeancountLexerUtil
{
    private static Logger LOG = Logger.getInstance(Log.class);

    public static int colonIndex(CharSequence match)
    {
        String s = String.valueOf(match);
        int i = s.indexOf(":") + 1;
        i = Math.min(i, s.length());
        i = s.length() - i;
//        LOG.info("pushedback   "+ match + " i:" + i + " len:"+s.length());
        return i;
    }

    public static int nonWsIndex(CharSequence match)
    {
        String s = String.valueOf(match);
        int i = s.lastIndexOf(" ") + 1;
        i = Math.min(i, s.length());
        i = s.length() - i;
//        LOG.info(match + " i:" + i + " len:"+s.length());
        return i;
    }
}
