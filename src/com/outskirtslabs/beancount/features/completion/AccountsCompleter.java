package com.outskirtslabs.beancount.features.completion;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

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
        this.reset(file.getAllAccounts());
    }

    private void reset(Stream<String> paths)
    {
        tree = new AccountTree();
        paths.filter(s -> !StringUtils.equals(s, DUMMY_IDENT) || StringUtils.isBlank(s))
             .forEach(tree::addAccountPaths);
    }

    /**
     * Get the length of the longest account string (including colons)
     */
    public int lengthOfLongestAccount() {
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
        Option<AccountTree.Node> node = tree.getFinalNode(partialAccount);
        if (node.isEmpty())
            return new TreeSet<>();

        AccountTree.Node node1 = node.get();
        return node1.buildChildrenIntermediate().toJavaSet();
    }

}
