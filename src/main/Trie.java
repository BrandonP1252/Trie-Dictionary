package main;

import java.util.ArrayList;

import javax.net.ssl.TrustManagerFactory;

/**
 * This class implements a Trie.
 *
 * @author Sesh Venugopal
 *
 */
public class Trie {

    // prevent instantiation
    private Trie() { }

    /**
     * Builds a trie by inserting all words in the input array, one at a time,
     * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
     * The words in the input array are all lower case.
     *
     * @param allWords Input array of words (lowercase) to be inserted.
     * @return Root of trie with all words inserted from the input array
     */
    public static TrieNode buildTrie(String[] allWords) {
        /** COMPLETE THIS METHOD **/
        // FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
        // MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
        TrieNode trieRoot = new TrieNode(null, null, null);

        if(allWords == null){
            return trieRoot;
        }

        Indexes ind = new Indexes(0, (short) 0, (short)(allWords[0].length()-1));
        TrieNode firstInsert = new TrieNode(ind, null, null);
        trieRoot.firstChild = firstInsert;

        TrieNode ptr = trieRoot.firstChild;
        TrieNode prev = null;
        for(int i = 1; i < allWords.length; i++){
            String prefix = "";
            String add = allWords[i];
            while(ptr != null){
                String nPrefix = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1);
                if(add.startsWith(prefix+nPrefix) == true){
                    prefix = prefix + nPrefix;
                    prev = ptr;
                    ptr = ptr.firstChild;

                }
                else if(add.charAt(ptr.substr.startIndex) != nPrefix.charAt(0)){
                    prev = ptr;
                    ptr = ptr.sibling;

                }
                else{
                    int max = 0;
                    for(int j = 0; j < nPrefix.length(); j++){
                        if(add.charAt(ptr.substr.startIndex+j) == nPrefix.charAt(j)){
                            max++;
                        }else{
                            break;
                        }
                    }
                    int prefixLength = ptr.substr.startIndex + max;
                    ind = new Indexes(ptr.substr.wordIndex, (short) prefixLength, (short) ptr.substr.endIndex);
                    TrieNode temp = new TrieNode(ind, ptr.firstChild, null);
                    ptr.firstChild = temp;
                    ptr.substr.endIndex = (short) (prefixLength - 1);
                    prefix = prefix + allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, prefixLength);
                    prev = ptr;
                    ptr = ptr.firstChild;
                }

            }
            ind = new Indexes(i, (short)(prefix.length()), (short)(add.length()-1));
            TrieNode addNode = new TrieNode(ind, null, null);
            prev.sibling = addNode;
            ptr = trieRoot.firstChild;
            prev = null;
        }

        return trieRoot;
    }

    /**
     * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the
     * trie whose words start with this prefix.
     * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
     * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
     * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell",
     * and for prefix "bell", completion would be the leaf node that holds "bell".
     * (The last example shows that an input prefix can be an entire word.)
     * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
     * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
     *
     * @param root Root of Trie that stores all words to search on for completion lists
     * @param allWords Array of words that have been inserted into the trie
     * @param prefix Prefix to be completed with words in trie
     * @return List of all leaf nodes in trie that hold words that start with the prefix,
     * 			order of leaf nodes does not matter.
     *         If there is no word in the tree that has this prefix, null is returned.
     */
    public static ArrayList<TrieNode> completionList(TrieNode root,
                                                     String[] allWords, String prefix) {
        /** COMPLETE THIS METHOD **/

        // FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
        // MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
        ArrayList<TrieNode> completionList = new ArrayList<>();
        TrieNode ptr = root.firstChild;
        TrieNode prev = null;
        String checkPrefix = "";
        while(prefix.length() > checkPrefix.length() && ptr != null){
            prev = ptr;
            String currentPrefix = allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex+1);
            if(prefix.startsWith(checkPrefix+currentPrefix)==true){
                checkPrefix = checkPrefix + currentPrefix;
                ptr=ptr.firstChild;
                continue;
            }
            if((checkPrefix + currentPrefix).startsWith(prefix)==true){
                ptr=ptr.firstChild;
                break;
            }
            ptr = ptr.sibling;
        }
        if(ptr==null){
            if(allWords[prev.substr.wordIndex].startsWith(prefix)==true){
                completionList.add(prev);
                return completionList;
            }
            return null;
        }
        insert(completionList, ptr);
        return completionList;
    }
    private static void insert(ArrayList<TrieNode> completionList, TrieNode word){
        if(word == null){
            return;
        }
        if(word.firstChild == null){
            completionList.add(word);
            insert(completionList, word.sibling);
            return;
        }
        insert(completionList, word.firstChild);
        insert(completionList, word.sibling);
    }


    public static void print(TrieNode root, String[] allWords) {
        System.out.println("\nTRIE\n");
        print(root, 1, allWords);
    }

    private static void print(TrieNode root, int indent, String[] words) {
        if (root == null) {
            return;
        }
        for (int i=0; i < indent-1; i++) {
            System.out.print("    ");
        }

        if (root.substr != null) {
            String pre = words[root.substr.wordIndex]
                    .substring(0, root.substr.endIndex+1);
            System.out.println("      " + pre);
        }

        for (int i=0; i < indent-1; i++) {
            System.out.print("    ");
        }
        System.out.print(" ---");
        if (root.substr == null) {
            System.out.println("root");
        } else {
            System.out.println(root.substr);
        }

        for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
            for (int i=0; i < indent-1; i++) {
                System.out.print("    ");
            }
            System.out.println("     |");
            print(ptr, indent+1, words);
        }
    }
}

