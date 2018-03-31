package com.outskirtslabs.beancount.features.highlighting;

import java.util.Map;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.outskirtslabs.beancount.BeancountIcons;

public class BeancountColorSettingsPage implements ColorSettingsPage
{
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[] {
        new AttributesDescriptor("Inflows", BeancountSyntaxHighlighter.NUMBER),
        new AttributesDescriptor("Outflows", BeancountSyntaxHighlighter.NEGATIVE_NUMBER),
        new AttributesDescriptor("Currency", BeancountSyntaxHighlighter.CURRENCY),
        new AttributesDescriptor("Account Names", BeancountSyntaxHighlighter.IDENT),
        new AttributesDescriptor("Strings", BeancountSyntaxHighlighter.STRING),
        new AttributesDescriptor("Metadata key", BeancountSyntaxHighlighter.META_KEY),
        new AttributesDescriptor("Comments", BeancountSyntaxHighlighter.COMMENT),
        new AttributesDescriptor("Directives", BeancountSyntaxHighlighter.DIRECTIVE),
        new AttributesDescriptor("Dates", BeancountSyntaxHighlighter.DATE),
    };

    @Nullable
    @Override
    public Icon getIcon()
    {
        return BeancountIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter()
    {
        return new BeancountSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText()
    {
        return "2016-01-01 * \"Opening Balance for checking account\"\n"
            + "  omg: \"yes\"\n"
            + "  Assets:US:BofA:Checking                         3460.77 USD\n"
            + "    omg: \"yes\"\n"
            + "  Equity:Opening-Balances                        -3460.77 USD\n"
            + ";; this is a comment\n"
            + "\n"
            + "option \"title\" \"Example Beancount file\"\n"
            + "\n"
            + "1792-01-01 commodity USD\n"
            + "  omg: \"yes\"\n"
            + "\n"
            + "2016-12-06 balance Assets:US:BofA:Checking        2917.06 USD";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
    {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors()
    {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors()
    {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName()
    {
        return "Beancount";
    }
}
