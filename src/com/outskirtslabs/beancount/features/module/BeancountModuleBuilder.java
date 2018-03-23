package com.outskirtslabs.beancount.features.module;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.outskirtslabs.beancount.BeancountIcons;

/**
 * Builds our module which consists of not more than giving it a name, an icon, and adding
 * all the files in the directory to the project.
 */
public class BeancountModuleBuilder extends ModuleBuilder implements ModuleBuilderListener
{
    BeancountModuleBuilder()
    {
        addListener(this);
    }

    @Override
    public void setupRootModel(ModifiableRootModel rootModel)
    {
        doAddContentEntry(rootModel);
    }

    @Override
    public ModuleType getModuleType()
    {
        return BeancountModuleType.getInstance();
    }

    @Override
    public boolean isSuitableSdkType(SdkTypeId sdkType)
    {
        return true;
    }

    @Override
    public Icon getNodeIcon()
    {
        return BeancountIcons.FILE;
    }

    @Override
    public void moduleCreated(@NotNull Module module)
    {
    }

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(final WizardContext context,
        final Disposable parentDisposable)
    {
        return new BeancountModuleWizardStep();
    }
}
