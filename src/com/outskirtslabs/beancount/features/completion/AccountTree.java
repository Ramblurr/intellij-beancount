package com.outskirtslabs.beancount.features.completion;

import static org.apache.commons.lang3.StringUtils.isAllUpperCase;
import static org.apache.commons.lang3.StringUtils.startsWith;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    // the root accounts
    private TreeSet<Node> roots = TreeSet
        .of(Comparator.comparing(node -> node.path),
            Node.of("Assets"),
            Node.of("Income"),
            Node.of("Liabilities"),
            Node.of("Equity"),
            Node.of("Expenses"));

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
        Node node = new Node(path);
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
        Node root = Node.of("ROOT");
        return Tree.of(root, roots.toList().map(Node::toTree));
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
        String path;
        Set<Node> children = HashSet.empty();

        public Node(final String path)
        {
            this.path = path;
        }

        public Node(final String path, final Set<Node> children)
        {
            this.path = path;
            this.children = children;
        }

        public static Node of(final String path)
        {
            return new Node(path);
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
            Node node = new Node(path);
            children = this.children.add(node);
            return node;
        }

        public Set<String> buildChildren()
        {
            return build(true, false, HashSet.empty(), new StringBuilder());
        }

        public Set<String> build()
        {
            return build(true, false, HashSet.empty(), new StringBuilder().append(path));
        }

        public Set<String> buildChildrenIntermediate()
        {
            return build(true, true, HashSet.empty(), new StringBuilder());
        }

        public Set<String> buildIntermediate()
        {
            return build(true, true, HashSet.of(path), new StringBuilder().append(path));
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
        public boolean equals(final Object o)
        {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return new EqualsBuilder()
                .append(path, node.path)
                .append(children, node.children)
                .isEquals();
        }

        @Override
        public int hashCode()
        {
            return new HashCodeBuilder(17, 37)
                .append(path)
                .append(children)
                .toHashCode();
        }

        @Override
        public String toString()
        {
            return path;
        }
    }

}
