package com.outskirtslabs.beancount.psi.elements;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.outskirtslabs.beancount.BeancountFileType;
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.BeancountCurrencySymbol;
import com.outskirtslabs.beancount.psi.BeancountFile;
import com.outskirtslabs.beancount.psi.BeancountRecursiveVisitor;

public class BeancountElementFactory
{
    public static BeancountAccount createAccount(Project project, String name)
    {
        String text = String.format("2018-01-01 open Expenses:%s                  USD", name);
        final BeancountFile file = createFile(project, text);
        BeancountAccount[] result = { null };
        file.acceptChildren(new BeancountRecursiveVisitor()
        {
            @Override
            public void visitAccount(@NotNull final BeancountAccount o)
            {
                result[0] = o;
            }
        });
        return result[0];
    }

    public static BeancountCurrencySymbol createCurrencySymbol(Project project, String name)
    {
        String text = String.format("1995-09-18 commodity %s", name);
        final BeancountFile file = createFile(project, text);
        BeancountCurrencySymbol[] result = { null };
        file.acceptChildren(new BeancountRecursiveVisitor()
        {
            @Override
            public void visitCurrencySymbol(@NotNull final BeancountCurrencySymbol o)
            {
                result[0] = o;
            }
        });
        return result[0];
    }

    public static BeancountFile createFile(Project project, String text)
    {
        String name = "file.beancount";
        return (BeancountFile) PsiFileFactory.getInstance(project).
            createFileFromText(name, BeancountFileType.INSTANCE, text);
    }
}
