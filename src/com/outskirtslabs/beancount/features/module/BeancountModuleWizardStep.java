package com.outskirtslabs.beancount.features.module;

import javax.swing.*;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;

/**
 * The settings page when creating the Beancount module.
 */
public class BeancountModuleWizardStep extends ModuleWizardStep
{
    @Override
    public JComponent getComponent()
    {
        return new JLabel("No settings required");
    }

    @Override
    public void updateDataModel()
    {
    }
}
