package com.outskirtslabs.beancount.features.highlighting;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import org.jetbrains.annotations.NotNull;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.outskirtslabs.beancount.parser.BeancountLexer;
import com.outskirtslabs.beancount.psi.BeancountTypeUtil;
import com.outskirtslabs.beancount.psi.BeancountTypes;

public class BeancountSyntaxHighlighter extends SyntaxHighlighterBase
{
    // @formatter:off
    public static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("BEANCOUNT_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[] { BAD_CHARACTER };
    public static final TextAttributesKey COMMENT = createTextAttributesKey("BEANCOUNT_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[] { COMMENT };
    public static final TextAttributesKey DATE = createTextAttributesKey("BEANCOUNT_DATE", DefaultLanguageHighlighterColors.CONSTANT);
    private static final TextAttributesKey[] DATE_KEYS = new TextAttributesKey[] { DATE };
    public static final TextAttributesKey FLAG = createTextAttributesKey("FLAG", DefaultLanguageHighlighterColors.KEYWORD);
    private static final TextAttributesKey[] FLAG_KEYS = new TextAttributesKey[] { FLAG };
    public static final TextAttributesKey IDENT = createTextAttributesKey("BEANCOUNT_IDENT", DefaultLanguageHighlighterColors.IDENTIFIER);
    private static final TextAttributesKey[] IDENT_KEYS = new TextAttributesKey[] { IDENT };

    public static final TextAttributesKey CURRENCY = createTextAttributesKey("BEANCOUNT_IDENT", DefaultLanguageHighlighterColors.IDENTIFIER);
    private static final TextAttributesKey[] CURRENCY_KEYS = new TextAttributesKey[] { CURRENCY};
    public static final TextAttributesKey DIRECTIVE = createTextAttributesKey("BEANCOUNT_DIRECTIVE", DefaultLanguageHighlighterColors.KEYWORD);
    private static final TextAttributesKey[] DIRECTIVE_KEYS = new TextAttributesKey[] { DIRECTIVE };
    public static final TextAttributesKey META_KEY = createTextAttributesKey("BEANCOUNT_META_KEY", DefaultLanguageHighlighterColors.LINE_COMMENT);
    private static final TextAttributesKey[] META_KEYS = new TextAttributesKey[] { META_KEY };
    public static final TextAttributesKey NEGATIVE_NUMBER = createTextAttributesKey("BEANCOUNT_NEGATIVE_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    private static final TextAttributesKey[] NEGATIVE_NUMBER_KEYS = new TextAttributesKey[] { NEGATIVE_NUMBER };
    public static final TextAttributesKey NUMBER = createTextAttributesKey("BEANCOUNT_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[] { NUMBER };
    public static final TextAttributesKey STRING = createTextAttributesKey("BEANCOUNT_STRING", DefaultLanguageHighlighterColors.STRING);
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[] { STRING };
    public static final TextAttributesKey VALUE = createTextAttributesKey("BEANCOUNT_VALUE", DefaultLanguageHighlighterColors.STRING);
    private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[] { VALUE };
    // @formatter:on

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer()
    {
        return new BeancountLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType)
    {
        if (tokenType.equals(BeancountTypes.CURRENCY))
            return CURRENCY_KEYS;
        else if (BeancountTypeUtil.DIRECTIVE_KEYWORDS.contains(tokenType))
            return DIRECTIVE_KEYS;
        if (BeancountTypeUtil.ACCOUNT_TOKENS.contains(tokenType))
            return IDENT_KEYS;
        else if (tokenType.equals(BeancountTypes.COMMENT))
            return COMMENT_KEYS;
        else if (tokenType.equals(BeancountTypes.NEGATIVE_NUMBER))
            return NEGATIVE_NUMBER_KEYS;
        else if (tokenType.equals(BeancountTypes.NUMBER))
            return NUMBER_KEYS;
        else if (tokenType.equals(BeancountTypes.STRING))
            return STRING_KEYS;
        else if (tokenType.equals(BeancountTypes.DATE))
            return DATE_KEYS;
        else if (tokenType.equals(BeancountTypes.FLAG))
            return FLAG_KEYS;
        else if (tokenType.equals(BeancountTypes.META_KEY))
            return META_KEYS;
        else if (tokenType.equals(TokenType.BAD_CHARACTER))
            return BAD_CHAR_KEYS;
        else
            return EMPTY_KEYS;
    }
}
