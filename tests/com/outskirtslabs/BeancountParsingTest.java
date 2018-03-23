package com.outskirtslabs;

import java.util.LinkedList;
import java.util.List;

import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.testFramework.ParsingTestCase;
import com.outskirtslabs.beancount.BeancountLanguage;
import com.outskirtslabs.beancount.BeancountParserDefinition;

public class BeancountParsingTest extends ParsingTestCase
{
    public BeancountParsingTest()
    {
        super("", "beancount", new BeancountParserDefinition());
    }

    public void testOption()
    {
        assertParsedCorrectly();
    }

    public void testComment()
    {
        assertParsedCorrectly();
    }

    public void testSimple()
    {
        assertParsedCorrectly();
    }

    public void assertParsedCorrectly()
    {
        doTest(true);
        assertWithoutLocalError();
    }

    protected void assertParsedWithErrors()
    {
        assertParsedWithErrors(true);
    }

    protected void assertParsedWithErrors(boolean checkResult)
    {
        doTest(checkResult);

        assertWithLocalError();
    }

    protected void assertWithLocalError()
    {
        final FileViewProvider fileViewProvider = myFile.getViewProvider();
        PsiFile root = fileViewProvider.getPsi(BeancountLanguage.INSTANCE);
        final List<PsiElement> errorElementList = new LinkedList<PsiElement>();

        root.accept(
            new PsiRecursiveElementWalkingVisitor()
            {
                @Override
                public void visitElement(PsiElement element)
                {
                    if (element instanceof PsiErrorElement)
                    {
                        errorElementList.add(element);
                    }

                    super.visitElement(element);
                }
            }
        );

        assertTrue("No PsiErrorElements found in parsed file PSI", !errorElementList.isEmpty());
    }

    protected void assertWithoutLocalError()
    {
        final FileViewProvider fileViewProvider = myFile.getViewProvider();
        PsiFile root = fileViewProvider.getPsi(BeancountLanguage.INSTANCE);
        final List<PsiElement> errorElementList = new LinkedList<PsiElement>();

        root.accept(
            new PsiRecursiveElementWalkingVisitor()
            {
                @Override
                public void visitElement(PsiElement element)
                {
                    if (element instanceof PsiErrorElement)
                    {
                        errorElementList.add(element);
                    }

                    super.visitElement(element);
                }
            }
        );

        assertTrue("PsiErrorElements found in parsed file PSI", errorElementList.isEmpty());
    }

    @Override
    protected String getTestDataPath()
    {
        return "resources-test/testData/parsing";
    }

    @Override
    protected boolean skipSpaces()
    {
        return false;
    }

    @Override
    protected boolean includeRanges()
    {
        return true;
    }
}
