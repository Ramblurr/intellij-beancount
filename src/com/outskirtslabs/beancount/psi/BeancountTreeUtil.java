package com.outskirtslabs.beancount.psi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public static Option<PsiElement> getNonWhitespacePreviousSibling(PsiElement element)
    {
        PsiElement sibling = element.getPrevSibling();
        while (sibling != null && sibling.getNode().getElementType().equals(TokenType.WHITE_SPACE))
            sibling = sibling.getPrevSibling();
        return Option.of(sibling);
    }

    public static io.vavr.collection.List<PsiElement> getPreviousSiblingsWhile(PsiElement element,
        Predicate<PsiElement> predicate)
    {
        PsiElement sibling = element.getPrevSibling();
        io.vavr.collection.List<PsiElement> objects = io.vavr.collection.List.empty();
        while (sibling != null && predicate.test(sibling))
        {
            objects = objects.append(sibling);
            sibling = sibling.getPrevSibling();
        }
        return objects.reverse();
    }

    public static boolean isAnyChildOfType(PsiElement element, IElementType type)
    {
        return BeancountTreeUtil.isAnyMatchInChildren(element, e -> isElementOfType(e, type));
    }

    public static boolean isElementOfType(PsiElement element, IElementType type)
    {
        return element instanceof ASTNode && ((ASTNode) element).getElementType().equals(type);
    }

    public static io.vavr.collection.Stream<PsiElement> findMatchesRecursively(PsiElement element,
        Predicate<PsiElement> predicate)
    {
        List<PsiElement> results = new ArrayList<>();
        element.acceptChildren(new PsiRecursiveElementWalkingVisitor()
        {
            @Override
            public void visitElement(final PsiElement element)
            {
                if (predicate.test(element))
                    results.add(element);
                super.visitElement(element);
            }
        });
        return io.vavr.collection.Stream.ofAll(results);
    }

    public static io.vavr.collection.Stream<Option<PsiElement>> findAllMatchesInChildren(
        PsiElement element, Predicate<PsiElement> predicate)
    {
        return getSiblingsStreamV(element.getFirstChild())
            .filter(elem -> elem.map(predicate::test).getOrElse(true))
            .filter(Option::isDefined);
    }

    public static Optional<PsiElement> findMatchInChildren(PsiElement element,
        Predicate<PsiElement> predicate)
    {
        return findInSiblingsStream(element.getFirstChild(), predicate);
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

    private static io.vavr.collection.Stream<Option<PsiElement>> getSiblingsStreamV(
        PsiElement element)
    {
        return io.vavr.collection.Stream.iterate(
            Option.of(element),
            prev -> prev.flatMap(e -> Option.of(e.getNextSibling()))
                        .orElse(Option.none()));
    }

    private static Stream<Optional<PsiElement>> getParentStream(PsiElement element)
    {
        return Stream.iterate(
            Optional.ofNullable(element),
            prev -> prev.map(e -> Optional.ofNullable(e.getParent())).orElse(Optional.empty()));
    }

    public static void debugElement(String label, PsiElement element)
    {
        if (element != null)
        {
            log.info("{}: {} \"{}\" refs: {}",
                label,
                element.getClass().getSimpleName(),
                element.getText().substring(0, Math.min(element.getTextLength(), 100)),
                element.getReferences());
        }
    }

    public static void debugTree(PsiElement position)
    {
        PsiElement parent = position.getParent();
        PsiElement grandp = getGrandParent(position);
        PsiElement ggrandp = getGrandGrandParent(position);

        debugElement("position", position);
        debugElement("parent", parent);
        debugElement("grandp", grandp);
        debugElement("ggrandp", ggrandp);
    }
}

