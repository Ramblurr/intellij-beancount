package com.outskirtslabs.beancount.features.completion;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.collection.TreeSet;

public class AccountTreeTest
{

    Set<String> manyAccounts = TreeSet.of(
        "Assets:US:Federal:PreTax401k",
        "Expenses:Financial:Commissions",
        "Expenses:Financial:Fees",
        "Expenses:Food:Alcohol",
        "Expenses:Food:Coffee",
        "Expenses:Food:Groceries",
        "Expenses:Food:Restaurant",
        "Expenses:Health:Dental:Insurance",
        "Expenses:Health:Life:GroupTermLife",
        "Expenses:Health:Medical:Insurance",
        "Expenses:Health:Vision:Insurance",
        "Expenses:Home:Electricity",
        "Expenses:Home:Internet",
        "Expenses:Home:Phone",
        "Expenses:Home:Rent",
        "Expenses:Taxes:Y2016:US:CityNYC",
        "Expenses:Taxes:Y2016:US:Federal:PreTax401k",
        "Expenses:Taxes:Y2016:US:Medicare",
        "Expenses:Taxes:Y2016:US:SDI",
        "Expenses:Taxes:Y2016:US:SocSec",
        "Expenses:Taxes:Y2016:US:State",
        "Expenses:Taxes:Y2017:US:CityNYC",
        "Expenses:Taxes:Y2017:US:Federal:PreTax401k",
        "Expenses:Taxes:Y2017:US:Medicare",
        "Expenses:Taxes:Y2017:US:SDI",
        "Expenses:Taxes:Y2017:US:SocSec",
        "Expenses:Taxes:Y2017:US:State",
        "Expenses:Taxes:Y2018:US:CityNYC",
        "Expenses:Taxes:Y2018:US:Federal:PreTax401k",
        "Expenses:Taxes:Y2018:US:Medicare",
        "Expenses:Taxes:Y2018:US:SDI",
        "Expenses:Taxes:Y2018:US:SocSec",
        "Expenses:Taxes:Y2018:US:State",
        "Expenses:Transport:Tram",
        "Income:US:Federal:PreTax401k"
    );

    Set<String> fewAccountsAll = TreeSet.of(
        "Expenses",
        "Income",
        "Equity",
        "Assets",
        "Assets:US",
        "Assets:US:BofA",
        "Assets:US:BofA:Checking",
        "Liabilities",
        "Liabilities:AccountsPayable"
    );
    List<String> fewAccounts = List.of("Assets:US:BofA:Checking", "Liabilities:AccountsPayable");

    @Test
    public void getRoot()
    {

        AccountTree.Node ROOT = new AccountTree.Node("ROOT", null);
        AccountTree tree = new AccountTree();
        assertThat(tree.getOrCreateRoot("Assets").path)
            .isEqualTo(new AccountTree.Node("Assets", null).path);
        assertThat(tree.getOrCreateRoot("OMG").path)
            .isEqualTo(new AccountTree.Node("OMG", ROOT).path);
    }

    @Test
    public void addAccountPaths()
    {
        AccountTree tree = new AccountTree();
        List<String> paths = AccountTree.tokenize(fewAccounts.head()).paths;
        AccountTree.Node node = tree.addAccountPaths(fewAccounts.head());

        for (String path : paths)
        {
            assertThat(node.path).isEqualTo(path);
            if (paths.last().equals(path)) break;
            assertThat(node.children.size()).isEqualTo(1);
            node = node.children.head();
        }
    }

    @Test
    public void getFinalNode()
    {
        AccountTree tree = new AccountTree();
        tree.addAccountPaths("Assets:US:BofA:Checking");

        AccountTree.Node node = tree.getFinalNode("Assets:US:BofA").get();
        assertThat(node.getPath()).isEqualTo("US");

        // with trailing delimiter it should resolve the immediate parent
        node = tree.getFinalNode("Assets:US:BofA:").get();
        assertThat(node.getPath()).isEqualTo("BofA");

        node = tree.getFinalNode("Assets:US:BofA:Ch").get();
        assertThat(node.getPath()).isEqualTo("BofA");

        node = tree.getFinalNode("Assets:").get();
        assertThat(node.getPath()).isEqualTo("Assets");
    }

    @Test
    public void buildIntermediateWithParents()
    {
        AccountTree tree = new AccountTree();
        tree.addAccountPaths("Assets:US:BofA:Checking");

        AccountTree.Node node = tree.getFinalNode("Assets:US:BofA").get();
        assertThat(node.getPath()).isEqualTo("US");

        assertThat(node.buildIntermediateWithParents().toJavaSet())
            .isEqualTo(HashSet.of(
                "Assets:US:BofA:Checking",
                "Assets:US:BofA",
                "Assets:US"
            ).toJavaSet());
    }

    @Test
    public void testBuild()
    {
        AccountTree tree = new AccountTree();
        tree.addAccountPaths(manyAccounts);
        Set<String> build = tree.build();
        assertThat(build.toJavaList()).isEqualTo(manyAccounts.toJavaList());
    }

    @Test
    public void testBuildChildren()
    {
        AccountTree tree = new AccountTree();
        String build = tree.addAccountPaths("Assets:US:BofA").buildChildren().head();
        assertThat(build).isEqualTo("US:BofA");
    }

    @Test
    public void testBuildWithIntermediates()
    {
        AccountTree tree = new AccountTree();
        tree.addAccountPaths(fewAccounts);
        Set<String> build = tree.buildIntermediate();
        assertThat(build.toJavaList()).isEqualTo(fewAccountsAll.toJavaList());
    }

    @Test
    public void testMaximumLengthSingle()
    {
        AccountTree tree = new AccountTree();
        String a = "Assets:US:BofA";
        tree.addAccountPaths(a);
        assertThat(tree.lengthOfLongestAccount()).isEqualTo(a.length());
    }

    @Test
    public void testMaximumLengthMultiple()
    {
        AccountTree tree = new AccountTree();
        String a = "Assets:US:Bof";
        String b = "Assets:Foo:Bar:DoubleBar";
        String c = "Liabilities:Foo:Bar:DoubleBar:OMGOMG";
        String d = "Assets:Foo:Joy:OMG:DoubleBar:Saloon:boop";
        String e = "Assets:Foo:Joy:OMG:Car:Baloon";
        tree.addAccountPaths(c);
        tree.addAccountPaths(a);
        tree.addAccountPaths(d);
        tree.addAccountPaths(b);
        tree.addAccountPaths(e);
        assertThat(tree.lengthOfLongestAccount()).isEqualTo(d.length());
    }
}
