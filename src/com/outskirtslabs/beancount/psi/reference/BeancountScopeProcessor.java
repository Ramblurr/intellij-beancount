package com.outskirtslabs.beancount.psi.reference;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.BaseScopeProcessor;

import io.vavr.Function2;

/**
 * A functional wrapper around {@link BaseScopeProcessor}
 */
public abstract class BeancountScopeProcessor extends BaseScopeProcessor
{
    public static boolean RESOLVE_CONTINUE = true;
    public static boolean RESOLVE_FINISHED = false;

    public static BeancountScopeProcessor of(
        Function2<PsiElement, ResolveState, Boolean> executor)
    {
        return new BeancountScopeProcessor()
        {

            @Override
            public boolean execute(@NotNull final PsiElement element,
                @NotNull final ResolveState state)
            {
                return executor.apply(element, state);
            }
        };
    }
}
