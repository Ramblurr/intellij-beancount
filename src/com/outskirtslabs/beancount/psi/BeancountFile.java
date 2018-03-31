package com.outskirtslabs.beancount.psi;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Stopwatch;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.outskirtslabs.beancount.BeancountFileType;
import com.outskirtslabs.beancount.BeancountLanguage;
import com.outskirtslabs.beancount.psi.stub.index.AccountStubIndex;
import com.outskirtslabs.beancount.psi.stub.index.CurrencySymbolStubIndex;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeancountFile extends PsiFileBase
{
    public BeancountFile(@NotNull FileViewProvider viewProvider)
    {
        super(viewProvider, BeancountLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType()
    {
        return BeancountFileType.INSTANCE;
    }

    @Override
    public String toString()
    {
        return "Beancount File";
    }

    public Stream<String> getAllAccountsStrings()
    {

        HashSet<String> names = new HashSet<>();
        this.acceptChildren(new BeancountRecursiveVisitor()
        {
            @Override
            public void visitAccountName(@NotNull final BeancountAccountName o)
            {
                names.add(o.getText());
//                super.visitAccountName(o);
            }
        });
        return names.stream();
    }

    public Stream<String> getAllAccountNames()
    {
//        Stream<BeancountAccount> accounts = Arrays.stream(this.getChildren())
//                                                  .peek( e -> log.info("\t " + e.getClass().getName()))
//                                                  .filter(e -> e instanceof BeancountAccount)
//                                                  .map(e -> (BeancountAccount) e)
//                                                  .distinct();
//        return accounts;

        HashSet<String> names = new HashSet<>();
        this.acceptChildren(new BeancountRecursiveVisitor()
        {
            @Override
            public void visitAccountName(@NotNull final BeancountAccountName o)
            {
                names.add(o.getText());
//                super.visitAccountName(o);
            }
        });
        return names.stream();
    }

    public Stream<BeancountAccount> getAllAccountElements()
    {
        return getAllAccountElements(null);
    }

    public Stream<BeancountAccount> getAllAccountElements(String byName)
    {
        HashSet<BeancountAccount> names = new HashSet<>();
        this.acceptChildren(new BeancountRecursiveVisitor()
        {
            @Override
            public void visitAccount(@NotNull final BeancountAccount o)
            {
                if (byName == null || byName.equals(o.getName()))
                {
                    names.add(o);
                }
            }
        });
        return names.stream();
    }

    public Stream<String> getAllAccountsCached()
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Stream<String> distinct = AccountStubIndex.findAllAccounts(this.getProject()).stream()
                                                  .distinct();
        log.info("getAllAccountsCached complete in {}", stopwatch.elapsed(TimeUnit.MICROSECONDS));
        return distinct;
    }

    public Stream<String> getAllCurrenciesCached()
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Stream<String> distinct = CurrencySymbolStubIndex.findAllCurrencySymbols(this.getProject())
                                                         .stream()
                                                         .distinct();
        log.info("getAllAccountsCached complete in {}", stopwatch.elapsed(TimeUnit.MICROSECONDS));
        return distinct;
    }

    public Stream<String> getAllAccounts()
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        HashSet<String> names = new HashSet<>();
        this.acceptChildren(new BeancountRecursiveVisitor()
        {
            @Override
            public void visitAccount(@NotNull final BeancountAccount o)
            {
                names.add(o.getText());
            }
        });

        log.info("getAllAccounts complete in {}", stopwatch.elapsed(TimeUnit.MICROSECONDS));
        return names.stream();
    }
}
