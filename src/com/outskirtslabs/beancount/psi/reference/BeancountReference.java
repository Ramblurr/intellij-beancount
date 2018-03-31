package com.outskirtslabs.beancount.psi.reference;

import static com.outskirtslabs.beancount.psi.reference.BeancountScopeProcessor.RESOLVE_CONTINUE;
import static com.outskirtslabs.beancount.psi.reference.BeancountScopeProcessor.RESOLVE_FINISHED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.ResolveState;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.scope.BaseScopeProcessor;
import com.intellij.util.containers.OrderedSet;
import com.outskirtslabs.beancount.BeancountIcons;
import com.outskirtslabs.beancount.psi.BeancountFile;
import com.outskirtslabs.beancount.psi.elements.BeancountNamedElement;

import io.vavr.collection.Stream;
/**
 * Documentation on references and resolving:
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/references_and_resolve.html
 * <p>
 * It is very dense but the key idea to understand about references is that any instance of an element that can be named
 * (like an account name in beancount) should return a reference (getReferences).
 * <p>
 * This reference should resolve() or multiResolve() to the _declaration_ instance of that element.
 * <p>
 * In concrete terms, every instance of a beancount account name in the AST returns a reference (this class)
 * whose resolve() methods return the PsiElement of the declaration, which in the account case is the explicit "open" directive.
 */
abstract public class BeancountReference<T extends BeancountNamedElement>
    extends PsiPolyVariantReferenceBase<T>
{
    private static final Key<SmartPsiElementPointer<PsiElement>> CONTEXT = Key.create("CONTEXT");
    private String elementText = "";
    private T myBeancountElement;

    public BeancountReference(T element, TextRange range)
    {
        super(element, range);
        this.myBeancountElement = element;
        init();
    }

    private void init()
    {
        String text = myBeancountElement.getText();
        if (text == null) return;
        this.elementText = text;
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    abstract Collection<T> getFromCache(Project project, String elementText);

    @NotNull
    @Override
    public Object[] getVariants()
    {
        Project project = myBeancountElement.getProject();
        Collection<T> cachedElements = getFromCache(project, elementText);
        List<LookupElement> variants = new ArrayList<>();
        for (final T element : cachedElements)
        {
            if (StringUtils.isNotBlank(element.getName()))
            {
                variants
                    .add(LookupElementBuilder.create(element)
                                             .withIcon(BeancountIcons.FILE)
                                             .withTypeText(element.getContainingFile().getName()));
            }
        }
        return variants.toArray();
    }

    protected ResolveCache.PolyVariantResolver<BeancountReference<T>> getResolver()
    {
        return (BeancountReference<T> r, boolean incompleteCode) -> r.resolveInner();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(final boolean incompleteCode)
    {
        if (!myBeancountElement.isValid()) return ResolveResult.EMPTY_ARRAY;
        return ResolveCache.getInstance(myBeancountElement.getProject())
                           .resolveWithCaching(
                               this, // reference to resolve
                               getResolver(), // resolver to use
                               false, // need to prevent recursion
                               incompleteCode // worry about invalid/incomplete code
                           );
    }

    @NotNull
    private ResolveResult[] resolveInner()
    {
        if (!myBeancountElement.isValid()) return ResolveResult.EMPTY_ARRAY;
        Collection<ResolveResult> result = new OrderedSet<>();
        processResolve(createResolveProcessor(result, myBeancountElement));
        return result.toArray(new ResolveResult[result.size()]);
    }

    abstract protected boolean isElementToResolve(T elementToSearch,
        BeancountNamedElement maybeElementDeclaration);

    @NotNull
    protected BaseScopeProcessor createResolveProcessor(@NotNull Collection<ResolveResult> result,
        @NotNull T elementToResolve)
    {
        /*
             execute() is called to process every declaration encountered during the resolve,
             and returns true if the resolve needs to be continued
                      or false if the declaration has been found.
        */
        return BeancountScopeProcessor.of((possibleDeclarationElement, state) -> {
            if (possibleDeclarationElement instanceof BeancountNamedElement)
            {
                if (isElementToResolve(elementToResolve,
                    (BeancountNamedElement) possibleDeclarationElement))
                {
                    result.add(new PsiElementResolveResult(possibleDeclarationElement));
                    return RESOLVE_FINISHED;
                }
            }

            return RESOLVE_CONTINUE;
        });

    }

    /**
     * The documentation says:
     * <p>
     * "A function which walks the PSI tree up from the reference location until the resolve has
     * successfully completed or until the end of the resolve scope has been reached."
     * <p>
     * This method is the "function which walks the psi tree". Refer to the scope processor for the
     * bit that actually decides if we are done or not.
     */
    private void processResolve(@NotNull BaseScopeProcessor processor)
    {
        PsiFile file = myBeancountElement.getContainingFile();
        if (!(file instanceof BeancountFile)) return;
        ResolveState state = createContextOnElement(myBeancountElement);

        // with this we have every usage of this particular elementText
        Collection<T> cachedElements = getFromCache(file.getProject(), elementText);

        // now we exec the processor to find which one of these elementText instances is the declaration
        Stream.ofAll(cachedElements)
              .find(a -> !processor.execute(a, state));
    }

    /**
     * Just some plumbing to create a ResolveState needed by the processor
     */
    @NotNull
    private static ResolveState createContextOnElement(@NotNull PsiElement element)
    {
        return ResolveState.initial().put(CONTEXT,
            SmartPointerManager.getInstance(element.getProject())
                               .createSmartPsiElementPointer(element));
    }

}
