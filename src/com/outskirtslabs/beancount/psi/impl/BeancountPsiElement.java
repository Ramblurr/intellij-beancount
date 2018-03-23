package com.outskirtslabs.beancount.psi.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.outskirtslabs.beancount.psi.BeancountVisitor;

public class BeancountPsiElement extends ASTWrapperPsiElement
{

    public BeancountPsiElement(@NotNull final ASTNode node)
    {
        super(node);
    }

    @NotNull
    @Contract(
        pure = true
    )
    public PsiReference[] getReferences()
    {
        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
    }

    public void accept(@NotNull PsiElementVisitor visitor)
    {
        if (visitor instanceof BeancountVisitor)
        {
            ((BeancountVisitor) visitor).visitPsiElement(this);
        } else super.accept(visitor);
    }

//    public Stream<BeancountReference> getReferencesStream()
//    {
//        return Arrays.stream(this.getChildren())
//                     .filter(c -> c instanceof BeancountPsiElement)
//                     .map(c -> getReferencesFromChild((BeancountPsiElement) c))
//                     .reduce(Stream.empty(), Stream::concat);
//    }
//
//    private Stream<BeancountReference> getReferencesFromChild(BeancountPsiElement element)
//    {
//        return element.getReferencesStream()
//                      .map(r -> r.referenceInAncestor(this));
//    }
}
