package com.outskirtslabs.beancount.psi.stub;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.DefaultStubBuilder;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.tree.IStubFileElementType;
import com.outskirtslabs.beancount.BeancountLanguage;
import com.outskirtslabs.beancount.psi.BeancountFile;

public class BeancountStubFileElementType
    extends IStubFileElementType<BeancountFileStub>
{
    public static final int VERSION = 1;
    public static final IStubFileElementType INSTANCE = new BeancountStubFileElementType();

    public BeancountStubFileElementType()
    {
        super("BEANCOUNT_FILE", BeancountLanguage.INSTANCE);
    }

    @Override
    public StubBuilder getBuilder()
    {
        return new DefaultStubBuilder()
        {
            @Override
            protected StubElement createStubForFile(@NotNull PsiFile file)
            {
                if (file instanceof BeancountFile)
                {
                    return new BeancountFileStub((BeancountFile) file);
                }
                return super.createStubForFile(file);
            }
        };
    }

    @Override
    public int getStubVersion()
    {
        return VERSION;
    }

    @NotNull
    @Override
    public BeancountFileStub deserialize(@NotNull StubInputStream dataStream,
        StubElement parentStub)
    {
        return new BeancountFileStub(null);
    }

    @NotNull
    @Override
    public String getExternalId()
    {
        return "beancount.FILE";
    }

//    @Nullable
//    @Override
//    protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi)
//    {
//        Project project = psi.getProject();
//        Language languageForParser = getLanguageForParser(psi);
//        PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, null,
//            languageForParser, chameleon.getChars());
//        PsiParser parser = LanguageParserDefinitions.INSTANCE.forLanguage(languageForParser)
//                                                             .createParser(project);
//        builder.putUserData(VIRTUAL_FILE, psi.getContainingFile().getVirtualFile());
//        ASTNode node = parser.parse(this, builder);
//        return node.getFirstChildNode();
//    }
}
