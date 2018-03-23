package com.outskirtslabs.beancount;

import java.util.regex.Pattern;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.fileTypes.LanguageFileType;

public class BeancountFileType
    extends LanguageFileType
{
    public static final BeancountFileType INSTANCE = new BeancountFileType();
    public static String DEFAULT_EXTENSION = "beancount";
    public static String DEFAULT_EXTENSION_RE = ".*\\.bean(count)?";
    public static Pattern EXTENSION_PATTERN = Pattern.compile(DEFAULT_EXTENSION_RE);

    private BeancountFileType()
    {
        super(BeancountLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName()
    {
        return "Beancount journal";
    }

    @NotNull
    @Override
    public String getDescription()
    {
        return "Beancount accounting language";
    }

    @NotNull
    @Override
    public String getDefaultExtension()
    {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon()
    {
        return BeancountIcons.FILE;
    }
}
