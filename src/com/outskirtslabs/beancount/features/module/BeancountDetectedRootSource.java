package com.outskirtslabs.beancount.features.module;

import java.io.File;

import org.jetbrains.annotations.NotNull;

import com.intellij.ide.util.projectWizard.importSources.DetectedSourceRoot;

/**
 * This shows up in the Idea UI when importing a project, it will say something like
 * "Source files for your project have been found"
 */
public class BeancountDetectedRootSource extends DetectedSourceRoot
{
    BeancountDetectedRootSource(final File directory)
    {
        super(directory, "");
    }

    @NotNull
    @Override
    public String getRootTypeName()
    {
        return "Beancount";
    }
}
