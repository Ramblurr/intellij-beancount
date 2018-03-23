package com.outskirtslabs.beancount.psi.reference;

import static com.outskirtslabs.beancount.psi.reference.BeancountScopeProcessor.RESOLVE_CONTINUE;
import static com.outskirtslabs.beancount.psi.reference.BeancountScopeProcessor.RESOLVE_FINISHED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
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
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.BeancountFile;
import com.outskirtslabs.beancount.psi.BeancountOpenDir;
import com.outskirtslabs.beancount.psi.BeancountTreeUtil;
import com.outskirtslabs.beancount.psi.stub.index.AccountStubIndex;

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
public class BeancountAccountReference extends PsiPolyVariantReferenceBase<PsiElement>
{
    private static final Key<SmartPsiElementPointer<PsiElement>> CONTEXT = Key.create("CONTEXT");
    private static final ResolveCache.PolyVariantResolver<BeancountAccountReference> ACCOUNT_RESOLVER =
        (r, incompleteCode) -> r.resolveInner();
    private static Logger LOG = Logger.getInstance(BeancountAccountReference.class);
    private String acccountPath = "";

    public BeancountAccountReference(BeancountAccount element, TextRange range)
    {
        super(element, range);
        init();
    }

    private void init()
    {
        String text = myElement.getText();
        if (text == null) return;
        this.acccountPath = text;
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        Project project = myElement.getProject();
        Collection<BeancountAccount> accountNames = AccountStubIndex.find(project, acccountPath);
        List<LookupElement> variants = new ArrayList<>();
        for (final BeancountAccount account : accountNames)
        {
            if (StringUtils.isNotBlank(account.getName()))
            {
                variants
                    .add(LookupElementBuilder.create(account)
                                             .withIcon(BeancountIcons.FILE)
                                             .withTypeText(account.getContainingFile().getName()));
            }
        }
        return variants.toArray();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(final boolean incompleteCode)
    {
        if (!myElement.isValid()) return ResolveResult.EMPTY_ARRAY;
        return ResolveCache.getInstance(myElement.getProject())
                           .resolveWithCaching(
                               this, // reference to resolve
                               ACCOUNT_RESOLVER, // resolver to use
                               false, // need to prevent recursion
                               incompleteCode // worry about invalid/incomplete code
                           );
    }

    @NotNull
    private ResolveResult[] resolveInner()
    {
        if (!myElement.isValid()) return ResolveResult.EMPTY_ARRAY;
        Collection<ResolveResult> result = new OrderedSet<>();
        processResolve(createResolveProcessor(result, (BeancountAccount) myElement));
        return result.toArray(new ResolveResult[result.size()]);
    }

    @NotNull
    private BaseScopeProcessor createResolveProcessor(@NotNull Collection<ResolveResult> result,
        @NotNull BeancountAccount accountToResolve)
    {
        /*
             execute() is called to process every declaration encountered during the resolve,
             and returns true if the resolve needs to be continued
                      or false if the declaration has been found.
        */
        return BeancountScopeProcessor.of((possibleDeclarationElement, state) -> {
            if (possibleDeclarationElement instanceof BeancountAccount)
            {
                BeancountAccount possibleAccount = (BeancountAccount) possibleDeclarationElement;
                if (isAccountDeclaration(accountToResolve, possibleAccount))
                {
                    result.add(new PsiElementResolveResult(possibleDeclarationElement));
                    return RESOLVE_FINISHED;
                }
            }

            return RESOLVE_CONTINUE;
        });

    }

    /**
     * Is maybeAccountDeclaration the instance that declares the account in accountToSearch?
     *
     * @param accountToSearch
     * @param maybeAccountDeclaration
     * @return
     */
    private static boolean isAccountDeclaration(BeancountAccount accountToSearch,
        BeancountAccount maybeAccountDeclaration)
    {

        if (StringUtils.equals(maybeAccountDeclaration.getName(), accountToSearch.getName()))
        {
            Optional<PsiElement> maybeOpenDirective = BeancountTreeUtil
                .findParent(maybeAccountDeclaration,
                    parent -> parent instanceof BeancountOpenDir);

            return maybeOpenDirective.isPresent();
        }
        return false;
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
        PsiFile file = myElement.getContainingFile();
        if (!(file instanceof BeancountFile)) return;
        ResolveState state = createContextOnElement(myElement);

        // with this we have every usage of this particular account
        Collection<BeancountAccount> accountInstances = AccountStubIndex
            .find(file.getProject(), acccountPath);

        // now we exec the processor to find which one of these account instances is the declaration
        Stream.ofAll(accountInstances)
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
