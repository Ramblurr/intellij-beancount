package com.outskirtslabs.beancount.features.completion;

import static org.apache.commons.lang3.StringUtils.isAllUpperCase;
import static org.apache.commons.lang3.StringUtils.startsWith;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.intellij.openapi.diagnostic.Logger;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.Tree;
import io.vavr.collection.TreeSet;
import io.vavr.control.Option;

/**
 * A tree structure for working with hierarchical accounts.
 */
public class AccountTree
{
    private static Logger LOG = Logger.getInstance(AccountTree.class);
    // root here functions as a sentinel
    private static final Node ROOT = new Node("ROOT", null);
    // the root accounts
    private TreeSet<Node> roots = TreeSet
        .of(Comparator.comparing(node -> node.path),
            Node.of("Assets", ROOT),
            Node.of("Income", ROOT),
            Node.of("Liabilities", ROOT),
            Node.of("Equity", ROOT),
            Node.of("Expenses", ROOT));

    public Set<Node> getRoots() {
        return roots;
    }
    public Option<Node> getRoot(String path)
    {
        return roots.find(n -> StringUtils.equals(path, n.path));
    }

    /**
     * Beancount plugins can add more roots, so we need to support root accounts besides the 5
     * <p>
     * Gets the root node specified by path or creates it if it doesn't exist
     *
     * @param path e.g., "Assets"
     * @return
     */
    public Node getOrCreateRoot(String path)
    {
        Option<Node> maybeNode = getRoot(path);
        if (maybeNode.isDefined())
            return maybeNode.get();
        Node node = new Node(path, ROOT);
        roots = roots.add(node);
        return node;
    }

    /**
     * Adds the accounts to the tree, inserting them if necessary
     *
     * @param accounts e.g., [ "Assets:US:BofA:Checking", .... ]
     */
    public void addAccountPaths(Iterable<String> accounts)
    {
        for (String account : accounts)
        {
            addAccountPaths(account);
        }
    }

    public static boolean isValidAccountString(String s)
    {
        // very basic validation
        return s.contains(":")
            && isAllUpperCase(s.substring(0, 1));
    }

    /**
     * Split a path into Tokens
     */
    public static Tokens tokenize(String path)
    {
        Validate.isTrue(isValidAccountString(path), "Not a valid account string '" + path + "'");
        return new Tokens(path.endsWith(":"), List.of(path.split(":")));
    }

    /**
     * Add a fully qualified account string to the index
     *
     * @param fullAccount e.g., "Assets:US:BofA:Checking"
     * @return the root node pf the account in the tree
     */
    public Node addAccountPaths(String fullAccount)
    {
        List<String> paths = tokenize(fullAccount).paths;
        Node root = getOrCreateRoot(paths.head());
        Node parent = root;
        for (String childpath : paths.tail())
        {
            Option<Node> child = parent.getChild(childpath);
            if (child.isEmpty())
            {
                parent = parent.addChild(childpath);
            } else
            {
                parent = child.get();
            }
        }
        return root;
    }

    /**
     * Used for completion.
     * <p>
     * Returns the child node of the final segment in the partial account path. It handles unfinished
     * accounts and trailing colons.
     * <p>
     * Examples:
     * <p>
     * input "Assets:Bank:Che" --> Node(Bank)
     * input "Assets:Bank:" --> Node(Bank)
     * input "Assets:" --> Node(Assets)
     * <p>
     * DOES NOT handle input ""
     *
     * @param partialPath e.g., "Assets:Bank:Che" or "Assets:Bank:"
     */
    public Option<Node> getFinalNode(String partialPath)
    {
        Tokens tokenize = tokenize(partialPath);
        String terminator = tokenize.paths.last();

        String head = tokenize.paths.head();
        Option<Node> root = getRoot(head);
        if (root.isEmpty()) return Option.none();
        Node parent = root.get();
        Iterator<String> it = tokenize.paths.tail().iterator();

        if (!it.hasNext() && tokenize.hasTrailingColon)
            // handle case Assets:<cursor>
            return root;
        while (it.hasNext())
        {
            String path = it.next();

            if (!it.hasNext()
                && startsWith(terminator, path)
                && !terminator.equals(path))
            {
                // handle case Assets:BofA:Check<cursor>
                return Option.of(parent);
            }

            if (terminator.equals(path))
                if (tokenize.hasTrailingColon)
                    // handle case Assets:Foo:<cursor>
                    return parent.getChild(path);
                else
                    // handle case Assets:Foo:Checking<cursor>
                    return Option.of(parent);

            Option<Node> child = parent.getChild(path);
            if (child.isEmpty()) return Option.none();
            parent = child.get();
        }
        return Option.none();
    }

    Tree<Node> toTree()
    {
        return Tree.of(ROOT, roots.toList().map(Node::toTree));
    }

    /**
     * Build every account in the tree back into the colon separated form, but only include the terminal
     * accounts.
     * <p>
     * Example:  "[Assets:US:BofA:Checking]"
     */
    Set<String> build()
    {
        return roots.flatMap(Node::build).toSortedSet();
    }

    /**
     * Build every account in the tree back into the colon separated form, with every intermediate
     * account represented
     * Example:  "[Assets, Assets:US, Assets:US:BofA, Assets:US:BofA:Checking]"
     */
    Set<String> buildIntermediate()
    {
        return roots.flatMap(Node::buildIntermediate).toSortedSet();
    }

    public int lengthOfLongestAccount()
    {
        return roots.map(Node::maximumLength).max().getOrElse(0);
    }

    /**
     * List of account segments
     */
    public static class Tokens
    {
        final boolean hasTrailingColon;
        final List<String> paths;

        public Tokens(final boolean hasTrailingColon, final List<String> paths)
        {
            this.hasTrailingColon = hasTrailingColon;
            this.paths = paths;
        }
    }

    public static class Node
    {
        final Node parent;
        final String path;
        Set<Node> children = HashSet.empty();

        public Node(final String path, Node parent)
        {
            this.path = path;
            this.parent = parent;
        }

        public static Node of(final String path, final Node parent)
        {
            return new Node(path, parent);
        }

        Option<Node> getParent()
        {
            if (this.parent == ROOT)
            {
                return Option.none();
            }
            return Option.of(parent);
        }

        Option<Node> getChild(String path)
        {
            return children.find(s -> StringUtils.equals(path, s.getPath()));
        }

        public String getPath()
        {
            return path;
        }

        public Set<Node> getChildren()
        {
            return children;
        }

        public Node addChild(String path)
        {
            Node node = new Node(path, this);
            children = this.children.add(node);
            return node;
        }

        public Set<String> buildChildren()
        {
            return build(true, false, HashSet.empty(), new StringBuilder());
        }

        public Set<String> build()
        {
            return build(true, false, HashSet.empty(), new StringBuilder(path));
        }

        public Set<String> buildChildrenIntermediate()
        {
            return build(true, true, HashSet.empty(), new StringBuilder());
        }

        public Set<String> buildIntermediateWithParents()
        {
            Option<Node> parent = getParent();
            List<String> ancestors = List.empty();
            while (parent.isDefined())
            {
                ancestors = ancestors.append(parent.get().path);
                parent = parent.get().getParent();
            }
            String prefix = StringUtils.join(ancestors.reverse(), ":");
            return build(false, true, HashSet.empty(), new StringBuilder(prefix));
        }

        public Set<String> buildIntermediate()
        {
            return build(true, true, HashSet.of(path), new StringBuilder(path));
        }

        private Set<String> build(boolean isFirst, boolean withIntermediates, Set<String> accum,
            StringBuilder builder)
        {
            if (!isFirst)
            {
                if (builder.length() > 0)
                    builder.append(":");
                builder.append(this.path);
                if (withIntermediates || children.isEmpty())
                    accum = accum.add(builder.toString());
            }
            for (Node child : children)
            {
                accum = child.build(false, withIntermediates, accum, new StringBuilder(builder));
            }
            return accum;
        }

        public Tree.Node<Node> toTree()
        {
            if (children.isSingleValued())
                return Tree.of(this);
            return Tree.of(this, children.map(Node::toTree));
        }

        @Override
        public String toString()
        {
            return path;
        }

        public int maximumLength()
        {
            Tuple2<Integer, Integer> tuple2 = maximumLength(Tuple.of(0, 0));
            return tuple2._2;
        }

        private Tuple2<Integer, Integer> maximumLengthLeaf(final Tuple2<Integer, Integer> currMax)
        {
            int subtreeCost = currMax._1 + path.length();
            return currMax.map2(i -> currMax._2 < subtreeCost ? subtreeCost : i);
        }

        private Tuple2<Integer, Integer> maximumLengthIntermediate(
            final Tuple2<Integer, Integer> currMax)
        {
            // add one to account for the :
            Tuple2<Integer, Integer> currMaxLocal = currMax.update1(currMax._1 + path.length() + 1);
            return this.children.foldLeft(currMaxLocal,
                (cost, node) -> node.maximumLength(cost).update1(currMaxLocal._1));
        }

        private Tuple2<Integer, Integer> maximumLength(Tuple2<Integer, Integer> currMax)
        {
            if (this.children.isEmpty())
                return maximumLengthLeaf(currMax);
            else
                return maximumLengthIntermediate(currMax);
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return new EqualsBuilder()
                .append(path, node.path)
                .append(children, node.children)
                .isEquals() && parent == node.parent;
        }

        @Override
        public int hashCode()
        {
            return new HashCodeBuilder(17, 37)
                .append(path)
                .append(children)
                .toHashCode();
        }
    }

}
