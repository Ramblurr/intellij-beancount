package com.outskirtslabs.beancount.features.completion;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Stopwatch;
import com.intellij.openapi.diagnostic.Logger;
import com.outskirtslabs.beancount.psi.BeancountFile;

import io.vavr.control.Option;

/**
 * Implements account path name completion
 */
public class AccountsCompleter
{
    private static Logger LOG = Logger.getInstance(AccountsCompleter.class);
    private static String DUMMY_IDENT = "IntellijIdeaRulezzz";
    private AccountTree tree;

    public AccountsCompleter(BeancountFile file)
    {
//        this.reset(file.getAllAccounts());
        this.reset(file.getAllAccountsCached());
    }

    private void reset(Stream<String> paths)
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        tree = new AccountTree();
        paths.filter(s -> !StringUtils.endsWith(s, DUMMY_IDENT) && AccountTree.isValidAccountString(s))
             .distinct()
             .forEach(tree::addAccountPaths);
        LOG.info("index complete in " + stopwatch.elapsed(TimeUnit.MICROSECONDS));
    }

    /**
     * Get the length of the longest account string (including colons)
     */
    public int lengthOfLongestAccount()
    {
        return tree.lengthOfLongestAccount();
    }

    public Set<String> getPrefixMatches(String partialAccount)
    {
        if (partialAccount.endsWith(DUMMY_IDENT))
            partialAccount = partialAccount
                .substring(0, partialAccount.indexOf(DUMMY_IDENT));
        LOG.info("getPrefixMatches(" + partialAccount + ")");
        if (StringUtils.isBlank(partialAccount))
        {
            // handle "<cursor>"
            return tree.buildIntermediate().toJavaSet();
        }

        if (!partialAccount.contains(":"))
        {
            // todo partial root path completion
            return tree.getRoots()
                       .map(AccountTree.Node::getPath)
                       .map(s -> s + ":")
                       .toJavaSet();
        } else
        {

            Option<AccountTree.Node> node = tree.getFinalNode(partialAccount);
            if (node.isEmpty())
                return new TreeSet<>();

            AccountTree.Node node1 = node.get();
            return node1.buildIntermediateWithParents().toJavaSet();
        }

    }

}
