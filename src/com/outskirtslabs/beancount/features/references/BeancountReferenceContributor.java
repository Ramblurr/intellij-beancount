package com.outskirtslabs.beancount.features.references;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import com.outskirtslabs.beancount.BeancountLanguage;
import com.outskirtslabs.beancount.psi.BeancountAccount;
import com.outskirtslabs.beancount.psi.reference.BeancountAccountReference;

public class BeancountReferenceContributor extends PsiReferenceContributor
{
    private static Logger LOG = Logger.getInstance(BeancountReferenceContributor.class);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar)
    {
        registrar
            .registerReferenceProvider(
                PlatformPatterns.psiElement().withLanguage(BeancountLanguage.INSTANCE),
                new PsiReferenceProvider()
                {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                        @NotNull ProcessingContext
                            context)
                    {
//                        LOG.info("element is " + element.getClass());
                        if (element instanceof BeancountAccount)
                        {

                            BeancountAccount segment = (BeancountAccount) element;
                            if (segment == null)
                                return PsiReference.EMPTY_ARRAY;
                            String value = segment.getText();
                            if (StringUtils.isBlank(value))
                                return PsiReference.EMPTY_ARRAY;

                            return new PsiReference[] {
                                new BeancountAccountReference(segment,
                                    new TextRange(0, value.length()))
                            };
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}

