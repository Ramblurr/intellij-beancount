package com.outskirtslabs.beancount.psi;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public class BeancountTreeUtil
{

    public static PsiElement getParent(PsiElement element)
    {
        if (element == null)
            return null;
        return element.getParent();
    }

    public static PsiElement getGrandGrandParent(PsiElement element)
    {
        return getParent(getGrandParent(element));
    }

    public static PsiElement getGrandParent(PsiElement element)
    {

        PsiElement parent = getParent(element);
        return Optional.ofNullable(parent)
                       .map(PsiElement::getParent)
                       .orElse(null);
    }

    public static boolean isAnyChildOfType(PsiElement element, IElementType type)
    {
        return BeancountTreeUtil.isAnyMatchInChildren(element, e -> isElementOfType(e, type));
    }

    public static boolean isElementOfType(PsiElement element, IElementType type)
    {
        return element instanceof ASTNode && ((ASTNode) element).getElementType().equals(type);
    }

    public static boolean isAnyMatchInChildren(PsiElement element, Predicate<PsiElement> predicate)
    {
        return findInSiblingsStream(element.getFirstChild(), predicate)
            .isPresent();
    }

    public static Optional<PsiElement> findFollowingSibling(PsiElement element,
        Predicate<PsiElement> predicate)
    {
        return findInSiblingsStream(element.getNextSibling(), predicate);
    }

    public static Optional<PsiElement> findParent(PsiElement element,
        Predicate<PsiElement> predicate)
    {
        return getParentStream(element)
            .filter(elem -> elem.map(predicate::test).orElse(true))
            .findFirst()
            .orElse(Optional.empty());
    }

    public static String joinUsingDot(List<? extends PsiElement> elements)
    {
        return elements.stream()
                       .map(PsiElement::getText)
                       .collect(Collectors.joining("."));
    }

    private static Optional<PsiElement> findInSiblingsStream(PsiElement element,
        Predicate<PsiElement> predicate)
    {
        return getSiblingsStream(element)
            .filter(elem -> elem.map(predicate::test).orElse(true))
            .findFirst()
            .orElse(Optional.empty());
    }

    private static Stream<Optional<PsiElement>> getSiblingsStream(PsiElement element)
    {
        return Stream.iterate(
            Optional.ofNullable(element),
            prev -> prev.map(e -> Optional.ofNullable(e.getNextSibling())).orElse(Optional.empty())
        );
    }

    private static Stream<Optional<PsiElement>> getParentStream(PsiElement element)
    {
        return Stream.iterate(
            Optional.ofNullable(element),
            prev -> prev.map(e -> Optional.ofNullable(e.getParent())).orElse(Optional.empty()));
    }
}

