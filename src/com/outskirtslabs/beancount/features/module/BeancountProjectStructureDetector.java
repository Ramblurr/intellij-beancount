package com.outskirtslabs.beancount.features.module;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.ide.util.importProject.ModuleDescriptor;
import com.intellij.ide.util.importProject.ProjectDescriptor;
import com.intellij.ide.util.projectWizard.importSources.DetectedProjectRoot;
import com.intellij.ide.util.projectWizard.importSources.ProjectFromSourcesBuilder;
import com.intellij.ide.util.projectWizard.importSources.ProjectStructureDetector;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.containers.ContainerUtil;
import com.outskirtslabs.beancount.BeancountFileType;

/**
 * Detects Beancount files in a directory
 */
public class BeancountProjectStructureDetector extends ProjectStructureDetector
{

    /**
     * We detect any folder containing Beancount files as a project root.
     *
     * @see ProjectStructureDetector#detectRoots(File, File[], File, List)
     */
    @NotNull
    @Override
    public DirectoryProcessingResult detectRoots(@NotNull final File dir,
        @NotNull final File[] children, @NotNull final File base,
        @NotNull final List<DetectedProjectRoot> result)
    {
        List<File> mask = FileUtil
            .findFilesByMask(BeancountFileType.EXTENSION_PATTERN, base);
        if (!mask.isEmpty())
            result.add(new BeancountDetectedRootSource(dir));
        return DirectoryProcessingResult.SKIP_CHILDREN;
    }

    /**
     * This is used to create the Beancount module when importing from existing sources.
     *
     * @see ProjectStructureDetector#setupProjectStructure(Collection, ProjectDescriptor, ProjectFromSourcesBuilder)
     */
    @Override
    public void setupProjectStructure(@NotNull final Collection<DetectedProjectRoot> roots,
        @NotNull final ProjectDescriptor projectDescriptor,
        @NotNull final ProjectFromSourcesBuilder builder)
    {
        List<ModuleDescriptor> modules = projectDescriptor.getModules();
        if (modules.isEmpty())
        {
            modules = new ArrayList<>();
            for (DetectedProjectRoot root : roots)
            {
                modules.add(
                    new ModuleDescriptor(root.getDirectory(), BeancountModuleType.getInstance(),
                        ContainerUtil.emptyList()));
            }
            projectDescriptor.setModules(modules);
        }
    }
}
