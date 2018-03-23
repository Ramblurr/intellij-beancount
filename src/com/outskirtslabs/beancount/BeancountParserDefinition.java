package com.outskirtslabs.beancount;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.outskirtslabs.beancount.parser.BeancountLexer;
import com.outskirtslabs.beancount.parser.BeancountParser;
import com.outskirtslabs.beancount.psi.BeancountFile;
import com.outskirtslabs.beancount.psi.BeancountTypes;
import com.outskirtslabs.beancount.psi.stub.BeancountStubFileElementType;

public class BeancountParserDefinition implements ParserDefinition
{
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(BeancountTypes.COMMENT);

    @NotNull
    @Override
    public Lexer createLexer(Project project)
    {
        return new BeancountLexer();
    }

    @NotNull
    public TokenSet getWhitespaceTokens()
    {
        return WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens()
    {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements()
    {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiParser createParser(final Project project)
    {
        return new BeancountParser();
    }

    @Override
    public IFileElementType getFileNodeType()
    {
        return BeancountStubFileElementType.INSTANCE;
    }

    public PsiFile createFile(FileViewProvider viewProvider)
    {
        return new BeancountFile(viewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right)
    {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node)
    {
        return BeancountTypes.Factory.createElement(node);
    }
}

